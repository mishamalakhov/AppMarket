package android.malakhov.appmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersComments extends AppCompatActivity implements View.OnClickListener {

    private Product product;
    private RecyclerView recycler;
    private Button leftCommentBtn;
    private Adapter adapter;
    private ConstraintLayout topCard;
    private List<Comment> listComments = new ArrayList<>();
    private TextView commentsText, sort, date, old, adv, disadv, lastBtn;
    private RatingBar mRatingBar;
    private CardView sortCard;
    private boolean touchCHeck = false;
    private DatabaseReference commentRef;



    public void DBLogic(DataSnapshot dataSnapshot){
        if (listComments.size() > 0) {
            listComments.clear();
            adapter.notifyDataSetChanged();
        }

        for (DataSnapshot ds : dataSnapshot.getChildren()){
            Comment comment = ds.getValue(Comment.class);
            listComments.add(comment);
            adapter.notifyDataSetChanged();
        }
    }


    public void sortWithDate(){
        commentRef.orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBLogic(snapshot);
                Collections.reverse(listComments);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void oldSort(){
        commentRef.orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBLogic(snapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void advSort(){
        commentRef.orderByChild("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBLogic(snapshot);
                Collections.reverse(listComments);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void disadvSort(){
        commentRef.orderByChild("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBLogic(snapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void updateUI(){
        if (lastBtn.getText().equals("Новые"))
            sortWithDate();

        if (lastBtn.getText().equals("Старые")) {

            oldSort();
        }
        if (lastBtn.getText().equals("Положительные"))
            advSort();

        if (lastBtn.getText().equals("Отрицательные"))
            disadvSort();
    }

    public void init(){
        commentsText = findViewById(R.id.comments);
        mRatingBar  = findViewById(R.id.ratingBar2);
        sortCard = findViewById(R.id.sortCard);
        sort = findViewById(R.id.sort);
        date = findViewById(R.id.date);
        old = findViewById(R.id.old);
        disadv = findViewById(R.id.disadv);
        adv = findViewById(R.id.adv);
        lastBtn = findViewById(R.id.date);
        topCard = findViewById(R.id.topCard);
        commentRef = FirebaseDatabase.getInstance().getReference().getRef().
                child("Products").child(product.getId()).child("Comments");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_comments);

        product = (Product) getIntent().getSerializableExtra("product");

        init();

        sortWithDate();
        adapter = new Adapter();
        recycler = findViewById(R.id.recyclerView2);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        commentsText.setText("Отзывы " + "(" + product.getCommentsCount() + ")");
        mRatingBar.setRating(product.getAverageRating());


        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortCard.getVisibility() == View.GONE){
                    Animation a = AnimationUtils.loadAnimation(UsersComments.this, R.anim.un_fade);
                    sortCard.startAnimation(a);
                    sortCard.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                    adv.setVisibility(View.VISIBLE);
                    disadv.setVisibility(View.VISIBLE);
                    old.setVisibility(View.VISIBLE);
                }
                else{
                    Animation a = AnimationUtils.loadAnimation(UsersComments.this, R.anim.fade);
                    sortCard.startAnimation(a);
                    sortCard.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    adv.setVisibility(View.GONE);
                    disadv.setVisibility(View.GONE);
                    old.setVisibility(View.GONE);
                }

            }
        });
        recyclerLogic();
        date.setOnClickListener(this);
        old.setOnClickListener(this);
        disadv.setOnClickListener(this);
        adv.setOnClickListener(this);

        leftCommentBtn = findViewById(R.id.leftComment);
        leftCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersComments.this, MyComment.class);
                intent.putExtra("product", product);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        lastBtn.setTextColor(getResources().getColor(R.color.black));
        ((TextView)v).setTextColor(getResources().getColor(R.color.price));
        lastBtn = (TextView) v;
        date.setVisibility(View.GONE);
        old.setVisibility(View.GONE);
        adv.setVisibility(View.GONE);
        disadv.setVisibility(View.GONE);
        Animation a = AnimationUtils.loadAnimation(UsersComments.this, R.anim.fade);
        sortCard.startAnimation(a);
        sortCard.setVisibility(View.GONE);
        sort.setText(((TextView) v).getText());

        updateUI();
    }

    public void recyclerLogic(){
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20 && !touchCHeck){
                    Animation a = AnimationUtils.loadAnimation(UsersComments.this, R.anim.fade);
                    topCard.startAnimation(a);
                    touchCHeck = true;
                    sort.setClickable(false);
                }
                if (dy < -20 && touchCHeck){
                    Animation a = AnimationUtils.loadAnimation(UsersComments.this, R.anim.un_fade);
                    topCard.startAnimation(a);
                    touchCHeck = false;
                    sort.setClickable(true);
                }

                if (sortCard.getVisibility() == View.VISIBLE) {
                    Animation a = AnimationUtils.loadAnimation(UsersComments.this, R.anim.fade);
                    sortCard.startAnimation(a);
                    sortCard.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    old.setVisibility(View.GONE);
                    adv.setVisibility(View.GONE);
                    disadv.setVisibility(View.GONE);
                }
            }
        });
    }


    public class Holder extends RecyclerView.ViewHolder{
        private TextView comment,advantages, disadvantages, date;
        private List<ImageView> images = new ArrayList<>();
        private RatingBar mRatingBar;

        public Holder(@NonNull View itemView, int viewType) {
            super(itemView);
                comment = itemView.findViewById(R.id.comment);
                advantages = itemView.findViewById(R.id.advantages);
                disadvantages = itemView.findViewById(R.id.disadvantages);
                date = itemView.findViewById(R.id.date);
                images.add((ImageView)itemView.findViewById(R.id.add1));
                images.add((ImageView)itemView.findViewById(R.id.add2));
                images.add((ImageView)itemView.findViewById(R.id.add3));
                mRatingBar = itemView.findViewById(R.id.ratingBar3);

        }

        public void onBind(Comment userComment) {

            FirebaseDatabase.getInstance().getReference("Users").child(userComment.getUserID())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            ((TextView)itemView.findViewById(R.id.name))
                                    .setText(user.getSecondName() + " " + user.getName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                mRatingBar.setRating(userComment.getRating());

                comment.setText(userComment.getCommentText());
                advantages.setText(userComment.getAdvantageText());
                disadvantages.setText(userComment.getDisadvantageText());
                String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(
                        userComment.getDate()
                );
                date.setText(dateStr);

                int i = 0;
                for (String uri : userComment.getPhotoURI()) {
                    images.get(i).setVisibility(View.VISIBLE);
                    Picasso.get().load(uri).into(images.get(i));
                    images.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(UsersComments.this, ZoomActivity.class);
                            ZoomActivity.photoURI = userComment.getPhotoURI();
                            startActivity(intent);
                        }
                    });
                    i++;
                }
        }
    }

    public class Adapter extends RecyclerView.Adapter<Holder>{

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            return new Holder(getLayoutInflater().inflate(R.layout.comments_second_item, parent, false), viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.onBind(listComments.get(position));
        }

        @Override
        public int getItemCount() {
            return listComments.size();
        }

    }
}
