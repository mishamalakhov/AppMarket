package android.malakhov.appmarket;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.List;

public class ZoomActivity extends AppCompatActivity {

    private static ViewPager mViewPager;
    public static List<String> photoURI;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.info_photo);
        FragmentManager fr = getSupportFragmentManager();
        mViewPager = findViewById(R.id.photo_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fr) {

            @Override
            public Fragment getItem(int position) {
                String uri = photoURI.get(position);
                return PhotoPagerFragment.newInstance(uri);
            }

            @Override
            public int getCount() {
                return photoURI.size();
            }
        });

    }
}