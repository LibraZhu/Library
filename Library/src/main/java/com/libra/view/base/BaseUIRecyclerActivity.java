package com.libra.view.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.libra.R;
import com.libra.uirecyclerView.OnLoadMoreListener;
import com.libra.uirecyclerView.OnRefreshListener;
import com.libra.uirecyclerView.UIRecycleViewAdapter;
import com.libra.uirecyclerView.UIRecyclerView;
import com.libra.uirecyclerView.UIViewHolder;
import com.libra.uirecyclerView.footer.LoadMoreFooterView;
import com.libra.uirecyclerView.header.ClassicRefreshHeaderView;
import com.libra.view.widget.RecycleViewDivider;
import com.libra.viewmodel.UIRecyclerViewModel;

/**
 * Created by libra on 16/3/8 下午2:42.
 */
public abstract class BaseUIRecyclerActivity<VM extends UIRecyclerViewModel>
        extends BaseActivity
        implements UIRecycleViewAdapter.OnItemClickListener,
        OnRefreshListener,
        OnLoadMoreListener {
    protected UIRecyclerView mUIRecyclerView;
    protected UIRecycleViewAdapter adapter;

    private VM viewModel;


    public void setViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }


    public VM getViewModel() {
        if (viewModel == null) {
            throw new NullPointerException("You should setViewModel first!");
        }
        return viewModel;
    }


    /**
     * 初始化RecyclerView
     */
    protected void initRecyclerView() {
        mUIRecyclerView = $(R.id.recyclerView);
        //默认LinearLayoutManager
        mUIRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UIRecycleViewAdapter(this) {
            @Override
            public UIViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return createHolder(parent, viewType);
            }
        };
        mUIRecyclerView.setIAdapter(adapter);
        getViewModel().mAdapter = adapter;
        getViewModel().mUIRecyclerView = mUIRecyclerView;
        getViewModel().onCreate();
    }


    /**
     * 添加EasyRecyclerView divide线
     */
    protected void addItemDecoration() {
        mUIRecyclerView.addItemDecoration(
                new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 2,
                        ContextCompat.getColor(this, R.color.md_grey_300)));
    }


    /**
     * 添加EasyRecyclerView item点击事件
     */
    protected void addOnItemClickListener() {
        if (adapter != null) {
            adapter.setOnItemClickListener(this);
        }
    }


    /**
     * 添加刷新
     */
    protected void addRefresh() {
        mUIRecyclerView.setRefreshHeaderView(
                new ClassicRefreshHeaderView(this));
        mUIRecyclerView.setRefreshEnabled(true);
        mUIRecyclerView.setOnRefreshListener(this);
    }


    /**
     * 添加加载更多
     */
    protected void addLoadMore() {
        mUIRecyclerView.setLoadMoreFooterView(new LoadMoreFooterView(this));
        mUIRecyclerView.setOnLoadMoreListener(this);
        mUIRecyclerView.setLoadMoreEnabled(true);
        final LoadMoreFooterView loadMoreFooterView
                = (LoadMoreFooterView) mUIRecyclerView.getLoadMoreFooterView();
        loadMoreFooterView.setOnRetryListener(
                new LoadMoreFooterView.OnRetryListener() {
                    @Override public void onRetry(LoadMoreFooterView view) {
                        onLoadMore(loadMoreFooterView);
                    }
                });
    }


    /**
     * Grid模式的时候调用
     *
     * @param spanCount 列
     */
    protected void setGridLayoutManager(int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                spanCount);
        if (adapter != null) {
            gridLayoutManager.setSpanSizeLookup(
                    adapter.obtainGridSpanSizeLookUp(spanCount));
        }
        else {
            throw new IllegalArgumentException("please init adapter before");
        }
        mUIRecyclerView.setLayoutManager(gridLayoutManager);
    }


    protected void setLayoutManager(RecyclerView.LayoutManager manager) {
        mUIRecyclerView.setLayoutManager(manager);
    }


    /**
     * 添加header
     */
    protected void addHeader(View headerView) {
        mUIRecyclerView.addHeaderView(headerView);
    }


    /**
     * 添加footer
     */
    protected void addFooter(View footerView) {
        mUIRecyclerView.addFooterView(footerView);
    }


    /**
     * 添加footer
     */
    public void addEmpty(View emptyView) {
        mUIRecyclerView.setEmptyView(emptyView);
    }


    /**
     * onItemClick 回调，事件传递给viewmodel处理
     */
    @Override public void onItemClick(int position) {
        viewModel.onItemClick(position);
    }


    /**
     * onLoadMore 回调，事件传递给viewmodel处理
     */
    @Override public void onLoadMore(View loadMoreView) {
        viewModel.onLoadMore(loadMoreView);
    }


    /**
     * onRefresh 回调，事件传递给viewmodel处理
     */
    @Override public void onRefresh() {
        viewModel.onRefresh(mUIRecyclerView.getLoadMoreFooterView());
    }


    public abstract UIViewHolder createHolder(ViewGroup parent, int viewType);


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
