package android.malakhov.appmarket.Activities;

import android.malakhov.appmarket.Data.Orders.Order;
import android.malakhov.appmarket.Data.Orders.OrdersRepository;
import android.malakhov.appmarket.Data.Pokupki.Pokupka;
import android.malakhov.appmarket.Data.Products.Product;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView recycler;
    public static List<Order> list = new ArrayList<>();
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_admins);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        recycler.setAdapter(adapter);
        new ItemTouchHelper(adapter.itemTouchHelperCallBAck).attachToRecyclerView(recycler);
    }


    private class Holder extends RecyclerView.ViewHolder{

        private TextView name, opisanie, amount, price, date, productID,
        orderID, adress, phone;
        private ImageView photo;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            opisanie = itemView.findViewById(R.id.opisanie);
            amount = itemView.findViewById(R.id.amount);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);
            productID = itemView.findViewById(R.id.productID);
            orderID = itemView.findViewById(R.id.orderID);
            photo = itemView.findViewById(R.id.photo);
            adress = itemView.findViewById(R.id.adress);
            phone = itemView.findViewById(R.id.phone);
        }

        public void onBind(Order order){
            Pokupka pokupka = order.getPokupka();
            Product product = pokupka.getProduct();
            name.setText(product.getName() + " " + product.getFleshMemory()
            +"gb" + " (" + product.getColor() + ")");

            opisanie.setText(product.getType()+"/"+product.getName()+"/"+product.getCPU()+"/"
                    +product.getOZU()+"gb/"+product.getFleshMemory()
                    +"gb/"+product.getCapacity()+"mah/"+product.getDiagonal()+
                    "/"+product.getFrontCamera()+"px/"
                    + product.getMainCamera() + "px");

            amount.setText(String.valueOf(product.getAmount()) + " шт");
            price.setText(String.valueOf(product.getPrice() * pokupka.getAmount())+" Br");
            date.setText(String.valueOf(pokupka.getDate()));
            productID.setText("ID товара: " + product.getId());
            orderID.setText("ID заказа: " + order.getId());
            Picasso.get().load(product.getPhotoURI().get(0)).fit().centerInside().into(photo);
            adress.setText("Адресс: " + order.getAdress());
            phone.setText("Номер: " + order.getUserNumber());
        }
    }

    private class Adapter extends RecyclerView.Adapter<Holder>{

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.order_layout, parent, false);
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

                OrdersRepository.get().deleteOrder(list.get(viewHolder.getAdapterPosition()).getId());
                list.remove(viewHolder.getAdapterPosition());
                if (list.size()==0) finish();
                adapter.notifyDataSetChanged();
            }
        };
    }
}
