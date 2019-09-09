package com.example.grabagecleanup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=(ViewPager)findViewById(R.id.ViewPager);
        fragmentPagerAdapter= new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(1);

        tabLayout=(TabLayout)findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return TAB_1.newInstance();


                case 1:
                    return TAB_2.newInstance();


                case 2:
                    return TAB_3.newInstance();

            }

            return null;

        }

        @Override
        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int Position)
        {

            switch (Position){
                case 0:
                    return "FEED";

                case 1:
                    return "CAMERA";


                case 2:
                    return "PROFILE";

            }

            return null;
        }
    }
}
