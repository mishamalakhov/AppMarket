package android.malakhov.appmarket.Activities;

import android.content.Intent;
import android.malakhov.appmarket.Data.Comments.Comment;
import android.malakhov.appmarket.Data.Comments.CommentsRepository;
import android.malakhov.appmarket.Data.Products.Product;
import android.malakhov.appmarket.Data.Products.ProductRepository;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SendComment extends AppCompatActivity {

    private TextView aboutWhat;
    private RatingBar ratingBar;
    private List<EditText> edit = new ArrayList<>();
    private List<ImageView> addBtns = new ArrayList<>();
    private Button sendComment;
    private Product product;
    private int RequestCode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_comment);
        init();
        product = (Product) getIntent().getSerializableExtra("product");
        aboutWhat.setText("Отзыв про " + product.getName() + " " + product.getOZU() + "gb");

        for (ImageView btn : addBtns) {
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

                if (ratingBar.getRating() == 0) {
                    Toast.makeText(SendComment.this, "Минимальная оценка равна 1", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (EditText text : edit) {
                    if (text.getText().toString().equals("")) {
                        Toast.makeText(SendComment.this, "Вы пропустили поле", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                Comment comment = new Comment();
                comment.setRating((int) ratingBar.getRating());
                comment.setCommentText(edit.get(0).getText().toString());
                comment.setTitle(edit.get(1).getText().toString());
                comment.setAdvantageText(edit.get(2).getText().toString());
                comment.setDisadvantageText(edit.get(3).getText().toString());
                UserRepository.get().getCurrentUser(new ICallback() {
                    @Override
                    public void callingBack(Object obj) {
                        comment.setUserID((String)obj);
                    }
                });
                comment.setProductName(product.getName() + " " + product.getFleshMemory());
                ProductRepository.get().setRating(product, (int) ratingBar.getRating());

                List<ImageView> list = new ArrayList<>();
                for (ImageView photo : addBtns)
                    if (photo.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.add_image).
                            getConstantState())
                        list.add(photo);

                CommentsRepository.get().loadCommentToDB(comment, product, list);
                Toast.makeText(SendComment.this, "Отзыв отправлен", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    public void init() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED)
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
}
