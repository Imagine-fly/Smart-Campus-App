package com.arima.healthyliving.pedometer;

import java.util.ArrayList;
import java.util.HashMap;

import com.arima.healthyliving.R;
import com.arima.healthyliving.Util.DatabaseManagement;
import com.arima.healthyliving.Util.DatabaseManagement.HealthyLivingPedometerTable;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PedometerHistory extends Activity{
	
	private ListView mHistoryDataView = null;
	private ArrayList<HashMap<String, Object>> mDataItems = new ArrayList<HashMap<String, Object>>();
	private String mStartTime;
	private String mEndTime;
	private String mStepCount;
	private String mDistance;
	private String mSpeed;
	private String mCalorie;
	private String mDelete;
	private Context mContext;
	//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
	private TextView mEmptyView;
	//>2013/02/26-34259-HenryPeng
	private DatabaseManagement mDatabaseManagement;
	
	public SettingListAdapter mHistoryAdapter = null;

	static final String[] ADAPTER_FROM = {
        "starttime",
        "endtime",
        "stepcount",
        "distance",
        "speed",
        "calorie",
        "delete",
        "id"
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedometer_history);
        mContext = this;
        initResource();
        prepareHistoryDataList();
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void initResource() {
		// TODO Auto-generated method stub
		if(mHistoryDataView == null)
			mHistoryDataView = (ListView)findViewById(R.id.pedometer_history_list);
		
		mStartTime = getString(R.string.pedometer_history_start_time);
		mEndTime = getString(R.string.pedometer_history_end_time);
		mStepCount = getString(R.string.pedometer_step_count);
		mDistance = getString(R.string.pedometer_history_distance);
		mSpeed = getString(R.string.pedometer_speed_title);
		mCalorie = getString(R.string.pedometer_calorie_title);
		mDelete = getString(R.string.pedometer_history_delete);
		
		mDatabaseManagement = new DatabaseManagement(mContext);
		//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
		mEmptyView = (TextView)findViewById(android.R.id.empty);
		//>2013/02/26-34259-HenryPeng
	}

	private Cursor queryHistoryDataTable(){
		Cursor cursor = null;
		String [] projection = new String[]{HealthyLivingPedometerTable.KEY_ID, HealthyLivingPedometerTable.KEY_START_TIME, HealthyLivingPedometerTable.KEY_END_TIME,
				HealthyLivingPedometerTable.KEY_STEP_COUNT, HealthyLivingPedometerTable.KEY_STEP_DISTANCE,
				HealthyLivingPedometerTable.KEY_STEP_SPEED, HealthyLivingPedometerTable.KEY_STEP_CALORIE};
		if (mDatabaseManagement == null)
			mDatabaseManagement = new DatabaseManagement(mContext);
		mDatabaseManagement.open();
		cursor = mDatabaseManagement.query(HealthyLivingPedometerTable.DATABASE_TABLE_PEDOMETER, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) return cursor;
		else{
			mDatabaseManagement.close();
			return null;
		}
	}
	
	private void prepareHistoryDataList() {
		// TODO Auto-generated method stub
		//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
		/*HashMap<String, Object> historyTitle = new HashMap<String, Object>();
		historyTitle.put(ADAPTER_FROM[0], mStartTime);
		historyTitle.put(ADAPTER_FROM[1], mEndTime);
		historyTitle.put(ADAPTER_FROM[2], mStepCount);
		historyTitle.put(ADAPTER_FROM[3], mDistance);
		historyTitle.put(ADAPTER_FROM[4], mSpeed);
		historyTitle.put(ADAPTER_FROM[5], mCalorie);
		historyTitle.put(ADAPTER_FROM[6], mDelete);
		mDataItems.add(historyTitle);
		*/
		//<2013/02/26-34259-HenryPeng
		Cursor cursor = queryHistoryDataTable();
		if (cursor != null){
			do{
				String startTime = cursor.getString(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_START_TIME));
				String endTime = cursor.getString(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_END_TIME));
				String stepCount = cursor.getString(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_STEP_COUNT));
				String distance = cursor.getString(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_STEP_DISTANCE));
				String speed = cursor.getString(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_STEP_SPEED));
				String calorie = cursor.getString(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_STEP_CALORIE));
				int id = cursor.getInt(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_ID));
				
				int startIndex = startTime.lastIndexOf(";");
				int start = startTime.indexOf("/");
				String startD = startTime.substring(start + 1, startIndex);
				String startT = startTime.substring(startIndex + 1, startTime.length());
				startTime = startD + "\n" + startT;
				
				int endIndex = endTime.lastIndexOf(";");
				int end = endTime.indexOf("/");
				String endD = endTime.substring(end + 1, endIndex);
				String endT = endTime.substring(endIndex + 1, endTime.length());
				endTime = endD + "\n" + endT;
				
				HashMap<String, Object> historyData = new HashMap<String, Object>();
				historyData.put(ADAPTER_FROM[0], startTime);
				historyData.put(ADAPTER_FROM[1], endTime);
				historyData.put(ADAPTER_FROM[2], stepCount);
				historyData.put(ADAPTER_FROM[3], distance);
				historyData.put(ADAPTER_FROM[4], speed);
				historyData.put(ADAPTER_FROM[5], calorie);
				historyData.put(ADAPTER_FROM[7], id);
				mDataItems.add(historyData);
			}while(cursor.moveToNext());
				mDatabaseManagement.close();
		}
		//<2013/02/26-34259-HenryPeng,[AAP1302][Bug-318]Walking exercise-Prompt "Unfortunately, the process com.arima.healthyliving has stopped".
		else{
			if (mEmptyView != null) mEmptyView.setVisibility(View.VISIBLE);
		}
		//>2013/02/26-34259-HenryPeng
		mHistoryAdapter = new SettingListAdapter(this, mDataItems, R.layout.pedometer_history_list, mEmptyView);
		mHistoryDataView.setAdapter(mHistoryAdapter);
	}
}