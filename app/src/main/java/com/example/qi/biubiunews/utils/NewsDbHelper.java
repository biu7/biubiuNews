package com.example.qi.biubiunews.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qi on 17-5-6.
 */

public class NewsDbHelper extends SQLiteOpenHelper {
    String CREATE_SITE = "CREATE TABLE sites(" +
            "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "site_id INTEGER NOT NULL," +
            "site_name INTEGER NOT NULL)";
    String CREATE_CATEGORY = "CREATE TABLE categorys(" +
            "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "category_id INTEGER NOT NULL," +
            "category_name TEXT NOT NULL," +
            "site_id INTEGER NOT NULL)";



    public NewsDbHelper(Context context) {
        super(context, "news", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SITE);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
