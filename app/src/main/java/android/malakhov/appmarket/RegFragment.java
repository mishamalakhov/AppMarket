package android.malakhov.appmarket;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegFragment extends Fragment {

    private EditText name, secondName, thirdName, email, password;
    private Button next;
    private TextView errorMessage;
    private FirebaseAuth mAuth;
    private ConstraintLayout constrLayout;
    private FirebaseDatabase mDatabase;
    private DatabaseReference userReference;



    public void init(View v){
        name = v.findViewById(R.id.editTextTextPersonName);
        secondName = v.findViewById(R.id.editTextTextPersonName2);
        thirdName = v.findViewById(R.id.editTextTextPersonName3);
        email = v.findViewById(R.id.editTextTextPersonName4);
        password = v.findViewById(R.id.editTextTextPersonName5);
        next = v.findViewById(R.id.button);
        errorMessage = v.findViewById(R.id.textView4);
        constrLayout = v.findViewById(R.id.constr_layout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userReference = mDatabase.getReference("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.reg_screen, container, false);
        init(v);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

        return v;
    }





    public void registration(){
        if (name.getText().toString().equals("")
            || secondName.getText().toString().equals("")
            || thirdName.getText().toString().equals("")
            || email.getText().toString().equals("")
            || password.getText().toString().equals("")
        ){
            Toast toast = Toast.makeText(getActivity(), "Вы не ввели полностью данные", Toast.LENGTH_SHORT);
            toast.show();
        }
         else {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {


                            if (task.isSuccessful()){
                                Intent intent = new Intent(getActivity(), MarketActivity.class);
                                startActivity(intent);
                                getActivity().finish();


                                User user = new User();
                                user.setName(name.getText().toString());
                                user.setSecondName(secondName.getText().toString());
                                user.setThirdName(thirdName.getText().toString());
                                user.setEmail(email.getText().toString());
                                user.setId(mAuth.getCurrentUser().getUid());
                                userReference.child(user.getId()).setValue(user);
                            }
                            else {

                                try {
                                    task.getResult().toString();
                                }catch (Exception e){

                                    errorMessage.setVisibility(View.VISIBLE);
                                    errorMessage.setText(e.getMessage().substring(e.getMessage().indexOf(":") + ":".length()));

                                    if (e.getMessage().contains("email")) {
                                        GradientDrawable gradientDrawable = (GradientDrawable) email.getBackground();
                                        gradientDrawable.setStroke(3, Color.RED);
                                    }

                                    if (e.getMessage().contains("password")) {
                                        GradientDrawable gradientDrawable = (GradientDrawable) password.getBackground();
                                        gradientDrawable.setStroke(3, Color.RED);
                                    }
                                    constrLayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            errorMessage.setVisibility(View.INVISIBLE);
                                            GradientDrawable gradientDrawable1 = (GradientDrawable)email.getBackground();
                                            gradientDrawable1.setStroke(2, Color.WHITE);
                                            GradientDrawable gradientDrawable2 = (GradientDrawable)password.getBackground();
                                            gradientDrawable2.setStroke(2, Color.WHITE);
                                        }
                                    });
                                }
                            }
                        }
                    });
        }


    }
}
