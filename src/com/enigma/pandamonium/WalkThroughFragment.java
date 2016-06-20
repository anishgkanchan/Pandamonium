package com.enigma.pandamonium;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enigma.logic.AlphaLogic;

public class WalkThroughFragment extends Fragment{
	GridView gridView;
	List<CellState> list;
	char[][] playerState;
	String p1,p2;
	AlphaLogic logic;
	GridViewAdapter adapter;
	AlphaAnimation animation;
	RelativeLayout layout;
	RelativeLayout tv;
	Button btnTutorial;
	boolean playable;
	int level = 0;
	boolean messageVisible = false;
	int pScore[][] = new int[5][5];
	RelativeLayout messageLayout;
	TextView playerScore, opponentScore, txtTutorial, txtDescription, next, txtProgress;
	
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.fragment_walk_through, container, false);
		
		logic = new AlphaLogic();
		gridView = (GridView) rootView.findViewById(R.id.boardgrid);
		playerScore = (TextView) rootView.findViewById(R.id.playerScore);
		txtTutorial =(TextView) rootView.findViewById(R.id.tutorial);
		txtDescription = (TextView)rootView.findViewById(R.id.desciption);
		opponentScore = (TextView) rootView.findViewById(R.id.opponentScore);
		
		initialize();
		tv = (RelativeLayout)rootView.findViewById(R.id.help_layout);
		layout = (RelativeLayout)rootView.findViewById(R.id.main_layout);
		messageLayout = (RelativeLayout)rootView.findViewById(R.id.message);
		next = (TextView)rootView.findViewById(R.id.next);
		txtProgress = (TextView)rootView.findViewById(R.id.text_progress);
		btnTutorial = (Button) rootView.findViewById(R.id.btn_tutorial);
		btnTutorial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (level == 0) {
					animation = new AlphaAnimation(1.0f, 0.0f);
					animation.setDuration(500);
					animation.setStartOffset(500);
					animation.setFillAfter(true);
					tv.startAnimation(animation);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(500);
					animation.setStartOffset(500);
					animation.setFillAfter(true);
					
					layout.startAnimation(animation);
					level = 1;
				}
				
			}
		});
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.e("", "Next is pressed");
				if(messageVisible) 
				{
					messageVisible = false;
					animation = new AlphaAnimation(1.0f, 0.0f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					animation.setAnimationListener(new AnimationListener(){
						@Override
						public void onAnimationEnd(Animation arg0) {
							Log.e("", "Reachable?");
							messageLayout.setVisibility(View.GONE);
							messageLayout.clearAnimation();
							next.clearAnimation();
						}
						@Override public void onAnimationRepeat(Animation animation) {}
						@Override public void onAnimationStart(Animation animation) {}
						
					});
					messageLayout.startAnimation(animation);
					if(level==1){
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
						txtTutorial.setText("Tutorial 2/4");
						txtDescription.setText("Capture the enemy panda by playing at the tile adjacent to your panda");
						level+=1;
						CellState object1 = list.get(16);
						object1.setImage(R.drawable.blink1);
						list.set(16, object1);
					}else if(level ==2){
						initialize();
						CellState object = new CellState();
						object.setImage(R.drawable.player2);
						playerState[3][2] = 'X';
						object.setScore(list.get(17).getScore());
						list.set(17, object);
						object = new CellState();
						object.setImage(R.drawable.player2);
						playerState[2][1] = 'X';
						object.setScore(list.get(11).getScore());
						list.set(11, object);
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
						level+=1;
						txtTutorial.setText("Tutorial 3/4");
						txtDescription.setText("You can't capture enemy Pandas if you don't have your own Panda adjacent to your next move.");
						CellState object1 = list.get(12);
						object1.setImage(R.drawable.blink1);
						list.set(12, object1);
					}else if(level == 3){
						CellState object = new CellState();
						object.setImage(0);
						playerState[2][2] = '*';
						object.setScore(list.get(12).getScore());
						list.set(12, object);
						object = new CellState();
						object.setImage(R.drawable.player1);
						playerState[1][2] = 'O';
						object.setScore(list.get(7).getScore());
						list.set(7, object);
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
						level+=1;
						txtTutorial.setText("Tutorial 4/4");
						txtDescription.setText("However if you do have your own panda next to your next move, you can capture enemy Pandas");
						CellState object1 = list.get(12);
						object1.setImage(R.drawable.blink1);
						list.set(12, object1);
					}else if(level == 4){
						SharedPreferences.Editor editor = getActivity().getSharedPreferences("Shared Preference", Context.MODE_PRIVATE).edit();
							editor.putInt(getString(R.string.first_time),1);
							editor.commit();
							FragmentManager fm = getActivity().getSupportFragmentManager();
							FragmentTransaction fragmentTransaction = fm.beginTransaction();
							fragmentTransaction.replace(R.id.fragment_place, new MainFragment(), "Main");
							fragmentTransaction.commit();
					}
				}
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("Item clicked "+position);
				Log.e("", "Item clicked "+position);
				if(position == 17 && level == 1 && messageVisible == false){
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
					txtProgress.setText("Well Done!!");
					messageLayout.setVisibility(View.INVISIBLE);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					messageLayout.startAnimation(animation);
					messageVisible = true;
				}else if(position == 16 && level == 2 && messageVisible == false){
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
					txtProgress.setText("Woohhooo!! No more enemy Pandas there.");
					messageLayout.setVisibility(View.INVISIBLE);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					messageLayout.startAnimation(animation);
					messageVisible = true;
				}else if(position == 12 && level == 3 && messageVisible == false){
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
					txtProgress.setText("It's not always possible to capture enemy Pandas, but you know that now!");
					messageLayout.setVisibility(View.INVISIBLE);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					messageLayout.startAnimation(animation);
					messageVisible = true;
				}else if(position == 12 && level == 4 && messageVisible == false){
					CellState object = new CellState();
					object.setImage(R.drawable.player1);
					playerState[position / 5][position % 5] = 'O';
					object.setScore(list.get(position).getScore());
					list.set(position, object);
					object = new CellState();
					object.setImage(R.drawable.player1);
					playerState[2][1] = 'O';
					object.setScore(list.get(11).getScore());
					list.set(11, object);
					object = new CellState();
					object.setImage(R.drawable.player1);
					playerState[3][2] = 'O';
					object.setScore(list.get(17).getScore());
					list.set(17, object);
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
					txtProgress.setText("Finally, remember that your objective is to increase the score, and not to increase the tiles you capture.");
					next.setText("Done");
					messageLayout.setVisibility(View.INVISIBLE);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					messageLayout.startAnimation(animation);
					messageVisible = true;
				}
				
				
			}
		});
	    return rootView;
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
			if(i == 17){
				gridData.setImage(R.drawable.blink1);
			}
			gridData.setScore(Scores[i]);
			list.add(gridData);
			pScore[i / 5][i % 5] = gridData.getScore();
		}
		playerState = new char[][] { { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' } };
		playable = true;
		adapter = new GridViewAdapter(getActivity(), R.layout.griditem_layout, list);
		gridView.setAdapter(adapter);
	}
}
