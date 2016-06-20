package com.enigma.pandamonium;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enigma.logic.AlphaLogic;

public class WalkThroughActivity extends Activity{
	GridView gridView;
	List<CellState> list;
	char[][] playerState;
	String p1,p2;
	AlphaLogic logic;
	GridViewAdapter adapter;
	AlphaAnimation animation;
	boolean playable;
	int level = 1;
	int pScore[][] = new int[5][5];
	RelativeLayout messageLayout;
	TextView playerScore, opponentScore, txtTutorial, txtDescription, next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		logic = new AlphaLogic();
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_walk_through);
		gridView = (GridView) findViewById(R.id.boardgrid);
		playerScore = (TextView) findViewById(R.id.playerScore);
		txtTutorial =(TextView) findViewById(R.id.tutorial);
		txtDescription = (TextView)findViewById(R.id.desciption);
		opponentScore = (TextView) findViewById(R.id.opponentScore);
		initialize();
		animation = new AlphaAnimation(1.0f, 0.0f);
		TextView tv = (TextView)findViewById(R.id.hello);
		animation.setDuration(1000);
		animation.setStartOffset(1000);
		animation.setFillAfter(true);
		tv.startAnimation(animation);
		animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		animation.setStartOffset(3000);
		animation.setFillAfter(true);
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.main_layout);
		layout.startAnimation(animation);
		messageLayout = (RelativeLayout)findViewById(R.id.message);
		next = (TextView)findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				animation = new AlphaAnimation(1.0f, 0.0f);
				animation.setDuration(1000);
				animation.setFillAfter(true);
				messageLayout.startAnimation(animation);
				if(level==1){
					//part 2 tutorial
					CellState object = new CellState();
					object.setImage(R.drawable.player2);
					playerState[3][0] = 'X';
					object.setScore(list.get(15).getScore());
					list.set(15, object);
					adapter.notifyDataSetChanged();						
					p1 = String.format(
							getResources().getString(
									R.string.player_score), logic
									.getScore(pScore, playerState,
											'O'));
					p2 = String.format(
							getResources().getString(
									R.string.opponent_score), logic
									.getScore(pScore, playerState,
											'X'));
					playerScore.setText(p1);
					opponentScore.setText(p2);
					txtTutorial.setText("Tutorial 2/5");
					txtDescription.setText("Capture the enemy panda by playing at the tile adjacent to your panda");

					level+=1;
				}
				if(level ==2){
					
				}
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 17){
					CellState object = new CellState();
					object.setImage(R.drawable.player1);
					playerState[position / 5][position % 5] = 'O';
					object.setScore(list.get(position).getScore());
					list.set(position, object);
					adapter.notifyDataSetChanged();						
					p1 = String.format(
							getResources().getString(
									R.string.player_score), logic
									.getScore(pScore, playerState,
											'O'));
					p2 = String.format(
							getResources().getString(
									R.string.opponent_score), logic
									.getScore(pScore, playerState,
											'X'));
					playerScore.setText(p1);
					opponentScore.setText(p2);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					messageLayout.startAnimation(animation);
				}
				if(position == 16 && level == 2){
					CellState object = new CellState();
					object.setImage(R.drawable.player1);
					playerState[position / 5][position % 5] = 'O';
					object.setScore(list.get(position).getScore());
					list.set(position, object);
					object = new CellState();
					object.setImage(R.drawable.player1);
					playerState[3][0] = 'O';
					object.setScore(list.get(15).getScore());
					list.set(15, object);
					adapter.notifyDataSetChanged();
					p1 = String.format(
							getResources().getString(
									R.string.player_score), logic
									.getScore(pScore, playerState,
											'O'));
					p2 = String.format(
							getResources().getString(
									R.string.opponent_score), logic
									.getScore(pScore, playerState,
											'X'));
					playerScore.setText(p1);
					opponentScore.setText(p2);
					txtTutorial.setText("Tutorial 3/5");
					txtDescription.setText("Capture the enemy panda by playing at the tile adjacent to your panda");
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					messageLayout.startAnimation(animation);
				}
				
				
			}
		});
	}
	
	void initialize(){
		list = new ArrayList<CellState>();
		Resources res = getResources();
		String playertext = String.format(res.getString(R.string.player_score),0);
		String opponentText = String.format(res.getString(R.string.opponent_score), 0);

		playerScore.setText(playertext);
		opponentScore.setText(opponentText);
		int[] Scores = new int[]{66,65,1,71,52,77,38,40,33,53,82,49,30,55,29,91,47,99,0,65,75,22,1,9,95};
		for (int i = 0; i < 25; i++) {
			CellState gridData = new CellState();
			gridData.setImage(0);
			gridData.setScore(Scores[i]);
			list.add(gridData);
			pScore[i / 5][i % 5] = gridData.getScore();
		}
		playerState = new char[][] { { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' } };
		playable = true;
		adapter = new GridViewAdapter(this, R.layout.griditem_layout, list);
		gridView.setAdapter(adapter);
	}
}
