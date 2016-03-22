package com.libra.view.base;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by libra on 16/3/9 下午1:10.
 */
public abstract class BindingViewHolder<B extends ViewDataBinding>
        extends BaseViewHolder {

    private B binding;


    public BindingViewHolder(View itemView) {
        super(itemView);
    }


    public BindingViewHolder(ViewGroup parent, int res) {
        super(parent, res);
    }


    public void setBinding(@NonNull B binding) {
        this.binding = binding;
    }


    public B getBinding() {
        if (binding == null) {
            throw new NullPointerException("You should setBinding first!");
        }
        return binding;
    }
}
