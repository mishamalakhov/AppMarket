package android.malakhov.appmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PokupkiActivity extends AppCompatActivity {

    public static TextView empty;
    public static RecyclerView mRecyclerView;
    private ConstraintLayout parent;
    public static List<Pokupka> list = new ArrayList<>();
    public static Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokupki);
        adapter = new Adapter();
        ProductLab.get().getPokupki();


        empty = findViewById(R.id.empty);
        mRecyclerView = findViewById(R.id.recycler);
        parent = findViewById(R.id.parent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        new ItemTouchHelper(adapter.itemTouchHelperCallBAck).attachToRecyclerView(mRecyclerView);
    }


    public class Holder extends RecyclerView.ViewHolder{

        private TextView name, amount, opisanie, price, date;
        private ImageView photo;

        public Holder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            opisanie = itemView.findViewById(R.id.opisanie);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            photo = itemView.findViewById(R.id.photo);
        }



        @SuppressLint("SetTextI18n")
        public void onBind(Pokupka pokupka){

            Product product = pokupka.getProduct();

            name.setText(product.getName() + " " + product.getFleshMemory()
                    +"gb" + " (" + product.getColor() + ")");

            amount.setText(String.valueOf(pokupka.getAmount()) + " шт");
            opisanie.setText(product.getType()+"/"+product.getName()+"/"+product.getCPU()+"/"
                    +product.getOZU()+"gb/"+product.getFleshMemory()
                    +"gb/"+product.getCapacity()+"mah/"+product.getDiagonal()+
                    "/"+product.getFrontCamera()+"px/"
                    + product.getMainCamera() + "px");
            price.setText(String.valueOf(pokupka.getPrice()) + " Br");

            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(pokupka.getDate());
            date.setText(dateStr);

            Picasso.get().load(product.getPhotoURI().get(0)).into(photo);
        }
    }


    public class Adapter extends RecyclerView.Adapter<Holder>{

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = getLayoutInflater().inflate(R.layout.pokupka, parent, false);

            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.onBind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        ItemTouchHelper.SimpleCallback itemTouchHelperCallBAck = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                ProductLab.get().removePokupka(list.get(viewHolder.getAdapterPosition()));
                list.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
                if (list.size() == 0)
                    MarketActivity.profileBtn.performClick();
            }
        };
    }
}