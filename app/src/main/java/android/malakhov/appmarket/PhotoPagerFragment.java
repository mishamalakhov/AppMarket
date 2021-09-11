package android.malakhov.appmarket;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;


public class PhotoPagerFragment extends Fragment {

    private String photoUri;
    private PhotoView mImageView;
    private PhotoViewAttacher mAttacher;

    public PhotoPagerFragment(String uri) {
        photoUri = uri;
    }


    public static Fragment newInstance(String uri) {
        return new PhotoPagerFragment(uri);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_photo_pager, container, false);
        mImageView =v.findViewById(R.id.photoView);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.update();
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.update();

        Picasso.get().load(photoUri).into((ImageView) v);
        return v;
    }
}