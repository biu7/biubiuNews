package com.example.qi.biubiunews.news;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.example.qi.biubiunews.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qi on 2016/11/22.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<Category> catList = new ArrayList<Category>();
    private NewsListFragment fragment;
    private FragmentManager fm;


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public NewsListFragment getItem(int position) {
        return NewsListFragment.newInstance(catList.get(position).getId());
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        fragment = (NewsListFragment) object;
    }

    public NewsListFragment getFragment(){

        return fragment;
    }


    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return catList.get(position).getName();
    }

    public void setData(List<Category> catList){
        this.catList = catList;
        notifyDataSetChanged();
    }


}