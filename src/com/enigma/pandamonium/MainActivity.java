package com.enigma.pandamonium;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends FragmentActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
			SharedPreferences sharedPref = getSharedPreferences("Shared Preference", Context.MODE_PRIVATE);
			int mode = sharedPref.getInt(getString(R.string.first_time), 0);

			 FragmentManager fm = getSupportFragmentManager();
			 FragmentTransaction fragmentTransaction = fm.beginTransaction();
			 Fragment fr;
			 if(mode == 0) {
			     fr = new WalkThroughFragment();
				 fragmentTransaction.replace(R.id.fragment_place, fr, "Walkthrough");
			 }else {
			     fr = new MainFragment();
				 fragmentTransaction.replace(R.id.fragment_place, fr, "Main");
			 }
			 fragmentTransaction.commit();
	}
	@Override
	public void onBackPressed() {

		MainFragment fragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("Main");
		if (fragment instanceof MainFragment) {
	        super.onBackPressed();
	    } else {
	    	FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_place, new MainFragment(), "Main");
			fragmentTransaction.commit();
	    }

	}

}
