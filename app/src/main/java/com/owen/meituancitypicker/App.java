package com.owen.meituancitypicker;

import android.app.Application;

import me.drakeet.library.CrashWoodpecker;

/**
 * Created by owen on 2016/10/30.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashWoodpecker.flyTo(this);
    }
}
