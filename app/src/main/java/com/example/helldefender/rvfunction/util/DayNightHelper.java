package com.example.helldefender.rvfunction.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.helldefender.rvfunction.entity.DayNight;

/**
 * Created by Helldefender on 2016/11/8.
 */

public class DayNightHelper {
    private final static String FILE_NAME = "settings";
    private final static String MODE = "day_night_mode";
    private static DayNightHelper dayNightHelper=null;

    private SharedPreferences mSharedPreferences;

    public DayNightHelper(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean setMode(DayNight mode) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(MODE, mode.getName());
        return editor.commit();
    }

    public boolean isNight() {
        String mode = mSharedPreferences.getString(MODE, DayNight.DAY.getName());
        if (DayNight.NIGHT.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDay() {
        String mode = mSharedPreferences.getString(MODE, DayNight.DAY.getName());
        if (DayNight.DAY.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }

    public static DayNightHelper getInstance(Context mContext) {
        if (dayNightHelper == null) {
            dayNightHelper = new DayNightHelper(mContext);
        }
        return dayNightHelper;
    }
}
