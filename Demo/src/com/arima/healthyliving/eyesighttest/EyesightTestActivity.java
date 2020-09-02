package com.arima.healthyliving.eyesighttest;


import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EyesightTestActivity extends Activity {
	private TextView remindText = null;
	private ImageView testImage = null;
	private Button timerBtn = null;
	private int[] imageResource;
	private int ivResource = 0;
	private Bitmap bitmapOrg = null;
	private int widthOrg = 0;
	private int heightOrg = 0;
	private Matrix matrix = null;
	private Bitmap resizedBitmap = null;
	private BitmapDrawable bmd = null;
	private int count = 0;
	private float scaleHeight = 1.0F;
	private float scaleWidth = 1.0F;
	private int witchEye = 0;
	//private String[] scores;
	private int[] scores;
	private final EyesightTestHandler mEyesightTestHandler;
	private AccelerometerSensorListener mGSensorListener;
	private boolean mDestorySelf = false;
	private EyeTestThread mTestThread;
	private int mTestLeftTime = 10;
	//Handler message
	private final int MESSAGE_AUTOSCALE = 1;
	private final int MESSAGE_UPDATE_TIME = 2;
	private final int MESSAGE_START_TEST = 3;
	private final int MESSAGE_TEST_PREPARE = 4;
	private int mTimeCount = -1;
	private PowerManager powerManager = null;
	private WakeLock wakeLock = null;
	private LinearLayout mLoading;
	private String TAG = "EyesightTestActivity";

	public EyesightTestActivity() {
		int[] arrayOfInt = new int[4];
		arrayOfInt[0] = R.drawable.up;
		arrayOfInt[1] = R.drawable.down;
		arrayOfInt[2] = R.drawable.right;
		arrayOfInt[3] = R.drawable.left;
		imageResource = arrayOfInt;
		scores = ApplicationContext.getInstance().getScores();
		mEyesightTestHandler = new EyesightTestHandler();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eyesight_test_activity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		remindText = (TextView)findViewById(R.id.remind_text);
		remindText.setText(getString(R.string.remind_text));
		timerBtn = (Button) findViewById(R.id.button_timer);
		testImage = (ImageView) findViewById(R.id.test_photo);
//		ivResource = ((int) (4.0D * Math.random()));
//		testImage.setImageResource(imageResource[ivResource]);
//		widthOrg = BitmapFactory.decodeResource(getResources(),
//				imageResource[ivResource]).getWidth();
//		heightOrg = BitmapFactory.decodeResource(getResources(),
//				imageResource[ivResource]).getHeight();
		powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);       
	    wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		mLoading = (LinearLayout) this.findViewById(R.id.loading_container);
		
		witchEye = getIntent().getIntExtra("witchEye", 0);
		mGSensorListener = AccelerometerSensorListener.getInstance(EyesightTestActivity.this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mGSensorListener != null)mGSensorListener.unregisterAccelerometerListener();
	}
	
	
	public void scalePic(float paramFloat1, float paramFloat2) {
		ivResource = ((int) (4.0D * Math.random()));
		bitmapOrg = BitmapFactory.decodeResource(getResources(),
				imageResource[ivResource]);
		widthOrg = bitmapOrg.getWidth();
		heightOrg = bitmapOrg.getHeight();
		matrix = new Matrix();
		matrix.postScale(paramFloat1, paramFloat2);
		resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, widthOrg,
				heightOrg, matrix, true);
		bmd = new BitmapDrawable(resizedBitmap);
		testImage.setImageDrawable(bmd);
		testImage.setScaleType(ImageView.ScaleType.CENTER);
	}

	class EyesightTestHandler extends Handler {

		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == MESSAGE_AUTOSCALE) {
				boolean result = mGSensorListener.getTheTestResult(true);
				mGSensorListener.resetTestResult(false);
				//if (!result || count >= 12) {
				if (!result) {
					mDestorySelf = true;
					if (witchEye == 1) {
						Intent scoreIntent = new Intent(
								EyesightTestActivity.this, ScoreActivity.class);
						ApplicationContext.getInstance().setLeftScore(count - 1);
						scoreIntent.putExtra("scores", scores);
						startActivityForResult(scoreIntent, 1002);
						return;
					}

					else if (witchEye == 0) {
						setResult(-1);
						ApplicationContext.getInstance().setRightScore(count - 1);
						finish();
					}
				} else if (count == 13 && result) {

					mDestorySelf = true;
					if (witchEye == 1) {
						Intent scoreIntent = new Intent(
								EyesightTestActivity.this, ScoreActivity.class);
						ApplicationContext.getInstance().setLeftScore(count);
						scoreIntent.putExtra("scores", scores);
						startActivityForResult(scoreIntent, 1002);
						return;
					}

					else if (witchEye == 0) {
						setResult(-1);
						ApplicationContext.getInstance().setRightScore(count);
						finish();
					}

				}
			}else if (paramMessage.what == MESSAGE_UPDATE_TIME){
            	String updateTime = String.valueOf(--mTestLeftTime);
            	timerBtn.setText(updateTime);
			}else if (paramMessage.what == MESSAGE_TEST_PREPARE){
            	//testImage.setImageDrawable(getResources().getDrawable(R.drawable.icon));
				mLoading.setVisibility(View.VISIBLE);
			
			}else if (paramMessage.what == MESSAGE_START_TEST){
				startTest();
			}
		}
	}
	protected void onActivityResult(int paramInt1, int paramInt2,
			Intent paramIntent) {
		if ((paramInt1 == 1002) && (paramInt2 == -1)) {
			setResult(1);
			finish();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mTestThread = new EyeTestThread();
		mTestThread.start();
		if (mGSensorListener!= null) mGSensorListener.registerAccelerometerListener();
		mDestorySelf = false;
		wakeLock.acquire();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mGSensorListener!= null) mGSensorListener.unregisterAccelerometerListener();
		mDestorySelf = true;
		wakeLock.release();
	}
	
	private void startSensorListener(){
		mGSensorListener.startTestMode(ivResource);	
	}
	
	private void startTest() {
		// TODO Auto-generated method stub
		Log.d(TAG, "The random is " + ivResource);
		mTestLeftTime = 10;
		mLoading.setVisibility(View.GONE);
		
		if (count == 0){
			ivResource = ((int) (4.0D * Math.random()));
			testImage.setImageResource(imageResource[ivResource]);
			/*widthOrg = BitmapFactory.decodeResource(getResources(),
					imageResource[ivResource]).getWidth();
			heightOrg = BitmapFactory.decodeResource(getResources(),
					imageResource[ivResource]).getHeight();*/
			scaleWidth *= 0.4F;
			scaleHeight *= 0.4F;
			scalePic(scaleWidth, scaleHeight);
			timerBtn.setText(String.valueOf(mTestLeftTime));
			count++;
		}
		else if ((count <= 12) && count > 0) {
			scaleWidth *= 0.794F;
			scaleHeight *= 0.794F;
			scalePic(scaleWidth, scaleHeight);
			timerBtn.setText(String.valueOf(mTestLeftTime));
			count++;
		}
		startSensorListener();
	} 
	
	public class EyeTestThread extends Thread {
		private long mStartTime = 0;
		private String TAG = "EyeTestThread";
		
		public EyeTestThread(){
			mStartTime = System.currentTimeMillis();
			Log.d(TAG, "Thread EyeTestThread()");;
		}
		
		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			Log.d(TAG, "Thread start()");
			super.start();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(TAG, "Thread run()");
			super.run();
			sendMessageForTestPrepare();
			while(!mDestorySelf){
				try {
					sleep(1* 1000);
					if (mTimeCount == -1){
						sendMessageForStartTest();
					}else if(mTimeCount < 10 && mTimeCount >= 0){
						sendMessageForUpdateTime();
						if (mTimeCount !=0 && mTimeCount%5 == 0 && getResult()){
							if (count < 13){
								sendMessageForStartTest();
							}
							else{
								mDestorySelf = true;
								sendMessageForGetResult();
							}
							mTimeCount = -1;
						}
					}else if (mTimeCount == 10){
						if (count >= 13)mDestorySelf = true;
						//sendMessageForUpdateTime();
						sendMessageForGetResult();
					}else if (mTimeCount == 11){
						sendMessageForStartTest();
						mTimeCount = -1;
					}
					mTimeCount++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public boolean getResult(){
			return mGSensorListener.getTheTestResult(false);
		}
		
		public void sendMessageForStartTest(){
			Message msg = new Message(); 
			msg.what = MESSAGE_START_TEST; 
			mEyesightTestHandler.sendMessage(msg);
		}
		
		public void sendMessageForGetResult(){
			Message msg = new Message(); 
            msg.what = MESSAGE_AUTOSCALE; 
            mEyesightTestHandler.sendMessage(msg); 
		}
		
		public void sendMessageForUpdateTime(){
			Message msg = new Message(); 
            msg.what = MESSAGE_UPDATE_TIME; 
            mEyesightTestHandler.sendMessage(msg); 
		}
		
		public void sendMessageForTestPrepare(){
			try {
				sleep(1* 1000);
				Message msg = new Message(); 
	            msg.what = MESSAGE_TEST_PREPARE; 
	           // mEyesightTestHandler.sendMessage(msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}