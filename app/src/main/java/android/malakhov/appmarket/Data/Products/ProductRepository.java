package android.malakhov.appmarket.Data.Products;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.malakhov.appmarket.Data.FireBaseDB;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductRepository implements IProductRepository{

    private DatabaseReference productsRef = FireBaseDB.get().getDataBase().getReference("Products");
    private static ProductRepository obj;
    private List<Product> list = new ArrayList<>();
    private int i = 0;
    private Uri uploadURI;



    //Singletone
    public static ProductRepository get(){
        if (obj == null)
            return new ProductRepository();
        else
            return obj;
    }


    @Override
    public void getProductsSortedByDate(ICallback callback) {

        productsRef.orderByChild("dateTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list.size() > 0)
                    list.clear();

                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if (product.getAmount() == 0)
                        deleteProduct(product);
                    else
                    list.add(product);
                }
                Collections.reverse(list);
                callback.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getProductsSortedByExpensive(ICallback callback) {
        productsRef.orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list.size() > 0)
                    list.clear();

                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if (product.getAmount() == 0)
                        deleteProduct(product);
                    else
                        list.add(product);
                }
                Collections.reverse(list);
                callback.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getProductsSortedByCheap(ICallback callback) {
        productsRef.orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list.size() > 0)
                    list.clear();

                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if (product.getAmount() == 0)
                        deleteProduct(product);
                    else
                        list.add(product);
                }
                callback.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getProductsSortedByRating(ICallback callback) {
        productsRef.orderByChild("averageRating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list.size() > 0)
                    list.clear();

                for (DataSnapshot ds : snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    if (product.getAmount() == 0)
                        deleteProduct(product);
                    else
                        list.add(product);
                }
                Collections.reverse(list);
                callback.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deleteProduct(Product product) {
        productsRef.child(product.getId()).removeValue();
        deleteFromCorzina(product);
    }

    @Override
    public void addProduct(ImageView[]images, Product product) {
        final StorageReference idRef = FireBaseDB.get().getStorageRef().child(product.getId());
        final StorageReference ref = idRef.child(String.valueOf(i));

        Bitmap bitmap = ((BitmapDrawable) images[i].getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        UploadTask up = ref.putBytes(byteArray);


        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {

                uploadURI = task.getResult();
                product.getPhotoURI().add(uploadURI.toString());
                if (i == 2) {
                    productsRef.child(product.getId()).setValue(product);
                } else {
                    i++;
                    addProduct(images, product);
                }
            }
        });
    }

    @Override
    public void addToCorzina(Product product) {
        UserRepository.get().getCurrUserRef().child("Corzina").child(product.getId()).setValue(product.getId());
    }

    @Override
    public void getCorzinaList(ICallback callback) {
        if (list.size() > 0)
            list.clear();

        List<Product> list = new ArrayList<>();
        UserRepository.get().getCurrUserRef().child("Corzina").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    callback.callingBack(null);
                for (DataSnapshot ds: snapshot.getChildren()){
                    String id = ds.getKey();
                    productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Product product = snapshot.child(id).getValue(Product.class);
                            list.add(product);
                            callback.callingBack(list);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void deleteFromCorzina(Product product) {
        UserRepository.get().getCurrUserRef().child("Corzina").child(product.getId()).removeValue();
    }

    @Override
    public void minusAmount(Product product, int a) {
        productsRef.child(product.getId()).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int amaunt;
                amaunt = Integer.parseInt(snapshot.getValue().toString());
                amaunt-=a;
                productsRef.child(product.getId()).child("amount").setValue(amaunt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private long count;
    private long allRating;
    @Override
    public void setRating(Product product, int rating) {
        setCommentsCount(product, new ICallback() {
            @Override
            public void callingBack(Object obj) {
                count = (Long) obj;
                setAllRating(product, rating, new ICallback() {
                    @Override
                    public void callingBack(Object obj) {
                        allRating = (long)obj;
                        float averageRating = allRating/(float)count;
                        setAverageRating(product,averageRating);
                    }
                });
            }
        });
    }

    //Обновление рейтинга товара
    private void setCommentsCount(Product product, ICallback callback){
        productsRef.child(product.getId()).child("commentsCount").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                currentData.setValue((Long) currentData.getValue() + 1);
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                callback.callingBack(currentData.getValue());
            }
        });
    }
    private void setAllRating(Product product,int rating, ICallback callback){
        productsRef.child(product.getId()).child("allRating").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                currentData.setValue((Long) currentData.getValue() + rating);
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                callback.callingBack(currentData.getValue());
            }
        });
    }
    private void setAverageRating(Product product, float averageRating){
        productsRef.child(product.getId()).child("averageRating").setValue(averageRating);
    }
}
