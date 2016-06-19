package com.example.boardg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.logic.AlphaLogic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class GameScreenActivity extends Activity {

	char playerState[][] = new char[][] { { '*', '*', '*', '*', '*' },
			{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' },
			{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' } };
	View v;
	SharedPreferences sharedPref;
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
	AlertDialog dialog;
	ImageView imgAnimTop;
	ImageView imgAnimBottom;
	TextView opponentScore;
	GridViewAdapter adapter;
	AlertDialog.Builder alertDialogBuilder;
	List<Nishant> list;
	List<Nishant> prevList;
	Resources res; 
	
	@Override
	public void onBackPressed() {
		v = View.inflate(GameScreenActivity.this, R.layout.dialog, null);
		v.setBackgroundResource(R.drawable.defeat_panda);
		TextView title = (TextView)v.findViewById(R.id.title);
		TextView message = (TextView)v.findViewById(R.id.message);
		Button btnYes = (Button)v.findViewById(R.id.btn_yes);
		Button btnNo = (Button)v.findViewById(R.id.btn_no);
		btnYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				gameEndSave(true);
				dialog.dismiss();
				GameScreenActivity.this.finish();	
			}
		});
		btnNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		title.setText("Are you sure?");
		if(singleplayer)
			message.setText("You will automatically lose the game.");
		else
			message.setText("Quit Game");
		alertDialogBuilder.setView(v);
		dialog = alertDialogBuilder.create();
		dialog.show();
		dialog.getWindow().setLayout(900, 600);
				
		
	}
	protected void gameEndSave(boolean lost) {
			   
				int highScore = 0;
				int currentStreak=0,maxStreak =0,totalGames = 0;
				int wins=0;
				int newHighScore = logic.getScore(pScore,
						playerState, 'O');
				if(singleplayer){
				// Statistical Calculations
				switch (difficulty) {
				case 1:
					highScore = sharedPref.getInt(getString(R.string.easy_high_score),0);
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putInt(getString(R.string.easy_high_score),newHighScore);
						editor.commit();
					}
					if(!lost){
						maxStreak = sharedPref.getInt(getString(R.string.Maxestreak), 0);
						currentStreak = sharedPref.getInt(getString(R.string.estreak), 0);
						currentStreak++;
						if(currentStreak>maxStreak){
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putInt(getString(R.string.Maxestreak),currentStreak);
							editor.putInt(getString(R.string.estreak),currentStreak);
							editor.commit();
						}
						Log.e("output",currentStreak+" "+maxStreak);
						wins = sharedPref.getInt(getString(R.string.eWins), 0);
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putInt(getString(R.string.eWins),wins+1);
						editor.commit();
					}else{
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putInt(getString(R.string.estreak),0);
						editor.commit();
					}
					totalGames = sharedPref.getInt(getString(R.string.Teasy),0);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putInt(getString(R.string.Teasy),totalGames+1);
					editor.commit();
					break;
				case 2:highScore = sharedPref.getInt(getString(R.string.medium_high_score),0);
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor1 = sharedPref.edit();
						editor1.putInt(getString(R.string.medium_high_score),newHighScore);
						editor1.commit();
					}
					if(!lost){
						maxStreak = sharedPref.getInt(getString(R.string.Maxmstreak), 0);
						currentStreak = sharedPref.getInt(getString(R.string.mstreak), 0)+1;
						if(currentStreak>maxStreak){
							SharedPreferences.Editor editor1 = sharedPref.edit();
							editor1.putInt(getString(R.string.Maxmstreak),currentStreak);
							editor1.putInt(getString(R.string.mstreak),currentStreak);
							editor1.commit();
						}
						wins = sharedPref.getInt(getString(R.string.mWins), 0);
						SharedPreferences.Editor editor1 = sharedPref.edit();
						editor1.putInt(getString(R.string.mWins),wins+1);
						editor1.commit();
					}else{
						SharedPreferences.Editor editor1 = sharedPref.edit();
						editor1.putInt(getString(R.string.mstreak),0);
						editor1.commit();
					}
					totalGames = sharedPref.getInt(getString(R.string.Tmedium),0);
					SharedPreferences.Editor editor1 = sharedPref.edit();
					editor1.putInt(getString(R.string.Tmedium),totalGames+1);
					editor1.commit();
					break;
				case 3:highScore = sharedPref.getInt(getString(R.string.difficult_high_score),0);
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor2 = sharedPref.edit();
						editor2.putInt(getString(R.string.difficult_high_score),newHighScore);
						editor2.commit();
					}
					if(!lost){
						maxStreak = sharedPref.getInt(getString(R.string.Maxdstreak), 0);
						currentStreak = sharedPref.getInt(getString(R.string.dstreak), 0)+1;
						if(currentStreak>maxStreak){
							SharedPreferences.Editor editor2 = sharedPref.edit();
							editor2.putInt(getString(R.string.Maxdstreak),currentStreak);
							editor2.putInt(getString(R.string.dstreak),currentStreak);
							editor2.commit();
						}
						wins = sharedPref.getInt(getString(R.string.dWins), 0);
						SharedPreferences.Editor editor2 = sharedPref.edit();
						editor2.putInt(getString(R.string.dWins),wins+1);
						editor2.commit();
					}else{
						SharedPreferences.Editor editor2 = sharedPref.edit();
						editor2.putInt(getString(R.string.dstreak),0);
						editor2.commit();
					}
					totalGames = sharedPref.getInt(getString(R.string.Tdifficult),0);
					SharedPreferences.Editor editor2 = sharedPref.edit();
					editor2.putInt(getString(R.string.Tdifficult),totalGames+1);
					editor2.commit();
					break;
				case 4:highScore = sharedPref.getInt(getString(R.string.expert_high_score),0);
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor3 = sharedPref.edit();
						editor3.putInt(getString(R.string.expert_high_score),newHighScore);
						editor3.commit();
						}
						if(!lost){
							maxStreak = sharedPref.getInt(getString(R.string.Maxexstreak), 0);
							currentStreak = sharedPref.getInt(getString(R.string.exstreak), 0)+1;
							if(currentStreak>maxStreak){
								SharedPreferences.Editor editor4= sharedPref.edit();
								editor4.putInt(getString(R.string.Maxexstreak),currentStreak);
								editor4.putInt(getString(R.string.exstreak),currentStreak);
								editor4.commit();

							}
							wins = sharedPref.getInt(getString(R.string.exWins), 0);
							SharedPreferences.Editor editor4 = sharedPref.edit();
							editor4.putInt(getString(R.string.exWins),wins+1);
							editor4.commit();
						}else{
							SharedPreferences.Editor editor4 = sharedPref.edit();
							editor4.putInt(getString(R.string.exstreak),0);
							editor4.commit();
						}
						totalGames = sharedPref.getInt(getString(R.string.Texpert),0);
						SharedPreferences.Editor editor4 = sharedPref.edit();
						editor4.putInt(getString(R.string.Texpert),totalGames+1);
						editor4.commit();
						break;
				default:
					break;
				}
				}
		   }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		sharedPref = this.getSharedPreferences("Shared Preference",
				Context.MODE_PRIVATE);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		logic = new AlphaLogic();
		singleplayer = getIntent().getBooleanExtra("single_player", true);
		difficulty = getIntent().getIntExtra("difficulty", 1);

		res = getResources(); 

		alertDialogBuilder = new AlertDialog.Builder(this);

		// set dialog message
		alertDialogBuilder
				.setCancelable(false);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_screen);
		gridView = (GridView) findViewById(R.id.boardgrid);
		playerScore = (TextView) findViewById(R.id.playerScore);
		opponentScore = (TextView) findViewById(R.id.opponentScore);
		undoImageView = (ImageView) findViewById(R.id.undo_button);
		if(!singleplayer)
			undoImageView.setVisibility(View.GONE);
		imgAnimTop = (ImageView) findViewById(R.id.bg_anim_top);
		imgAnimBottom = (ImageView) findViewById(R.id.bg_anim_bottom);
		//Resources res = getResources();
		init();
		undoImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean same  = true;
				if (playable) {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 5; j++) {
							if(playerState[i][j] != oldPlayerState[i][j])
								same = false;
							playerState[i][j] = oldPlayerState[i][j];
						}
					}
					if(same){
						Toast.makeText(GameScreenActivity.this, "You can only undo 1 move at a time.", Toast.LENGTH_SHORT).show();
						movesPlayed += 2;
					}
					copyStuff(list, prevList);
					adapter.notifyDataSetChanged();
					movesPlayed -= 2;

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
					imgAnimTop.post(
							new Runnable(){
							  @Override
							  public void run() {
								  ((AnimationDrawable)imgAnimTop.getDrawable()).stop();
							  }
							});
					imgAnimBottom.post(
							new Runnable(){
							  @Override
							  public void run() {
								  ((AnimationDrawable)imgAnimBottom.getDrawable()).start();
							  }
							});
					if (list.get(position).getImage() == 0 && playable
							&& movesPlayed < 25) {
						
						for (int i = 0; i < 5; i++) {
							for (int j = 0; j < 5; j++) {
								oldPlayerState[i][j] = playerState[i][j];
							}
						}
						copyStuff(prevList, list);

						Nishant object = new Nishant();
						object.setImage(R.drawable.player1);
						if (logic.isRaid(position / 5, position % 5,
								playerState, 'O')) {
							int x = position / 5;
							int y = position % 5;
							char[][] templayer = playerState.clone();
							playerState = logic.raid(position / 5,
									position % 5, 'O', playerState, templayer);
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
						new Handler().postDelayed(new Runnable() {
						    @Override
						    public void run() {
								new Task2().execute();
						    }
						}, 3000);
					   
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
							playerState = logic.raid(position / 5,
									position % 5, player, playerState,
									templayer);
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
		imgAnimTop.setImageDrawable((AnimationDrawable)getResources().getDrawable(R.anim.bg_anim_top));
		imgAnimTop.post(
				new Runnable(){

				  @Override
				  public void run() {
					  ((AnimationDrawable)imgAnimTop.getDrawable()).start();
				  }
				});
		imgAnimBottom.setImageDrawable((AnimationDrawable)getResources().getDrawable(R.anim.bg_anim_bottom)
		);
		
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
						nicePandaDrawable.start();
						 }
					 }));
				
				
			}
			else if (item.getImage()==R.drawable.player2){
				runOnUiThread(new Thread(new Runnable() {
					 public void run() {
						holder.image.setImageDrawable(evilPandaDrawable);
						evilPandaDrawable.start();
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

					for (int i = 0; i < 25; i++) {
						int x = i / 5;
						int y = i % 5;
						if (playerState[x][y] == 'X')
							list.get(i).setImage(
									R.drawable.player2);
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
					boolean lost = logic.getScore(pScore, playerState, 'X') > logic.getScore(pScore, playerState, 'O');
					gameEndSave(lost);
					v = View.inflate(GameScreenActivity.this, R.layout.dialog, null);
					TextView title = (TextView)v.findViewById(R.id.title);
					TextView message = (TextView)v.findViewById(R.id.message);
					Button btnYes = (Button)v.findViewById(R.id.btn_yes);
					Button btnNo = (Button)v.findViewById(R.id.btn_no);
					btnNo.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
							GameScreenActivity.this.finish();							
						}
					});
					btnYes.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
							init();
							adapter.notifyDataSetChanged();
						}
					});
					if (logic
							.getScore(pScore, playerState, 'X') > logic
							.getScore(pScore, playerState, 'O'))
					{
						v.setBackgroundResource(R.drawable.defeat_panda);
						title.setText("You Lost");
						message.setText("Play Again?");
						alertDialogBuilder.setView(v);
					//alertDialogBuilder.set
					}
					else{
						v.setBackgroundResource(R.drawable.victory_panda);
						title.setText("You Won");
						message.setText("Play Again?");
						alertDialogBuilder.setView(v);
					}
					// create alert dialog
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							dialog = alertDialogBuilder
							.create();
							dialog.show();
							
							
						}
					});
				}
				return null;
		   }
		 

		   @Override
		   protected void onProgressUpdate(Integer... values) {
		      super.onProgressUpdate(values);
		   }
		   @Override
		   protected void onPostExecute(Void values){
				imgAnimBottom.post(new Runnable() {
					
					@Override
					public void run() {
						((AnimationDrawable)imgAnimBottom.getDrawable()).stop();
					}
				});
				imgAnimTop.post(new Runnable() {
									
					@Override
					public void run() {
						((AnimationDrawable)imgAnimTop.getDrawable()).start();
						 
					}
				});
		   }
		   
		 
	   }
}
