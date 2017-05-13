package com.example.qi.biubiunews.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qi.biubiunews.models.Category;
import com.example.qi.biubiunews.models.News;
import com.example.qi.biubiunews.models.Site;

import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qi on 17-5-6.
 */

public class DbUtils {
    private NewsDbHelper dbHelper;
    private SQLiteDatabase db;

    public DbUtils(Context context) {
        dbHelper = new NewsDbHelper(context);
        db = this.dbHelper.getReadableDatabase();
    }

    public void insert_sites(List<Site> site_list){
        for(Site site : site_list){
            ContentValues values = new ContentValues();
            values.put("site_id",site.getId());
            values.put("site_name",site.getName());
            db.insert("sites",null,values);
        }
    }
    public List<Site> get_sites(){
        Cursor cursor = db.query("sites",null,null,null,null,null,null);
        List<Site> sites = new ArrayList<Site>();
        while(cursor.moveToNext()){
            Site site = new Site();
            site.setId(cursor.getInt(cursor.getColumnIndex("site_id")));
            site.setName(cursor.getString(cursor.getColumnIndex("site_name")));
            sites.add(site);
        }
        cursor.close();
        return sites;
    }

    public void insert_category(List<Category> categoryList){
        for(Category category : categoryList){
            ContentValues values = new ContentValues();
            values.put("category_id",category.getId());
            values.put("category_name",category.getName());
            values.put("site_id",category.getSite_id());
            db.insert("categorys",null,values);
        }
    }

    public List<Category> get_categorys(int site_id){
        Cursor cursor = db.query("categorys",null,"site_id = ?",new String[]{String.valueOf(site_id)},null,null,null);
        List<Category> categories = new ArrayList<>();
        while (cursor.moveToNext()){
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex("category_id")));
            category.setName(cursor.getString(cursor.getColumnIndex("category_name")));
            categories.add(category);
        }
        return categories;
    }






}
