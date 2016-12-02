package com.libra.http;

import android.content.Context;
import com.libra.R;
import com.libra.utils.ToastUtil;
import com.libra.view.base.BaseActivity;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by libra on 16/11/11 下午1:21.
 */

public class ProgressSubscriber<T> extends Subscriber<T> {
    final Action1<? super T> onNext;
    final Action1<Throwable> onError;
    final Action0 onCompleted;
    final Context context;


    public ProgressSubscriber(Context context, Action1<? super T> onNext, Action1<Throwable> onError, Action0 onCompleted) {
        this.onNext = onNext;
        this.onError = onError;
        this.onCompleted = onCompleted;
        this.context = context;
    }


    @Override public void onStart() {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).runOnUiThread(new Runnable() {
                @Override public void run() {
                    ((BaseActivity) context).showProgressDialog();
                }
            });
        }
    }


    @Override public void onNext(T t) {
        ((BaseActivity) context).closeProgressDialog();
        onNext.call(t);
    }


    @Override public void onError(Throwable e) {
        ((BaseActivity) context).closeProgressDialog();
        String errorMessage = context.getString(R.string.http_exception_error);
        if (e != null) {
            if (e instanceof ApiException) {
                errorMessage = ((ApiException) e).getErrorMessage(context);
            }
            else {
                errorMessage = e.getMessage();
            }
        }
        ToastUtil.showToast(context, errorMessage);
        onError.call(e);
    }


    @Override public void onCompleted() {
        onCompleted.call();
    }
}
