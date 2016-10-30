package com.owen.meituancitypicker.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.owen.meituancitypicker.db.CityDbSchema.CityTable;
import com.owen.meituancitypicker.db.CityDbSchema.Cols;
import com.owen.meituancitypicker.model.City;

import java.util.ArrayList;
import java.util.List;

public class CityBaseDbHelper {

    private static final String ASSETS_DB_NAME = "china_cities.db";
    private static final String DB_FILE_NAME = ASSETS_DB_NAME;

    private SQLiteDatabase mDb;

    public CityBaseDbHelper(Context context) {
        mDb = DbTransformer.go(context, ASSETS_DB_NAME, DB_FILE_NAME);
    }

    public List<City> getAllCities() {
        List<City> allCities = new ArrayList<>();

        Cursor cursor = mDb.query(CityTable.NAME,
                new String[]{Cols.ID, Cols.NAME, Cols.PINYIN},
                null,
                null,
                null,
                null,
                Cols.PINYIN
        );
        int idColumnIndex = cursor.getColumnIndex(Cols.ID);
        int nameColumnIndex = cursor.getColumnIndex(Cols.NAME);
        int pinyinColumnIndex = cursor.getColumnIndex(Cols.PINYIN);
        String currentTag = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(idColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            String pinyin = cursor.getString(pinyinColumnIndex);

            String tag = pinyin.substring(0, 1);  // 拼音首字母
            if (currentTag == null) {
                currentTag = tag;
                allCities.add(new City(true, currentTag.toUpperCase()));
            } else if (currentTag.equals(tag)) {
                allCities.add(new City(id, name, pinyin));
            } else {
                currentTag = tag;
                allCities.add(new City(true, currentTag.toUpperCase()));
                allCities.add(new City(id, name, pinyin));
            }
        }
        cursor.close();

        return allCities;
    }

    public List<City> searchCity(String keyword) {
        List<City> cities = new ArrayList<>();
        // http://www.jb51.net/article/50952.htm
        Cursor cursor = mDb.query(CityTable.NAME,
                new String[]{Cols.ID, Cols.NAME, Cols.PINYIN},
                Cols.NAME + " LIKE ?" + " OR " + Cols.PINYIN + " LIKE ?",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"},
                null,
                null,
                Cols.PINYIN
        );
        int idColumnIndex = cursor.getColumnIndex(Cols.ID);
        int nameColumnIndex = cursor.getColumnIndex(Cols.NAME);
        int pinyinColumnIndex = cursor.getColumnIndex(Cols.PINYIN);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(idColumnIndex);
            String name = cursor.getString(nameColumnIndex);
            String pinyin = cursor.getString(pinyinColumnIndex);
            cities.add(new City(id, name, pinyin));
        }
        cursor.close();

        return cities;
    }

    public void close() {
        if (mDb != null) {
            mDb.close();
        }
    }
}
