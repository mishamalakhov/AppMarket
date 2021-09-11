package android.malakhov.appmarket;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProductInfo extends AppCompatActivity {


    public static final String TAG1 = "product";
    public static final String TAG2 = "amount";
    private InfoAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static ViewPager mViewPager;
    private Product mProduct;
    private final String[] characteristics = {"Тип продукта", "Название", "Процессор", "ОЗУ",
            "Флеш память", "Емкость аккумулятора", "Диагональ экрана", "Основная камера",
            "Фронтальная камера", "Год выхода на рынок", "Цвет", "Цена"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        Intent intent = getIntent();
        mProduct = (Product) intent.getSerializableExtra(MarketFragment.TAG0);

        mRecyclerView = findViewById(R.id.info_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InfoAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }






    public class InfoHolder extends RecyclerView.ViewHolder{

        private List<String> photoURI = new ArrayList<>();
        private TextView chkType;
        private TextView chk;
        private Button corzina;
        private Button buy;

        public InfoHolder(View itemView, int type) {
            super(itemView);

            if (type == 0) {
                photoURI = mProduct.getPhotoURI();
                mViewPager = itemView.findViewById(R.id.photo_pager);
                FragmentManager fragmentManager = getSupportFragmentManager();
                mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {


                    @Override
                    public Fragment getItem(int position) {
                        String uri = photoURI.get(position);
                        return PhotoPagerFragment.newInstance(uri);
                    }

                    @Override
                    public int getCount() {
                        return photoURI.size();
                    }
                });
            }

            if (type == 1){
                TextView name = itemView.findViewById(R.id.nameTV);
                name.setText(mProduct.getName() + " " + mProduct.getFleshMemory() + "gb");
                TextView price = itemView.findViewById(R.id.priceTV);
                price.setText(String.valueOf(mProduct.getPrice()) + " Br");
                TextView amount = itemView.findViewById(R.id.amount);
                amount.setText(String.valueOf(mProduct.getAmount()) + "шт");
                RatingBar ratingBar = itemView.findViewById(R.id.ratingBar);
                ratingBar.setRating(mProduct.getAverageRating());

                TextView comments = itemView.findViewById(R.id.comments);
                comments.setText("Отзывы " + "(" + mProduct.getCommentsCount() + ")");
                comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductInfo.this, UsersComments.class);

                        intent.putExtra("product", (Serializable) mProduct);
                        intent.putExtra("commentsCount", mProduct.getCommentsCount());
                        intent.putExtra("averageRating", mProduct.getAverageRating());
                        startActivity(intent);
                        finish();
                    }
                });
                buy = itemView.findViewById(R.id.buy);
                corzina = itemView.findViewById(R.id.add_to_corzina);

                corzina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductLab.get().addToCorzina(mProduct);
                        Toast.makeText(ProductInfo.this, "Товар добавлен в корзину", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                buy.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                                .getUid()).child("cardNumber");

                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                    if (snapshot.exists()){
                                        Intent intent = new Intent(ProductInfo.this, BuyActivity.class);
                                        intent.putExtra(TAG1, (Serializable) mProduct);
                                        intent.putExtra(TAG2, "1");
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(ProductInfo.this, AddCard.class);
                                        startActivity(intent);
                                    }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            if (type == 2) {
                chkType = itemView.findViewById(R.id.chk_type);
                chk = itemView.findViewById(R.id.chk);
            }
        }

        public void onBind(int position) {

            if (position > 1) {
                chkType.setText(characteristics[position - 2]);
                switch (position) {
                    case 2:
                        chk.setText(mProduct.getType());
                        break;

                    case 3:
                        chk.setText(mProduct.getName());
                        break;

                    case 4:
                        chk.setText(mProduct.getCPU());
                        break;

                    case 5:
                        chk.setText(mProduct.getOZU() + " gb");
                        break;

                    case 6:
                        chk.setText(mProduct.getFleshMemory() + " gb");
                        break;

                    case 7:
                        chk.setText(mProduct.getCapacity() + " mah");
                        break;

                    case 8:
                        chk.setText(mProduct.getDiagonal() + " dm");
                        break;

                    case 9:
                        if (mProduct.getMainCamera() == 0)
                            chk.setText("Нет");
                        else
                            chk.setText(mProduct.getMainCamera() + " px");
                        break;

                    case 10:
                        if (mProduct.getFrontCamera() == 0)
                            chk.setText("Нет");
                        else
                            chk.setText(mProduct.getFrontCamera() + " px");
                        break;

                    case 11:
                        chk.setText(String.valueOf(mProduct.getDateOfRelease()));
                        break;

                    case 12:
                        chk.setText(mProduct.getColor());
                        break;

                        case 13:
                        chk.setText(mProduct.getPrice() + " Br");
                        break;
                }

            }
        }
    }




    public class InfoAdapter extends RecyclerView.Adapter<InfoHolder>{


        public InfoAdapter() {
        }

        @NonNull
        @Override
        public InfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == 0){
                View v = getLayoutInflater().inflate(R.layout.info_photo, parent, false);
                return new InfoHolder(v,viewType);
            }
            if (viewType == 1){
                View v = getLayoutInflater().inflate(R.layout.after_photo, parent, false);
                return new InfoHolder(v,viewType);
            }
            View v = getLayoutInflater().inflate(R.layout.slected_product_chk, parent, false);
            return new InfoHolder(v,viewType);
        }

        @Override
        public void onBindViewHolder(ProductInfo.InfoHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return 14;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return 0;
            if (position == 1)
                return 1;
            return 2;
        }
    }

}