package com.example.mobileapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class InfoFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<InfoFragment> fragments;

    public InfoFragmentPagerAdapter(FragmentManager fm, List<InfoFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
