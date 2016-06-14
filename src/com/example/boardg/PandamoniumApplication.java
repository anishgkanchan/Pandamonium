package com.example.boardg;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PandamoniumApplication extends Application{
	SharedPreferences sharedPref;
	
	public PandamoniumApplication() {
		 sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	public static Object getSharedPrefs(String variable){
		return variable;
		
	}
	
}
