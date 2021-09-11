package android.malakhov.appmarket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyComment extends AppCompatActivity {

    private TextView aboutWhat;
    private RatingBar ratingBar;
    private List<EditText> edit = new ArrayList<>();
    private List<ImageView> addBtns = new ArrayList<>();
    private Button sendComment;
    private Product product;
    private int RequestCode;
    private StorageReference storageRef;
    private Uri uploadURI;
    private int i = 0;
    private int count;
    private int allRating;




    public void init(){
        aboutWhat = findViewById(R.id.aboutWhat);
        ratingBar = findViewById(R.id.ratingBar4);
        edit.add(findViewById(R.id.comment));
        edit.add(findViewById(R.id.title));
        edit.add(findViewById(R.id.advantages));
        edit.add(findViewById(R.id.disadvantages));
        addBtns.add(findViewById(R.id.add1));
        addBtns.add(findViewById(R.id.add2));
        addBtns.add(findViewById(R.id.add3));
        sendComment = findViewById(R.id.sendBtn);
        storageRef = FirebaseStorage.getInstance().getReference("ProductImages");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_comment);
        init();
        product = (Product) getIntent().getSerializableExtra("product");
        aboutWhat.setText("Отзыв про " +product.getName() + " " + product.getOZU() + "gb");

        for (ImageView btn : addBtns){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    if (((ImageView) v) == addBtns.get(0))
                        RequestCode = 1;
                    if (((ImageView) v) == addBtns.get(1))
                        RequestCode = 2;
                    if (((ImageView) v) == addBtns.get(2))
                        RequestCode = 3;

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
                }
            });
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ratingBar.getRating() == 0){
                    Toast.makeText(MyComment.this, "Минимальная оценка равна 1", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (EditText text : edit){
                    if (text.getText().toString().equals("")){
                        Toast.makeText(MyComment.this, "Вы пропустили поле", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                Comment comment = new Comment();
                comment.setRating((int) ratingBar.getRating());
                comment.setCommentText(edit.get(0).getText().toString());
                comment.setTitle(edit.get(1).getText().toString());
                comment.setAdvantageText(edit.get(2).getText().toString());
                comment.setDisadvantageText(edit.get(3).getText().toString());
                comment.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                incrementCounter((int)ratingBar.getRating());

                loadPhotoToDb(addBtns, comment);
                Toast.makeText(MyComment.this, "Отзыв отправлен", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED)
            return;
        else {
            if (RequestCode == 1)
                addBtns.get(0).setImageURI(data.getData());
            if (RequestCode == 2)
                addBtns.get(1).setImageURI(data.getData());
            if (RequestCode == 3)
                addBtns.get(2).setImageURI(data.getData());
        }
    }


    public void loadPhotoToDb (List<ImageView> images, Comment comment){


        final StorageReference idRef = storageRef.child(comment.getId());


                if (i == 3){
                    ProductLab.get().loadCommentToDB(comment, product);
                    Toast.makeText(MyComment.this, "Отзыв отправлен", Toast.LENGTH_SHORT);
                }else
                    if (!(images.get(i).getDrawable().getConstantState() == getResources().getDrawable(R.drawable.add_image).getConstantState())) {
                    final StorageReference ref = idRef.child(String.valueOf(i));

                    Bitmap bitmap = ((BitmapDrawable) images.get(i).getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
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
                            comment.getPhotoURI().add(uploadURI.toString());
                            if (i == 2) {
                                ProductLab.get().loadCommentToDB(comment, product);
                            } else {
                                i++;
                                loadPhotoToDb(images, comment);
                            }
                        }
                    });
                }else {
                        i++;
                        loadPhotoToDb(images, comment);
                    }
        }


    public void incrementCounter(int rating) {
        FirebaseDatabase.getInstance().getReference("Products")
                .child(product.getId()).child("allRating").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                currentData.setValue((Long) currentData.getValue() + rating);
                allRating = currentData.getValue(Integer.class);
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                incrementComments();
            }
        });
    }

    public void incrementComments(){
        FirebaseDatabase.getInstance().getReference("Products")
                .child(product.getId()).child("commentsCount").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                currentData.setValue((Long) currentData.getValue() + 1);
                count = currentData.getValue(Integer.class);
                return Transaction.success(currentData);
            }
            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                float averageRating = allRating/(float)count;
                FirebaseDatabase.getInstance().getReference("Products").child(product.getId())
                        .child("averageRating").setValue(averageRating);
            }
        });
    }
}
