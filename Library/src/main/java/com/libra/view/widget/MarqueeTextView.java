package com.libra.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by libra on 16/3/9 上午10:55.
 */
public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context context) {
        super(context);
    }


    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override public boolean isFocused() {
        return true;
    }
}
