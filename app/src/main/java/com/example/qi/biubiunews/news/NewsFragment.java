package com.example.qi.biubiunews.news;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.main.view.MainActivity;
import com.example.qi.biubiunews.models.Category;
import com.example.qi.biubiunews.models.Site;
import com.example.qi.biubiunews.utils.DbUtils;
import com.example.qi.biubiunews.utils.Utils;

import java.util.List;


public class NewsFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tab;
    private SectionsPagerAdapter pagerAdapter;
    private List<Category> catList;
    private DbUtils dbUtils;
    private Site site;


    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("实时新闻");

        viewPager = (ViewPager) root.findViewById(R.id.container);
        tab = (TabLayout) root.findViewById(R.id.tabs);
        dbUtils = new DbUtils(getActivity());
        pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        tab.setupWithViewPager(viewPager);
        refreshData();
        return root;
    }

    public void refreshData(){
        site = Utils.getSite(getActivity());
        catList = dbUtils.get_categorys(site.getId());
        pagerAdapter.setData(catList);
    }

}
