package com.enigma.pandamonium;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	private int layoutResourceId;
	Resources res;
	/*
	 * 1. won easy
	 * 2. x captures easy
	 * 3. x streak easy
	 * 4. x points easy
	 * 5. won medium
	 * 6. x captures medium
	 * 7. x streak medium
	 * 8. x points medium
	 * 9. won hard
	 * 10. x captures hard
	 * 11. x streak hard
	 * 12. x points hard
	 * 13. won expert
	 * 14. x captures expert
	 * 15. x streak expert
	 * 16. x points expert
	 * 17. facebook share
	 * 18. one man standing medium
	 * 19. complete easy with 100 and no enemy capture
	 * 20. game won hard without undo
	 * 21. panda slayer
	 */
	private ArrayList<AchieveState> mList;
	String achievements;
	int achieveLock;
	SharedPreferences sharedPref;
	public ListViewAdapter(Context context, int resourceId, List<AchieveState> list) {
		sharedPref = context.getSharedPreferences("Shared Preference", Context.MODE_PRIVATE);
		achievements =  sharedPref.getString(context.getString(R.string.achievements), "000000000000000000000");
		achieveLock =  sharedPref.getInt(context.getString(R.string.locked_achievements), 4);
		this.context = context;
		res = ((Activity)context).getResources();
		layoutResourceId = resourceId;
		mList = (ArrayList<AchieveState>) list;
	}
	
	class ViewHolder {
		TextView imageTitle;
		TextView status;
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
		Log.i("achieve String",achievements+"");
		View row = convertView;
		ViewHolder temp = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			temp = new ViewHolder();
			temp.imageTitle = (TextView) row.findViewById(R.id.desc);
			temp.status = (TextView) row.findViewById(R.id.status);
			temp.image = (ImageView) row.findViewById(R.id.mission_img);
			row.setTag(temp);
		} else {
			temp = (ViewHolder) row.getTag();
		}
		final ViewHolder holder = temp;
		int pos = 0;
		if(position == 0){
			AchieveState item = mList.get(16);
			if(achievements.charAt(16)=='1'){
				holder.imageTitle.setText(item.getTitle());
				holder.status.setText("Completed");
				holder.status.setTextColor(res.getColor(R.color.light_green));
				holder.status.setVisibility(View.VISIBLE);
				holder.image.setImageDrawable(res.getDrawable(item.getImage()));
			}else{
				holder.imageTitle.setText(item.getTitle());
				holder.status.setVisibility(View.VISIBLE);
				holder.image.setImageDrawable(res.getDrawable(item.getImage()));
			}
		}else{
		pos = position;
		if(position<=16)
			pos=position-1;
		AchieveState item = mList.get(pos);
		Log.i("position",position+"");
		if(position<=achieveLock && achievements.charAt(pos)=='1'){
			holder.imageTitle.setText(item.getTitle());
			holder.status.setText("Completed");
			holder.status.setTextColor(res.getColor(R.color.light_green));
			holder.status.setVisibility(View.VISIBLE);
			holder.image.setImageDrawable(res.getDrawable(item.getImage()));
		}else if(position<=achieveLock && achievements.charAt(pos)=='0'){
			holder.status.setText("Incomplete");
			holder.status.setTextColor(res.getColor(R.color.red));
			holder.imageTitle.setText(item.getTitle());
			holder.status.setVisibility(View.VISIBLE);
			holder.image.setImageDrawable(res.getDrawable(item.getImage()));
		}else{
			holder.imageTitle.setText("Locked");
			holder.status.setVisibility(View.INVISIBLE);
			holder.image.setImageDrawable(res.getDrawable(R.drawable.question));
			
		}
		}
		return row;
	}
}
