package android.malakhov.appmarket.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.malakhov.appmarket.Activities.AddCard;
import android.malakhov.appmarket.Activities.BuyActivity;
import android.malakhov.appmarket.Activities.MarketActivity;
import android.malakhov.appmarket.Activities.ProductInfo;
import android.malakhov.appmarket.Data.Products.Product;
import android.malakhov.appmarket.Data.Products.ProductRepository;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CorzinaFragment extends Fragment {


    public static final String TAG1 = "product";
    public static final String TAG2 = "amount";
    private RecyclerView mRecyclerView;
    private static CorzinaAdapter mAdapter;
    private static List<Product> list = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        mAdapter.notifyDataSetChanged();
        ProductRepository.get().getCorzinaList(new ICallback() {
            @Override
            public void callingBack(Object obj) {
                if (obj == null) MarketActivity.mainBtn.performClick();
                else {
                    list.addAll((List<Product>) obj);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.corzina_recycler, container, false);

        mRecyclerView = v.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CorzinaAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public class CorzinaHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private TextView opisanie;
        private TextView price;
        private ImageView delete;
        private Button buy;
        private EditText amount;
        private TextView amountTV;


        public CorzinaHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            opisanie = itemView.findViewById(R.id.opisanie);
            price = itemView.findViewById(R.id.price);
            delete = itemView.findViewById(R.id.delete);
            buy = itemView.findViewById(R.id.buy_btn);
            amount = itemView.findViewById(R.id.amount);
            amountTV = itemView.findViewById(R.id.amountTV);

        }


        public void onBind(Product product) {

            Picasso.get().load(product.getPhotoURI().get(0)).fit().centerInside().into(image);
            name.setText(product.getName() + " " + product.getFleshMemory() + "gb");
            opisanie.setText(product.getType() + "/" + product.getName() + "/" + product.getOZU() + "/" + product.getFleshMemory()
                    + "/" + product.getCapacity() + "/" + product.getYear() + "/" + product.getFrontCamera() + "/" + product.getMainCamera());
            price.setText(product.getPrice() + "Br");
            amountTV.setText(String.valueOf(product.getAmount()));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductRepository.get().deleteFromCorzina(product);
                    list.remove(product);
                    if (list.size() == 0)
                        MarketActivity.mainBtn.performClick();
                    mAdapter.notifyDataSetChanged();
                }
            });

            buy.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    UserRepository.get().getCardNumber(new ICallback() {
                        @Override
                        public void callingBack(Object obj) {
                            if (obj != null) {
                                Intent intent = new Intent(getActivity(), BuyActivity.class);
                                intent.putExtra(TAG1, (Serializable) product);
                                intent.putExtra(TAG2, "1");
                                startActivityForResult(intent, 1);
                            } else {
                                Intent intent = new Intent(getActivity(), AddCard.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProductInfo.class);
                    intent.putExtra(MarketFragment.TAG0, product);
                    startActivity(intent);
                }
            });

        }
    }

    public class CorzinaAdapter extends RecyclerView.Adapter<CorzinaHolder> {


        @NonNull
        @Override
        public CorzinaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();

            View v = inflater.inflate(R.layout.corzina_item, parent, false);
            return new CorzinaHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CorzinaHolder holder, int position) {
            if (list.size() > 0)
                holder.onBind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}