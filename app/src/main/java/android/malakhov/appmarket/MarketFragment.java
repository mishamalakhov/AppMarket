package android.malakhov.appmarket;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.malakhov.appmarket.databinding.MarketFragmentScreenBinding;
import android.malakhov.appmarket.databinding.ProductLayoutBinding;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MarketFragment extends Fragment implements View.OnClickListener {

    private static ProductAdapter mAdapter;
    private EditText search;
    private CardView sortCard;
    private ConstraintLayout topCard;
    private boolean touchCHeck = false;
    private ImageView plus, photo;
    private MarketFragmentScreenBinding marketBinding;
    private List<Product> productList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView sort, date, expensive, cheap, popular, lastBtn, filter, empty;
    public static final String TAG0 = "PoductExtra";
    public static SharedPreferences settings = null;
    private String whoIsUser = "";
    private List<Product> copy = new ArrayList<Product>(productList);
    private ProgressBar mProgressBar;
    private Toast mToast;



    public static MarketFragment newInstance(String str){
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString("whoIsUser", str);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateUI() {

        if (lastBtn.getText().equals("Новые"))
            sortWithDate();
        if (lastBtn.getText().equals("Дорогие")) {
            expensive();
        }
        if (lastBtn.getText().equals("Дешевые"))
            cheap();

        if (lastBtn.getText().equals("По рейтингу"))
            popular();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void DBLogic(DataSnapshot dataSnapshot) {

        if (productList.size() > 0) {
            productList.clear();
            mAdapter.notifyDataSetChanged();
        }

        if (!dataSnapshot.exists())
            empty.setVisibility(View.VISIBLE);
        else{
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                empty.setVisibility(View.INVISIBLE);
                Product product = ds.getValue(Product.class);

                if (product.getAmount() == 0) {
                    ProductLab.get().deleteProductFromDB(product);
                } else {

                    if (settings != null) {
                        if (product.compareWithFilter(settings))
                            productList.add(product);
                    } else
                        productList.add(product);
                }
            }
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void sortWithDate() {


        myRef.orderByChild("dateTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DBLogic(dataSnapshot);
                Collections.reverse(productList);
                copy = new ArrayList<Product>(productList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void expensive() {

        myRef.orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DBLogic(dataSnapshot);
                Collections.reverse(productList);
                copy = new ArrayList<Product>(productList);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void cheap() {

        myRef.orderByChild("price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DBLogic(dataSnapshot);
                mAdapter.notifyDataSetChanged();
                copy = new ArrayList<Product>(productList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void popular(){
        Comparator<Product> comparator = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return Float.compare(o1.getAverageRating(), o2.getAverageRating());
            }
        };
        Collections.sort(productList, comparator);
        Collections.reverse(productList);
        mAdapter.notifyDataSetChanged();
        copy = new ArrayList<Product>(productList);
    }


    public void init(MarketFragmentScreenBinding marketBinding) {
        sortCard = marketBinding.sortCard;
        sort = marketBinding.sort;
        date = marketBinding.date;
        expensive = marketBinding.expensive;
        cheap = marketBinding.cheap;
        popular = marketBinding.popular;
        topCard = marketBinding.topCard;
        plus = marketBinding.plus;
        lastBtn = date;
        search = marketBinding.search;
        filter = marketBinding.filter;
        empty = marketBinding.empty;
        whoIsUser = getArguments().getString("whoIsUser");
        mProgressBar = marketBinding.marketPrgBar;

        if (whoIsUser.equals("SuperAdmin") || whoIsUser.equals("Admin"))
            plus.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Products");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        marketBinding = DataBindingUtil
                .inflate(inflater, R.layout.market_fragment_screen, container, false);
        init(marketBinding);

        marketBinding.recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        marketBinding.recycler.addItemDecoration(new SpacesItemDecoration(30));
        mAdapter = new MarketFragment.ProductAdapter(productList);
        marketBinding.recycler.setAdapter(mAdapter);




        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                productList.clear();
                mAdapter.notifyDataSetChanged();

                if (search.getText().toString().equals("")){
                    for (Product product : copy){
                        productList.add(product);
                    }
                    mAdapter.notifyDataSetChanged();
                }else {
                    for (Product product : copy) {

                        String name = product.getName();
                        if (name.contains(search.getText().toString())) {
                            productList.add(product);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CharacteristicsActivity.class);
                startActivity(intent);
            }
        });

        date.setOnClickListener(this);
        expensive.setOnClickListener(this);
        cheap.setOnClickListener(this);
        popular.setOnClickListener(this);

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortCard.getVisibility() == View.GONE) {
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.un_fade);
                    sortCard.startAnimation(a);
                    sortCard.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                    expensive.setVisibility(View.VISIBLE);
                    cheap.setVisibility(View.VISIBLE);
                    popular.setVisibility(View.VISIBLE);
                } else {
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
                    sortCard.startAnimation(a);
                    sortCard.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    expensive.setVisibility(View.GONE);
                    cheap.setVisibility(View.GONE);
                    popular.setVisibility(View.GONE);
                }

            }
        });
        recyclerLogic();

        return marketBinding.getRoot();
    }


    public void recyclerLogic() {
        marketBinding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20 && !touchCHeck) {
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
                    topCard.startAnimation(a);
                    touchCHeck = true;
                    sort.setClickable(false);
                }
                if (dy < -20 && touchCHeck) {
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.un_fade);
                    topCard.startAnimation(a);
                    touchCHeck = false;
                    sort.setClickable(true);
                }

                if (sortCard.getVisibility() == View.VISIBLE) {
                    Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
                    sortCard.startAnimation(a);
                    sortCard.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    expensive.setVisibility(View.GONE);
                    cheap.setVisibility(View.GONE);
                    popular.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        lastBtn.setTextColor(getResources().getColor(R.color.black));
        ((TextView) v).setTextColor(getResources().getColor(R.color.price));
        lastBtn = (TextView) v;
        date.setVisibility(View.GONE);
        expensive.setVisibility(View.GONE);
        cheap.setVisibility(View.GONE);
        popular.setVisibility(View.GONE);
        Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
        sortCard.startAnimation(a);
        sortCard.setVisibility(View.GONE);
        sort.setText(((TextView) v).getText());

        updateUI();
    }


    private class ProductHolder extends RecyclerView.ViewHolder {
        ProductLayoutBinding binding;
        private Product mProduct;
        private ConstraintLayout clickConstr;
        private ImageView corzina, delete;
        private RatingBar mRatingBar;


        public ProductHolder(ProductLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            clickConstr = binding.onClickConstraint;
            corzina = binding.corzina;
            mRatingBar = binding.ratingBar5;
            delete = binding.delete;

            if (whoIsUser.equals("SuperAdmin") || whoIsUser.equals("Admin"))
                delete.setVisibility(View.VISIBLE);

            clickConstr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProductInfo.class);
                    intent.putExtra(TAG0, (Serializable) mProduct);
                    startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductLab.get().deleteProductFromDB(mProduct);
                }
            });
        }

        public void onBind(Product product, String path) {

            mRatingBar.setRating(product.getAverageRating());
            mProduct = product;
            binding.setProduct(new ProductViewModel(product));
            photo = binding.productPhoto;

            Picasso.get().load(path).into(photo);


            corzina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductLab.get().addToCorzina(product);
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getActivity(), "Товар добавлен в корзину", Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
        }
    }


    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {


        private List<Product> mProducts;

        public ProductAdapter(List<Product> galleryItems) {
            mProducts = galleryItems;
        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ProductLayoutBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.product_layout, viewGroup, false);
            return new ProductHolder(binding);
        }

        @Override
        public void onBindViewHolder(ProductHolder productHolder, int position) {
            productHolder.onBind(mProducts.get(position), mProducts.get(position).getPhotoURI().get(0));
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }
    }
}
