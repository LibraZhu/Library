package com.libra.uirecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class UIViewHolder extends RecyclerView.ViewHolder {

    public UIViewHolder(View itemView) {
        super(itemView);
    }


    public void onBindViewHolder(Object data) {
    }


    @Deprecated public final int getIPosition() {
        return getPosition() - 2;
    }


    public final int getILayoutPosition() {
        return getLayoutPosition() - 2;
    }


    public final int getIAdapterPosition() {
        return getAdapterPosition() - 2;
    }


    public final int getIOldPosition() {
        return getOldPosition() - 2;
    }


    public final long getIItemId() {
        return getItemId();
    }


    public final int getIItemViewType() {
        return getItemViewType();
    }
}
