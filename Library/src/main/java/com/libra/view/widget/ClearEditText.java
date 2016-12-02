package com.libra.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import com.libra.R;

public class ClearEditText extends TextInputEditText implements OnFocusChangeListener,
        TextWatcher
{
    private Drawable mClearDrawable;

    public ClearEditText(Context context)
    {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        if (mClearDrawable == null)
        {
            mClearDrawable = ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_edit_clear);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (getCompoundDrawables()[2] != null)
        {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                boolean touchable = event.getX() > getWidth()
                        - getPaddingRight()
                        - mClearDrawable.getIntrinsicWidth()
                        && event.getX() < getWidth() - getPaddingRight();
                if (touchable)
                {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            setClearIconVisible(getText().length() > 0);
        }
        else
        {
            setClearIconVisible(false);
        }
    }

    public void setClearIconVisible(boolean visible)
    {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after)
    {
        setClearIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after)
    {
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    public void setShakeAnimation()
    {
        this.setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts)
    {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    /**
     * 设置带图片的hint
     */
    public void setHintWithDrawable(Context context, String str)
    {
        String token = "<image>    ";
        Drawable d = ContextCompat.getDrawable(getContext(),
                R.drawable.ic_edit_search);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        SpannableStringBuilder spanStr = new SpannableStringBuilder();
        spanStr.append(token);
        spanStr.append(str);
        spanStr.setSpan(span, 0, token.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setHint(spanStr);
        setSelection(getText().length());
    }
}
