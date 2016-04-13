package com.libra.view.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.libra.viewmodel.ViewModel;

/**
 * Created by libra on 16/3/7 下午1:22.
 */
public abstract class BaseBindingActivity<VM extends ViewModel, B extends ViewDataBinding>
        extends BaseActivity {
    private VM viewModel;
    private B binding;


    public void setViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }


    public VM getViewModel() {
        if (viewModel == null) {
            throw new NullPointerException("You should setViewModel first!");
        }
        return viewModel;
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


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().onCreate();
    }


    @Override protected void onStart() {
        super.onStart();
        getViewModel().onStart();
    }


    @Override protected void onStop() {
        super.onStop();
        getViewModel().onStop();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        getViewModel().onDestroy();
    }
}
