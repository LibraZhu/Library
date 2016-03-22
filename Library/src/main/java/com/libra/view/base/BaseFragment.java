package com.libra.view.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by libra on 15/7/28 下午4:53.
 */
public abstract class BaseFragment extends Fragment {

    public void fetchData() {
    }


    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }


    @Override public void onResume() {
        super.onResume();
    }


    @Override public void onPause() {
        super.onPause();
    }


    protected void showProgressDialog(int resId) {
        ((BaseActivity) getActivity()).showProgressDialog(getString(resId));
    }


    /**
     * 描述：显示进度框.
     *
     * @param message the message
     */
    protected void showProgressDialog(String message) {
        ((BaseActivity) getActivity()).showProgressDialog(message);
    }


    /**
     * 关闭进度框
     */
    protected void closeProgressDialog() {
        ((BaseActivity) getActivity()).closeProgressDialog();
    }


    /**
     * 显示短时间Toast
     */
    public void showShortToast(int pResId) {
        showShortToast(getString(pResId));
    }


    /**
     * 显示长时间Toast
     */
    public void showLongToast(int pResId) {
        showLongToast(getString(pResId));
    }


    /**
     * 显示短时间Toast
     */
    public void showShortToast(String pMsg) {
        ((BaseActivity) getActivity()).showShortToast(pMsg);
    }


    /**
     * 显示长时间Toast
     */
    public void showLongToast(String pMsg) {
        ((BaseActivity) getActivity()).showLongToast(pMsg);
    }


    public int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * px转dip
     *
     * @return int
     */
    public  int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
