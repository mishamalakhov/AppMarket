package android.malakhov.appmarket.Activities;

import android.content.Context;
import android.malakhov.appmarket.Data.Users.UserRepository;
import android.malakhov.appmarket.Fragments.CorzinaFragment;
import android.malakhov.appmarket.Fragments.MarketFragment;
import android.malakhov.appmarket.Fragments.ProfileFragment;
import android.malakhov.appmarket.ICallback;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MarketActivity extends AppCompatActivity implements View.OnClickListener {


    public static LinearLayout mainBtn, corzinaBtn, profileBtn, lastBtn;
    private FragmentManager manager;
    private static String userRole = "";
    private FirebaseDatabase db;
    private Toast mToast;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_activity);


        this.getSharedPreferences("Filter", Context.MODE_PRIVATE)
                .edit().clear().apply();

        intit();

        mainBtn.setBackgroundColor(getResources().getColor(R.color.btn_click));

        mainBtn.setOnClickListener(this);
        corzinaBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);

        manager = getSupportFragmentManager();
        checkWhoIsUser();

        db = FirebaseDatabase.getInstance();
    }

    public void intit(){
        mainBtn = findViewById(R.id.btn_main);
        corzinaBtn = findViewById(R.id.btn_korzina);
        profileBtn = findViewById(R.id.btn_profile);
        lastBtn = mainBtn;
        lastBtn.setBackgroundColor(getResources().getColor(R.color.btn_click));
    }

    public void checkWhoIsUser(){
        UserRepository.get().getUserRole(getResources(), new ICallback() {
            @Override
            public void callingBack(Object obj) {
                userRole = (String)obj;
                Fragment fragment = MarketFragment.newInstance(userRole);
                manager.beginTransaction()
                        .add(R.id.market_container, fragment, "MarketFragment")
                        .commit();
            }
        });
    }

    @Override
    public void onClick(View v) {

       if (v.getId() == R.id.btn_main){

           if (manager.findFragmentByTag("MarketFragment") == null) {

               Fragment fragment = MarketFragment.newInstance(userRole);
               manager.beginTransaction()
                       .replace(R.id.market_container, fragment,"MarketFragment" )
                       .commit();
           }

           lastBtn.setBackgroundColor(getResources().getColor(R.color.white));
           lastBtn = mainBtn;
       }

       if (v.getId() == R.id.btn_korzina){

           db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                   .child("Corzina")
                   .addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()){
                       if (manager.findFragmentByTag("CorzinaFragment") == null) {

                           Fragment fragment = new CorzinaFragment();
                           manager.beginTransaction()
                                   .replace(R.id.market_container, fragment, "CorzinaFragment")
                                   .commit();
                       }
                       lastBtn.setBackgroundColor(getResources().getColor(R.color.white));
                       lastBtn = corzinaBtn;
                       lastBtn.setBackgroundColor(getResources().getColor(R.color.btn_click));
                   }else{
                       if (mToast!=null)
                           mToast.cancel();
                       mToast = Toast.makeText(MarketActivity.this,"Корзина пуста", Toast.LENGTH_SHORT);
                       mToast.show();
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }


       if (v.getId() == R.id.btn_profile){

           if (manager.findFragmentByTag("ProfileFragment") == null) {

               ProfileFragment fragment = ProfileFragment.newInstance(userRole);
               manager.beginTransaction()
                       .replace(R.id.market_container, fragment, "ProfileFragment")
                       .commit();
           }
           lastBtn.setBackgroundColor(getResources().getColor(R.color.white));
           lastBtn = profileBtn;
       }
        lastBtn.setBackgroundColor(getResources().getColor(R.color.btn_click));
    }


}
