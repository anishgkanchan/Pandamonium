package com.example.boardg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.logic.AlphaLogic;

public class GameScreenActivity extends Activity {
	char playerState[][] = new char[][] { { '*', '*', '*', '*', '*' },
			{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' },
			{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' } };

	char[][] oldPlayerState = new char[5][5];
	int movesPlayed = 0; // can have a max value of 25, to identify end of game
	int pScore[][] = new int[5][5];
	GridView gridView;
	AlphaLogic logic = null;
	boolean playable = true;
	boolean singleplayer = true;
	private int difficulty;
	boolean player1 = true;
	TextView playerScore;
	ImageView undoImageView;
	TextView opponentScore;
	GridViewAdapter adapter;
	AlertDialog.Builder alertDialogBuilder;
	List<Nishant> list;
	List<Nishant> prevList;

	Resources res; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		logic = new AlphaLogic();
		singleplayer = getIntent().getBooleanExtra("single_player", true);
		difficulty = getIntent().getIntExtra("difficulty", 1);

		res = getResources(); 

		alertDialogBuilder = new AlertDialog.Builder(this);

		// set dialog message
		alertDialogBuilder
				.setMessage("Play Again?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and refresh the board
								init();
								adapter.notifyDataSetChanged();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						GameScreenActivity.this.finish();
					}
				});

		if (difficulty == 4) {
			System.out.println(difficulty);
			difficulty = 5;
		}
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		gridView = (GridView) findViewById(R.id.boardgrid);
		playerScore = (TextView) findViewById(R.id.playerScore);
		opponentScore = (TextView) findViewById(R.id.opponentScore);
		undoImageView = (ImageView) findViewById(R.id.undo_button);
		Resources res = getResources();
		init();
		undoImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (playable) {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 5; j++) {
							playerState[i][j] = oldPlayerState[i][j];
						}
						System.out.println(Arrays.toString(playerState[i]));
					}

					copyStuff(list, prevList);
					for (int i = 0; i < list.size(); i++)
						System.out.println(list.get(i).imageId);
					adapter.notifyDataSetChanged();
					movesPlayed-=2;

					Runnable r1 = new Runnable() {

						@Override
						public void run() {
							final String text = String.format(getResources()
									.getString(R.string.player_score), logic
									.getScore(pScore, playerState, 'O'));
							final String data = String.format(getResources()
									.getString(R.string.opponent_score), logic
									.getScore(pScore, playerState, 'X'));

							playerScore.setText(text);
							opponentScore.setText(data);
						}
					};
					new Handler().postDelayed(r1, 0);
				}
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (singleplayer) {

					if (list.get(position).getImage() == 0 && playable
							&& movesPlayed < 25) {
						for (int i = 0; i < 5; i++) {
							for (int j = 0; j < 5; j++) {
								oldPlayerState[i][j] = playerState[i][j];
							}
						}
						copyStuff(prevList, list);
						for (int i = 0; i < list.size(); i++)
							System.out.println(prevList.get(i).imageId);

						Nishant object = new Nishant();
						object.setImage(R.drawable.player1);
						if (logic.isRaid(position / 5, position % 5,
								playerState, 'O')) {
							int x = position / 5;
							int y = position % 5;
							char[][] templayer = playerState.clone();
							for (int i = 0; i < playerState.length; i++) {
								System.out.println(Arrays
										.toString(playerState[i]));
							}
							playerState = logic.raid(position / 5,
									position % 5, 'O', playerState, templayer);
							System.out.println("after raid");
							for (int i = 0; i < playerState.length; i++) {
								System.out.println(Arrays
										.toString(playerState[i]));
							}
							if (x - 1 >= 0
									&& playerState[position / 5 - 1][position % 5] == 'O')
								list.get((x - 1) * 5 + y).setImage(
										R.drawable.player1);
							if (x + 1 <= 4
									&& playerState[position / 5 + 1][position % 5] == 'O')
								list.get((x + 1) * 5 + y).setImage(
										R.drawable.player1);
							if (y - 1 >= 0
									&& playerState[position / 5][position % 5 - 1] == 'O')
								list.get(x * 5 + y - 1).setImage(
										R.drawable.player1);
							if (y + 1 <= 4
									&& playerState[position / 5][position % 5 + 1] == 'O')
								list.get(x * 5 + y + 1).setImage(
										R.drawable.player1);
						}
						System.out.println("Was not a raid");
						playerState[position / 5][position % 5] = 'O';
						object.setScore(list.get(position).getScore());
						list.set(position, object);
						movesPlayed += 1;

						playable = false;
						adapter.notifyDataSetChanged();						
						final String text = String.format(
								getResources().getString(
										R.string.player_score), logic
										.getScore(pScore, playerState,
												'O'));
						final String data = String.format(
								getResources().getString(
										R.string.opponent_score), logic
										.getScore(pScore, playerState,
												'X'));
						playerScore.setText(text);
						opponentScore.setText(data);						
						
						new Task2().execute();
					   
					}
				} else {
					char player;
					int playerImage;
					if (player1) {
						player = 'O';
						playerImage = R.drawable.player1;
						player1 = false;
					} else {
						player = 'X';
						playerImage = R.drawable.player2;
						player1 = true;
					}
					if (list.get(position).getImage() == 0) {
						Nishant object = new Nishant();
						object.setImage(playerImage);
						if (logic.isRaid(position / 5, position % 5,
								playerState, player)) {
							int x = position / 5;
							int y = position % 5;
							char[][] templayer = playerState.clone();
							for (int i = 0; i < playerState.length; i++) {
								System.out.println(Arrays
										.toString(playerState[i]));
							}
							playerState = logic.raid(position / 5,
									position % 5, player, playerState,
									templayer);
							System.out.println("after raid");
							for (int i = 0; i < playerState.length; i++) {
								System.out.println(Arrays
										.toString(playerState[i]));
							}
							if (x - 1 >= 0
									&& playerState[position / 5 - 1][position % 5] == player)
								list.get((x - 1) * 5 + y).setImage(playerImage);
							if (x + 1 <= 4
									&& playerState[position / 5 + 1][position % 5] == player)
								list.get((x + 1) * 5 + y).setImage(playerImage);
							if (y - 1 >= 0
									&& playerState[position / 5][position % 5 - 1] == player)
								list.get(x * 5 + y - 1).setImage(playerImage);
							if (y + 1 <= 4
									&& playerState[position / 5][position % 5 + 1] == player)
								list.get(x * 5 + y + 1).setImage(playerImage);
						}
						System.out.println("Was not a raid");
						playerState[position / 5][position % 5] = player;
						object.setScore(list.get(position).getScore());
						list.set(position, object);
						adapter.notifyDataSetChanged();
						final String text = String.format(getResources()
								.getString(R.string.player_score), logic
								.getScore(pScore, playerState, 'O'));
						final String data = String.format(getResources()
								.getString(R.string.opponent_score), logic
								.getScore(pScore, playerState, 'X'));

						playerScore.setText(text);
						opponentScore.setText(data);

					}
				}
			}
		});
		gridView.setAdapter(adapter);
		
	}

	void copyStuff(List<Nishant> dest, List<Nishant> source) {
		for (int i = 0; i < source.size(); i++) {
			dest.set(i, new Nishant(source.get(i).getScore(), source.get(i)
					.getImage()));
		}
	}

	void init() {
		list = new ArrayList<Nishant>();
		prevList = new ArrayList<Nishant>();
		Resources res = getResources();
		String playertext = String.format(res.getString(R.string.player_score),
				0);
		String opponentText = String.format(
				res.getString(R.string.opponent_score), 0);

		playerScore.setText(playertext);
		opponentScore.setText(opponentText);
		Random rand = new Random();
		for (int i = 0; i < 25; i++) {
			Nishant gridData = new Nishant();
			gridData.setImage(0);
			gridData.setScore(rand.nextInt(100));
			list.add(gridData);
			prevList.add(gridData);
			pScore[i / 5][i % 5] = gridData.getScore();
		}
		movesPlayed = 0;
		playerState = new char[][] { { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' } };
		playable = true;
		adapter = new GridViewAdapter(this, R.layout.griditem_layout, list);
		gridView.setAdapter(adapter);

	}

	class GridViewAdapter extends BaseAdapter {
		private Context context;
		private int layoutResourceId;
		private LayoutInflater mLayoutInflater = null;
		private ArrayList<Nishant> mList;

		public GridViewAdapter(Context context, int resourceId,
				List<Nishant> list) {

			this.context = context;
			layoutResourceId = resourceId;
			mList = (ArrayList<Nishant>) list;
			mLayoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		class ViewHolder {
			TextView imageTitle;
			ImageView image;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			ViewHolder temp = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				temp = new ViewHolder();
				temp.imageTitle = (TextView) row
						.findViewById(R.id.tile_score);
				temp.image = (ImageView) row.findViewById(R.id.player_icon);

				row.setTag(temp);
			} else {
				temp = (ViewHolder) row.getTag();
			}
			final ViewHolder holder = temp;
			Nishant item = mList.get(position);
			if (position % 2 == 0)
				row.setBackgroundColor(getResources().getColor(
						R.color.light_brown));
			else
				row.setBackgroundColor(getResources().getColor(
						R.color.dark_brown));
			holder.imageTitle.setText(item.getScore() + "");
			final AnimationDrawable nicePandaDrawable = (AnimationDrawable)res.getDrawable(R.anim.anim_android);
			final AnimationDrawable evilPandaDrawable = (AnimationDrawable)res.getDrawable(R.anim.anim_android_evil);

			if (item.getImage()==R.drawable.player1){
				runOnUiThread(new Thread(new Runnable() {
					 public void run() {
						holder.image.setImageDrawable(nicePandaDrawable);
						holder.image.post(
								new Runnable(){
	
								  @Override
								  public void run() {
									  nicePandaDrawable.start();
								  }
								});
						 }
					 }));
				
				
			}
			else if (item.getImage()==R.drawable.player2){
				runOnUiThread(new Thread(new Runnable() {
					 public void run() {
						holder.image.setImageDrawable(evilPandaDrawable);
						holder.image.post(
								new Runnable(){

								  @Override
								  public void run() {
									  evilPandaDrawable.start();
								  }
								});
						 }
					 }));
			}
			else {
				holder.image.setImageDrawable(null);
			}
			return row;
		}
	}

	class Nishant {
		private int score;
		private int imageId;

		Nishant() {
		}

		Nishant(int source, int imageId) {
			this.score = source;
			this.imageId = imageId;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public int getImage() {
			return imageId;
		}

		public void setImage(int image) {
			this.imageId = image;
		}

	}
		// The definition of our task class
	   private class Task2 extends AsyncTask<String, Integer, Void> {
		   @Override
		   protected void onPreExecute() {
		      super.onPreExecute();
		   }
		 
		   @Override
		   protected Void doInBackground(String... params) {
				if (movesPlayed < 25) {
					playerState = logic.CallAlpha(pScore,
							playerState, difficulty, 'X',
							-99999, 99999);
					movesPlayed += 1;
					System.out.println("Computer plays 1"
							+ prevList.size() + list.size());
					for (int i = 0; i < list.size(); i++) {
						Log.e("Le", prevList.get(i).imageId
								+ "");
					}

					for (int i = 0; i < 25; i++) {
						int x = i / 5;
						int y = i % 5;
						if (playerState[x][y] == 'X')
							list.get(i).setImage(
									R.drawable.player2);
					}
					for (int i = 0; i < list.size(); i++) {
						Log.e("Le23", prevList.get(i).imageId
								+ "");
					}
					final String data = String.format(
							getResources().getString(
									R.string.opponent_score),
							logic.getScore(pScore, playerState,
									'X'));
					final String text = String.format(
							getResources().getString(
									R.string.player_score),
							logic.getScore(pScore, playerState,
									'O'));
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							opponentScore.setText(data);
							playerScore.setText(text);			
							adapter.notifyDataSetChanged();			
						}
					});
					
					playable = true;
				} else {

					
					if (logic
							.getScore(pScore, playerState, 'X') > logic
							.getScore(pScore, playerState, 'O'))
						alertDialogBuilder
								.setTitle("Your Lost");
					else
						alertDialogBuilder
								.setTitle("Your Won!");

					// create alert dialog
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							alertDialogBuilder
							.create().show();
						}
					});
				}
				return null;
		   }
		 
		   @Override
		   protected void onProgressUpdate(Integer... values) {
		      super.onProgressUpdate(values);
		   }
		 
		   
	   }
}
