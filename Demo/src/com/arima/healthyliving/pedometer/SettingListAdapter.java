package com.arima.healthyliving.pedometer;

import java.util.ArrayList;
import java.util.HashMap;

import com.arima.healthyliving.R;
import com.arima.healthyliving.Util.DatabaseManagement;
import com.arima.healthyliving.Util.DatabaseManagement.HealthyLivingPedometerTable;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingListAdapter extends BaseAdapter{
	
	 private LayoutInflater mInflater;
	 private ArrayList<HashMap<String, Object>> mArrayList;
	 private int mResID;
	 private TextView mItemtext;
	 private TextView mItemSubText;
	 private TextView mHistoryStartTime;
	 private TextView mHistoryEndTime;
	 private TextView mHistoryStepCount;
	 private TextView mHistoryDistance;
	 private TextView mHistorySpeed;
	 private TextView mHistoryColorie;
	 private TextView mHistoryDelete;
	private TextView mEmptyView;
	 private FrameLayout mHistoryDelteButton;
	 private int mDatabaseId;
	 private DatabaseManagement mDatabaseManagement;
	 private Context mContext;
	 private LinearLayout.LayoutParams mLayoutparam;

	//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".	 
	public SettingListAdapter(Context context,ArrayList<HashMap<String, Object>> arrayList, int resId, TextView emptyView){
	//>2013/02/26-34259-HenryPeng	
		mInflater = LayoutInflater.from(context);
		mArrayList = arrayList;
		mResID = resId;
		mContext = context;
		//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
		mEmptyView = emptyView;
		//>2013/02/26-34259-HenryPeng
		mLayoutparam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
		mLayoutparam.setMargins(0, 5, 0, 5);
		mLayoutparam.weight = 1;
		mLayoutparam.gravity = Gravity.CENTER;
	}	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
		if (position >=0 && position < mArrayList.size()){
			return mArrayList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HashMap<String, Object> listObject;
				
		listObject = getItem(position);
		 
		if (convertView == null) {
			convertView = mInflater.inflate(mResID, parent, false);
			mItemtext = (TextView)convertView.findViewById(R.id.settingItemName);
			mItemSubText = (TextView)convertView.findViewById(R.id.settingItemSubName);
			mHistoryStartTime = (TextView)convertView.findViewById(R.id.pedometer_history_start_time);
			mHistoryEndTime = (TextView)convertView.findViewById(R.id.pedometer_history_end_time);
			mHistoryStepCount = (TextView)convertView.findViewById(R.id.pedometer_history_step_count);
			mHistoryDistance = (TextView)convertView.findViewById(R.id.pedometer_history_distance);
			mHistorySpeed = (TextView)convertView.findViewById(R.id.pedometer_history_speed);
			mHistoryColorie = (TextView)convertView.findViewById(R.id.pedometer_history_calorie);
			mHistoryDelete = (TextView)convertView.findViewById(R.id.pedometer_history_delete);
			mHistoryDelteButton = (FrameLayout)convertView.findViewById(R.id.pedometer_history_delete_icon);
		    convertView.setTag(position);
		} else {
			mItemtext = (TextView)convertView.findViewById(R.id.settingItemName);
			mItemSubText = (TextView)convertView.findViewById(R.id.settingItemSubName);
			mHistoryStartTime = (TextView)convertView.findViewById(R.id.pedometer_history_start_time);
			mHistoryEndTime = (TextView)convertView.findViewById(R.id.pedometer_history_end_time);
			mHistoryStepCount = (TextView)convertView.findViewById(R.id.pedometer_history_step_count);
			mHistoryDistance = (TextView)convertView.findViewById(R.id.pedometer_history_distance);
			mHistorySpeed = (TextView)convertView.findViewById(R.id.pedometer_history_speed);
			mHistoryColorie = (TextView)convertView.findViewById(R.id.pedometer_history_calorie);
			mHistoryDelete = (TextView)convertView.findViewById(R.id.pedometer_history_delete);
			mHistoryDelteButton = (FrameLayout)convertView.findViewById(R.id.pedometer_history_delete_icon);
			convertView.getTag(position);
		}
		
		if(mHistoryStartTime != null){
			String str = (String)listObject.get(PedometerHistory.ADAPTER_FROM[0]);
			/*//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
			if(position == 0){
				mHistoryStartTime.setLayoutParams(mLayoutparam);
			}
			//>2013/02/26-34259-HenryPeng*/
			mHistoryStartTime.setText(str);
		}
		if(mHistoryEndTime != null){
			String str = (String)listObject.get(PedometerHistory.ADAPTER_FROM[1]);
			/*//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
			if(position == 0){
				mHistoryEndTime.setLayoutParams(mLayoutparam);
			}
			//>2013/02/26-34259-HenryPeng*/
			mHistoryEndTime.setText(str);
		}
		if(mHistoryStepCount != null){
			String str = (String)listObject.get(PedometerHistory.ADAPTER_FROM[2]);
			/*//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
			if(position == 0){
				mHistoryStepCount.setLayoutParams(mLayoutparam);
			}
			//>2013/02/26-34259-HenryPeng*/
			mHistoryStepCount.setText(str);
		}
		if(mHistoryDistance != null){
			String str = (String)listObject.get(PedometerHistory.ADAPTER_FROM[3]);
			/*//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
			if(position == 0){
				mHistoryDistance.setLayoutParams(mLayoutparam);
			}
			//<2013/02/26-34259-HenryPeng*/
			mHistoryDistance.setText(str);
		}
		if(mHistorySpeed != null){
			String str = (String)listObject.get(PedometerHistory.ADAPTER_FROM[4]);
			/*//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
			if(position == 0){
				mHistorySpeed.setLayoutParams(mLayoutparam);
			}
			//<2013/02/26-34259-HenryPeng*/
			mHistorySpeed.setText(str);
		}
		if(mHistoryColorie != null){
			String str = (String)listObject.get(PedometerHistory.ADAPTER_FROM[5]);
			/*//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
			if(position == 0){
				mHistoryColorie.setLayoutParams(mLayoutparam);
			}
			//<2013/02/26-34259-HenryPeng*/
			mHistoryColorie.setText(str);
		}
		//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
		/*if(mHistoryDelete != null && position == 0){
			String str = (String)listObject.get(PedometerHistory.ADAPTER_FROM[6]);
			mHistoryDelete.setLayoutParams(mLayoutparam);
			mHistoryDelete.setText(str);
			mHistoryDelete.setVisibility(View.VISIBLE);
			if(mHistoryDelteButton != null)mHistoryDelteButton.setVisibility(View.GONE);
			Log.d(TAG, "Show delete text");
		}
		if(position != 0 && mHistoryDelteButton != null && mHistoryDelete != null){
		*/
		if(mHistoryDelteButton != null && mHistoryDelete != null){
		//<2013/02/26-34259-HenryPeng	
			mDatabaseId = (Integer)listObject.get(PedometerHistory.ADAPTER_FROM[7]);
			mHistoryDelteButton.setVisibility(View.VISIBLE);
			int[] mDeleteTag = new int[]{mDatabaseId, position};
			mHistoryDelteButton.setTag(mDeleteTag);
			
			mHistoryDelteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					int[] delete = (int[]) view.getTag();
					int databaseId = delete[0];
					int position = delete[1];
					//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
					if(mArrayList != null && position < mArrayList.size()){
						mArrayList.remove(position);
						notifyDataSetChanged();
						removeTheDataFromDatabase(databaseId);
						if (mArrayList.size() == 0 && mEmptyView != null)mEmptyView.setVisibility(View.VISIBLE);
					}
					//>2013/02/26-34259-HenryPeng
				}
			});
			mHistoryDelete.setVisibility(View.GONE);
		}
		if(mItemtext != null){
			String str = (String)listObject.get("name");
			mItemtext.setText(str);
		}
		if(mItemSubText != null){
			mItemSubText.setVisibility(View.VISIBLE);
			String subStr = (String)listObject.get("subName");
			mItemSubText.setText(subStr);
		}
		return convertView;
	}

	protected void removeTheDataFromDatabase(int databaseId) {
		// TODO Auto-generated method stub
		if (mDatabaseManagement == null) mDatabaseManagement = new DatabaseManagement(mContext);
		mDatabaseManagement.open();
		String selection = HealthyLivingPedometerTable.KEY_ID + " = '" + databaseId + "'";
		mDatabaseManagement.delete(HealthyLivingPedometerTable.DATABASE_TABLE_PEDOMETER, selection, null);
		mDatabaseManagement.close();
	}	
}