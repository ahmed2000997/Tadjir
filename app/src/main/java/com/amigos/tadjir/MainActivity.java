package com.amigos.tadjir;

import static com.amigos.tadjir.Model.User_control.isTest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.amigos.tadjir.Model.User_control;
import com.amigos.tadjir.adapter.ViewPagerAdapter;
import com.amigos.tadjir.fragments.CreatAccountFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
ViewPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   FirebaseAuth auth=FirebaseAuth.getInstance();
        final FirebaseUser user=auth.getCurrentUser();
        init();
        addTabs();

    }

    private void init(){

      viewPager = findViewById(R.id.viewPager);
    tabLayout  = findViewById(R.id.tabLayout);





    }
    private void addTabs(){
tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search));
        if(isTest()) tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_add));
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_heart));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile_file));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home_fill);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        break;

                   case 2:
                       if(isTest())        tab.setIcon(R.drawable.ic_add);
                       break;

                    case 3:
                        tab.setIcon(R.drawable.ic_heart_fill);
                        break;

                    case 4:
                        tab.setIcon(R.drawable.profile);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        break;

                    case 2:
                        if(isTest())         tab.setIcon(R.drawable.ic_add);
                        break;

                    case 3:
                        tab.setIcon(R.drawable.ic_heart);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.profile_file);
                        break;

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home_fill);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        break;

                    case 2:
                        if(isTest())     tab.setIcon(R.drawable.ic_add);
                        break;

                    case 3:

                        tab.setIcon(R.drawable.ic_heart_fill);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.profile_file);
                        break;
                }

            }
        });

    }
}