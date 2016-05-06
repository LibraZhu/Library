package com.libra.touchgallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import java.util.List;

public class FilePagerAdapter extends BasePagerAdapter {
    public FilePagerAdapter(Context context, List<String> resources) {
        super(context, resources);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((GalleryViewPager) container).mCurrentView
                = ((FileTouchImageView) object).getImageView();
        if (((GalleryViewPager) container).listener != null) {
            ((GalleryViewPager) container).mCurrentView.setOnPhotoTapListener(
                    ((GalleryViewPager) container).listener);
            ((GalleryViewPager) container).mCurrentView.setOnLongClickListener(
                    ((GalleryViewPager) container).mLongClickListener);
        }
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final FileTouchImageView iv = new FileTouchImageView(mContext);
        iv.setUrl(mResources.get(position));
        iv.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        collection.addView(iv, 0);
        return iv;
    }


    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        super.destroyItem(collection, position, view);
        if (view instanceof ViewGroup) {
            collection.removeView((View) view);
            ViewGroup viewGroup = (ViewGroup) view;
            recycleViewGroupAndChildViews(viewGroup, true);
        }
    }


    private void recycleViewGroupAndChildViews(ViewGroup viewGroup, boolean recycleBitmap) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof WebView) {
                WebView webView = (WebView) child;
                webView.loadUrl("about:blank");
                webView.stopLoading();
                continue;
            }
            if (child instanceof ViewGroup) {
                recycleViewGroupAndChildViews((ViewGroup) child, true);
                continue;
            }
            if (child instanceof ImageView) {
                ImageView iv = (ImageView) child;
                Drawable drawable = iv.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (recycleBitmap && bitmap != null &&
                            !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                iv.setImageBitmap(null);
                iv.setBackgroundDrawable(null);
                continue;
            }
            child.setBackgroundDrawable(null);
        }
        viewGroup.setBackgroundDrawable(null);
    }
}
