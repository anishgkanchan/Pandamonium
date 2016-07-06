package com.enigma.pandamonium;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
 
@SuppressLint("NewApi") public class LevelFragment extends Fragment {
	String text;
	TextView txtWinScore, txtLossScore, txtHighScore, txtStreak, txtCaptures, txtCurrStreak;
	Button btnReset;
	 SharedPreferences sharedPref;
	FrameLayout FrmwinProgress, FrmlossProgress;
	public LevelFragment(String t) {
		text = t;
	}
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
    		
            View view = inflater.inflate(R.layout.scorefragment1, container, false);
            sharedPref = getActivity().getSharedPreferences("Shared Preference", Context.MODE_PRIVATE);
            int highScore=0,maxStreak=0,gamesPlayed=0,currStreak=0,captures=0;
            int winProgress = 0;
            if("Easy".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.easy_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxestreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Teasy), 0);
            	winProgress = sharedPref.getInt(getString(R.string.eWins), 0);
            	captures = sharedPref.getInt(getString(R.string.eCaptures), 0);
            	currStreak = sharedPref.getInt(getString(R.string.estreak), 0);
            }else if("Medium".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.medium_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxmstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Tmedium), 0);
            	winProgress = sharedPref.getInt(getString(R.string.mWins), 0);
            	captures = sharedPref.getInt(getString(R.string.mCaptures), 0);
            	currStreak = sharedPref.getInt(getString(R.string.mstreak), 0);
            }else if("Difficult".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.difficult_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxdstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Tdifficult), 0);
            	winProgress = sharedPref.getInt(getString(R.string.dWins), 0);
            	captures = sharedPref.getInt(getString(R.string.dCaptures), 0);
            	currStreak = sharedPref.getInt(getString(R.string.dstreak), 0);
            }else if("Expert".equals(text)){
            	highScore = sharedPref.getInt(getString(R.string.expert_high_score), 0);
            	maxStreak = sharedPref.getInt(getString(R.string.Maxexstreak), 0);
            	gamesPlayed = sharedPref.getInt(getString(R.string.Texpert), 0);
            	winProgress = sharedPref.getInt(getString(R.string.exWins), 0);
            	captures = sharedPref.getInt(getString(R.string.exCaptures), 0);
            	currStreak = sharedPref.getInt(getString(R.string.exstreak), 0);
            }
            txtWinScore = ((TextView)view.findViewById(R.id.winScore));
            txtLossScore = ((TextView)view.findViewById(R.id.lossScore));
            txtHighScore = ((TextView)view.findViewById(R.id.textView));
            txtStreak = ((TextView)view.findViewById(R.id.textView1));
            txtCaptures = ((TextView)view.findViewById(R.id.textView3));
            txtCurrStreak = ((TextView)view.findViewById(R.id.textView4));
            FrmwinProgress = ((FrameLayout)view.findViewById(R.id.winProgress));
            FrmlossProgress = ((FrameLayout)view.findViewById(R.id.lossProgress));
            btnReset = (Button) view.findViewById(R.id.btn_reset);
          
            btnReset.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					SharedPreferences.Editor editor1 = sharedPref.edit();
					if("Easy".equals(text)){
						editor1.putInt(getString(R.string.easy_high_score),0);
		            	editor1.putInt(getString(R.string.Maxestreak),0);
						editor1.putInt(getString(R.string.estreak),0);
						editor1.putInt(getString(R.string.Teasy),0);
						editor1.putInt(getString(R.string.eWins),0);
						editor1.putInt(getString(R.string.eCaptures),0);
						editor1.commit();
		            }else if("Medium".equals(text)){
		            	editor1.putInt(getString(R.string.medium_high_score),0);
		            	editor1.putInt(getString(R.string.Maxmstreak),0);
						editor1.putInt(getString(R.string.mstreak),0);
						editor1.putInt(getString(R.string.Tmedium),0);
						editor1.putInt(getString(R.string.mWins),0);
						editor1.putInt(getString(R.string.mCaptures),0);
						editor1.commit();
		            }else if("Difficult".equals(text)){
		            	editor1.putInt(getString(R.string.difficult_high_score),0);
		            	editor1.putInt(getString(R.string.Maxdstreak),0);
						editor1.putInt(getString(R.string.dstreak),0);
						editor1.putInt(getString(R.string.Tdifficult),0);
						editor1.putInt(getString(R.string.dWins),0);
						editor1.putInt(getString(R.string.dCaptures),0);
						editor1.commit();
		            }else if("Expert".equals(text)){
		            	editor1.putInt(getString(R.string.expert_high_score),0);
		            	editor1.putInt(getString(R.string.Maxexstreak),0);
						editor1.putInt(getString(R.string.exstreak),0);
						editor1.putInt(getString(R.string.Texpert),0);
						editor1.putInt(getString(R.string.exWins),0);
						editor1.putInt(getString(R.string.exCaptures),0);
						editor1.commit();
		            }
					int highScore = 0,maxStreak=0,gamesPlayed = 0,winProgress = 0,captures = 0, currStreak =0;
		            if("Easy".equals(text)){
		            	highScore = sharedPref.getInt(getString(R.string.easy_high_score), 0);
		            	maxStreak = sharedPref.getInt(getString(R.string.Maxestreak), 0);
		            	gamesPlayed = sharedPref.getInt(getString(R.string.Teasy), 0);
		            	winProgress = sharedPref.getInt(getString(R.string.eWins), 0);
		            	captures = sharedPref.getInt(getString(R.string.eCaptures), 0);
		            	currStreak = sharedPref.getInt(getString(R.string.estreak), 0);
		            }else if("Medium".equals(text)){
		            	highScore = sharedPref.getInt(getString(R.string.medium_high_score), 0);
		            	maxStreak = sharedPref.getInt(getString(R.string.Maxmstreak), 0);
		            	gamesPlayed = sharedPref.getInt(getString(R.string.Tmedium), 0);
		            	winProgress = sharedPref.getInt(getString(R.string.mWins), 0);
		            	captures = sharedPref.getInt(getString(R.string.mCaptures), 0);
		            	currStreak = sharedPref.getInt(getString(R.string.mstreak), 0);
		            }else if("Difficult".equals(text)){
		            	highScore = sharedPref.getInt(getString(R.string.difficult_high_score), 0);
		            	maxStreak = sharedPref.getInt(getString(R.string.Maxdstreak), 0);
		            	gamesPlayed = sharedPref.getInt(getString(R.string.Tdifficult), 0);
		            	winProgress = sharedPref.getInt(getString(R.string.dWins), 0);
		            	captures = sharedPref.getInt(getString(R.string.dCaptures), 0);
		            	currStreak = sharedPref.getInt(getString(R.string.dstreak), 0);
		            }else if("Expert".equals(text)){
		            	highScore = sharedPref.getInt(getString(R.string.expert_high_score), 0);
		            	maxStreak = sharedPref.getInt(getString(R.string.Maxexstreak), 0);
		            	gamesPlayed = sharedPref.getInt(getString(R.string.Texpert), 0);
		            	winProgress = sharedPref.getInt(getString(R.string.exWins), 0);
		            	captures = sharedPref.getInt(getString(R.string.exCaptures), 0);
		            	currStreak = sharedPref.getInt(getString(R.string.exstreak), 0);
		            }
		            txtWinScore.setText(winProgress+"");
		            txtLossScore.setText((gamesPlayed-winProgress)+"");
		            txtHighScore.setText(highScore+"");
		            txtCurrStreak.setText(currStreak+"");
		            txtCaptures.setText(captures+"");            
		            txtStreak.setText(maxStreak+"");
		            float f = (float)(winProgress);
		            if(winProgress == gamesPlayed-winProgress){
		            	f= 1.0f;
		            	FrmwinProgress.setBackground(getResources().getDrawable((R.drawable.roundedgewin)));
		            	FrmlossProgress.setBackground(getResources().getDrawable((R.drawable.roundedgeloss)));
		            }else if(winProgress == 0){
		            	FrmwinProgress.setBackground(getResources().getDrawable((R.drawable.roundedgewin0)));
		            }else if((gamesPlayed-winProgress)==0){
		            	FrmlossProgress.setBackground(getResources().getDrawable((R.drawable.roundedgeloss0)));
		            }else{
		            	FrmwinProgress.setBackground(getResources().getDrawable((R.drawable.roundedgewin)));
		            	FrmlossProgress.setBackground(getResources().getDrawable((R.drawable.roundedgeloss)));
		            }
		            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, f);
		            FrmwinProgress.setLayoutParams(param);
		            f =(float)(gamesPlayed-winProgress);
		            if(winProgress == gamesPlayed-winProgress)
		            	f= 1.0f;
		            param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, f);
		            FrmlossProgress.setLayoutParams(param);
				}
			});
            
            txtWinScore.setText(winProgress+"");
            txtLossScore.setText((gamesPlayed-winProgress)+"");
            txtHighScore.setText(highScore+"");            
            txtStreak.setText(maxStreak+"");
            txtCurrStreak.setText(currStreak+"");
            txtCaptures.setText(captures+"");
            float f = (float)(winProgress);
            if(winProgress == gamesPlayed-winProgress){
            	f= 1.0f;
            	FrmwinProgress.setBackground(getResources().getDrawable((R.drawable.roundedgewin)));
            	FrmlossProgress.setBackground(getResources().getDrawable((R.drawable.roundedgeloss)));
            }else if(winProgress == 0){
            	FrmwinProgress.setBackground(getResources().getDrawable((R.drawable.roundedgewin0)));
            }else if((gamesPlayed-winProgress)==0){
            	FrmlossProgress.setBackground(getResources().getDrawable((R.drawable.roundedgeloss0)));
            }else{
            	FrmwinProgress.setBackground(getResources().getDrawable((R.drawable.roundedgewin)));
            	FrmlossProgress.setBackground(getResources().getDrawable((R.drawable.roundedgeloss)));
            }
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, f);
            FrmwinProgress.setLayoutParams(param);
            f =(float)(gamesPlayed-winProgress);
            if(winProgress == gamesPlayed-winProgress)
            	f= 1.0f;
            param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, f);
            FrmlossProgress.setLayoutParams(param);
            return view;
}}