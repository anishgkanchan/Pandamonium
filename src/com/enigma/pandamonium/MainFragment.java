package com.enigma.pandamonium;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainFragment extends Fragment {
	private Button play; 
	private Button help;
	ImageView imgVideo;
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		
		//	this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		play = (Button)rootView.findViewById(R.id.btn_play);
		help = (Button)rootView.findViewById(R.id.btn_how2play);
		imgVideo = (ImageView)rootView.findViewById(R.id.img_video);
		imgVideo.post(
				new Runnable(){
				  @Override
				  public void run() {
					  ((AnimationDrawable)imgVideo.getDrawable()).start();
				  }
				});
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.gc();
				Intent intent = new Intent(getActivity(), OptionsActivity.class);
				startActivity(intent);
				
			}
		});
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.fragment_place, new WalkThroughFragment(), "Walkthrough");
				fragmentTransaction.commit();
				
			}
		});
		return rootView;
	}

}
