package com.libra.viewmodel;

import android.content.Context;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by libra on 16/3/9 上午9:58.
 */
public abstract class RecyclerViewModel extends ViewModel {

    private int currentPage = 1;
    private long currentCount;
    private long totalCount;
    public RecyclerArrayAdapter mAdapter;


    public RecyclerViewModel(Context context) {
        super(context);
    }


    /**
     * 刷新
     */
    public void onRefresh() {
        currentPage = 1;
        fetchData();
    }


    /**
     * 加载更多
     */
    public void onLoadMore() {
        fetchData();
    }


    /**
     * 判断是否还能加载
     */
    public void loadComplete() {
        currentPage++;
        currentCount = mAdapter.getCount();
        if (totalCount <= currentCount) {
            mAdapter.stopMore();
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
