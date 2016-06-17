package com.example.boardg;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
 
public class LevelActivity extends Fragment {
	String text;
	TextView txtWinScore, txtLossScore;
	FrameLayout FrmwinProgress, FrmlossProgress;
	public LevelActivity(String t) {
		text = t;
	}
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
    		
            View view = inflater.inflate(R.layout.scorefragment1, container, false);
            SharedPreferences sharedPref = getActivity().getSharedPreferences("Shared Preference", Context.MODE_PRIVATE);
            int highScore=0,maxStreak=0,gamesPlayed=0;
            int winProgress = 0;
            if("Easy".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.easy_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxestreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Teasy), 0);
            	winProgress = sharedPref.getInt(getString(R.string.eWins), 0);
            }else if("Medium".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.medium_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxmstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Tmedium), 0);
            	winProgress = sharedPref.getInt(getString(R.string.mWins), 0);
            }else if("Difficult".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.difficult_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxdstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Tdifficult), 0);
            	winProgress = sharedPref.getInt(getString(R.string.dWins), 0);
            }else if("Expert".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.expert_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxexstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Texpert), 0);
            	winProgress = sharedPref.getInt(getString(R.string.exWins), 0);
            }
            txtWinScore = ((TextView)view.findViewById(R.id.winScore));
            txtLossScore = ((TextView)view.findViewById(R.id.lossScore));
            FrmwinProgress = ((FrameLayout)view.findViewById(R.id.winProgress));
            FrmlossProgress = ((FrameLayout)view.findViewById(R.id.lossProgress));
            txtWinScore.setText(winProgress+"");
            txtLossScore.setText((gamesPlayed-winProgress)+"");
            ((TextView)view.findViewById(R.id.textView)).setText(highScore+"");
            ((TextView)view.findViewById(R.id.textView1)).setText(maxStreak+"");
            float f = (float)(winProgress);
            if(winProgress == gamesPlayed-winProgress)
            	f= 1.0f;
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, f);
            FrmwinProgress.setLayoutParams(param);
            f =(float)(gamesPlayed-winProgress);
            if(winProgress == gamesPlayed-winProgress)
            	f= 1.0f;
            param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, f);
            FrmlossProgress.setLayoutParams(param);
            return view;
}}