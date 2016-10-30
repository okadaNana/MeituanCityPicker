package com.owen.meituancitypicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.owen.meituancitypicker.adapter.CityAdapter;
import com.owen.meituancitypicker.db.CityBaseDbHelper;
import com.owen.meituancitypicker.model.City;
import com.owen.meituancitypicker.util.MultipleItemTypeSupport;
import com.owen.meituancitypicker.view.SideBar;

import java.util.List;

public class CityPickerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtKeyWord;
    private ImageView mIvClear;
    private RecyclerView mRvAllCity;
    private RecyclerView mRvSearchResult;
    private CityBaseDbHelper mDbHelper;
    private SideBar mSideBar;
    private TextView mTvTag;

    private CityAdapter allCityAdapter;
    private CityAdapter mSearchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);

        initViews();
        mDbHelper = new CityBaseDbHelper(this);

        mRvAllCity.setLayoutManager(new LinearLayoutManager(this));
        final List<City> allCities = mDbHelper.getAllCities();
        allCityAdapter = new CityAdapter(allCities, new MultipleItemTypeSupport() {

            static final int VIEW_TYPE_TAG = 1;
            static final int VIEW_TYPE_CITY = 2;

            @Override
            public int getViewType(int position) {
                if (allCities.get(position).isTag()) {
                    return VIEW_TYPE_TAG;
                } else {
                    return VIEW_TYPE_CITY;
                }
            }

            @Override
            public int getLayoutResId(int viewType) {
                if (VIEW_TYPE_TAG == viewType) {
                    return R.layout.item_city_tag;
                } else if (VIEW_TYPE_CITY == viewType) {
                    return R.layout.item_city;
                } else {
                    throw new IllegalArgumentException("Do you have define viewType?");
                }
            }
        });
        mRvAllCity.setAdapter(allCityAdapter);

        mRvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultAdapter = new CityAdapter(new MultipleItemTypeSupport() {
            @Override
            public int getViewType(int position) {
                return 0;
            }

            @Override
            public int getLayoutResId(int viewType) {
                return R.layout.item_city;
            }
        });
        mRvSearchResult.setAdapter(mSearchResultAdapter);
    }

    private void initViews() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        mEdtKeyWord = (EditText) findViewById(R.id.edt_key_word);
        mIvClear = (ImageView) findViewById(R.id.iv_clear);
        mIvClear.setOnClickListener(this);
        mRvAllCity = (RecyclerView) findViewById(R.id.rv_all_city);
        mRvSearchResult = (RecyclerView) findViewById(R.id.rv_search_result);
        mEdtKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    mIvClear.setVisibility(View.INVISIBLE);
                    mRvAllCity.setVisibility(View.VISIBLE);
                    mRvSearchResult.setVisibility(View.GONE);
                } else {
                    mIvClear.setVisibility(View.VISIBLE);
                    List<City> searchResult = mDbHelper.searchCity(keyword);
                    if (searchResult.isEmpty()) {
                        Toast.makeText(CityPickerActivity.this, "找不到", Toast.LENGTH_SHORT).show();
                        mRvAllCity.setVisibility(View.VISIBLE);
                        mRvSearchResult.setVisibility(View.GONE);
                    } else {
                        mSearchResultAdapter.setData(searchResult);
                        mRvAllCity.setVisibility(View.GONE);
                        mRvSearchResult.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mTvTag = (TextView) findViewById(R.id.tv_tag);
        mSideBar = (SideBar) findViewById(R.id.sideBar);
        mSideBar.setTextViewDialog(mTvTag);
        mSideBar.setOnLetterTouchedChangeListener(new SideBar.onLetterTouchedChangeListener() {
            @Override
            public void onTouchedLetterChange(String letterTouched) {
                mTvTag.setVisibility(View.VISIBLE);
                int position = allCityAdapter.getPositionForSection(letterTouched.charAt(0));
                if (position != -1) {
                    // http://stackoverflow.com/questions/27377830/what-is-the-equivalent-listview-setselection-in-case-of-recycler-view
                    ((LinearLayoutManager) mRvAllCity.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                mEdtKeyWord.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
        super.onDestroy();
    }
}
