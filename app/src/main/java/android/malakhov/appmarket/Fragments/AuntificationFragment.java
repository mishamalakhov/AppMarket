package android.malakhov.appmarket.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.malakhov.appmarket.Activities.MarketActivity;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class AuntificationFragment extends Fragment {

    private Button loginBtn, regButton;
    private EditText emaileEdit, passwordEdit;
    private TextView error, logo;
    private String ErrorMessage;
    private ConstraintLayout constr;


    private void intit(View v) {
        loginBtn = v.findViewById(R.id.sign_button);
        emaileEdit = v.findViewById(R.id.email);
        passwordEdit = v.findViewById(R.id.password);
        error = v.findViewById(R.id.error);
        logo = v.findViewById(R.id.logo);
        regButton = v.findViewById(R.id.reg_button);
        constr = v.findViewById(R.id.parentConstr);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.auntification_layout, container, false);
        intit(v);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegFragment rfr = new RegFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_container, rfr, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


        constr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.GONE);
            }
        });

        return v;
    }


    public void updateUi() {

        Intent intent = new Intent(getActivity(), MarketActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    public void login() {
        if (emaileEdit.getText().toString().equals("") || passwordEdit.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity(), "Вы не ввели полностью данные", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            UserRepository.get().signIn(emaileEdit.getText().toString(), passwordEdit.getText().toString(), new ICallback() {
                @Override
                public void callingBack(Object obj) {
                    if (((Task<AuthResult>) obj).isSuccessful())
                        updateUi();
                    else {

                        try {
                            ErrorMessage = ((Task<AuthResult>) obj).getResult().toString();
                        } catch (Exception e) {
                            logo.setVisibility(View.INVISIBLE);
                            error.setVisibility(View.VISIBLE);
                            error.setText(e.getMessage().substring(e.getMessage().indexOf(":") + ":".length()));

                            if (e.getMessage().contains("email")) {
                                GradientDrawable gradientDrawable = (GradientDrawable) emaileEdit.getBackground();
                                gradientDrawable.setStroke(3, Color.RED);
                            }

                            if (e.getMessage().contains("password")) {
                                GradientDrawable gradientDrawable = (GradientDrawable) passwordEdit.getBackground();
                                gradientDrawable.setStroke(3, Color.RED);
                            }
                        }
                    }
                }
            });
        }
    }


}
