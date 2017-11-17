package ru.snatcher.stoket.ui.common.pagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentsPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();

    public FragmentsPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public final Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public final int getCount() {
        return fragments.size();
    }

    public final void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    @Override
    public final CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}