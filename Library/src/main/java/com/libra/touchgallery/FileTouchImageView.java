package com.libra.touchgallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.libra.R;
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
        mProgressBar = new ProgressBar(mContext);
        params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(30, 0, 30, 0);
        mProgressBar.setLayoutParams(params);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setVisibility(GONE);
        this.addView(mProgressBar);
    }


    public void setUrl(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Glide.with(mContext)
             .load(imageUrl)
             .error(ContextCompat.getDrawable(getContext(),
                     R.color.md_grey_900))
             .placeholder(ContextCompat.getDrawable(getContext(),
                     R.color.md_grey_900))
             .listener(new RequestListener<String, GlideDrawable>() {
                 @Override
                 public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                     return false;
                 }


                 @Override
                 public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                     return false;
                 }
             })
             .into(new GlideDrawableImageViewTarget(mImageView) {
                 @Override
                 public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                     mProgressBar.setVisibility(View.GONE);
                     mImageView.setVisibility(View.VISIBLE);
                     super.onResourceReady(resource, animation);
                 }


                 @Override
                 public void onLoadFailed(Exception e, Drawable errorDrawable) {
                     mProgressBar.setVisibility(View.GONE);
                     Log.d("onLoadFailed", "onLoadFailed");
                     super.onLoadFailed(e, errorDrawable);
                 }


                 @Override public void onLoadCleared(Drawable placeholder) {
                     mProgressBar.setVisibility(View.GONE);
                     super.onLoadCleared(placeholder);
                 }


                 @Override public void onLoadStarted(Drawable placeholder) {
                     mProgressBar.setVisibility(View.VISIBLE);
                     Log.d("onLoadStarted", "onLoadStarted");
                     super.onLoadStarted(placeholder);
                 }
             });
    }
}
