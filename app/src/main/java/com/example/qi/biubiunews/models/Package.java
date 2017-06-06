package com.example.qi.biubiunews.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qi on 17-5-18.
 */

public class Package implements Parcelable{


    /**
     * count : 0
     * id : 1
     * name : 测试收藏夹1
     */

    private int count;
    private int id;
    private String name;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Package{" +
                "count=" + count +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);

    }

    public static final Parcelable.Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel source) {
            Package mPackage = new Package();
            mPackage.id = source.readInt();
            mPackage.name = source.readString();
            return mPackage;
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
}
