package com.libra.view.base;

import android.databinding.ViewDataBinding;
import com.libra.uirecyclerView.UIViewHolder;

/**
 * Created by libra on 16/6/15 上午10:43.
 */
public class BindingUIViewHolder<B extends ViewDataBinding>
        extends UIViewHolder {

    private B binding;


    public BindingUIViewHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }


    public B getBinding() {
        if (binding == null) {
            throw new NullPointerException("You should setBinding first!");
        }
        return binding;
    }
}

