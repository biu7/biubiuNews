package com.example.qi.biubiunews.main.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.qi.biubiunews.R;
import com.example.qi.biubiunews.blog.BlogFragment;
import com.example.qi.biubiunews.news.NewsFragment;
import com.example.qi.biubiunews.user.UserFragment;
import com.patloew.navigationviewfragmentadapters.NavigationViewFragmentAdapter;

/**
 * Created by qi on 17-5-7.
 */

public class MyNanigationViewAdapter extends NavigationViewFragmentAdapter {
    private FragmentManager fragmentManager;

    public MyNanigationViewAdapter(FragmentManager fragmentManager, @IdRes int containerId, @IdRes int defaultMenuItemId, Bundle savedInstanceState) {
        super(fragmentManager, containerId, defaultMenuItemId, savedInstanceState);
        this.fragmentManager = fragmentManager;
    }


    public FragmentManager getFragmentManager(){

        return this.fragmentManager;
    }
    @NonNull
    @Override
    public Fragment getFragment(@IdRes int menuItemId) {
        switch (menuItemId){
            case R.id.navigation_news:
                return NewsFragment.newInstance();
            case R.id.navigation_blog:
                return BlogFragment.newInstance();
            case R.id.navigation_profile:
                return UserFragment.newInstance();
            default:
                return null;
        }
    }



}
