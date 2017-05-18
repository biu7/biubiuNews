package com.example.qi.biubiunews.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qi on 17-4-25.
 */

public class User implements Parcelable{


    /**
     * about_me : 这是一条示例简介
     * confirmed : true
     * followed_count : 1
     * follower_count : 1
     * icon : http://www.zzulinews.cn/static/images/user_icon/default.jpg
     * id : 2
     * last_seen : Sat, 06 May 2017 04:20:19 GMT
     * location : 郑州
     * member_since : Sat, 06 May 2017 04:20:19 GMT
     * name : 潘佳佳
     * news_count : 9
     * phone : 17703739771
     * username : test1
     */

    private String about_me;
    private boolean confirmed;
    private int followed_count;
    private int follower_count;
    private String icon;
    private int id;
    private String last_seen;
    private String location;
    private String member_since;
    private String name;
    private int news_count;
    private String phone;
    private String username;

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public int getFollowed_count() {
        return followed_count;
    }

    public void setFollowed_count(int followed_count) {
        this.followed_count = followed_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMember_since() {
        return member_since;
    }

    public void setMember_since(String member_since) {
        this.member_since = member_since;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(about_me);
        dest.writeString(icon);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.id = source.readInt();
            user.name = source.readString();
            user.location = source.readString();
            user.about_me = source.readString();
            user.icon = source.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
