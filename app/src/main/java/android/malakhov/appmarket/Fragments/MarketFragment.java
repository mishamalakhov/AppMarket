package android.malakhov.appmarket.Fragments;


import android.content.Intent;
import android.malakhov.appmarket.Activities.CharacteristicsActivity;
import android.malakhov.appmarket.Activities.FilterActivity;
import android.malakhov.appmarket.Activities.ProductInfo;
import android.malakhov.appmarket.CompareWithFilters;
import android.malakhov.appmarket.Data.Products.Product;
import android.malakhov.appmarket.Data.Products.ProductRepository;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.ProductViewModel;
import android.malakhov.appmarket.R;
import android.malakhov.appmarket.Design.SpacesItemDecoration;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;
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
    private TextView sort, date, expensive, cheap, popular, lastBtn, filter, empty;
    public static final String TAG0 = "PoductExtra";
    private String whoIsUser = "";
    private List<Product> copy = new ArrayList<Product>(productList);
    private ProgressBar mProgressBar;
    private Toast mToast;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        marketBinding = DataBindingUtil
                .inflate(inflater, R.layout.market_fragment_screen, container, false);
        init(marketBinding);

        marketBinding.recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        marketBinding.recycler.addItemDecoration(new SpacesItemDecoration(30));//Добавляет одинаковые отступы в recycler
        mAdapter = new MarketFragment.ProductAdapter();
        marketBinding.recycler.setAdapter(mAdapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                productList.clear();
                mAdapter.notifyDataSetChanged();

                if (s.equals("")){
                    productList.addAll(copy);
                    mAdapter.notifyDataSetChanged();
                }else {
                    Log.d("dgmdkg", String.valueOf(copy.size()));
                    for (Product product : copy) {
                        String name = product.getName();
                        if (name.contains(s))
                            productList.add(product);
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

        //Обработка кнопок сортировки
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

    //Инициализация переменных
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

        if (whoIsUser.equals(getResources().getString(R.string.SuperAdmin)) ||
                whoIsUser.equals(getResources().getString(R.string.Admin)))
            plus.setVisibility(View.VISIBLE);
    }

    //Здесь передаю переменную которая определяет роль пользователя
    public static MarketFragment newInstance(String str){
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString("whoIsUser", str);
        fragment.setArguments(args);
        return fragment;
 }

    //Обновляю список товаров
    public void updateUI() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (productList.size() > 0)
            productList.clear();

        //Создаем объект реализующий колбэк
        ICallback callback = new ICallback() {
            @Override
            public void callingBack(Object obj) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (((List<Product>) obj).size() == 0) empty.setVisibility(View.VISIBLE);
                else {
                    empty.setVisibility(View.INVISIBLE);
                    if (FilterActivity.getSettings() == null)
                        productList = CompareWithFilters.compare((List<Product>) obj, FilterActivity.getSettings());
                    mAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    copy.addAll(productList);
                }
            }
        };

        switch (lastBtn.getText().toString()){
            case "Новые":
                ProductRepository.get().getProductsSortedByDate(callback);
                break;
            case "Дорогие":
                ProductRepository.get().getProductsSortedByExpensive(callback);
                break;
            case "Дешевые":
                ProductRepository.get().getProductsSortedByCheap(callback);
                break;
            case "По рейтингу":
                ProductRepository.get().getProductsSortedByRating(callback);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    //Скрытие и появление верхней поисковой панели при прокрутке списка товаров
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

    //
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

            if (whoIsUser.equals(getResources().getString(R.string.SuperAdmin)) ||
                    whoIsUser.equals(getResources().getString(R.string.Admin)))
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
                    ProductRepository.get().deleteProduct(mProduct);
                    productList.remove(mProduct);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        public void onBind(Product product, String path) {

            mRatingBar.setRating(product.getAverageRating());
            mProduct = product;
            binding.setProduct(new ProductViewModel(product));
            photo = binding.productPhoto;

            Picasso.get().load(path).fit().centerInside().into(photo);


            corzina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductRepository.get().addToCorzina(product);
                    if (mToast != null)
                        mToast.cancel();
                    mToast = Toast.makeText(getActivity(), "Товар добавлен в корзину", Toast.LENGTH_SHORT);
                    mToast.show();
                }
            });
        }
    }


    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ProductLayoutBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.product_layout, viewGroup, false);
            return new ProductHolder(binding);
        }

        @Override
        public void onBindViewHolder(ProductHolder productHolder, int position) {
            productHolder.onBind(productList.get(position), productList.get(position).getPhotoURI().get(0));
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
    }
}
