package com.libra.touchgallery;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class GalleryViewPager extends ViewPager {
    public PhotoView mCurrentView;
    public OnPhotoTapListener listener;
    public OnLongClickListener mLongClickListener;


    public GalleryViewPager(Context context) {
        super(context);
    }


    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public PhotoView getmCurrentView() {
        return mCurrentView;
    }


    public void init() {
    }


    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        this.listener = listener;
    }


    public void setLongClickListener(OnLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }


    /**
     * 防止双指缩放过小而出异常
     */
    @Override public boolean onInterceptTouchEvent(MotionEvent arg0) {
        boolean b = false;
        try {
            b = super.onInterceptTouchEvent(arg0);
        } catch (Exception e) {

        }
        return b;
    }


    @Override public boolean onTouchEvent(MotionEvent arg0) {
        try {
            super.onTouchEvent(arg0);
        } catch (Exception e) {

        }
        return false;
    }
}
