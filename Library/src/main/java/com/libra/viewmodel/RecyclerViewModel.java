package com.libra.viewmodel;

import android.content.Context;
import android.os.Handler;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.libra.R;
import com.libra.http.ApiException;
import com.libra.view.base.BaseActivity;

/**
 * Created by libra on 16/3/9 上午9:58.
 */
public abstract class RecyclerViewModel extends ViewModel {

    public int currentPage = 1;
    public long currentCount;
    public long totalCount;
    public RecyclerArrayAdapter mAdapter;
    public EasyRecyclerView mEasyRecyclerView;


    public RecyclerViewModel(Context context) {
        super(context);
    }


    public void setStartPage() {
        this.currentPage = 1;
    }


    public void autoRefresh() {
        mEasyRecyclerView.setRefreshing(true);
        onRefresh();
    }


    /**
     * 刷新
     */
    public void onRefresh() {
        setStartPage();
        fetchData();
    }


    /**
     * 加载更多
     */
    public void onLoadMore() {
        fetchData();
    }


    public void error(Throwable e) {
        {
            if (currentPage == 1) {
                String errorMessage = context.getString(
                        R.string.http_exception_error);
                if (e != null) {
                    if (e instanceof ApiException) {
                        errorMessage = ((ApiException) e).getErrorMessage(
                                context);
                    }
                    else {
                        errorMessage = e.getMessage();
                    }
                }
                ((BaseActivity) context).showShortToast(errorMessage);
                refreshComplete();
            }
            else {
                loadComplete(true);
                refreshComplete();
            }
        }
    }


    public void refreshComplete() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if (mEasyRecyclerView != null) {
                    mEasyRecyclerView.setRefreshing(false);
                }
            }
        }, 50);
    }


    /**
     * 判断是否还能加载
     */
    public void loadComplete(boolean error) {
        if (!error) {
            currentPage++;
            currentCount = mAdapter.getCount();
            if (totalCount <= currentCount) {
                mAdapter.stopMore();
            }
        }
        else {
            mAdapter.pauseMore();
        }
    }


    /**
     * 获取数据数据
     */
    public abstract void fetchData();


    /**
     * 点击RecyclerView 事件
     */
    public void onItemClick(int position) {

    }
}
