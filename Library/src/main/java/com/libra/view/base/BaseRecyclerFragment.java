package com.libra.view.base;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.libra.R;
import com.libra.view.widget.RecycleViewDivider;
import com.libra.viewmodel.RecyclerViewModel;

/**
 * Created by libra on 16/3/8 下午2:42.
 */
public abstract class BaseRecyclerFragment<VM extends RecyclerViewModel>
        extends BaseFragment
        implements RecyclerArrayAdapter.OnItemClickListener,
        RecyclerArrayAdapter.OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {
    protected EasyRecyclerView mEasyRecyclerView;
    protected RecyclerArrayAdapter adapter;

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
    protected void initRecyclerView(View view) {
        mEasyRecyclerView = $(view, R.id.recyclerView);
        //默认LinearLayoutManager
        mEasyRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        adapter = new RecyclerArrayAdapter(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return createHolder(parent, viewType);
            }
        };
        mEasyRecyclerView.setAdapterWithProgress(adapter);
    }


    /**
     * 添加EasyRecyclerView divide线
     */
    protected void addItemDecoration() {
        mEasyRecyclerView.addItemDecoration(
                new RecycleViewDivider(getActivity(),
                        LinearLayoutManager.HORIZONTAL, 2,
                        ContextCompat.getColor(getActivity(),
                                R.color.md_grey_300)));
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
    protected void addRefreshing() {
        mEasyRecyclerView.setRefreshListener(this);
    }


    /**
     * 添加加载更多
     */
    protected void addLoadMore() {
        if (adapter != null) {
            adapter.setMore(R.layout.view_more, this);
            adapter.setNoMore(R.layout.view_nomore);

            adapter.setError(R.layout.view_error)
                   .setOnClickListener(new View.OnClickListener() {
                       @Override public void onClick(View v) {
                           adapter.resumeMore();
                       }
                   });
        }
    }


    /**
     * Grid模式的时候调用
     *
     * @param spanCount 列
     */
    protected void setGridLayoutManager(int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                getActivity(), spanCount);
        if (adapter != null) {
            gridLayoutManager.setSpanSizeLookup(
                    adapter.obtainGridSpanSizeLookUp(spanCount));
        }
        else {
            throw new IllegalArgumentException("please init adapter before");
        }
        mEasyRecyclerView.setLayoutManager(gridLayoutManager);
    }


    protected void setLayoutManager(RecyclerView.LayoutManager manager) {
        mEasyRecyclerView.setLayoutManager(manager);
    }


    /**
     * 添加header
     */
    protected void addHeader(RecyclerArrayAdapter.ItemView itemView) {
        if (this.adapter != null) {
            this.adapter.addHeader(itemView);
        }
    }


    /**
     * 添加footer
     */
    protected void addFooter(RecyclerArrayAdapter.ItemView itemView) {
        if (this.adapter != null) {
            this.adapter.addFooter(itemView);
        }
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
    @Override public void onLoadMore() {
        viewModel.onLoadMore();
    }


    /**
     * onRefresh 回调，事件传递给viewmodel处理
     */
    @Override public void onRefresh() {
        viewModel.onRefresh();
    }


    public abstract BaseViewHolder createHolder(ViewGroup parent, int viewType);
}
