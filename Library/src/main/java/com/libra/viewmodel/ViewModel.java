package com.libra.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

/**
 * Created by libra on 16/3/7 下午1:25.
 */
public abstract class ViewModel extends BaseObservable {
    protected Context context;


    public ViewModel(Context context) {
        this.context = context;
    }
}
