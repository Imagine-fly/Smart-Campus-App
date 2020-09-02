package com.arima.healthyliving.pedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.util.MonthDisplayHelper;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ShakeListener
  implements SensorEventListener
{
  public int IntSensor;
  private float mLastX;
  private float mLastY;
  private float mLastZ;
  private OnShakeListener mListener;
  private long mPreTime;
  private SensorManager mSensorManager;
  private int mShakeCount;
  private boolean mStepMode = false;
  private double mDelta = 0.02;
  private double mThreshold = 0.0;
  private double mAcceleration = 0.0;
  private double mTempTotalValue = 0.0;
  private int mSamplingCount = 20;
  private int mTempSamplingCount = 0;
  private double mSampleNew = 0.0;
  private double mSampleOld = 0.0;
  private double mTempStepCount = 0;
  private boolean mIsFirstTime = true;
  private String TAG = "ShakeListener";

  public ShakeListener(Context paramContext)
  {
    mSensorManager = ((SensorManager)paramContext.getSystemService(paramContext.SENSOR_SERVICE));
    File f = new File("mnt/sdcard/result.txt");
    if (f.exists())f.delete();
  }

  public void onAccuracyChanged(Sensor paramSensor, int paramInt)
  {
  }
//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
  public void onDestory()
  {
	  if (mSensorManager != null)mSensorManager.unregisterListener(this);
  }

  public void onCreate()
  {
	  if (mSensorManager != null)
		  mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
	        		SensorManager.SENSOR_DELAY_FASTEST);	
  }
//>2013/01/27-33465-HenryPeng
  public void setSensorListenerMode(boolean stepMode){
	  mStepMode = stepMode;
	  if (mStepMode){
		  mThreshold = 0.0;
		  mAcceleration = 0.0;
		  mTempTotalValue = 0.0;
		  mTempSamplingCount = 0;
		  mSampleNew = 0.0;
		  mSampleOld = 0.0;
		  mTempStepCount = 0;
		  mIsFirstTime = true;
	  }
  }
  
  public void onSensorChanged(SensorEvent paramSensorEvent)
  {
		int sensor = paramSensorEvent.sensor.getType();
		if (sensor != Sensor.TYPE_ACCELEROMETER || !mStepMode){
			return;
		}
				
		long l1 = System.currentTimeMillis();
						
		float f1 = paramSensorEvent.values[0];
		float f2 = paramSensorEvent.values[1];
		float f3 = paramSensorEvent.values[2];
				
		//mAcceleration = Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3) - 9.8d;
		mAcceleration = (f1 + f2 + f3) - 9.8d;
		
		if (mIsFirstTime){
			mPreTime = l1;
			mIsFirstTime = false;
			mSampleOld = mAcceleration;
		}
		
		if (mTempSamplingCount < mSamplingCount){
			mTempTotalValue += Math.abs(mAcceleration);
			mTempSamplingCount ++;
		}
		else{
			mThreshold = mTempTotalValue / mSamplingCount;
			mTempTotalValue = 0;
			mTempSamplingCount = 0;
		}
		//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.	
//		if ((l1 - mPreTime) > 100L && (l1 - mPreTime) < 2000L){
//		Log.d(TAG, "onSensorChanged, the mAcceleration is " + mAcceleration + "the mThreshold is " + mThreshold);	
//		if (Math.abs(mAcceleration) - Math.abs(mThreshold) > 0){
//			mSampleNew = mAcceleration;
//			//String content = String.valueOf(f1) + ", " + String.valueOf(f2) + ", " + String.valueOf(f3);
//			//writeFile("mnt/sdcard/result.txt", content, true, true);
//			if (mSampleNew * mSampleOld < 0 && Math.abs(mSampleNew - mSampleOld) > mDelta){
//				//mTempStepCount += 1;
//				mSampleOld = mSampleNew;
//				//if (mTempStepCount > 1){
//					//mTempStepCount = 0;
//					if (mListener != null)
//						mListener.onShake();
//				//}
//			}
//		}
		mPreTime = l1;
//	}
	//>2013/01/27-33465-HenryPeng
//    long l1;
//    long l2;
//	do
//    {
//      l1 = System.currentTimeMillis();
//      l2 = l1 - mPreTime;
//      Log.d(TAG, "The l1 is " + l1);
//    }while (l2 <= 100L);
//    float f1 = paramSensorEvent.values[0];
//    float f2 = paramSensorEvent.values[1];
//    float f3 = paramSensorEvent.values[2];
//    
//    if (10000.0F * (Math.abs(f3 + (f1 + f2) - mLastX - mLastY - mLastZ) / (float)l2) > PedometerData.IntSpeed)
//    {
//      mShakeCount = (1 + mShakeCount);
//      if (mShakeCount > 1)
//      {
//        mShakeCount = 0;
//        if (mListener != null)
//          mListener.onShake();
//      }
//    }else{
//	    mPreTime = l1;
//		mLastX = f1;
//		mLastY = f2;
//		mLastZ = f3;
//		mShakeCount = 0;
//    }
//    else{
//		 mPreTime = l1;
//		 mLastX = f1;
//		 mLastY = f2;
//		 mLastZ = f3;
//		 mShakeCount = 0;
//    }
  }
//<2013/01/27-33465-HenryPeng,[AAP1302][Bug-280]The pedometer worked abnormal when screen lock/unlock.
  public void updateStep(){
	  if (Math.abs(mAcceleration) - Math.abs(mThreshold) > 0){
		mSampleNew = mAcceleration;
		if (mSampleNew * mSampleOld < 0 && Math.abs(mSampleNew - mSampleOld) > mDelta){
			mSampleOld = mSampleNew;
				if (mListener != null)
					mListener.onShake();
		}
	  }
  }
//>2013/01/27-33465-HenryPeng 
  public static void writeFile(String path, String content, boolean enter, boolean append) {
      try {
          File f = new File(path);
          if(!f.getParentFile().exists()){
           f.getParentFile().mkdirs();
          }
          if (!f.exists()) {
              f.createNewFile();
              f = new File(path);
          }
          FileWriter fw = new FileWriter(f, append);
          if ((content != null) && !"".equals(content)) {
          	if (enter){
					fw.write(content+"\r\n");
          	}
          	else {
          		fw.write(content);
				}
              fw.flush();
          }
          fw.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  
  public void setOnShakeListener(OnShakeListener paramOnShakeListener)
  {
    mListener = paramOnShakeListener;
  }

  public static abstract interface OnShakeListener
  {
    public abstract void onShake();
  }
}