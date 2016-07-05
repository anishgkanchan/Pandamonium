package com.enigma.pandamonium;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enigma.logic.AlphaLogic;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;



public class GameScreenActivity extends Activity {

	char playerState[][] = new char[][] { { '*', '*', '*', '*', '*' },
			{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' },
			{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' } };
	 String[] tasks = new String[]{"Capture every tile in easy mode",
	 "Win 50 games in Easy mode",
	 "Win 30 game Medium mode",
	 "Win 20 games in Difficult mode",
	 "Win 15 games in Expert mode",
	 "Achieve a winning streak of 15 games in Easy mode",
	 "Achieve a winning streak of 10 games in Medium mode",
	 "Achieve a winning streak of 7 games in Difficult mode",
	 "Achieve a winning streak of 7 games in Expert mode",
	 "Capture 250 enemies in Easy mode",
	 "Capture 250 enemies in Medium mode",
	 "Capture 250 enemies in Difficult mode",
	 "Capture 250 enemies in Expert mode",
	 "Win a game without undo for Hard mode",
	 "Share a post on Facebook",
	 "Win against Easy without getting yourself captured",
	 "Complete a game without capturing enemy with a minimum score of 100",
	 "Make 1000 points in Easy in a single game",
	 "Make 1000 points in Medium in a single game",
	 "Make 900 points in Hard in a single game",
	 "Make 900 points in Expert in a single game"};
	
	
	View v;
	SharedPreferences sharedPref;
	//must be changes as they are added
	char[][] oldPlayerState = new char[5][5];
	int movesPlayed = 0; // can have a max value of 25, to identify end of game
	int pScore[][] = new int[5][5];
	GridView gridView;
	boolean globalCaptFlag;
	AlphaLogic logic = null;
	boolean playable = true;
	boolean singleplayer = true;
	private boolean undoUsed;
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
	List<CellState> list;
	boolean gameBegan = false;
	List<CellState> prevList;
	int capture = 0;
	int newHighScore = 0;
	int sound = 1;
	Resources res;
	int achieveLock;
	boolean fbShare;
	boolean audioFlag = true;
	private final String PENDING_ACTION_BUNDLE_KEY =
            "com.example.hellofacebook:PendingAction";

    private ImageView postStatusUpdateButton;
    private PendingAction pendingAction = PendingAction.NONE;
    private boolean canPresentShareDialog;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
	private MediaPlayer mediaPlayer1 = null;
	private MediaPlayer mediaPlayer2 = null;
	float mpVol = 0.0f;
    
	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
    	@Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            String achieve =  sharedPref.getString(getString(R.string.achievements), achievements);
			if(achieve.charAt(16)=='0'){
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(getString(R.string.achievements),  achieve.substring(0,16)+'1'+achieve.substring(17));
				achieveLock++;
				Toast.makeText(GameScreenActivity.this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
				editor.putInt(getString(R.string.locked_achievements), achieveLock);
				editor.commit();
			}
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            	
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(GameScreenActivity.this)
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
	@Override
	public void onBackPressed() {
		if(!gameBegan)
			super.onBackPressed();
		else{
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
				dialog.dismiss();
			}
		});
		title.setText("Are you sure?");
		if(singleplayer)
			message.setText("You will automatically lose the game.");
		else
			message.setText("Quit game");
		alertDialogBuilder.setView(v);
		dialog = alertDialogBuilder.create();
		dialog.show();
		}
		
	}
	int lock;
	// bit for every achievement
	final String achievements = "000000000000000000000";
	protected void gameEndSave(boolean lost) {
			   
				int highScore = 0;
				int captures = 0;
				lock = sharedPref.getInt(getString(R.string.difficulty_lock),0);
				int currentStreak=0,maxStreak =0,totalGames = 0;
				int wins=0;
				newHighScore = logic.getScore(pScore,
						playerState, 'O');
				if(singleplayer){
					

				Log.i("Achieve lock Value",achieveLock+"------------BEFORE SWITCH---------------");
				
				
				// Statistical Calculations
				switch (difficulty) {
				case 1:
					highScore = sharedPref.getInt(getString(R.string.easy_high_score),0);
					captures = sharedPref.getInt(getString(R.string.eCaptures),0);
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putInt(getString(R.string.easy_high_score),newHighScore);
						editor.commit();
					}
					String easyAchievements = sharedPref.getString(getString(R.string.achievements), achievements);
					if(!lost){
						maxStreak = sharedPref.getInt(getString(R.string.Maxestreak), 0);
						currentStreak = sharedPref.getInt(getString(R.string.estreak), 0);
						currentStreak++;
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putInt(getString(R.string.eWins),wins+1);
						editor.commit();
						wins = sharedPref.getInt(getString(R.string.eWins), 0);
						int easyIndices[] = {0,1,2,3,18,20};
						for (int i:easyIndices){
							easyAchievements =  sharedPref.getString(getString(R.string.achievements), achievements);
							if(easyAchievements.charAt(i)!='1'){
								editor = sharedPref.edit();
								switch(i){
								
									case 0://30 games in easy
										if(wins>=30 && easyAchievements.charAt(0)=='0'){
											editor.putString(getString(R.string.achievements),  '1'+easyAchievements.substring(1));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 1:
										System.out.println("BlaBLabefore"+sharedPref.getString(getString(R.string.achievements), achievements));
										//200 captures easy
										if(captures+capture>=200 && easyAchievements.charAt(1)=='0'){
											editor.putString(getString(R.string.achievements),  easyAchievements.substring(0,1)+'1'+easyAchievements.substring(2));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 2://Streak 10 easy
										if(currentStreak>=10 && easyAchievements.charAt(2)=='0'){
												editor.putString(getString(R.string.achievements),  easyAchievements.substring(0,2)+'1'+easyAchievements.substring(3));
												editor.commit();
												achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
											}
										break;
									case 3:
										//Make 1000 points in Easy
										if(newHighScore>=1000 && easyAchievements.charAt(3)=='0'){
											editor.putString( getString(R.string.achievements),  easyAchievements.substring(0,3)+'1'+ easyAchievements.substring(4));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
											
										}
									
										break;
								
									case 18://Complete a game without capturing enemy and making 100
										break;
									case 20:
										if(achieveLock<20 || easyAchievements.charAt(20)=='1')
											break;
										//Panda slayer
										boolean flag = true;
										for(int l=0;l<5;l++)
											System.out.println(Arrays.toString(playerState[l]));
											for (int p=0;p<5;p++){
												for(int q=0;q<5;q++){
													if(playerState[p][q] == 'X')
													{
														flag = false;
													}
												}
											}
											if(flag){
													editor.putString(getString(R.string.achievements),  easyAchievements.substring(0,20)+'1');
													editor.commit();
													achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
												}
								}
							}
						}
						
						editor = sharedPref.edit();
						
						if(currentStreak>maxStreak){
							editor.putInt(getString(R.string.Maxestreak),currentStreak);
							editor.putInt(getString(R.string.locked_achievements),achieveLock);
							editor.putInt(getString(R.string.estreak),currentStreak);
							editor.commit();
						}
						
						if(lock<1)
							editor.putInt(getString(R.string.difficulty_lock),1);
						editor.putInt(getString(R.string.estreak),currentStreak);
						editor.commit();
					}else{
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putInt(getString(R.string.estreak),0);
						editor.commit();
					}
					totalGames = sharedPref.getInt(getString(R.string.Teasy),0);
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putInt(getString(R.string.eCaptures),captures+capture);
					editor.putInt(getString(R.string.Teasy),totalGames+1);
					editor.commit();
					if(!globalCaptFlag && newHighScore>=100 && achieveLock>=18 && easyAchievements.charAt(18)=='0'){
						editor.putString(getString(R.string.achievements),  easyAchievements.substring(0,18)+'1'+easyAchievements.substring(19));
						editor.commit();
						achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
					}
					break;
				case 2:highScore = sharedPref.getInt(getString(R.string.medium_high_score),0);
					captures = sharedPref.getInt(getString(R.string.mCaptures),0);
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor1 = sharedPref.edit();
						editor1.putInt(getString(R.string.medium_high_score),newHighScore);
						editor1.commit();
					}
					wins = sharedPref.getInt(getString(R.string.mWins), 0);
					String mediumAchievements = sharedPref.getString(getString(R.string.achievements), achievements);
					if(!lost){
						maxStreak = sharedPref.getInt(getString(R.string.Maxmstreak), 0);
						currentStreak = sharedPref.getInt(getString(R.string.mstreak), 0)+1;
						SharedPreferences.Editor editor1 = sharedPref.edit();
						editor1.putInt(getString(R.string.mWins),wins+1);
						editor1.commit();
						wins = sharedPref.getInt(getString(R.string.mWins), 0);
						int mediumIndices[] = {4,5,6,7,17};
						for (int i:mediumIndices){
							mediumAchievements =  sharedPref.getString(getString(R.string.achievements), achievements);
							if(mediumAchievements.charAt(i)!='1'){
								editor = sharedPref.edit();
								switch(i){
								
									case 4://20 games in medium
										if(achieveLock<5)
											break;
										if(wins>=20 && mediumAchievements.charAt(4)=='0'){
											editor.putString(getString(R.string.achievements),  mediumAchievements.substring(0,4)+'1'+mediumAchievements.substring(5));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 5:
										if(achieveLock<150 || mediumAchievements.charAt(5)=='1')
											break;
										//150 captures medium
										if(captures+capture>=5){
											editor.putString(getString(R.string.achievements),  mediumAchievements.substring(0,5)+'1'+mediumAchievements.substring(6));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 6://Streak 7 medium
										if(achieveLock<6 || mediumAchievements.charAt(6)=='1')
											break;
										if(currentStreak>=7){
											editor.putString(getString(R.string.achievements),   mediumAchievements.substring(0,6)+'1'+mediumAchievements.substring(7));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 7://Make 1000 points in Medium
										if(achieveLock<7 || mediumAchievements.charAt(7)=='1')
											break;
										if(newHighScore>=1000){
											editor.putString(getString(R.string.achievements),  mediumAchievements.substring(0,7)+'1'+mediumAchievements.substring(8));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 17:
										break;
								}
							}
						}
						
						editor1 = sharedPref.edit();
						
						if(currentStreak>maxStreak){
							editor1.putInt(getString(R.string.Maxmstreak),currentStreak);
							editor1.putInt(getString(R.string.locked_achievements),achieveLock);
							editor1.putInt(getString(R.string.mstreak),currentStreak);
							editor1.commit();
						}
						if(lock<2)
							editor1.putInt(getString(R.string.difficulty_lock),2);
						editor1.commit();
					}else{
						SharedPreferences.Editor editor1 = sharedPref.edit();
						editor1.putInt(getString(R.string.mstreak),0);
						editor1.commit();
						//check for loss achievement
						if(achieveLock>=17 && mediumAchievements.charAt(17)=='0'){
							int count = 0;
							anish: for(int k=0;k<5;k++)
							{	
								for(int j=0;j<5;j++){
									if (playerState[k][j]=='O')
									{
										count++;
										if(count==2)
											break anish;
									}
								}
							}
								if (count==1)
								{
									editor1.putString(getString(R.string.achievements),  mediumAchievements.substring(0,17)+'1'+mediumAchievements.substring(18));
									editor1.commit();
									achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
									
								}
							
						}
					}
					totalGames = sharedPref.getInt(getString(R.string.Tmedium),0);
					SharedPreferences.Editor editor1 = sharedPref.edit();
					editor1.putInt(getString(R.string.mCaptures),captures+capture);
					editor1.putInt(getString(R.string.Tmedium),totalGames+1);
					editor1.commit();
					break;
				case 3:highScore = sharedPref.getInt(getString(R.string.difficult_high_score),0);
					captures = sharedPref.getInt(getString(R.string.dCaptures),0);	
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor2 = sharedPref.edit();
						editor2.putInt(getString(R.string.difficult_high_score),newHighScore);
						editor2.commit();
					}
					if(!lost){
						maxStreak = sharedPref.getInt(getString(R.string.Maxdstreak), 0);
						currentStreak = sharedPref.getInt(getString(R.string.dstreak), 0)+1;

						wins = sharedPref.getInt(getString(R.string.dWins), 0);
						SharedPreferences.Editor editor2 = sharedPref.edit();
						editor2.putInt(getString(R.string.dWins),wins+1);
						editor2.commit();
						wins = sharedPref.getInt(getString(R.string.dWins), 0);
						String hardAchievements =  sharedPref.getString(getString(R.string.achievements), achievements);
						int hardIndices[] = {8,9,10,11,19};
						for (int i: hardIndices){
							hardAchievements =  sharedPref.getString(getString(R.string.achievements), achievements);
							if(hardAchievements.charAt(i)!='1'){
								editor = sharedPref.edit();
								switch(i){
								
									case 8://15 games in hard
										if(achieveLock<8 || hardAchievements.charAt(8)=='1')
											break;
										if(wins>=15){
											editor.putString(getString(R.string.achievements),  hardAchievements.substring(0,8)+'1'+hardAchievements.substring(9));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 9://150 captures hard
										if(achieveLock<9 || hardAchievements.charAt(9)=='1')
											break;
										if(captures+capture >= 150){
											editor.putString(getString(R.string.achievements),  hardAchievements.substring(0,9)+'1'+hardAchievements.substring(10));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 10:
										if(achieveLock<10 || hardAchievements.charAt(10)=='1')
											break;
										//Streak 5 hard
										if(currentStreak>=5){
												editor.putString(getString(R.string.achievements),  hardAchievements.substring(0,10)+'1'+hardAchievements.substring(11));
												editor.commit();
												achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
											}
										break;
									case 11://Make 900 points in hard
										if(achieveLock<11 || hardAchievements.charAt(11)=='1')
											break;
										if(newHighScore>=900){
											editor.putString(getString(R.string.achievements),  hardAchievements.substring(0,11)+'1'+hardAchievements.substring(12));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
										break;
									case 19://Game  won without Undo for Hard
										if(achieveLock<19 || hardAchievements.charAt(19)=='1')
											break;
										if(!undoUsed){
											editor.putString(getString(R.string.achievements),  hardAchievements.substring(0,19)+'1'+hardAchievements.substring(0,20));
											editor.commit();
											achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
										}
								}
							}
						}

						editor2 = sharedPref.edit();
						if(currentStreak>maxStreak){
							editor2.putInt(getString(R.string.Maxdstreak),currentStreak);
							editor2.putInt(getString(R.string.locked_achievements),achieveLock);
							editor2.putInt(getString(R.string.dstreak),currentStreak);
							editor2.commit();
						}
						if(lock<3)
							editor2.putInt(getString(R.string.difficulty_lock),3);
						editor2.commit();
					}else{
						SharedPreferences.Editor editor2 = sharedPref.edit();
						editor2.putInt(getString(R.string.dstreak),0);
						editor2.commit();
					}
					totalGames = sharedPref.getInt(getString(R.string.Tdifficult),0);
					SharedPreferences.Editor editor2 = sharedPref.edit();
					editor2.putInt(getString(R.string.dCaptures),captures+capture);
					editor2.putInt(getString(R.string.Tdifficult),totalGames+1);
					editor2.commit();
					break;
				case 4:highScore = sharedPref.getInt(getString(R.string.expert_high_score),0);
					captures = sharedPref.getInt(getString(R.string.exCaptures),0);
					if (newHighScore > highScore) {
						SharedPreferences.Editor editor3 = sharedPref.edit();
						editor3.putInt(getString(R.string.expert_high_score),newHighScore);
						editor3.commit();
						}
						if(!lost){
							maxStreak = sharedPref.getInt(getString(R.string.Maxexstreak), 0);
							currentStreak = sharedPref.getInt(getString(R.string.exstreak), 0)+1;
							

							wins = sharedPref.getInt(getString(R.string.exWins), 0);
							SharedPreferences.Editor editor4 = sharedPref.edit();
							editor4.putInt(getString(R.string.exWins),wins+1);
							editor4.commit();
							wins = sharedPref.getInt(getString(R.string.exWins), 0);
							String expertAchievements =  sharedPref.getString(getString(R.string.achievements), achievements);
							int expertIndices[]={12,13,14,15};
							for (int i:expertIndices){
								expertAchievements =  sharedPref.getString(getString(R.string.achievements), achievements);
								if(expertAchievements.charAt(i)!='1'){
									editor = sharedPref.edit();
									switch(i){
										case 12://10 games in expert
											if(achieveLock<12 || expertAchievements.charAt(12)=='1')
												break;
											if(wins>=10){
												editor.putString(getString(R.string.achievements),  expertAchievements.substring(0,12)+'1'+expertAchievements.substring(13));
												editor.commit();
												achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
											}
											break;
										case 13://150 captures expert
											if(achieveLock<13 || expertAchievements.charAt(13)=='1')
												break;
											if(capture+captures>=150){
												editor.putString(getString(R.string.achievements),  expertAchievements.substring(0,13)+'1'+expertAchievements.substring(14));
												editor.commit();
												achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
											}
											break;
										case 14:
											if(achieveLock<14 || expertAchievements.charAt(14)=='1')
												break;
											//Streak 5 expert
											if(currentStreak>=5){
													editor.putString(getString(R.string.achievements),  expertAchievements.substring(0,14)+'1'+expertAchievements.substring(15));
													editor.commit();
													achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
												}
											break;
										case 15://Make 800 points in expert
											if(achieveLock<15 || expertAchievements.charAt(15)=='1')
												break;
											if(newHighScore>=800){
												editor.putString(getString(R.string.achievements),  expertAchievements.substring(0,15)+'1'+expertAchievements.substring(16));
												editor.commit();
												achieveLock++;		Toast.makeText(this, "You have completed an achievement", Toast.LENGTH_SHORT).show();
;
											}
									}
								}
							}
							

							editor4= sharedPref.edit();
							if(currentStreak>maxStreak){
								editor4.putInt(getString(R.string.Maxexstreak),currentStreak);
								editor4.putInt(getString(R.string.locked_achievements),achieveLock);
								editor4.putInt(getString(R.string.exstreak),currentStreak);
								editor4.commit();

							}
							
						}else{
							SharedPreferences.Editor editor4 = sharedPref.edit();
							editor4.putInt(getString(R.string.exstreak),0);
							editor4.commit();
						}
						totalGames = sharedPref.getInt(getString(R.string.Texpert),0);
						SharedPreferences.Editor editor4 = sharedPref.edit();
						editor4.putInt(getString(R.string.exCaptures),captures+capture);
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
		sharedPref = this.getSharedPreferences("Shared Preference", Context.MODE_PRIVATE);
		achieveLock = sharedPref.getInt(getString(R.string.locked_achievements),4);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		logic = new AlphaLogic();
		singleplayer = getIntent().getBooleanExtra("single_player", true);
		difficulty = getIntent().getIntExtra("difficulty", 1);
		audioFlag = getIntent().getBooleanExtra("audio", true);
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        if(audioFlag){
        	 mpVol = 1;
	    	 mediaPlayer1 = MediaPlayer.create(this, R.raw.musicforpanda);
	    	 mediaPlayer1.setVolume(mpVol, mpVol);
	    	 mediaPlayer1.start();
	    	 mediaPlayer2 = MediaPlayer.create(this, R.raw.musicforpanda);
	    	 mediaPlayer2.setVolume(mpVol, mpVol);
	    	 final Handler h = new Handler();
	    	 h.post(new Runnable() {
				
				@Override
				public void run() {
					if(sound==1){
						double diff = mediaPlayer1.getDuration() - mediaPlayer1.getCurrentPosition();
						  if (diff<=200 && diff>=0) {
							  mediaPlayer1.stop();
							  mediaPlayer2.start();
							  sound = 2;
						  }
					} else{
						double diff = mediaPlayer2.getDuration() - mediaPlayer2.getCurrentPosition();
						  if (diff<=200 && diff>=0) {
							  mediaPlayer2.stop();
							  mediaPlayer1.start();
							  sound = 1;
						  }
					}
					 h.postDelayed(this, 150);
				}
			});
	    	
        }
        canPresentShareDialog = ShareDialog.canShow(
                ShareLinkContent.class);

 
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
		undoImageView = (ImageView) findViewById(R.id.undo_button);;
		imgAnimTop = (ImageView) findViewById(R.id.bg_anim_top);
		imgAnimBottom = (ImageView) findViewById(R.id.bg_anim_bottom);
		init();
		undoImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				undoUsed = true;
				boolean same  = true;
				if(!singleplayer){
					if(player1)
						player1 = false;
					else
						player1 = true;
				}
				if (playable || !singleplayer) {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 5; j++) {
							if(playerState[i][j] != oldPlayerState[i][j])
								same = false;
							playerState[i][j] = oldPlayerState[i][j];
						}
					}
					if(same){
						Toast.makeText(GameScreenActivity.this, "You can only undo 1 move at a time.", Toast.LENGTH_SHORT).show();
						if(!singleplayer)
							movesPlayed+=1;
						else
							movesPlayed += 2;
					}
					copyStuff(list, prevList);
					adapter.notifyDataSetChanged();
					if(!singleplayer)
						movesPlayed-=1;
					else
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
				gameBegan = true;
				if(playerState[position/5][position%5]=='*'){
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

						CellState object = new CellState();
						object.setImage(R.drawable.player1);
						boolean captFlag = false;
						if (logic.isRaid(position / 5, position % 5,
								playerState, 'O')) {
							int x = position / 5;
							int y = position % 5;
							if((x+1<=4&&playerState[x+1][y]=='X')||(y+1<=4&&playerState[x][y+1]=='X')||(x-1>=0&&playerState[x-1][y]=='X')||(y-1>=0&&playerState[x][y-1]=='X'))
							{
								captFlag = true;
								globalCaptFlag = true;
							}
								char[][] templayer = playerState.clone();
							playerState = logic.raid(position / 5,
									position % 5, 'O', playerState, templayer);
							if (x - 1 >= 0
									&& playerState[position / 5 - 1][position % 5] == 'O'){
								list.get((x - 1) * 5 + y).setImage(R.drawable.player1);
							}
							if (x + 1 <= 4&& playerState[position / 5 + 1][position % 5] == 'O'){
								list.get((x + 1) * 5 + y).setImage(R.drawable.player1);
							}
							if (y - 1 >= 0 && playerState[position / 5][position % 5 - 1] == 'O'){
								list.get(x * 5 + y - 1).setImage(R.drawable.player1);
							}
							if (y + 1 <= 4 && playerState[position / 5][position % 5 + 1] == 'O'){
								list.get(x * 5 + y + 1).setImage(R.drawable.player1);
							}
						if(captFlag)
							capture++;
						}
						playerState[position / 5][position % 5] = 'O';
						object.setScore(list.get(position).getScore());
						list.set(position, object);
						movesPlayed += 1;

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
						playable = false;
						if(movesPlayed==25)
						{
						
							boolean lost = logic.getScore(pScore, playerState, 'X') > logic.getScore(pScore, playerState, 'O');
							gameEndSave(lost);
							
							v = View.inflate(GameScreenActivity.this, R.layout.dialog, null);

							 postStatusUpdateButton = (ImageView) v.findViewById(R.id.postStatusUpdateButton);
							 postStatusUpdateButton.setVisibility(View.VISIBLE);
						        postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
						            public void onClick(View view) {
						                onClickPostStatusUpdate();
						            }
						        });
						        
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
						else{
							new Handler().postDelayed(new Runnable() {
							    @Override
							    public void run() {
									new Task2().execute();
							    }
							}, 2000);
						}
					}
				} else {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 5; j++) {
							oldPlayerState[i][j] = playerState[i][j];
						}
					}
					copyStuff(prevList, list);
					char player;
					int playerImage;
					if (player1) {
						player = 'O';
						playerImage = R.drawable.player1;
						player1 = false;
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
					} else {
						player = 'X';
						playerImage = R.drawable.player2;
						player1 = true;
						imgAnimTop.post(
								new Runnable(){
								  @Override
								  public void run() {
									  ((AnimationDrawable)imgAnimTop.getDrawable()).start();
								  }
								});
						imgAnimBottom.post(
								new Runnable(){
								  @Override
								  public void run() {
									  ((AnimationDrawable)imgAnimBottom.getDrawable()).stop();
								  }
								});
					}
					if (list.get(position).getImage() == 0) {
						CellState object = new CellState();
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
								.getString(R.string.player1_score), logic
								.getScore(pScore, playerState, 'O'));
						final String data = String.format(getResources()
								.getString(R.string.player2_score), logic
								.getScore(pScore, playerState, 'X'));

						playerScore.setText(text);
						opponentScore.setText(data);
							
					}
					movesPlayed+=1;
					if(movesPlayed==25)
					{
						v = View.inflate(GameScreenActivity.this, R.layout.dialog, null);
						
						 postStatusUpdateButton = (ImageView) v.findViewById(R.id.postStatusUpdateButton);
						 postStatusUpdateButton.setVisibility(View.VISIBLE);
					        postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
					            public void onClick(View view) {
					                onClickPostStatusUpdate();
					            }
					        });
					     
					        
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
							v.setBackgroundResource(R.drawable.evil_victory_panda);
							title.setText("Player 2 Won");
							message.setText("Play Again?");
							alertDialogBuilder.setView(v);
						}
						else{
							v.setBackgroundResource(R.drawable.victory_panda);
							title.setText("Player 1 Won");
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
						player1 = true;
					}
				}
			}
			}
		});
		gridView.setAdapter(adapter);
	}
	
	private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }
	private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();
        String mode = "";
        switch(difficulty){
			case 1:
				mode = "easy";
				break;
			case 2:
				mode = "medium";
				break;
			case 3:
				mode = "hard";
				break;
			case 4:
				mode = "expert";
		}
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("I just won a match in Pandamonium!")
                .setContentDescription(
                		
                        "I defeated the evil pandas in the "+mode+" mode and scored "+newHighScore+".I challenge you to beat it!")
                .setContentUrl(Uri.parse("https://play.google.com/apps/testing/com.enigma.pandamonium"))
                .build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }
	private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }
	private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

	private void performPublish(PendingAction action, boolean allowNoToken) {
			
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null || allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	void copyStuff(List<CellState> dest, List<CellState> source) {
		for (int i = 0; i < source.size(); i++) {
			dest.set(i, new CellState(source.get(i).getScore(), source.get(i)
					.getImage()));
		}
	}

	void init() {
		if(!singleplayer){
			final String data = String.format(getResources()
					.getString(R.string.player2_score), logic
					.getScore(pScore, playerState, 'X'));
			opponentScore.setText(data);
		}
		gameBegan = false;
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
		
		list = new ArrayList<CellState>();
		prevList = new ArrayList<CellState>();
		Resources res = getResources();
		String playertext = String.format(res.getString(R.string.player_score),
				0);
		String opponentText = String.format(
				res.getString(R.string.opponent_score), 0);

		playerScore.setText(playertext);
		opponentScore.setText(opponentText);
		Random rand = new Random();
		for (int i = 0; i < 25; i++) {
			CellState gridData = new CellState();
			gridData.setImage(0);
			gridData.setScore(rand.nextInt(100));
			list.add(gridData);
			prevList.add(gridData);
			pScore[i / 5][i % 5] = gridData.getScore();
		}
		globalCaptFlag = false;
		movesPlayed = 0;
		playerState = new char[][] { { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' },
				{ '*', '*', '*', '*', '*' }, { '*', '*', '*', '*', '*' } };
		playable = true;
		adapter = new GridViewAdapter(this, R.layout.griditem_layout, list);
		gridView.setAdapter(adapter);

	}
	
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    callbackManager.onActivityResult(requestCode, resultCode, data);
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
					if(singleplayer)
					{
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
					}
					else{

						final String data = String.format(
								getResources().getString(
										R.string.player2_score),
								logic.getScore(pScore, playerState,
										'X'));
						final String text = String.format(
								getResources().getString(
										R.string.player1_score),
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
					}
					playable = true;
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		 if(audioFlag)
		 {
			 mediaPlayer1.stop();
			 mediaPlayer2.stop();
		 }
	}
	@Override
	protected void onPause(){
		super.onPause();
		 if(audioFlag)
		 {
			 mediaPlayer1.stop();
			 mediaPlayer2.stop();
		 }
	}
}
