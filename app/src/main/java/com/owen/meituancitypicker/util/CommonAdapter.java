package com.owen.meituancitypicker.util;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    protected List<T> mData;
    protected MultipleItemTypeSupport mMultipleItemTypeSupport;

    public CommonAdapter(MultipleItemTypeSupport multipleItemTypeSupport) {
        this(null, multipleItemTypeSupport);
    }

    public CommonAdapter(List<T> data, MultipleItemTypeSupport multipleItemTypeSupport) {
        mData = data;
        mMultipleItemTypeSupport = multipleItemTypeSupport;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = mMultipleItemTypeSupport.getLayoutResId(viewType);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        bind(holder, mData.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mMultipleItemTypeSupport.getViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public abstract void bind(CommonViewHolder holder, T item);
}
