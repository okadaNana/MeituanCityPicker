package com.owen.meituancitypicker.model;

public class City {

    private boolean isTag;
    private String tag;  // 拼音首字母
    private int id;
    private String name;
    private String pinyin;

    public City(boolean isTag, String tag) {
        this.isTag = isTag;
        this.tag = tag;
    }

    public City(int id, String name, String pinyin) {
        this.id = id;
        this.name = name;
        this.pinyin = pinyin;
    }

    public boolean isTag() {
        return isTag;
    }

    public String getTag() {
        return tag;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPinyin() {
        return pinyin;
    }
}
