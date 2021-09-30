package android.malakhov.appmarket.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCard extends AppCompatActivity {

    private EditText number, date, CVC;
    private Button bind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        init();

        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (number.getText().toString().equals("") ||
                        date.getText().toString().equals("")||
                        CVC.getText().toString().equals(""))
                {
                    Toast.makeText(AddCard.this, "Вы пропустили поле", Toast.LENGTH_SHORT).show();
                }else{
                    if (number.getText().length() != 16) {
                        Toast.makeText(AddCard.this, "Номер карты должен состоять из 16 цифр", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (date.getText().length() != 4) {
                        Toast.makeText(AddCard.this, "Дата должна состоять из 4 цифр", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (CVC.getText().length() != 3) {
                        Toast.makeText(AddCard.this, "CVC должен состоять из 3 цифр", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    UserRepository.get().addCardToDB(number.getText().toString(), date.getText().toString(),
                            CVC.getText().toString());
                    finish();
                }
            }
        });
    }

    public void init(){

        number = findViewById(R.id.number);
        date = findViewById(R.id.date);
        CVC = findViewById(R.id.CVC);
        bind = findViewById(R.id.bind);
    }
}