package com.libra.touchgallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;

public class GalleryActivity extends AbsGalleryActivity {

    public static void startFrom(Activity activity, int index, ArrayList<String> picPaths, boolean isShowNum) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(AbsGalleryActivity.ACT_EXTRANAME_PICPATHS, picPaths);
        intent.putExtra(AbsGalleryActivity.ACT_EXTRANAME_PICPATHS_CURRENT, index);
        intent.putExtra(AbsGalleryActivity.ACT_EXTRANAME_ISSHOWNUM, isShowNum);
        activity.startActivity(intent);
    }


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override public void setPageAdapter(ArrayList<String> items) {
        if (items == null) {
            return;
        }
        FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, items);
        mViewPager.setAdapter(pagerAdapter);
    }
}