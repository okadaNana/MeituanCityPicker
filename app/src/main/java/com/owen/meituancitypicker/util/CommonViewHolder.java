package com.owen.meituancitypicker.util;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;


public class CommonViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mItemView;

    public CommonViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mItemView = itemView;
    }

    private <T extends View> T getView(int resId) {
        View view = mViews.get(resId);
        if (view == null) {
            view = mItemView.findViewById(resId);
        }
        return (T) view;
    }

    public void setText(int resId, String text) {
        TextView textView = getView(resId);
        textView.setText(text);
    }

    public void setOnclickListener(int resId, View.OnClickListener listener) {
        getView(resId).setOnClickListener(listener);
    }
}
