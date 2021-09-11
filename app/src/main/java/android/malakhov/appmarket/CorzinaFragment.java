package android.malakhov.appmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CorzinaFragment extends Fragment {


    public static final String TAG1 = "product";
    public static final String TAG2 = "amount";
    private RecyclerView mRecyclerView;
    public static CorzinaAdapter mAdapter;
    public static List<Product> list = new ArrayList<>();
    public static final String TAG = "productInfo";
    public static TextView empty;


    @Override
    public void onResume() {
        super.onResume();

        list.clear();
        list = ProductLab.get().getCorzinaList();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.corzina_recycler, container, false);


        list = ProductLab.get().getCorzinaList();
        empty = v.findViewById(R.id.empty);
        mRecyclerView = v.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CorzinaAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public class CorzinaHolder extends RecyclerView.ViewHolder{

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


        public void onBind(Product product){

            Picasso.get().load(product.getPhotoURI().get(0)).into(image);
            name.setText(product.getName() + " " + product.getFleshMemory() + "gb");
            opisanie.setText(product.getType()+"/"+product.getName()+"/"+product.getOZU()+"/"+product.getFleshMemory()
            +"/"+product.getCapacity()+"/"+product.getDateOfRelease()+"/"+product.getFrontCamera()+"/"+ product.getMainCamera());
            price.setText(product.getPrice() + "Br");
            amountTV.setText(String.valueOf(product.getAmount()));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.notifyDataSetChanged();
                    ProductLab.get().deleteFromCorzina(product);
                }
            });

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(amount.getText().toString()) > product.getAmount()){
                        Toast.makeText(getActivity(), "Недостаточно товаров на складе", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).child("cardNumber");

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (snapshot.exists()){
                                Intent intent = new Intent(getActivity(), BuyActivity.class);
                                intent.putExtra(TAG1, (Serializable) product);
                                intent.putExtra(TAG2, amount.getText().toString());
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getActivity(), AddCard.class);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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



    public class CorzinaAdapter extends RecyclerView.Adapter<CorzinaHolder>{


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