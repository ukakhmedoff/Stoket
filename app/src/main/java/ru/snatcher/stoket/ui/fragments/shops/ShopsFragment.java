package ru.snatcher.stoket.ui.fragments.shops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.snatcher.stoket.R;
import ru.snatcher.stoket.ui.activity.main.MainActivity;
import ru.snatcher.stoket.ui.common.pagers.FragmentsPagerAdapter;
import ru.snatcher.stoket.ui.fragments.shops.list.ListFragment;
import ru.snatcher.stoket.ui.fragments.shops.maps.MapsFragment;

public class ShopsFragment extends Fragment {

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shops, container, false);
        final ViewPager viewPager = view.findViewById(R.id.viewpager);
        final TabLayout tabLayout = view.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.navigation_bar_item_shops);
        }
    }

    private void setupViewPager(final ViewPager viewPager) {
        final FragmentsPagerAdapter adapter = new FragmentsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ListFragment(), getResources().getString(R.string.shops_list));
        adapter.addFragment(new MapsFragment(), getResources().getString(R.string.shops_map));
        viewPager.setAdapter(adapter);
    }
}
