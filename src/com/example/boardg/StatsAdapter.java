package com.example.boardg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
 
public class StatsAdapter extends FragmentStatePagerAdapter {
    public StatsAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }
 
    @Override
    public Fragment getItem(int i) {
        switch (i) {
        case 0:
            //Fragement for Android Tab
            return new LevelFragment("Easy");
        case 1:
           //Fragment for Ios Tab
            return new LevelFragment("Medium");
        case 2:
            //Fragment for Windows Tab
            return new LevelFragment("Difficult");
        case 3:
            //Fragment for Windows Tab
            return new LevelFragment("Expert");
        }
        return null;
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 4; //No of Tabs
    }
 
    }