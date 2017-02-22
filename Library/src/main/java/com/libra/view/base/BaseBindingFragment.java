package com.libra.view.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.libra.viewmodel.ViewModel;

/**
 * Created by libra on 16/3/7 下午1:22.
 */
public abstract class BaseBindingFragment<VM extends ViewModel, B extends ViewDataBinding>
        extends BaseFragment {
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


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = onCreateBindView(inflater, container, savedInstanceState);
        getViewModel().onCreateView();
        return view;
    }


    public abstract View onCreateBindView(LayoutInflater inflater,
                                          @Nullable ViewGroup container,
                                          @Nullable Bundle savedInstanceState);


    @Override public void onDestroyView() {
        super.onDestroyView();
        getViewModel().onDestroyView();
    }


    @Override public void onStart() {
        super.onStart();
        getViewModel().onStart();
    }


    @Override public void onStop() {
        super.onStop();
        getViewModel().onStop();
    }
}
