package com.enigma.pandamonium;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

public class AchievementActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_achievement);
		ListView lv = (ListView) findViewById(R.id.list_achievements);
		String []achivementsList = {"50 games won in Easy mode"," 250 captures in Easy mode","A win streak of 15 games in Easy mode", "Get 1000 points in Easy mode in a single game",
				"30 games won in Medium mode", " 250 captures in Medium mode","A win streak of 10 games in Medium mode","Get 1000 points in Medium mode in a single game",
				"20 games won in Difficult mode"," 250 captures in Difficult mode","A win streak of 7 games in Difficult mode","Get 900 points in Difficult mode in a single game",
				"15 games won in Expert mode"," 250 captures in Expert mode","A win streak of 5 games in Expert mode","Get 900 points in Expert mode in a single game",
				 "Share on Facebook","Have just a single on the board in Medium mode","Complete a game in Easy mode without capturing a single enemy panda with minimum score of 100",
				 "Win a game in Difficult mode without using the undo button","Capture all the tiles on the board (All White)"};
		
		int []imageIds = new int[]{R.drawable.x_won_easy, R.drawable.x_captures_easy, R.drawable.x_streak_easy, R.drawable.x_points_win_easy,
				R.drawable.x_won_medium, R.drawable.x_captures_medium, R.drawable.x_streak_medium, R.drawable.x_points_win_medium,
				R.drawable.x_won_hard, R.drawable.x_captures_hard, R.drawable.x_streak_hard, R.drawable.x_points_win_hard,
				R.drawable.x_won_expert, R.drawable.x_captures_expert, R.drawable.x_streak_expert, R.drawable.x_points_win_expert,
				R.drawable.facebook,R.drawable.x_won_easy,R.drawable.x_streak_easy,R.drawable.x_streak_hard,R.drawable.panda_slayer};
		List<AchieveState> items = new ArrayList<AchieveState>();
		for (int i = 0; i<21; i++){
			AchieveState state = new AchieveState();
			state.setImage(imageIds[i]);
			state.setTitle(achivementsList[i]);
			items.add(state);
		}
		ListViewAdapter adapter = new ListViewAdapter(this,R.layout.list_item, items);
		lv.setAdapter(adapter);
	}
}