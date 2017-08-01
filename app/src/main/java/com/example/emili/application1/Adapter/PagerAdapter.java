package com.example.emili.application1.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.emili.application1.fragment.ContactFragment;

/**
 * Created by emili on 23/07/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Membre", "Chat", "Setting" };

    Context context;
    public PagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return ContactFragment.newInstance(0, "Page # 1");

            case 1: // Fragment # 0 - This will show FirstFragment
                return ContactFragment.newInstance(1, "Page # 2");

            case 2: // Fragment # 0 - This will show FirstFragment
                return ContactFragment.newInstance(2, "Page # 3");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
