package com.example.helldefender.rvfunction.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helldefender on 2016/11/15.
 */

public class InfoStorageManager {
    private static int Version = 1;
    private static final String DB_NAME = "zhihudaily";
    private static InfoStorageManager infoStorageManager;
    private SQLiteDatabase db;

    public InfoStorageManager(Context context) {
        InfoStorageOpenHelper dbHelper = new InfoStorageOpenHelper(context, DB_NAME, null, Version);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static InfoStorageManager getInstance(Context context) {
        if (infoStorageManager == null) {
            infoStorageManager = new InfoStorageManager(context);
        }
        return infoStorageManager;
    }

    public void saveNews(String data, String date) {
        if (data != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("json", data);
            contentValues.put("date", date);
            db.insert("News", null, contentValues);
        }
    }

    public String getNews(String date) {
        Cursor cursor = db.query("News", null, "date=?", new String[]{date}, null, null, null);
        String result = null;
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex("json"));
        }
        return result;
    }

    public void savaThemes(String data) {
        if (null != data) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("themes", data);
            db.insert("Themes", null, contentValues);
        }
    }

    public String getThemes() {
        Cursor cursor = db.query("Themes", null, "id=?", new String[]{1 + ""}, null, null, null);
        String result = null;
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex("themes"));
        }
        return result;
    }

    public void saveThemeNews(String data, int id) {
        if (data != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("json", data);
            contentValues.put("date", "theme" + id);
            db.insert("News", null, contentValues);
        }
    }

    public String getThemeNews(int id) {
        Cursor cursor = db.query("News", null, "date=?", new String[]{"theme" + id}, null, null, null);
        String result = null;
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex("json"));
        }
        return result;
    }

    public void saveDetail(String data, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("news", data);
        contentValues.put("detNewId", id);
        db.insert("DetailNews", null, contentValues);
    }

    public String getDetail(int id) {
        Cursor cursor = db.query("DetailNews", null, "detNewId=?", new String[]{id + ""}, null, null, null);
        String result = null;
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex("news"));
        }
        return result;
    }

    public void saveId(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("newsId", id);
        db.insert("SavedNewsId", null, contentValues);
    }

    public void deleteId(int id) {
        db.delete("SavedNewsId", "newsId = ?", new String[]{"" + id});
    }

    public List<Integer> getId() {
        Cursor cursor = db.query("SavedNewsId", null, null, null, null, null, null);
        List<Integer> idList = new ArrayList<Integer>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("newsId"));
                Integer integer = new Integer(id);
                idList.add(integer);
            } while (cursor.moveToNext());
        }
        return idList;
    }

    public void saveCollectionContent(String result, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("newsContent", result);
        db.update("SavedNewsId", contentValues, "newsId=?", new String[]{id + ""});
    }

    public String getCollectionContent(int id) {
        StringBuilder stringBuilder = new StringBuilder();
        Cursor cursor = db.query("SavedNewsId", null, "newsId=?", new String[]{id + ""}, null, null, null);
        if (cursor.moveToFirst()) {
            String result = cursor.getString(cursor.getColumnIndex("newsContent"));
            stringBuilder.append(result);
        }
        cursor.close();
        String temp = stringBuilder.toString();
        return temp;
    }

    public void saveSupportInfo(int id, int supportQuantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("newId", id);
        contentValues.put("supportQuantity", supportQuantity);
        db.insert("SupportInfo", null, contentValues);
    }

    public void deleteSupportInfo(int id) {
        db.delete("SupportInfo", "newId = ?", new String[]{"" + id});
    }

    public List<Integer> getSupportId() {
        List<Integer> supportInfo = new ArrayList<Integer>();
        Cursor cursor = db.query("SupportInfo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                supportInfo.add(new Integer(cursor.getInt(cursor.getColumnIndex("newId"))));
                supportInfo.add(new Integer(cursor.getInt(cursor.getColumnIndex("supportQuantity"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return supportInfo;
    }
    public String getSupportQuantity(int id) {
        String quantity=null;
        Cursor cursor = db.query("SupportInfo", null, "newId=?", new String[]{id + ""}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                quantity=cursor.getInt(cursor.getColumnIndex("supportQuantity"))+"";
            } while (cursor.moveToNext());
        }
        cursor.close();
        return quantity;
    }
}
