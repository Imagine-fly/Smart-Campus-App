package com.arima.healthyliving.eyesighttest;

import com.arima.healthyliving.R;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class AccelerometerSensorListener implements SensorEventListener{
	public static AccelerometerSensorListener mSensorListener;
	private SensorManager mSensorManager;
	private boolean mIsTestMode = false;
	private boolean mTestResult = false;
	private static Context mContext;
	private Toast mToast;
	private String TAG = "AccelerometerSensorListener";
	
	private int mOrientation = -1;/* 0---Up;1---Down;2---Right;3--Left*/
	
	private int mPhoneOrientation = 1;
	private int mPreOrientation = 1;
	private final double mTolerance = 2.5;
	//Vertical
	private final double vertical_X = 0;
    private final double vertical_Y = 9.8;
    private final double vertical_Z = 0;
    private final int STATE_VERTICAL_UP = 1;
    private final int STATE_VERTICAL_DOWN = 2;
    //Horizontal
    private final double horizontal_X = 9.8;
    private final double horizontal_Y = 0;
    private final double horizontal_Z = 0;
    private final int STATE_HORIZONTAL_RIGHT = 3;
    private final int STATE_HORIZONTAL_LEFT = 4;
    //flatwise
    private final double flatwise_X = 0;
    private final double flatwise_Y = 0;
    private final double flatwise_Z = 9.8;
    private final int STATE_FLATWISE = 5;
	
	public static AccelerometerSensorListener getInstance(Context paramContext){
		mContext = paramContext;
		if (mSensorListener == null){
			mSensorListener = new AccelerometerSensorListener();
		}
		return mSensorListener;
	}
	
	private AccelerometerSensorListener(){
		mSensorManager = ((SensorManager)mContext.getSystemService(mContext.SENSOR_SERVICE));
	}
	
	public void registerAccelerometerListener(){
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
        		SensorManager.SENSOR_DELAY_NORMAL);		
	}
	
	public void unregisterAccelerometerListener(){
		mSensorManager.unregisterListener(this);
		mSensorListener = null;
		if (mToast != null) mToast.cancel();
	}
	
	public void startTestMode(int orientation){
		mIsTestMode = true;
		mOrientation = orientation;
	}
	
	public boolean getTheTestResult(boolean isTimeOut){
		if (isTimeOut){
			mIsTestMode = false;
			mPhoneOrientation = mPreOrientation;
		}
		return mTestResult;
	}
	
	public void resetTestResult(boolean value){
		mTestResult = value;
	}
	
	public int getTestOrientation(){
		return mPreOrientation;
	}
	
	public int getPhoneOrientation(){
		return mPhoneOrientation;
	}
	
	private void setTheTestResult(int orientation){		
		Log.d(TAG, "The phone orientation is " + mPhoneOrientation);
		Log.d(TAG, "The phone orientation " + orientation);
		switch(mOrientation){
			//Up
			case 0:
			{
				if ((mPhoneOrientation == STATE_VERTICAL_UP
						|| mPhoneOrientation == STATE_VERTICAL_DOWN
						|| mPhoneOrientation == STATE_HORIZONTAL_LEFT
						|| mPhoneOrientation == STATE_HORIZONTAL_RIGHT)
						&& orientation == STATE_HORIZONTAL_RIGHT)mTestResult = true;				
				else mTestResult = false;
				mPreOrientation = orientation;
				break;
			}
			//Down
			case 1:
			{
				if ((mPhoneOrientation == STATE_VERTICAL_UP
						|| mPhoneOrientation == STATE_VERTICAL_DOWN
						|| mPhoneOrientation == STATE_HORIZONTAL_LEFT
						|| mPhoneOrientation == STATE_HORIZONTAL_RIGHT)
						&& orientation == STATE_HORIZONTAL_LEFT)mTestResult = true;
				else mTestResult = false;
				mPreOrientation = orientation;
				break;
			}
			//Right
			case 2:
			{
				if ((mPhoneOrientation == STATE_VERTICAL_UP
						|| mPhoneOrientation == STATE_VERTICAL_DOWN
						|| mPhoneOrientation == STATE_HORIZONTAL_LEFT
						|| mPhoneOrientation == STATE_HORIZONTAL_RIGHT)
						&& orientation == STATE_VERTICAL_UP)mTestResult = true;
				else mTestResult = false;
				mPreOrientation = orientation;
				break;
			}
			//Left	
			case 3:
			{
				if ((mPhoneOrientation == STATE_VERTICAL_UP
						|| mPhoneOrientation == STATE_VERTICAL_DOWN
						|| mPhoneOrientation == STATE_HORIZONTAL_LEFT
						|| mPhoneOrientation == STATE_HORIZONTAL_RIGHT)
						&& orientation == STATE_VERTICAL_DOWN)mTestResult = true;
				else mTestResult = false;
				mPreOrientation = orientation;
				break;
			}
			default:
				mTestResult = false;
				mPreOrientation = orientation;
				break;
		}
	}
	
		
	private int getThePhoneOrientation(double axisX, double axisY, double axisZ){
		int state = -1;
		if (Math.abs(axisX - vertical_X) <= mTolerance
				&&Math.abs(axisY - vertical_Y) <= mTolerance
				&&Math.abs(axisZ - vertical_Z) <= mTolerance){
			state = STATE_VERTICAL_UP;
		}else if (Math.abs(axisX - vertical_X) <= mTolerance
				&&Math.abs(axisY + vertical_Y) <= mTolerance
				&&Math.abs(axisZ - vertical_Z) <= mTolerance){
			state = STATE_VERTICAL_DOWN;
		}else if (Math.abs(axisX - horizontal_X) <= mTolerance
				&&Math.abs(axisY - horizontal_Y) <= mTolerance
				&&Math.abs(axisZ - horizontal_Z) <= mTolerance){
			state = STATE_HORIZONTAL_LEFT;
		}else if (Math.abs(axisX + horizontal_X) <= mTolerance
				&&Math.abs(axisY - horizontal_Y) <= mTolerance
				&&Math.abs(axisZ - horizontal_Z) <= mTolerance){
			state = STATE_HORIZONTAL_RIGHT;
		}else if (Math.abs(axisX - flatwise_X) <= mTolerance
				&&Math.abs(axisY - flatwise_Y) <= mTolerance
				&&(Math.abs(axisZ - flatwise_Z) <= mTolerance || Math.abs(axisZ + flatwise_Z) <= mTolerance)){
			state = STATE_FLATWISE;
		}
		return state;
	}
	
	
	public void onAccuracyChanged(Sensor event, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensor = event.sensor.getType();
		if (sensor == Sensor.TYPE_ACCELEROMETER){
			double axisX = Math.round(event.values[0]);
            double axisY = Math.round(event.values[1]);
            double axisZ = Math.round(event.values[2]);
            if (mToast != null)mToast.cancel();
			if (mIsTestMode){
				int state = getThePhoneOrientation(axisX, axisY, axisZ);
				if (state == -1) return;
				if (state == STATE_FLATWISE){
					mToast = Toast.makeText(mContext,mContext.getResources().getText(R.string.notification_text), Toast.LENGTH_SHORT);    
					mToast.setDuration(Toast.LENGTH_SHORT);  
					mToast.show();
				}
				setTheTestResult(state);
			}else{ 
				mPhoneOrientation = getThePhoneOrientation(axisX, axisY, axisZ);
				if (mPhoneOrientation == -1) return;
				if (mPhoneOrientation == STATE_FLATWISE){
					mToast = Toast.makeText(mContext,mContext.getResources().getText(R.string.notification_text), Toast.LENGTH_SHORT);
					mToast.show();
				}
			}
		}
	}
}