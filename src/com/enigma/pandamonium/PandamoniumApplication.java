package com.enigma.pandamonium;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View.OnCreateContextMenuListener;

public class PandamoniumApplication extends Application{
	SharedPreferences sharedPref;
	
	public PandamoniumApplication() {
		 sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent(getApplicationContext(), WalkThroughActivity.class);
		startActivity(intent);
		
	}
	public static Object getSharedPrefs(String variable){
		return variable;
		
	}      
	
	
}
