package com.enigma.pandamonium;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
public class StatsActivity extends FragmentActivity implements TabLayout.OnTabSelectedListener {
    ViewPager Tab;
    StatsAdapter TabAdapter;
    ActionBar actionBar;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.stats_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Easy"));
        tabLayout.addTab(tabLayout.newTab().setText("Medium"));
        tabLayout.addTab(tabLayout.newTab().setText("Difficult"));
        tabLayout.addTab(tabLayout.newTab().setText("Expert"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
 
        TabAdapter = new StatsAdapter(getSupportFragmentManager());
 
        Tab = (ViewPager)findViewById(R.id.pager);
        Tab.setAdapter(TabAdapter);
        tabLayout.setOnTabSelectedListener(this);
        Tab.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
 
    }


	@Override
	public void onTabReselected(android.support.design.widget.TabLayout.Tab arg0) {
		
	}

	@Override
	public void onTabSelected(android.support.design.widget.TabLayout.Tab arg0) {
		Tab.setCurrentItem(arg0.getPosition());
	} 

	@Override
	public void onTabUnselected(android.support.design.widget.TabLayout.Tab arg0) {
		
	}
 
}