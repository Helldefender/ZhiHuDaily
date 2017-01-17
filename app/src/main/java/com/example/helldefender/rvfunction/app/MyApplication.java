package com.example.helldefender.rvfunction.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.helldefender.rvfunction.db.InfoStorageManager;
import com.jude.utils.JUtils;

/**
 * Created by Helldefender on 2016/11/9.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;
    public static InfoStorageManager infoStorageManager;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();
        JUtils.initialize(this);
        myApplication = this;
        infoStorageManager = InfoStorageManager.getInstance(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}
