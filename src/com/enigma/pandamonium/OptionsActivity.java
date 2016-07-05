package com.enigma.pandamonium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends Activity {
	private Button btnsPlayer;
	private Button btnmPlayer;
	private Button btnStats;
	private Button btnAchievements;
	SharedPreferences sharedPref;
	private Context mContext;
	private TextView txtDifficulty;
	boolean soundFlag = true;
	private String[] difficulty= new String[]{"Easy","Medium","Difficult","Expert"};
	int lock;
	int count = 0;
	
    @Override
    protected void onResume() {
    	super.onResume();
    	lock = sharedPref.getInt(getString(R.string.difficulty_lock),0);
    }
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		sharedPref = this.getSharedPreferences("Shared Preference",Context.MODE_PRIVATE);
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		mContext = this;
		lock = sharedPref.getInt(getString(R.string.difficulty_lock),0);;
		btnsPlayer = (Button) findViewById(R.id.singlePlayer);
		btnmPlayer = (Button) findViewById(R.id.multiPlayer);
		btnStats = (Button) findViewById(R.id.score);
		btnAchievements = (Button) findViewById(R.id.achievements);
		final ImageView imgSound = (ImageView) findViewById(R.id.img_sound);
				
		imgSound.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(soundFlag)
				{
					soundFlag = false;
					imgSound.setImageResource(R.drawable.audio_off);
				} else{
					soundFlag = true;
					imgSound.setImageResource(R.drawable.audio_on);
				}
			}
				
		});
		
		final ImageView imgLeft = (ImageView) findViewById(R.id.lft_arrow);
		final ImageView imgRight = (ImageView) findViewById(R.id.rght_arrow);
		txtDifficulty = (TextView) findViewById(R.id.txt_dificulty);
				
		imgLeft.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				imgRight.setVisibility(View.VISIBLE);
				if(count>0){
					count--;
					txtDifficulty.setText(difficulty[count]);
				}
				if(count==0){
					imgLeft.setVisibility(View.INVISIBLE);
				}
				
			}
		});
		
		imgRight.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(lock != 0)
					imgLeft.setVisibility(View.VISIBLE);
				if(count<3){
				if(count < lock){
					count++;
					txtDifficulty.setText(difficulty[count]);
				}else
					Toast.makeText(OptionsActivity.this, "Clear "+difficulty[lock]+" first", Toast.LENGTH_SHORT).show();
				}
				if(count==3){
					imgRight.setVisibility(View.INVISIBLE);
				}
				
			}
		});
		btnsPlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, GameScreenActivity.class);
				i.putExtra("single_player", true);
				i.putExtra("difficulty", (count+1));
				i.putExtra("audio", soundFlag);
				startActivity(i);			
			}
		});

		btnmPlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, GameScreenActivity.class);
				i.putExtra("single_player", false);
				i.putExtra("audio", soundFlag);
				startActivity(i);
			}
		});
		
		btnStats.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, StatsActivity.class);
				i.putExtra("single_player", false);
				startActivity(i);
			}
		});
		btnAchievements.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, AchievementActivity.class);
				startActivity(i);
			}
		});
	}

}
