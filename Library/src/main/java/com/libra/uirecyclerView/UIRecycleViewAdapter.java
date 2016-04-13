package com.libra.uirecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by libra on 16/4/11 下午2:45.
 */
public abstract class UIRecycleViewAdapter<T>
        extends RecyclerView.Adapter<UIViewHolder> {

    private List<T> mObjects;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;


    public UIRecycleViewAdapter(Context context) {
        this(context, new ArrayList<T>());
    }


    public UIRecycleViewAdapter(Context context, ArrayList<T> objects) {
        this.mContext = context;
        this.mObjects = objects;
    }


    public void setList(List<T> objects) {
        mObjects.clear();
        append(objects);
    }


    public void append(List<T> objects) {
        int positionStart = mObjects.size();
        int itemCount = objects.size();
        mObjects.addAll(objects);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        }
        else {
            notifyDataSetChanged();
        }
    }


    public void clear() {
        mObjects.clear();
        notifyDataSetChanged();
    }


    @Override
    public UIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final UIViewHolder viewHolder = OnCreateViewHolder(parent, viewType);
        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mItemClickListener.onItemClick(
                            viewHolder.getIAdapterPosition());
                }
            });
        }

        if (mItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override public boolean onLongClick(View v) {
                            return mItemLongClickListener.onItemClick(
                                    viewHolder.getIAdapterPosition());
                        }
                    });
        }
        return viewHolder;
    }


    @Override public void onBindViewHolder(UIViewHolder holder, int position) {
        holder.onBindViewHolder(mObjects.get(position));
    }


    @Override public int getItemCount() {
        return mObjects == null ? 0 : mObjects.size();
    }


    public abstract UIViewHolder OnCreateViewHolder(ViewGroup parent, int viewType);


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }
}
