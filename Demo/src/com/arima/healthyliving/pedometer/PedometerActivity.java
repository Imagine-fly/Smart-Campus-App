package com.arima.healthyliving.pedometer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.arima.healthyliving.HealthyLivingActivity;
import com.arima.healthyliving.R;
import com.arima.healthyliving.Util.DatabaseManagement;
import com.arima.healthyliving.Util.DatabaseManagement.HealthyLivingPedometerTable;
import com.arima.healthyliving.Util.ScreenShotForShare;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PedometerActivity extends Activity implements OnClickListener{

	private TextView mDateView;
	private TextView mTimeView;
	private TextView mStepCount;
	private TextView mDistance;
	private TextView mSpeed;
	private TextView mCalorie;
	private TextView mSportMode;
	private TextView mRunnedTime;
	
	private Button mResetButton;
	private Button mStartButton;
	private Button mSaveButton;
			
	private EditText mStepWidthSetEditText;
	
	private String [] mPedometerSportMode = new String[2];
	private int mMode = 0;
	private int mScreenOn = 0;
	
	//Handler message type
	public static final int MESSAGE_UPDATE_STEP = 1;
	public static final int MESSAGE_UPDATE_DISTANCE = 2;
	public static final int MESSAGE_UPDATE_SPEED = 3;
	public static final int MESSAGE_UPDATE_CALORIE = 4;
	public static final int MESSAGE_UPDATE_DATE = 5;
	public static final int MESSAGE_UPDATE_CURRENT_TIME = 6;
	public static final int MESSAGE_UPDATE_RUNNED_TIME = 7;
	public static final int MESSAGE_UPDATE_SPORT_MODE = 8;
	private UpdateMainInfo mUpdateMainInfoHandler;
	private String mSharePath;
	private String mShareFileName;
	private DatabaseManagement mDatabaseManagement;
	private WakeLock mWakeLock = null;
	private PowerManager mPowerManager = null;
	
	private PedometerData mPedometerData;
	
	private boolean mHasStart = false;
	
	private final int PEDOMETET_SETTING = 1;
	
	private String mShareInfo = "";

	private String mStartTime = null;
	
	private String TAG = "PedometerActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pedometer_main);
		ActionBar actionbar = getActionBar();
		actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.pedometer_action_bar));
		//actionbar.setDisplayShowHomeEnabled(false);
		initPedometerMode();
		initResource();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
//		if(mPedometerData != null){
//			mPedometerData.onResume();
//		}
		//2013/01/27-33465-HenryPeng
		if(mScreenOn == 1)mWakeLock.acquire();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
		//if(mPedometerData != null)mPedometerData.onPause();
		//>2013/01/27-33465-HenryPeng
		if(mScreenOn == 1)mWakeLock.release();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mPedometerData != null) mPedometerData.onDestory();
		mDatabaseManagement = null;
	}
	
	private void initPedometerMode() {
		// TODO Auto-generated method stub
		mUpdateMainInfoHandler = new UpdateMainInfo();
		mPedometerData = PedometerData.getInstance(PedometerActivity.this, mUpdateMainInfoHandler);
		
		mPedometerSportMode[0] = getResources().getString(R.string.pedometer_sport_mode_walk);
		mPedometerSportMode[1] = getResources().getString(R.string.pedometer_sport_mode_run);
		
		getSettingValues();
	}
	
	private Cursor hasTheSettingInDatabase(){
		if (mDatabaseManagement == null)
			mDatabaseManagement = new DatabaseManagement(this);
		mDatabaseManagement.open();
		Cursor cursor = mDatabaseManagement.query(HealthyLivingPedometerTable.DATABASE_TABLE_PEDOMETER_SETTINGS, 
				null, null, null, null);
		if(cursor != null && cursor.moveToFirst()){
			return cursor;
		}
		else{
			return null;
		}
	}
	
	private void getSettingValues(){
		Cursor cursor = hasTheSettingInDatabase();
		if (cursor != null){
			mMode = cursor.getInt(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_SPORT_MODE));
			if (mPedometerData != null){
				mPedometerData.mStepWidth = cursor.getInt(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_STEP_WIDTH));
				mPedometerData.mUserWeight = cursor.getInt(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_WEIGHT));
				mScreenOn = cursor.getInt(cursor.getColumnIndex(HealthyLivingPedometerTable.KEY_SCREEN_ON));
			}
			mDatabaseManagement.close();
		}else{
			mMode = 0;
			mPedometerData.mStepWidth = 70;
			mPedometerData.mUserWeight = 75;
			mScreenOn = 0;
			writeSettingToDB();
		}
	}
	
	private void writeSettingToDB(){
		if (mDatabaseManagement == null)
			mDatabaseManagement = new DatabaseManagement(this);
		ContentValues settingValues = new ContentValues();
		settingValues.put(HealthyLivingPedometerTable.KEY_SPORT_MODE, mMode);
		if (mPedometerData != null){
			settingValues.put(HealthyLivingPedometerTable.KEY_WEIGHT, mPedometerData.mUserWeight);
			settingValues.put(HealthyLivingPedometerTable.KEY_STEP_WIDTH, mPedometerData.mStepWidth);
		}
		settingValues.put(HealthyLivingPedometerTable.KEY_SCREEN_ON, mScreenOn);
		if (hasTheSettingInDatabase() == null){
			mDatabaseManagement.insert(HealthyLivingPedometerTable.DATABASE_TABLE_PEDOMETER_SETTINGS, settingValues);
		}else{
			mDatabaseManagement.update(HealthyLivingPedometerTable.DATABASE_TABLE_PEDOMETER_SETTINGS, settingValues, null, null);
		}
		mDatabaseManagement.close();
	}
	
	private void setStepValue(String stepCount){
		if (mStepCount != null)mStepCount.setText(stepCount);		
	}
	
	private String getStepValue(){
		String count = null;
		if (mStepCount != null)count = mStepCount.getText().toString();	
		return count;
	}
	
	private void setDistanceValue(String distance){
		if (mDistance != null)mDistance.setText(distance);		
	}
	
	private String getDistanceValue(){
		String distance = null;
		if (mDistance != null)distance = mDistance.getText().toString();	
		return distance;
	}
	
	private void setSpeedValue(String speed){
		if (mSpeed != null)mSpeed.setText(speed);		
	}
	
	private String getSpeedValue(){
		String speed = null;
		if (mSpeed != null)speed = mSpeed.getText().toString();	
		return speed;
	}
	
	private void setCalorieValue(String calorie){
		if (mCalorie != null)mCalorie.setText(calorie);		
	}
	
	private String getCalorieValue(){
		String calorie = null;
		if (mCalorie != null)calorie = mCalorie.getText().toString();	
		return calorie;
	}
	
	private void setRunnedTimeValue(String time){
		if (mRunnedTime != null)
			mRunnedTime.setText(time);
	}
	
	private void setDate(String date){
		if (mDateView != null) 
			mDateView.setText(date);
	}
	
	private String getDate(){
		String date = null;
		if (mDateView != null)date = mDateView.getText().toString();	
		return date;
	}
	
	private void setTime(String time){
		if (mTimeView != null){ 
			mTimeView.setText(time);
			mTimeView.invalidate();
		}
	}
	
	private String getTime(){
		String time = null;
		if (mTimeView != null)time = mTimeView.getText().toString();	
		return time;
	}
	
	private void initResource() {
		// TODO Auto-generated method stub
		mDateView = (TextView)findViewById(R.id.dateInfo);
		mTimeView = (TextView)findViewById(R.id.timeInfo);
		mStepCount = (TextView)findViewById(R.id.stepCount);
		mDistance = (TextView)findViewById(R.id.distance);
		mSpeed = (TextView)findViewById(R.id.speed);
		mCalorie = (TextView)findViewById(R.id.calorie);
		mSportMode = (TextView)findViewById(R.id.pedometer_sport_mode);
		mSportMode.setOnClickListener(this);
		mSportMode.setText(mPedometerSportMode[0]);
		mRunnedTime = (TextView)findViewById(R.id.pedometer_last_time);
		
		mResetButton = (Button)findViewById(R.id.reset_button);
		mResetButton.setOnClickListener(this);
		mStartButton = (Button)findViewById(R.id.start_button);
		mStartButton.setOnClickListener(this);
		mSaveButton = (Button)findViewById(R.id.save_button);
		mSaveButton.setOnClickListener(this);
		
		mShareInfo = getResources().getString(R.string.pedometer_share_info);
		mShareFileName = File.separator + "/PedometerShare.png";
		
		mPowerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);       
	    mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Pedometer Lock");
	    mWakeLock.setReferenceCounted(false);
	    //<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
		if(mPedometerData != null){
			mPedometerData.onCreate();
		}
		//>2013/01/27-33465-HenryPeng
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pedometer_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.menu_about:
				showAboutDialog();
				return true;
			case R.id.menu_settings:
				Intent intent = new Intent(this, PedometerSetting.class);
				intent.putExtra(PedometerSetting.mModeKey, mMode);
				intent.putExtra(PedometerSetting.mWeightKey, mPedometerData.mUserWeight);
				intent.putExtra(PedometerSetting.mWidthKey, mPedometerData.mStepWidth);
				intent.putExtra(PedometerSetting.mScreenOnKey, mScreenOn);
				startActivityForResult(intent, PEDOMETET_SETTING);
				return true;
			case R.id.menu_close:
				PedometerActivity.this.finish();
				return true;
			case R.id.menu_share:
				startShareAction();
				return true;
			case R.id.menu_history:
				checkHistory();
				return true;
        }
		return false;
	}
	
	private void showAboutDialog() {
		// TODO Auto-generated method stub
		String dialogTitle = getResources().getString(R.string.pedometer_about_title);
		String buttonOK = getResources().getString(R.string.pedometer_OK);
		String dialogInfo = getResources().getString(R.string.pedometer_about_info);
		
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle(dialogTitle).setMessage(dialogInfo).setPositiveButton(buttonOK, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		localBuilder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PEDOMETET_SETTING){
			if (data != null){
				if(mPedometerData != null){
					mPedometerData.mStepWidth = data.getIntExtra(PedometerSetting.mWidthKey, 70);
					mPedometerData.mUserWeight = data.getIntExtra(PedometerSetting.mWeightKey, 75);
				}
				mMode = data.getIntExtra(PedometerSetting.mModeKey, 0);
				int screenOn = data.getIntExtra(PedometerSetting.mScreenOnKey, 0);
				if (mScreenOn != screenOn){
					mScreenOn = screenOn;
					if(mScreenOn == 1)mWakeLock.acquire();
					else mWakeLock.release();
				}
				if (mSportMode != null){
					mSportMode.setText(mPedometerSportMode[mMode]);
				}
				writeSettingToDB();
			}
		}
	}

	private void updateSportMode(){
		Message message = new Message();
		message.what = MESSAGE_UPDATE_SPORT_MODE;
		mUpdateMainInfoHandler.sendMessage(message);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int clickID = arg0.getId();
		switch (clickID){
			case R.id.pedometer_sport_mode:
				updateSportMode();
				break;
			case R.id.reset_button:
				enableOrDisableTestMode(false, true);
				break;
			case R.id.save_button:
				doSaveAction();
				break;
			case R.id.start_button:
				if (mHasStart) startOrStopStepMode();
				else showStepWidth();
				break;
		}
	}
	
	 private void doSaveAction() {
		// TODO Auto-generated method stub
		 String startTime = mStartTime;
		 if(startTime == null)startTime = getDate() + ";" + getTime();
		 String endTime = getDate() + ";" + getTime();
		 String stepCount = getStepValue();
		 String distance = getDistanceValue();
		 String speed = getSpeedValue();
		 String calorie = getCalorieValue();
		 
		 ContentValues pedometerValues = new ContentValues();
		 pedometerValues.put(HealthyLivingPedometerTable.KEY_START_TIME, startTime);
		 pedometerValues.put(HealthyLivingPedometerTable.KEY_END_TIME, endTime);
		 pedometerValues.put(HealthyLivingPedometerTable.KEY_STEP_COUNT, stepCount);
		 pedometerValues.put(HealthyLivingPedometerTable.KEY_STEP_DISTANCE, distance);
		 pedometerValues.put(HealthyLivingPedometerTable.KEY_STEP_SPEED, speed);
		 pedometerValues.put(HealthyLivingPedometerTable.KEY_STEP_CALORIE, calorie);
		 
		 if (mDatabaseManagement == null){
			 mDatabaseManagement = new DatabaseManagement(this);
		 }
		 mDatabaseManagement.open();
		 mDatabaseManagement.insert(HealthyLivingPedometerTable.DATABASE_TABLE_PEDOMETER, pedometerValues);
		 mDatabaseManagement.close();
	}

	private void checkHistory() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PedometerActivity.this, PedometerHistory.class);
		startActivity(intent);
	}

	private void startShareAction() {
		// TODO Auto-generated method stub
		Window window = getWindow();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		File file = ScreenShotForShare.GetScreenShotFile(window, width, height, mShareFileName); 
		if (file != null)
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("text/image/png");
		shareIntent.putExtra(Intent.EXTRA_TEXT, mShareInfo);
    	startActivity(Intent.createChooser(shareIntent, getString(R.string.share_text))); 
	}
	
	private void showStepWidth()
	  {
		String widthTitle = getResources().getString(R.string.pedometer_step_width);
		String buttonOK = getResources().getString(R.string.pedometer_OK);
		
		mStepWidthSetEditText = new EditText(this);
		mStepWidthSetEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		InputFilter[] arrayOfInputFilter = new InputFilter[1];
		arrayOfInputFilter[0] = new InputFilter.LengthFilter(3);
		mStepWidthSetEditText.setFilters(arrayOfInputFilter);
		if (mPedometerData != null)
			mStepWidthSetEditText.setText(Integer.toString(mPedometerData.mStepWidth));
		mStepWidthSetEditText.setSelection(mStepWidthSetEditText.getText().length());
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle(widthTitle);
		localBuilder.setView(mStepWidthSetEditText);
		localBuilder.setPositiveButton(buttonOK, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if (mStepWidthSetEditText != null && mPedometerData != null){
					Editable vaule = mStepWidthSetEditText.getText();
					if (vaule != null){
						int width = Integer.parseInt(vaule.toString());
						if (width != mPedometerData.mStepWidth){
							mPedometerData.mStepWidth = width;
							writeSettingToDB();
						}
					}
				}
				startOrStopStepMode();
			}
		});
		localBuilder.setCancelable(false);
		localBuilder.create();
		localBuilder.show();
	  }
	
	private void startOrStopStepMode(){
		if(mHasStart){
			mHasStart = false;
			mStartButton.setText(getResources().getString(R.string.pedometer_start));
		}else{
			mHasStart = true;
			mStartButton.setText(getResources().getString(R.string.pedometer_stop));
		}
		enableOrDisableTestMode(mHasStart, false);
	}
	
	private void enableOrDisableTestMode(boolean enable, boolean isReset){
		if (mPedometerData != null){
			mPedometerData.startOrStopUpdateRunnedTime(enable);
		}
		if (enable){
			mPedometerData.resetAllData();
			mPedometerData.initRunnedTime();
			mStartTime = getDate() + ";" + getTime();
		}
		if(isReset){
			mPedometerData.resetAllData();
			mPedometerData.initRunnedTime();
			mPedometerData.startOrStopUpdateRunnedTime(mHasStart);
			mStartTime = getDate() + ";" + getTime();
		}
	}
	
	private void switchTheSportMode() {
		// TODO Auto-generated method stub
		if (mMode == 0)mMode = 1;
		else if(mMode == 1)mMode = 0;
		if (mSportMode != null){
			mSportMode.setText(mPedometerSportMode[mMode]);
		}
	}

	class UpdateMainInfo extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			if(msg != null){
				switch(msg.what){
					case MESSAGE_UPDATE_STEP:
						String stepCount = "";
						stepCount = String.valueOf(msg.arg1);
						setStepValue(stepCount);
						break;
					case MESSAGE_UPDATE_DISTANCE:
						String distance = "";
						if(msg.obj != null)distance = msg.obj.toString();
						setDistanceValue(distance);
						break;
					case MESSAGE_UPDATE_SPEED:
						String speed = "";
						if(msg.obj != null)speed = msg.obj.toString();
						setSpeedValue(speed);
						break;
					case MESSAGE_UPDATE_CALORIE:
						String calorie = "";
						if(msg.obj != null)calorie = msg.obj.toString();
						setCalorieValue(calorie);
						break;
					case MESSAGE_UPDATE_DATE:
						String date = "";
						if(msg.obj != null) date = msg.obj.toString();
						setDate(date);
						break;
					case MESSAGE_UPDATE_CURRENT_TIME:
						String time = msg.obj.toString();
						setTime(time);
						break;
					case MESSAGE_UPDATE_RUNNED_TIME:
						String runnedTime = msg.obj.toString();
						setRunnedTimeValue(runnedTime);
						break;
					case MESSAGE_UPDATE_SPORT_MODE:
						switchTheSportMode();
						break;
					default:
						break;
				}
			}
		}
	}
}
