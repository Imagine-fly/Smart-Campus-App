package com.arima.healthyliving.pedometer;

import java.text.NumberFormat;
import java.util.Calendar;

import com.arima.healthyliving.R;
import com.arima.healthyliving.pedometer.PedometerActivity.UpdateMainInfo;

import android.content.Context;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class PedometerData implements ShakeListener.OnShakeListener
{
  public int AvgHEARTS = 0;
  public final int FIFTH_HEART = 5;
  public final int FIRST_HEART = 1;
  public final int FORTH_HEART = 4;
  public int HEARTS = 0;
  public final int IntRetErr = -1;
  public final int IntRetOk = 0;
  public static int IntSpeed = 300;
  public int mStepCount = 0;
  public int mInitStepCount = 0;
  public double mStepDistance = 0;//km
  public double mStepCalorie = 0;//c/km/h
  public double mTempCalorie = 0;
  public double mStepSpeed = 0;//km/h
  public double mTempSpeed = 0;
  public int mUserWeight = 70;//kg
  public int mStepWidth = 85;//cm
  public int mTotalRunnedTime = 0;//s
  public final int REGISTER = 6;
  public final int RESET = 7;
  public final int RETURN = 8;
  public final int ROW = 5;
  public int RegisteredDays = 0;
  public final int SECOND_HEART = 2;
  public final int THIRD_HEART = 3;
  
  private final int TIME_BASE = 60; 
  private String mTimeFormat = null;
  private String mDateFormat = null;
  private int mPreviousHour = 0;
  private int mPreviousMin = 0;
  private int mPreviousSec = 0;
  private long mSampleStart = 0;
  private int mPreviousYear = 0;
  private int mPreviousMonth = 0;
  private int mPreviousDay = 0;
  private int mRunnedTimeHour = 0;
  private int mRunnedTimeMin = 0;
  private int mRunnedTimeSec = 0;
  
  private static PedometerData mPedometerData;
  private ShakeListener mShakeListener;
  private UpdateMainInfo mHandler;
  private updateDateAndTimeInfo mUpdatThread;
  private boolean mStopThread = false;
  private boolean mIsRunning = false;
  
  @SuppressWarnings("unused")
private Context mContext;
  
  private String TAG = "PedometerData";

  public static PedometerData getInstance(Context context, UpdateMainInfo handler){
	  if (mPedometerData == null) mPedometerData = new PedometerData(context, handler);
	  return mPedometerData;
  }
  
  public PedometerData(Context context, UpdateMainInfo handler){
	  mHandler = handler;
	  mContext = context;
	  mShakeListener = new ShakeListener(context);
	  mTimeFormat = context.getResources().getString(R.string.timer_format);
	  mDateFormat = context.getResources().getString(R.string.date_format);
  }
  //<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
  public void onCreate(){
	//if(mShakeListener != null)mShakeListener.onCreate();
	//>2013/01/27-33465-HenryPeng
	initDate();
	initTime();
	initRunnedTime();
	mStopThread = false;
	mUpdatThread = new updateDateAndTimeInfo();
	mUpdatThread.start();
  }
  public void onPause(){
//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
	  //if(mShakeListener != null)mShakeListener.onPause();
	  //mStopThread = true;
//>2013/01/27-33465-HenryPeng
  }
  
  public void onDestory(){
//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
	  if(mShakeListener != null)mShakeListener.onDestory();
	  mStopThread = true;
//>2013/01/27-33465-HenryPeng
	  mPedometerData = null;
  }
   
  public void updateRunnedDate() {
	// TODO Auto-generated method stub
	  calcDistance();
	  calcSpeed();
	  calcKcal();
	  updateDistance();
	  updateSpeed();
	  updateCalorie();
  }

  private void calcSpeed(){
	  mTempSpeed = (mStepCount - mInitStepCount) * 18 * ((double)mStepWidth / 1000);
	  if (mTempSpeed != 0)
		  mStepSpeed = cutVaulue(mTempSpeed);
	  mInitStepCount = mStepCount;
  }
  
  private void calcKcal(){
	  float cal = 0;
	  if (mTempSpeed != 0){
		  mTempCalorie += 4.5f * mStepSpeed * ((double)mUserWeight / 1800);
		  if(cutVaulue(mTempCalorie) > 0.0d){
			  mStepCalorie += mTempCalorie;
			  mTempCalorie = 0;
		  }
	  }else{
		  cal = 1 * ((float)mUserWeight / 1800);
		  mStepCalorie += cal;
	  }
	  mStepCalorie = cutVaulue(mStepCalorie);
  }
  
  private void calcDistance(){
	  double distance = mStepCount * ((double)mStepWidth / 100000);
	  distance = cutVaulue(distance);
	  mStepDistance = distance;
  }
  
  private double cutVaulue(double value){
	  NumberFormat format = NumberFormat.getInstance();
	  format.setMaximumFractionDigits(3);
	  String m = format.format(value);
	  return Double.valueOf(m);
  }
  
  public void startOrStopUpdateRunnedTime(boolean run){
	  mIsRunning = run;
	  if(mShakeListener != null){
		  if (mIsRunning){
			  mShakeListener.setOnShakeListener(this);
			  //<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
			  mShakeListener.onCreate();
			  //>2013/01/27-33465-HenryPeng
		  }else{
			  mShakeListener.setOnShakeListener(null);
			  //<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
			  mShakeListener.onDestory();
			  //>2013/01/27-33465-HenryPeng
		  }
		  mShakeListener.setSensorListenerMode(mIsRunning);
	  }
  }
  
  public String getDate()
  {
	  String date = null;
	  Calendar localCalendar = Calendar.getInstance();
	  int year = localCalendar.get(Calendar.YEAR);
	  int month = 1 + localCalendar.get(Calendar.MONTH);
	  int day = localCalendar.get(Calendar.DAY_OF_MONTH);
	  if (mPreviousYear != year || mPreviousMonth != month || mPreviousDay != day){
		  date = String.format(mDateFormat, year, month, day);
		  mPreviousYear = year;
		  mPreviousMonth = month;
		  mPreviousDay = day;
	  }
	  return date;
  }
  
  public String getTime(){
	  String time = null;
	  Calendar localCalendar = Calendar.getInstance();
	  int hour = localCalendar.get(Calendar.HOUR_OF_DAY);
	  int min = localCalendar.get(Calendar.MINUTE);
	  int sec = localCalendar.get(Calendar.SECOND);
	  if (mPreviousHour != hour || mPreviousMin != min || mPreviousSec != sec){
		 time = String.format(mTimeFormat, hour, min, sec);
		 mPreviousHour = hour;
		 mPreviousMin = min;
		 mPreviousSec = sec;
	  }
	  return time;
  }
  
  private String getRunnedTime(){
	String timeString = null;
	int time = getCurrentProgressInSecond();
	int timerFormatHour = time / (TIME_BASE * TIME_BASE);
	int timerFormatMin = time / TIME_BASE;
	int timerFormatSec = time % TIME_BASE;
	if (timerFormatMin != mRunnedTimeMin || timerFormatSec != mRunnedTimeSec || timerFormatHour != mRunnedTimeHour){
	  	timeString = String.format(mTimeFormat, timerFormatHour, timerFormatMin, timerFormatSec);
	  	mRunnedTimeMin = timerFormatMin;
	  	mRunnedTimeSec = timerFormatSec;
	  	mRunnedTimeHour = timerFormatHour;
	}
	return timeString;
  }
  
  private int getCurrentProgressInSecond(){
	return (int) (getCurrentProgressInMillSecond() / 1000L);
  }
  
  private long getCurrentProgressInMillSecond() {
      long current = SystemClock.elapsedRealtime();
      return (long) (current - mSampleStart);
  }
  
  public void initTime(){
	  Calendar localCalendar = Calendar.getInstance();
	  mPreviousHour = localCalendar.get(Calendar.HOUR_OF_DAY);
	  mPreviousMin = localCalendar.get(Calendar.MINUTE);
	  mPreviousSec = localCalendar.get(Calendar.SECOND);
	  String time = String.format(mTimeFormat, mPreviousHour, mPreviousMin, mPreviousSec);
	  updateTime(time);
	 // mStartTime += (";" + time);
  }
  
  public void initRunnedTime(){
 	mSampleStart = SystemClock.elapsedRealtime();
 	mTotalRunnedTime = 0;
 	mRunnedTimeHour = 0;
 	mRunnedTimeMin = 0;
 	mRunnedTimeSec = 0;
 	String startTime = String.format(mTimeFormat, mRunnedTimeHour, mRunnedTimeMin, mRunnedTimeSec);
 	updateRunnedTime(startTime);
  }
  
  public void initDate(){
	  Calendar localCalendar = Calendar.getInstance();
	  int mPreviousYear = localCalendar.get(Calendar.YEAR);
	  int mPreviousMonth = 1 + localCalendar.get(Calendar.MONTH);
	  int mPreviousDay = localCalendar.get(Calendar.DAY_OF_MONTH);
	  String date = String.format(mDateFormat, mPreviousYear, mPreviousMonth, mPreviousDay);
	  updateDate(date);
	  //mStartTime = date;
  }
  
  public void resetAllData(){
	  mStepCount = 0;
	  mStepDistance = 0;
	  mStepCalorie = 0;
	  mStepSpeed = 0;
	  mTotalRunnedTime = 0;
	  mInitStepCount = 0;
	  
	  updateStepCount();
	  updateDistance();
	  updateSpeed();
	  updateCalorie();
  }
  
  public String getDayOfWeek()
  {
    Calendar localCalendar = Calendar.getInstance();
    StringBuffer localStringBuffer = new StringBuffer();
    switch (localCalendar.get(7))
    {
    	case 1:
	        localStringBuffer.append("SUNDAY");
	        return localStringBuffer.toString();
	    case 2:
	        localStringBuffer.append("MONDAY");
	    	return localStringBuffer.toString();
	    case 3:
	        localStringBuffer.append("TUESDAY");
	    	return localStringBuffer.toString();
	    case 4:
	        localStringBuffer.append("WEDNESDAY");
	    	return localStringBuffer.toString();
	    case 5:
	        localStringBuffer.append("THURSDAY");
	    	return localStringBuffer.toString();
	    case 6:
	        localStringBuffer.append("FRIDAY");
	    	return localStringBuffer.toString();
	    case 7:
	    	return localStringBuffer.toString();
	    default:
	        localStringBuffer.append("SATURDAY");
	    	return localStringBuffer.toString();
    }
  }
 private void updateStepCount(){
	  if (mHandler != null){
			 Message msg = new Message();
			 msg.what = PedometerActivity.MESSAGE_UPDATE_STEP;
			 msg.arg1 = mStepCount;
			 mHandler.sendMessage(msg);
	 }
	 Log.d(TAG, "The step count is " + mStepDistance);
  }	
 
 private void updateDistance(){
	  if (mHandler != null){
			 Message msg = new Message();
			 msg.what = PedometerActivity.MESSAGE_UPDATE_DISTANCE;
			 msg.obj = mStepDistance;
			 mHandler.sendMessage(msg);
	 }
	 Log.d(TAG, "The distance is " + mStepDistance);
 }
 
 private void updateSpeed(){
	  if (mHandler != null){
			 Message msg = new Message();
			 msg.what = PedometerActivity.MESSAGE_UPDATE_SPEED;
			 msg.obj = mStepSpeed;
			 mHandler.sendMessage(msg);
	 }
	 Log.d(TAG, "The speed is " + mStepSpeed);
 }
 
 private void updateCalorie(){
	  if (mHandler != null){
			 Message msg = new Message();
			 msg.what = PedometerActivity.MESSAGE_UPDATE_CALORIE;
			 msg.obj = mStepCalorie;
			 mHandler.sendMessage(msg);
	 }
	 Log.d(TAG, "The calorie is " + mStepCalorie);
 }
  
  private void updateTime(String time){
	  if (mHandler != null){
			 Message msg = new Message();
			 msg.what = PedometerActivity.MESSAGE_UPDATE_CURRENT_TIME;
			 msg.obj = time;
			 mHandler.sendMessage(msg);
	 }
  }
  
  private void updateRunnedTime(String runnedTime){
	  if (mHandler != null){
			 Message msg = new Message();
			 msg.what = PedometerActivity.MESSAGE_UPDATE_RUNNED_TIME;
			 msg.obj = runnedTime;
			 mHandler.sendMessage(msg);
	 }
  }
  
  private void updateDate(String date){
	  if (mHandler != null){
			 Message msg = new Message();
			 msg.what = PedometerActivity.MESSAGE_UPDATE_DATE;
			 msg.obj = date;
			 mHandler.sendMessage(msg);
	 }
  }
  
  class updateDateAndTimeInfo extends Thread{
	  public updateDateAndTimeInfo(){
	  }
	  @Override
	  public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(!mStopThread){
			try {
				//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
				sleep(100);
				//>2013/01/27-33465-HenryPeng
				String time = getTime();
				String date = getDate();
				if(time != null) updateTime(time);
				if (mIsRunning){
					String runnedTime = getRunnedTime();
					if(runnedTime != null) updateRunnedTime(runnedTime);
					mTotalRunnedTime ++;
					//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
					if (mTotalRunnedTime != 0 && mTotalRunnedTime % 20 == 0)
						updateRunnedDate();
					mShakeListener.updateStep();
					//>2013/01/27-33465-HenryPeng
				}
				if(date != null) updateDate(date);
			} catch (InterruptedException e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	  }
  }
  	@Override
	public void onShake() {
		// TODO Auto-generated method stub
	  	mStepCount += 1;
	  	updateStepCount();
	}
}