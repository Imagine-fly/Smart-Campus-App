package com.arima.healthyliving.colorblindness;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import com.arima.healthyliving.R;
import com.arima.healthyliving.Util.ScreenShotForShare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends Activity {

	private static final String TAG = "TestActivity";
	//<2014/02/12-A123456-RubyJiang, [AAP1302][ColorBlindness]Change test screen style
	//private TextView mTestTime;
	private ImageView mTestTime;
	//>2014/02/12-A123456-RubyJiang
	private MyTimer mTimer;
	private ImageView mImageView;
	private Button mButton1;
	private Button mButton2;
	private Button mButton3;
	
	private Button mNextButton;	
	private Button mShareButton;
	
	private Drawable mButtonDrawable1;
	private Drawable mButtonDrawable2;
	private Drawable mButtonDrawable3;
	
	private ArrayList<Integer> mImagesID;
	private ArrayList<Integer> mRightResult;
	private ArrayList<Integer> mShowImageIndex;
	private int mRandomIndex;
	private int mRightResultCount = 0;
	private boolean mShowResult = false;
	private String mFinalResult;
    private View.OnClickListener mClickListener = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub			
			
			switch (v.getId()) {
			case R.id.result1:
				mFinalResult = (String) mButton1.getText();
				//<2014/02/12-A123456-RubyJiang, [AAP1302][ColorBlindness]Change test screen style
				mButton1.setBackgroundResource(R.drawable.test_button_3);
				mButton2.setBackgroundDrawable(mButtonDrawable2);
				mButton3.setBackgroundDrawable(mButtonDrawable3);
				Log.d(TAG, "button on click, mFinalResult = " + mFinalResult);
				break;
			case R.id.result2:
				mFinalResult = (String) mButton2.getText();
				mButton1.setBackgroundDrawable(mButtonDrawable1);
				mButton2.setBackgroundResource(R.drawable.test_button_3);
				mButton3.setBackgroundDrawable(mButtonDrawable3);
				Log.d(TAG, "button on click, mFinalResult = " + mFinalResult);
				break;
			case R.id.result3:
				mFinalResult = (String) mButton3.getText();
				mButton1.setBackgroundDrawable(mButtonDrawable1);
				mButton2.setBackgroundDrawable(mButtonDrawable2);
				mButton3.setBackgroundResource(R.drawable.test_button_3);
				//>2014/02/12-A123456-RubyJiang
				Log.d(TAG, "button on click, mFinalResult = " + mFinalResult);
				break;
				
			}
		}};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_test);
        //<2014/02/12-A123456-RubyJiang, [AAP1302][ColorBlindness]Change test screen style
        //mTestTime = (TextView) findViewById(R.id.testTime);
        mTestTime = (ImageView) findViewById(R.id.testTime);
        //>2014/02/12-A123456-RubyJiang
        mTimer = new MyTimer(5000, 1000);
        mImageView = (ImageView) findViewById(R.id.testImage);
        mButton1 = (Button)findViewById(R.id.result1);
        mButton2 = (Button)findViewById(R.id.result2);
        mButton3 = (Button)findViewById(R.id.result3);
        mButtonDrawable1 = mButton1.getBackground();
        mButtonDrawable2 = mButton2.getBackground();
        mButtonDrawable3 = mButton3.getBackground();
        
    	mImagesID = new ArrayList<Integer>();
    	mImagesID.add(R.drawable.daltonism_00);
    	mImagesID.add(R.drawable.daltonism_26);
    	mImagesID.add(R.drawable.daltonism_29);
    	mImagesID.add(R.drawable.daltonism_3);
    	mImagesID.add(R.drawable.daltonism_5);
    	mImagesID.add(R.drawable.daltonism_6);
    	mImagesID.add(R.drawable.daltonism_66);
    	mImagesID.add(R.drawable.daltonism_689);
    	mImagesID.add(R.drawable.daltonism_806);
    	mImagesID.add(R.drawable.daltonism_n);
    	
    	mRightResult = new ArrayList<Integer>();
    	mRightResult.add(R.string.unknown);
    	mRightResult.add(R.string.two_six);
    	mRightResult.add(R.string.two_nine);
    	mRightResult.add(R.string.three);
    	mRightResult.add(R.string.five);
    	mRightResult.add(R.string.six);
    	mRightResult.add(R.string.six_six);
    	mRightResult.add(R.string.six_eight_nine);
    	mRightResult.add(R.string.eight_zero_six);
    	mRightResult.add(R.string.cattle);
    	
    	mShowImageIndex = new ArrayList<Integer>();
        loadImage();
        mNextButton = (Button)findViewById(R.id.next);
        mNextButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mButton1.setBackgroundDrawable(mButtonDrawable1);
				mButton2.setBackgroundDrawable(mButtonDrawable2);
				mButton3.setBackgroundDrawable(mButtonDrawable3);

				String rightResult = getString(mRightResult.get(mRandomIndex));
				Log.d(TAG, "next button onClick start, mRandomIndex =" + mRandomIndex + "; rightResult is " + rightResult + ",mFinalResult is " + mFinalResult + "; mRightResultCount =" + mRightResultCount);
				if (mFinalResult == null) {
					mTimer.stop();
					showErrorDialog();
					return;
				} else if (mFinalResult != null && mFinalResult.equals(rightResult)) {
					mFinalResult = null;
					mRightResultCount++;
				} else {
					mFinalResult = null;
				}
				refreshScreen();
				Log.d(TAG, "next button onClick end, mRandomIndex =" + mRandomIndex + "; rightResult is " + rightResult + ",mFinalResult is " + mFinalResult + "; mRightResultCount =" + mRightResultCount);
			}
		});
    }
    
    private void showErrorDialog(){	
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName("com.arima.healthyliving", "com.arima.healthyliving.PopupActivity");
        intent.putExtra("popup_title_string", getString(R.string.error));
        intent.putExtra("popup_message_string", getString(R.string.invalid_answer));
		intent.putExtra("popup_button_string",getString(R.string.label_ok));
        startActivity(intent);
    }
    
    public void refreshScreen(){
		Log.d(TAG, "refreshScreen(), mShowResult =" + mShowResult);
    	if (!mShowResult)
    		loadImage();
    	else
    		//<2014/02/13-A123456-RubyJiang, [AAP1302][ColorBlindness]Change result screen style
    		//showResults();
    	{
            Intent intent = new Intent();
            intent.putExtra("rightAnswer", mRightResultCount);
            intent.putExtra("allImages", mImagesID.size());
            intent.setClass(TestActivity.this, ResultActivity.class);
			startActivity(intent);
    	}
    		//>2014/02/13-A123456-RubyJiang
    }
    
    public void loadImage(){
		Log.d(TAG, "loadImage()");
        mTimer.start();
    	mRandomIndex = nextRandomImageIndex();
    	Drawable drawable = getResources().getDrawable(mImagesID.get(mRandomIndex));
		mImageView.setImageDrawable(drawable);
		
		switch (mRandomIndex) {
		case 0:
			mButton1.setText(R.string.unknown);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.five);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.other);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 1:
			mButton1.setText(R.string.six);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.two);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.two_six);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 2:
			mButton1.setText(R.string.seven_zero);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.two_nine);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.other);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 3:
			mButton1.setText(R.string.unknown);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.other);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.three);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 4:
			mButton1.setText(R.string.other);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.five);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.unknown);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 5:
			mButton1.setText(R.string.five);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.six);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.unknown);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 6:
			mButton1.setText(R.string.unknown);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.six);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.six_six);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 7:
			mButton1.setText(R.string.other);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.six_eight_nine);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.unknown);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 8:
			mButton1.setText(R.string.unknown);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.other);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.eight_zero_six);
			mButton3.setOnClickListener(mClickListener);
			break;
		case 9:
			mButton1.setText(R.string.unknown);
			mButton1.setOnClickListener(mClickListener);
			mButton2.setText(R.string.deer);
			mButton2.setOnClickListener(mClickListener);
			mButton3.setText(R.string.cattle);
			mButton3.setOnClickListener(mClickListener);
			break;
		default:
			break;
		}
    }
    
    private int nextRandomImageIndex() {
	    int allImageCount = mImagesID.size();
	    Random rm = new Random();
	    int index;
	    for (index = rm.nextInt(allImageCount); ;index = rm.nextInt(allImageCount)) {
	    	if (!mShowImageIndex.contains(index)) {
	    		Log.d(TAG, "nextRandomImageIndex(), index =" + index + ",mShowImageIndex = " + mShowImageIndex );
	    		mShowImageIndex.add(index);
	    		if (mShowImageIndex.size() == allImageCount)
	    		    mShowResult = true;
	    		return index;
	    	}
	    }
    }

    private void showResults() {
		Log.d(TAG, "showResults(), mRightResultCount =" + mRightResultCount);
    	setContentView(R.layout.activity_result);
    	TextView correctRate = (TextView)findViewById(R.id.correct_rate);
		final int i = 100 * mRightResultCount/mImagesID.size();
		correctRate.setText(i + "%");
		TextView diagnosisCertificate = (TextView)findViewById(R.id.diagnosis);
		if (i == 100) {
			diagnosisCertificate.setText(R.string.first);
		} else if (i>=90 && i<100) {
			diagnosisCertificate.setText(R.string.second);
		} else if (i>=60 && i<90) {
			diagnosisCertificate.setText(R.string.third);
			correctRate.setTextColor(Color.RED);
		} else {
			diagnosisCertificate.setText(R.string.four);
			correctRate.setTextColor(Color.RED);
		}
		//<2014/01/15-A123456-RubyJiang, Modify theme and layout and add share function
    	mShareButton = (Button)findViewById(R.id.shareButton);
    	mShareButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//<2014/01/24-33383-RubyJiang, [AAP1302][ColorBlindness]Modify share function
				/*String shareContent = "" + i;
				Intent intent=new Intent(Intent.ACTION_SEND); 
				intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
			    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareResult, shareContent) + "%");
			    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(Intent.createChooser(intent, getText(R.string.share))); */
				addShareAction();
				//>2014/01/24-33383-RubyJiang
				
			}
		});
    	//>2014/01/15-A123456-RubyJiang
    }
	//<2014/01/24-33383-RubyJiang, [AAP1302][ColorBlindness]Modify share function
	private void addShareAction() {
		// TODO Auto-generated method stub
		Window window = getWindow();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		String shareContent = "" + 100 * mRightResultCount/mImagesID.size();
		String mShareFileName = File.separator + "/color_blindness_result.png";
		
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		File file = ScreenShotForShare.GetScreenShotFile(window, width, height, mShareFileName); 
		if (file != null)
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("text/image/png");   
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
		shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareResult, shareContent) + "%");
    	startActivity(Intent.createChooser(shareIntent, getString(R.string.share))); 
	}
	//>2014/01/24-33383-RubyJiang

    public void onResume(){
    	super.onResume();
    	if (mTimer.mStartTime > 0)
    		mTimer.resume();
    }
    
    public void onPause(){
    	super.onPause();
    	mTimer.stop();
    }
    
    public class MyTimer extends MyCountDownTimer {

		public MyTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}
		
		public void onFinish (){
			mButton1.setBackgroundDrawable(mButtonDrawable1);
			mButton2.setBackgroundDrawable(mButtonDrawable2);
			mButton3.setBackgroundDrawable(mButtonDrawable3);
			String rightResult = getString(mRightResult.get(mRandomIndex));
			Log.d(TAG, "MyTimer onFinish() start, mRandomIndex =" + mRandomIndex + "; rightResult is " + rightResult + ",mFinalResult is " + mFinalResult + "; mRightResultCount =" + mRightResultCount);
			if (mFinalResult == null) {
				Log.d(TAG, "MyTimer onFinish(), mFinalResult is " + mFinalResult + "; mRightResultCount =" + mRightResultCount);
			} else if (mFinalResult != null && mFinalResult.equals(rightResult)) {
				mFinalResult = null;
				mRightResultCount++;
			} else {
				mFinalResult = null;
			}
			refreshScreen();
			Log.d(TAG, "MyTimer onFinish() end, mRandomIndex =" + mRandomIndex + "; rightResult is " + rightResult + ",mFinalResult is " + mFinalResult + "; mRightResultCount =" + mRightResultCount);
		}
		
		public void onTick(long millisUntilFinished){
			Log.d(TAG,"onTick(), millisUntilFinished/1000 = " + millisUntilFinished/1000);
			//<2014/02/12-A123456-RubyJiang, [AAP1302][ColorBlindness]Change test screen style
			//mTestTime.setText("" + (millisUntilFinished/1000));
			switch ((int)millisUntilFinished/1000) {
			case 4:
				mTestTime.setImageResource(R.drawable.four);
				break;
			case 3:
				mTestTime.setImageResource(R.drawable.three);
				break;
			case 2:
				mTestTime.setImageResource(R.drawable.two);
				break;
			case 1:
				mTestTime.setImageResource(R.drawable.one);
				break;
			case 0:
				mTestTime.setImageResource(R.drawable.zero);
				break;
			}
			//>2014/02/12-A123456-RubyJiang
		}
    	
    }
}
