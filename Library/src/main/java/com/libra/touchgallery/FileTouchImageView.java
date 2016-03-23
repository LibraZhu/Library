package com.libra.touchgallery;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import uk.co.senab.photoview.PhotoView;

public class FileTouchImageView extends RelativeLayout {
    protected ProgressBar mProgressBar;

    protected PhotoView mImageView;

    protected Context mContext;


    public FileTouchImageView(Context ctx) {
        super(ctx);
        mContext = ctx;
        init();
    }


    public FileTouchImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }


    public PhotoView getImageView() {
        return mImageView;
    }


    @SuppressWarnings("deprecation") protected void init() {
        mImageView = new PhotoView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        //mImageView.setVisibility(GONE);
        mProgressBar = new ProgressBar(mContext);
        params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(30, 0, 30, 0);
        mProgressBar.setLayoutParams(params);
        mProgressBar.setIndeterminate(false);
        this.addView(mProgressBar);
    }


    public void setUrl(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            mProgressBar.setVisibility(View.GONE);
            return;
        }
        Glide.with(mContext)
             .load(imageUrl)
             .placeholder(-1)
             .into(new GlideDrawableImageViewTarget(mImageView) {
                 @Override
                 public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                     super.onResourceReady(resource, animation);
                     mProgressBar.setVisibility(View.GONE);
                 }
             });
    }
}
