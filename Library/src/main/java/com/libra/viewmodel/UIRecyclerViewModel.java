package com.libra.viewmodel;

import android.content.Context;
import android.view.View;
import com.libra.R;
import com.libra.http.ApiException;
import com.libra.uirecyclerView.UIRecycleViewAdapter;
import com.libra.uirecyclerView.UIRecyclerView;
import com.libra.uirecyclerView.footer.LoadMoreFooterView;
import com.libra.view.base.BaseActivity;

/**
 * Created by libra on 16/3/9 上午9:58.
 */
public abstract class UIRecyclerViewModel extends ViewModel {

    public int currentPage = 1;
    public long currentCount;
    public long totalCount;
    public UIRecycleViewAdapter mAdapter;
    public UIRecyclerView mUIRecyclerView;


    public UIRecyclerViewModel(Context context) {
        super(context);
    }


    public void setStartPage() {
        this.currentPage = 1;
    }


    /**
     * 刷新
     */
    public void onRefresh(View loadMoreFooterView) {
        setStartPage();
        fetchData();
        if (loadMoreFooterView != null) {
            LoadMoreFooterView view = (LoadMoreFooterView) loadMoreFooterView;
            view.setStatus(LoadMoreFooterView.Status.GONE);
        }
    }


    /**
     * 加载更多
     */
    public void onLoadMore(View loadMoreFooterView) {
        if (loadMoreFooterView != null) {
            LoadMoreFooterView view = (LoadMoreFooterView) loadMoreFooterView;
            if (view.canLoadMore() && mAdapter.getItemCount() > 0) {
                view.setStatus(LoadMoreFooterView.Status.LOADING);
                fetchData();
            }
        }
    }


    public void error(Throwable e) {
        {
            if (currentPage == 1) {
                String errorMessage = context.getString(
                        R.string.http_exception_error);
                if (e != null) {
                    if (e instanceof ApiException) {
                        errorMessage = ((ApiException) e).getCause()
                                                         .getMessage();
                    }
                    else {
                        errorMessage = e.getMessage();
                    }
                }
                ((BaseActivity) context).showShortToast(errorMessage);
                refreshComplete();
            }
            else {
                loadComplete(mUIRecyclerView.getLoadMoreFooterView(), true);
                refreshComplete();
            }
        }
    }


    public void refreshComplete() {
        if (mUIRecyclerView != null) {
            mUIRecyclerView.setRefreshing(false);
        }
    }


    /**
     * 判断是否还能加载
     */
    public void loadComplete(View loadMoreFooterView, boolean error) {
        if (!error) {
            currentPage++;
            currentCount = mAdapter.getItemCount();
            if (currentCount == 0) {
                ((LoadMoreFooterView) loadMoreFooterView).setStatus(
                        LoadMoreFooterView.Status.GONE);
            }
            else if (totalCount <= currentCount) {
                ((LoadMoreFooterView) loadMoreFooterView).setStatus(
                        LoadMoreFooterView.Status.THE_END);
            }
            else {
                ((LoadMoreFooterView) loadMoreFooterView).setStatus(
                        LoadMoreFooterView.Status.GONE);
            }
        }
        else {
            ((LoadMoreFooterView) loadMoreFooterView).setStatus(
                    LoadMoreFooterView.Status.ERROR);
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
