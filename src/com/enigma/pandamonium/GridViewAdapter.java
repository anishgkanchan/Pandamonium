package com.enigma.pandamonium;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private int layoutResourceId;
	Resources res;
	private LayoutInflater mLayoutInflater = null;
	private ArrayList<CellState> mList;

	public GridViewAdapter(Context context, int resourceId,
			List<CellState> list) {

		this.context = context;
		res = ((Activity)context).getResources();
		layoutResourceId = resourceId;
		mList = (ArrayList<CellState>) list;
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
		CellState item = mList.get(position);
		if (position % 2 == 0)
			row.setBackgroundColor(((Activity)context).getResources().getColor(
					R.color.light_brown));
		else
			row.setBackgroundColor(((Activity)context).getResources().getColor(
					R.color.dark_brown));
		holder.imageTitle.setText(item.getScore() + "");
		final AnimationDrawable nicePandaDrawable = (AnimationDrawable)res.getDrawable(R.anim.anim_android);
		final AnimationDrawable evilPandaDrawable = (AnimationDrawable)res.getDrawable(R.anim.anim_android_evil);

		if (item.getImage()==R.drawable.player1){
			((Activity)context).runOnUiThread(new Thread(new Runnable() {
				 public void run() {
					holder.image.setImageDrawable(nicePandaDrawable);
					nicePandaDrawable.start();
					 }
				 }));
			
		}
		else if (item.getImage()==R.drawable.player2){
			((Activity)context).runOnUiThread(new Thread(new Runnable() {
				 public void run() {
					holder.image.setImageDrawable(evilPandaDrawable);
					evilPandaDrawable.start();
					 }
				 }));
		}
		else if(item.getImage()==R.drawable.blink1){
			final AnimationDrawable blink = (AnimationDrawable)res.getDrawable(R.anim.anim_blink);
			((Activity)context).runOnUiThread(new Thread(new Runnable() {
				 public void run() {
					holder.image.setImageDrawable(blink);
					blink.start();
					 }
				 }));
		}
		else {
			holder.image.setImageDrawable(null);
		}
		
		return row;
	}
}
