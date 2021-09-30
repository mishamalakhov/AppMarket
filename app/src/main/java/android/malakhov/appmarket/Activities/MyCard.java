package android.malakhov.appmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyCard extends AppCompatActivity {

    private TextView cardNumber, cardDate, CVC;
    private Button delete;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_card);

        init();

        UserRepository.get().getCardNumber(new ICallback() {
            @Override
            public void callingBack(Object obj) {
                String number = (String)obj;
                StringBuilder sb = new StringBuilder();

                for(int index=0; index < number.length(); index+=4) {
                    sb.append(number.substring((index), (index+4)));
                    sb.append(" ");
                }
                cardNumber.setText(sb);
            }
        });

        UserRepository.get().getCardDate(new ICallback() {
            @Override
            public void callingBack(Object obj) {
                String date = (String)obj;
                cardDate.setText(date.substring(0,2) + "/" + date.substring(2,4));
            }
        });

        UserRepository.get().getCardCVC(new ICallback() {
            @Override
            public void callingBack(Object obj) {
                CVC.setText((String)obj);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRepository.get().deleteCard();
                finish();
            }
        });

    }

    public void init(){
        cardNumber = findViewById(R.id.cardNumber);
        cardDate = findViewById(R.id.cardDate);
        CVC = findViewById(R.id.CVC);
        delete = findViewById(R.id.delete);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
    }

}