package android.malakhov.appmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyCommentsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private Adapter adapter;
    private List<Comment> listComments = new ArrayList<>();
    private ConstraintLayout topCard;
    private DatabaseReference commentRef;

    public void init(){
        topCard = findViewById(R.id.topCard);
        recycler = findViewById(R.id.recyclerView2);
        recycler.setPadding(0,10,0,0);
        findViewById(R.id.leftComment).setVisibility(View.INVISIBLE);
        adapter = new Adapter();

        commentRef = FirebaseDatabase.getInstance().getReference().getRef().
                child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Comments");
    }


    public void loadComments(){

        if (listComments.size() > 0) {
            listComments.clear();
            adapter.notifyDataSetChanged();
        }

        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Comment comment = ds.getValue(Comment.class);
                    listComments.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_comments);
        init();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        topCard.setVisibility(View.GONE);
        loadComments();
        new ItemTouchHelper(adapter.itemTouchHelperCallBAck).attachToRecyclerView(recycler);
    }


    public class Holder extends RecyclerView.ViewHolder{
        private TextView comment,advantages, disadvantages, date2, aboutWhat;
        private List<ImageView> images = new ArrayList<>();
        private RatingBar mRatingBar;
        private ConstraintLayout constraintLayout;

        public Holder(@NonNull View itemView, int viewType) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            advantages = itemView.findViewById(R.id.advantages);
            disadvantages = itemView.findViewById(R.id.disadvantages);
            date2 = itemView.findViewById(R.id.date);
            constraintLayout = itemView.findViewById(R.id.parentConstr);
            images.add((ImageView)itemView.findViewById(R.id.add1));
            images.add((ImageView)itemView.findViewById(R.id.add2));
            images.add((ImageView)itemView.findViewById(R.id.add3));
            mRatingBar = itemView.findViewById(R.id.ratingBar3);
            aboutWhat = itemView.findViewById(R.id.aboutWhat);
            itemView.findViewById(R.id.userPhoto).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.name).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.date).setVisibility(View.INVISIBLE);
            aboutWhat.setVisibility(View.VISIBLE);


            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.date,ConstraintSet.RIGHT,R.id.aboutWhat,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.date,ConstraintSet.TOP,R.id.aboutWhat,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(constraintLayout);
        }

        public void onBind(Comment myComment) {

            commentRef.child(myComment.getId()).child("productName")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){
                                aboutWhat.setText(ds.getValue(String.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            mRatingBar.setRating(myComment.getRating());
            comment.setText(myComment.getCommentText());
            advantages.setText(myComment.getAdvantageText());
            disadvantages.setText(myComment.getDisadvantageText());
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(
                    myComment.getDate()
            );
            date2.setText(dateStr);

            int i = 0;
            for (String uri : myComment.getPhotoURI()) {
                images.get(i).setVisibility(View.VISIBLE);
                Picasso.get().load(uri).into(images.get(i));
                images.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyCommentsActivity.this, ZoomActivity.class);
                        ZoomActivity.photoURI = myComment.getPhotoURI();
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


        ItemTouchHelper.SimpleCallback itemTouchHelperCallBAck = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                ProductLab.get().removeMyComment(listComments.get(viewHolder.getAdapterPosition()));
                listComments.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        };
    }
}
