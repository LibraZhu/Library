package com.libra.uirecyclerView.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.libra.R;

public class LoadMoreFooterView extends FrameLayout {

    private Status mStatus;

    private View mLoadingView;

    private View mErrorView;

    private View mTheEndView;

    private FrameLayout mEmptyView;

    private OnRetryListener mOnRetryListener;


    public LoadMoreFooterView(Context context) {
        this(context, null);
    }


    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context)
                      .inflate(
                              R.layout.layout_uirecyclerview_load_more_footer_view,
                              this, true);

        mLoadingView = findViewById(R.id.loadingView);
        mErrorView = findViewById(R.id.errorView);
        mTheEndView = findViewById(R.id.theEndView);
        mEmptyView = (FrameLayout) findViewById(R.id.emptyView);
        if (getEmptyLayoutID() != -1) {
            View view = LayoutInflater.from(this.getContext())
                                      .inflate(getEmptyLayoutID(), null, false);
            mEmptyView.addView(view);
        }

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry(LoadMoreFooterView.this);
                }
            }
        });

        setStatus(Status.GONE);
    }


    public int getEmptyLayoutID() {
        return -1;
    }


    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }


    public Status getStatus() {
        return mStatus;
    }


    public void setStatus(Status status) {
        this.mStatus = status;
        change();
    }


    public boolean canLoadMore() {
        return mStatus == Status.GONE || mStatus == Status.ERROR;
    }


    private void change() {
        switch (mStatus) {
            case GONE:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;

            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;

            case ERROR:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                break;

            case THE_END:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);
                mEmptyView.setVisibility(GONE);
                break;
            case ENPTY:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                mEmptyView.setVisibility(VISIBLE);
                break;
        }
    }


    public enum Status {
        GONE, LOADING, ERROR, THE_END, ENPTY
    }

    public interface OnRetryListener {
        void onRetry(LoadMoreFooterView view);
    }
}
