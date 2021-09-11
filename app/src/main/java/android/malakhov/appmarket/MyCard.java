package android.malakhov.appmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyCard extends AppCompatActivity {

    private TextView cardNumber, cardDate, CVC;
    private Button delete;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;


    public void init(){
        cardNumber = findViewById(R.id.cardNumber);
        cardDate = findViewById(R.id.cardDate);
        CVC = findViewById(R.id.CVC);
        delete = findViewById(R.id.delete);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);
        Log.d("gfmkg", "Yes");

        init();

        ref.child("cardNumber").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String number = snapshot.getValue().toString();
                StringBuilder sb = new StringBuilder();

                for(int index=0; index < number.length(); index+=4) {
                    sb.append(number.substring((index), (index+4)));
                    sb.append(" ");
                }
                cardNumber.setText(sb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ref.child("cardDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String date = snapshot.getValue().toString();
                cardDate.setText(date.substring(0,2) + "/" + date.substring(2,4));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("CVC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CVC.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("cardNumber").removeValue();
                ref.child("cardDate").removeValue();
                ref.child("CVC").removeValue();
                finish();
            }
        });

    }
}