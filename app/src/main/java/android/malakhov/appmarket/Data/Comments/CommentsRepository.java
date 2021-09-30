package android.malakhov.appmarket.Data.Comments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.malakhov.appmarket.Data.FireBaseDB;
import android.malakhov.appmarket.Data.Products.Product;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CommentsRepository implements ICommentsRepository {

    private static CommentsRepository obj;


    //Singletone
    public static CommentsRepository get() {
        if (obj == null)
            return new CommentsRepository();
        else
            return obj;
    }

    @Override
    public void loadCommentToDB(Comment comment, Product product, List<ImageView> list) {

        if (list.size() == 0) {
            FireBaseDB.get().getDataBase().getReference("Products").child(product.getId())
                    .child("Comments").child(comment.getId()).setValue(comment);
            UserRepository.get().getCurrUserRef().child("Comments").child(comment.getId()).setValue(comment);
            return;
        }

        //Сохранение фоток коммента в  Firebase Storage
        final StorageReference idRef = FireBaseDB.get().getStorageRef().child(comment.getId());
        int i = 0;

        for (ImageView image : list) {
            final StorageReference ref = idRef.child(String.valueOf(i));

            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            UploadTask up = ref.putBytes(byteArray);


            int finalI = i;
            Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {

                    Uri uploadURI = task.getResult();
                    comment.getPhotoURI().add(uploadURI.toString());

                    if (finalI == list.size() - 1) {
                        FireBaseDB.get().getDataBase().getReference("Products").child(product.getId())
                                .child("Comments").child(comment.getId()).setValue(comment);
                        UserRepository.get().getCurrUserRef().child("Comments").child(comment.getId()).setValue(comment);
                    }
                }
            });
            i++;
        }
    }

    @Override
    public void deleteComment(Comment comment) {
        UserRepository.get().getCurrUserRef().child("Comments").child(comment.getId()).removeValue();
    }

    @Override
    public void getUserComments(ICallback callback) {
        UserRepository.get().getCurrUserRef().child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists())
                    callback.callingBack(null);
                else {
                    List<Comment> list = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren())
                        list.add(ds.getValue(Comment.class));
                    callback.callingBack(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void getProductComments(Product product, ICallback callback) {
        FireBaseDB.get().getDataBase().getReference("Products").child(product.getId()).
                child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren())
                    list.add(ds.getValue(Comment.class));
                callback.callingBack(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
