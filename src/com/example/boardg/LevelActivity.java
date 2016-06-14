package com.example.boardg;

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
 
            View android = inflater.inflate(R.layout.scorefragment1, container, false);
            ((TextView)android.findViewById(R.id.textView)).setText(text);
            return android;
}}