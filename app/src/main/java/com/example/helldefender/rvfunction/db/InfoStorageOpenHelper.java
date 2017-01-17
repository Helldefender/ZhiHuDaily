package com.example.helldefender.rvfunction.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Helldefender on 2016/11/15.
 */

public class InfoStorageOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_NEWS = "create table News ("
            + "id integer primary key autoincrement, "
            + "json text, "
            + "date text)";
    public static final String CREATE_DETAILNEWS = "create table DetailNews ("
            + "id integer primary key autoincrement, "
            + "detNewId integer, "
            + "news text)";
    public static final String CREATE_SAVEDID = "create table SavedNewsId ("
            + "id integer primary key autoincrement, "
            + "newsContent text, "
            + "newsId integer)";
    public static final String CREATE_SUPPORT = "create table SupportInfo ("
            + "id integer primary key autoincrement, "
            + "newId integer, "
            + "supportQuantity integer)";

    public InfoStorageOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NEWS);
        sqLiteDatabase.execSQL(CREATE_DETAILNEWS);
        sqLiteDatabase.execSQL(CREATE_SAVEDID);
        sqLiteDatabase.execSQL(CREATE_SUPPORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
