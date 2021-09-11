package android.malakhov.appmarket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class BuyActivity extends AppCompatActivity {

    private TextView name, price, amount, itogo,
            nameIs, priceIs, amountIs, itogoIs;
    private Button buy;
    private Product product;
    private String amountNumber;
    private EditText phone, adress;


    public void init() {
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        amount = findViewById(R.id.amount);
        itogo = findViewById(R.id.itogo);
        nameIs = findViewById(R.id.nameIs);
        priceIs = findViewById(R.id.priceIs);
        amountIs = findViewById(R.id.amountIs);
        itogoIs = findViewById(R.id.itogoIs);
        buy = findViewById(R.id.buy);
        phone = findViewById(R.id.phone);
        adress = findViewById(R.id.adress);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_activity);
        init();

        product = (Product) getIntent().getSerializableExtra(ProductInfo.TAG1);
        amountNumber = getIntent().getStringExtra(ProductInfo.TAG2);


        nameIs.setText(product.getName() + " " + product.getFleshMemory() + "gb");
        priceIs.setText(product.getPrice() + " Br");
        amountIs.setText(String.valueOf(amountNumber));

        int ITOGO = product.getPrice() *Integer.parseInt(amountNumber);
        itogoIs.setText(String.valueOf(ITOGO) + " Br");



        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (phone.getText().toString().equals("") ||
              adress.getText().toString().equals("")){
                  Toast.makeText(BuyActivity.this, "Вы пропустили поле", Toast.LENGTH_SHORT).show();
                  return;
              }

                Pokupka pokupka = new Pokupka();
                pokupka.setAmount(Integer.parseInt(amountIs.getText().toString()));
                pokupka.setProduct(product);
                pokupka.setPrice(ITOGO);

                ProductLab.get().addToPokupki(pokupka);

              Order order = new Order();
              order.setAdress(adress.getText().toString());
              order.setUserNumber(phone.getText().toString());
              order.setPokupka(pokupka);
              order.setAmount(Integer.parseInt(amountIs.getText().toString()));
              order.setItogo(ITOGO);

              FirebaseDatabase.getInstance().getReference("Orders")
                     .child(order.getId()).setValue(order);

             Toast.makeText(BuyActivity.this, "Оплачено", Toast.LENGTH_LONG).show();
             ProductLab.get().deleteFromCorzina(product);
             finish();

             ProductLab.get().minusAmount(product, Integer.parseInt(amountNumber));

            }
        });
    }
}