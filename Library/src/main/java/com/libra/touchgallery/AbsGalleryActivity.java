package com.libra.touchgallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.libra.R;
import com.libra.view.base.BaseActivity;
import java.util.ArrayList;
import uk.co.senab.photoview.PhotoViewAttacher;

public abstract class AbsGalleryActivity extends BaseActivity {

    protected GalleryViewPager mViewPager;

    private TextView mPosition, mSum;

    public static final String ACT_EXTRANAME_PICPATHS = "picPaths";

    public static final String ACT_EXTRANAME_PICPATHS_CURRENT = "currentIndex";

    public static final String ACT_EXTRANAME_ISSHOWNUM = "isShowNum";


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryactivity_layout);
        ArrayList<String> items = getIntent().getStringArrayListExtra(
                ACT_EXTRANAME_PICPATHS);
        int position = getIntent().getIntExtra(ACT_EXTRANAME_PICPATHS_CURRENT,
                0);
        boolean isShowNum = getIntent().getBooleanExtra(ACT_EXTRANAME_ISSHOWNUM,
                true);
        mPosition = (TextView) findViewById(R.id.position);
        mSum = (TextView) findViewById(R.id.sum);
        if (items != null && items.size() > 0) {
            mPosition.setText(String.valueOf(position + 1));
            mSum.setText(String.valueOf(items.size()));
            if (!isShowNum) {
                findViewById(R.id.position_layout).setVisibility(View.GONE);
            }
            mViewPager = (GalleryViewPager) findViewById(R.id.galleryviewpager);
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mViewPager.setOnPageChangeListener(
                    new ViewPager.SimpleOnPageChangeListener() {
                        @Override public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            mPosition.setText(String.valueOf(position + 1));
                        }
                    });
            mViewPager.setOnPhotoTapListener(
                    new PhotoViewAttacher.OnPhotoTapListener() {
                        @Override
                        public void onPhotoTap(View view, float x, float y) {
                            finish();
                        }
                    });
            setPageAdapter(items);
            mViewPager.setCurrentItem(position);
        }
    }


    public abstract void setPageAdapter(ArrayList<String> items);
}