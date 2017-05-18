package com.example.qi.biubiunews.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.qi.biubiunews.models.Category;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.models.Token;
import com.example.qi.biubiunews.models.User;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qi on 17-5-8.
 */

public class Utils {
    public static Token loadToken(Context context){
        Token token = new Token();
        SharedPreferences preferences = context.getSharedPreferences("token",MODE_PRIVATE);
        token.setToken(preferences.getString("token",""));
        token.setIs_admin(preferences.getBoolean("is_admin",false));
        Log.i("ss",token.toString());
        return token;
    }
    public static void setToken(Context context,Token token) {
        SharedPreferences preferences = context.getSharedPreferences("token",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token.getToken());
        editor.putBoolean("is_admin",token.isIs_admin());
        editor.commit();
    }
    public static Site getSite(Context context){
        Site site = new Site();
        SharedPreferences preferences = context.getSharedPreferences("setting",MODE_PRIVATE);
        site.setId(preferences.getInt("site_id",1));
        site.setName(preferences.getString("site_name","腾讯新闻"));
        return site;
    }

    public static void setSite(Context context,Site site) {
        SharedPreferences preferences = context.getSharedPreferences("setting",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("site_id",site.getId());
        editor.putString("site_name",site.getName());
        editor.commit();
    }

     public static User getUser(Context context){
         User user = new User();
         SharedPreferences preferences = context.getSharedPreferences("user",MODE_PRIVATE);
         user.setId(preferences.getInt("id",1));
         user.setName(preferences.getString("name",""));
         user.setAbout_me(preferences.getString("about_me",""));
         user.setLocation(preferences.getString("location",""));
         user.setPhone(preferences.getString("phone",""));
         user.setIcon(preferences.getString("icon",""));

        return user;
    }

    public static void setUser(Context context,User user){
        SharedPreferences preferences = context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id",user.getId());
        editor.putString("name",user.getName());
        editor.putString("about_me",user.getAbout_me());
        editor.putString("location",user.getLocation());
        editor.putString("phone",user.getPhone());
        editor.putString("icon",user.getIcon());
        editor.commit();
    }



}
