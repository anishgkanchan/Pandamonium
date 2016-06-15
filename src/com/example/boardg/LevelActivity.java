package com.example.boardg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class LevelActivity extends Fragment {
	String text;
	public LevelActivity(String t) {
		text = t;
	}
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
    		
            View view = inflater.inflate(R.layout.scorefragment1, container, false);
            SharedPreferences sharedPref = getActivity().getSharedPreferences("Shared Preference", Context.MODE_PRIVATE);
            int highScore=0,maxStreak=0,gamesPlayed=0;
            if("Easy".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.easy_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxestreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Teasy), 0);
            }else if("Medium".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.medium_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxmstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Tmedium), 0);
            }else if("Difficult".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.difficult_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxdstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Tdifficult), 0);
            }else if("Expert".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.expert_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxexstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Texpert), 0);
            }
            ((TextView)view.findViewById(R.id.textView)).setText(highScore+"");
            ((TextView)view.findViewById(R.id.textView1)).setText(maxStreak+"");
            ((TextView)view.findViewById(R.id.textView2)).setText(gamesPlayed+"");
            return view;
}}