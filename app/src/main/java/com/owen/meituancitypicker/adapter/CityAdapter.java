package com.owen.meituancitypicker.adapter;

import android.widget.SectionIndexer;

import com.owen.meituancitypicker.R;
import com.owen.meituancitypicker.model.City;
import com.owen.meituancitypicker.util.CommonAdapter;
import com.owen.meituancitypicker.util.CommonViewHolder;
import com.owen.meituancitypicker.util.MultipleItemTypeSupport;

import java.util.List;

public class CityAdapter extends CommonAdapter<City> implements SectionIndexer {

    public CityAdapter(MultipleItemTypeSupport multipleItemTypeSupport) {
        super(multipleItemTypeSupport);
    }

    public CityAdapter(List<City> data, MultipleItemTypeSupport multipleItemTypeSupport) {
        super(data, multipleItemTypeSupport);
    }

    @Override
    public void bind(CommonViewHolder holder, City item) {
        if (item.isTag()) {
            holder.setText(R.id.tv_city_tag, item.getTag());
        } else {
            holder.setText(R.id.tv_city_name, item.getName());
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < mData.size(); i++) {
            char firstChar;
            if (mData.get(i).isTag()) {
                firstChar = mData.get(i).getTag().charAt(0);
            } else {
                firstChar = mData.get(i).getName().charAt(0);
            }

            if (section == firstChar) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        if (mData != null) {
            if (mData.get(position).isTag()) {
                return mData.get(position).getTag().charAt(0);
            } else {
                return mData.get(position).getName().charAt(0);
            }
        }

        return -1;
    }
}
