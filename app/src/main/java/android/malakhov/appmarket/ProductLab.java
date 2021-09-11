package android.malakhov.appmarket;






import android.view.View;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductLab {

    private static ProductLab mProductLab;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storageRef;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private List<Product> corzinaList = new ArrayList<>();
    private String strNumber;
    private boolean isCard;
    private List<User> list = new ArrayList<>();



    public static ProductLab get(){
        if (mProductLab == null)
            return new ProductLab();
        else
            return mProductLab;
    }



    public ProductLab() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Products");
        storageRef = FirebaseStorage.getInstance().getReference("ProductImages");
        userRef = database.getReference("Users").child(mAuth.getCurrentUser().getUid());
    }


    public void addProductToDB(Product product){

        myRef.child(product.getId()).setValue(product);

    }

    public void deleteProductFromDB(Product product){

        DatabaseReference ref = myRef.child(product.getId());
        ref.removeValue();
        deleteFromCorzina(product);

//        StorageReference str = FirebaseStorage.getInstance().getReferenceFromUrl(product.getPhotoURI().get(0));
//        str.delete();
//
//        str = FirebaseStorage.getInstance().getReferenceFromUrl(product.getPhotoURI().get(1));
//        str.delete();
//
//        str = FirebaseStorage.getInstance().getReferenceFromUrl(product.getPhotoURI().get(2));
//        str.delete();
    }

    public void addToCorzina(Product product){

        userRef.child("Corzina").child(product.getId()).setValue(product.getId());
    }

    public void loadFromCorzina(){

        if (corzinaList.size() > 0) {

            corzinaList.clear();
        }

        userRef.child("Corzina").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot ds : snapshot.getChildren()) {

                        String id = ds.getKey();
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Product product = snapshot.child(id).getValue(Product.class);
                                corzinaList.add(product);
                                CorzinaFragment.mAdapter.notifyDataSetChanged();
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

    public List<Product> getCorzinaList(){
        loadFromCorzina();
        return corzinaList;
    }

    public void deleteFromCorzina(Product product){

        userRef.child("Corzina").child(product.getId()).removeValue();
    }


    public void addCardToDB(String number, String date, String CVC){

        userRef.child("cardNumber").setValue(number);
        userRef.child("cardDate").setValue(date);
        userRef.child("CVC").setValue(CVC);
    }


    public void minusAmount(Product product, int a){


        myRef.child(product.getId()).child("amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int amaunt;
                amaunt = Integer.parseInt(snapshot.getValue().toString());
                amaunt-=a;
                myRef.child(product.getId()).child("amount").setValue(amaunt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addToPokupki(Pokupka pokupka){

        String id = userRef.child("Pokupki").push().getKey();
        pokupka.setId(id);
        userRef.child("Pokupki").child(id).setValue(pokupka);
    }




    public void getPokupki(){

        if (PokupkiActivity.list.size() > 0)
            PokupkiActivity.list.clear();


        userRef.child("Pokupki").orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    PokupkiActivity.list.add(ds.getValue(Pokupka.class));
                    PokupkiActivity.adapter.notifyDataSetChanged();
                }
                Collections.reverse(PokupkiActivity.list);
                if (PokupkiActivity.list.size() == 0) {
                    PokupkiActivity.mRecyclerView.setVisibility(View.INVISIBLE);
                    PokupkiActivity.empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void removePokupka(Pokupka pokupka){
        userRef.child("Pokupki").child(pokupka.getId()).removeValue();
    }

    public void loadCommentToDB(Comment comment, Product product){
        myRef.child(product.getId()).child("Comments").child(comment.getId()).setValue(comment);
        userRef.child("Comments").child(comment.getId()).setValue(comment);
        userRef.child("Comments").child(comment.getId()).child("productName").setValue(product.getName()
        + " " + product.getFleshMemory() + "gb");
    }

    public void removeMyComment(Comment comment){
        userRef.child("Comments").child(comment.getId()).removeValue();
    }


    public void removeAdmin(String id){
        FirebaseDatabase.getInstance().getReference("Admins").child(id)
                .removeValue();
    }

    public void removeOrder(String id){
        FirebaseDatabase.getInstance().getReference("Orders").child(id)
                .removeValue();
    }

    public void getUserById(SomeInt obj){
        database.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot users: snapshot.getChildren()){
                    User user = users.getValue(User.class);
                    list.add(user);
                }
                obj.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
