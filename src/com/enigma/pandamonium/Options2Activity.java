package com.enigma.pandamonium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Options2Activity extends Activity {
	private Button btnsPlayer;
	private Button btnmPlayer;
	private Button btnStats;
	private Context mContext;
	private TextView txtDifficulty;
	private String[] difficulty= new String[]{"Easy","Medium","Difficult","Expert"};
	int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options2);
		mContext = this;
		btnsPlayer = (Button) findViewById(R.id.singlePlayer);
		btnmPlayer = (Button) findViewById(R.id.multiPlayer);
		btnStats = (Button) findViewById(R.id.score);
		final ImageView imgLeft = (ImageView) findViewById(R.id.lft_arrow);
		final ImageView imgRight = (ImageView) findViewById(R.id.rght_arrow);
		txtDifficulty = (TextView) findViewById(R.id.txt_dificulty);
				
		imgLeft.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				imgLeft.setVisibility(View.VISIBLE);
				if(count<3){
					count++;
					txtDifficulty.setText(difficulty[count]);
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
				startActivity(i);			
			}
		});

		btnmPlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, GameScreenActivity.class);
				i.putExtra("single_player", false);
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
	}

}
