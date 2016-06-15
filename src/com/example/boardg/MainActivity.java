package com.example.boardg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends Activity {
	private Button play; 
	private Button help;
	Context mContext;
	private VideoView video;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		video = (VideoView) findViewById(R.id.video_h);
		String uri = "android.resource://" + getPackageName() + "/" + R.raw.pandamonium;
		video.setVideoURI(Uri.parse(uri));
		video.start();
		play = (Button)findViewById(R.id.btn_play);
		help = (Button)findViewById(R.id.btn_how2play);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, Options2Activity.class);
				startActivity(intent);
				
			}
		});
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, HelpActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		video.start();
	}

}
