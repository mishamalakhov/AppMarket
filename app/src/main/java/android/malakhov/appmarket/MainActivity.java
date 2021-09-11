package android.malakhov.appmarket;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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