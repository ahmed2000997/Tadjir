package com.amigos.tadjir.adapter;

import static com.amigos.tadjir.Model.User_control.isTest;
import static com.amigos.tadjir.Model.User_control.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.amigos.tadjir.fragments.Add;
import com.amigos.tadjir.fragments.Home;
import com.amigos.tadjir.fragments.Notification;
import com.amigos.tadjir.fragments.Profile;
import com.amigos.tadjir.fragments.Search;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            default:
            case 0:
                return new Home();

            case 1:
                return new Search();

           case 2:
         if (isTest())  return new Add();
else return new Notification();

            case 3:
                if (isTest())       return new Notification();
else   return new Profile();
            case 4:
                return new Profile();


        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
