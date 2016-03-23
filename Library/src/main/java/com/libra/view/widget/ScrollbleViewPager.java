package com.libra.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by libra on 16/2/17 上午10:54.
 */
public class ScrollbleViewPager extends ViewPager {

    private boolean scrollble = true;

    public ScrollbleViewPager(Context context) {
        super(context);
    }

    public ScrollbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollble){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
