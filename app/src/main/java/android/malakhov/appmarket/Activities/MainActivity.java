package android.malakhov.appmarket.Activities;


import android.content.Intent;
import android.malakhov.appmarket.Fragments.AuntificationFragment;
import android.malakhov.appmarket.R;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.auth_container);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){

            if (fragment == null) {
                fragment = new AuntificationFragment();
                manager.beginTransaction()
                        .add(R.id.auth_container, fragment)
                        .commit();
            }

        }else{
            Intent intent = new Intent(this, MarketActivity.class);
            startActivity(intent);
            finish();
        }

    }
}