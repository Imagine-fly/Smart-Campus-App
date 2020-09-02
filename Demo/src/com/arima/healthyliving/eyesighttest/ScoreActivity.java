package com.arima.healthyliving.eyesighttest;

import java.io.File;

import com.arima.healthyliving.R;
import com.arima.healthyliving.Util.ScreenShotForShare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends Activity{
	private Button shareBtn = null;
	private Button restartBtn = null;
	private TextView leftScore = null;
	private TextView rightScore = null;
	private TextView titleScore = null;
	private String[] arrayOfString = new String[14];
	private int[] scores = new int[2];
	private PowerManager powerManager = null;
	private WakeLock wakeLock = null;
	private String[] myScore = new String[2]; 
	private String myScores = null;
	//2014/01/24-33378-WendyWan,[AAP1302][Eyesight]Change share style from text format to image format
	private String mShareFileName;
	//2014/01/24-33378-WendyWan
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);       
	    wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        titleScore = (TextView)findViewById(R.id.title_score);
        shareBtn = (Button)findViewById(R.id.button_share);
        restartBtn = (Button)findViewById(R.id.button_retest);
        leftScore = (TextView)findViewById(R.id.left_score);
        rightScore = (TextView)findViewById(R.id.right_score);
        restartBtn.setOnClickListener(mRestartButtonClickListener); 
        shareBtn.setOnClickListener(mShareButtonClickListener); 
        initField();
        
    }

	 private void initField()
	  {
	    this.scores = getIntent().getIntArrayExtra("scores");
	    arrayOfString[0] = getString(R.string.score_level_0_txt);//"小数视力小于：0.1   五分视力小于：4.0";
	    arrayOfString[1] = getString(R.string.score_level_1_txt);//"小数视力为：0.1   五分视力为：4.0";
    	arrayOfString[2] = getString(R.string.score_level_2_txt);//"小数视力为：0.12   五分视力为：4.1";
    	arrayOfString[3] = getString(R.string.score_level_3_txt);//"小数视力为：0.15   五分视力为：4.2";
    	arrayOfString[4] = getString(R.string.score_level_4_txt);//"小数视力为：0.2   五分视力为：4.3";
    	arrayOfString[5] = getString(R.string.score_level_5_txt);// "小数视力为：0.25   五分视力为：4.4";
    	arrayOfString[6] = getString(R.string.score_level_6_txt);//"小数视力为：0.3   五分视力为：4.5";
    	arrayOfString[7] = getString(R.string.score_level_7_txt);//"小数视力为：0.4   五分视力为：4.6";
    	arrayOfString[8] = getString(R.string.score_level_8_txt);//"小数视力为：0.5   五分视力为：4.7";
    	arrayOfString[9] = getString(R.string.score_level_9_txt);//"小数视力为：0.6   五分视力为：4.8";
    	arrayOfString[10] = getString(R.string.score_level_10_txt);//"小数视力为：0.8   五分视力为：4.9";
    	arrayOfString[11] = getString(R.string.score_level_11_txt);//"小数视力为：1.0   五分视力为：5.0";
    	arrayOfString[12] = getString(R.string.score_level_12_txt);//"小数视力为：1.2   五分视力为：5.1";
    	arrayOfString[13] = getString(R.string.score_level_13_txt);//"小数视力为：1.5   五分视力为：5.2";
    	myScore[0] = getString(R.string.left_score_text) + "\n" + arrayOfString[scores[1]];
	    myScore[1] = getString(R.string.right_score_text) + "\n" +arrayOfString[scores[0]];
        //leftScore.setText(getString(R.string.left_score_text) + "\n" + arrayOfString[scores[1]]);
	    //rightScore.setText(getString(R.string.right_score_text) + "\n" +arrayOfString[scores[0]]);
	    myScores = myScore[0] + "\n" + myScore[1];
	    leftScore.setText(myScore[0]);
	    rightScore.setText(myScore[1]);
	    titleScore.setText(getString(R.string.title_score_text));
	    shareBtn.setText(getString(R.string.share_text));
	    restartBtn.setText(getString(R.string.retest_text));
	    //2014/01/24-33378-WendyWan,[AAP1302][Eyesight]Change share style from text format to image format
	    mShareFileName = File.separator + "/EyesightShare.png";
	    //2014/01/24-33378-WendyWan
	  }
	
    private final OnClickListener mRestartButtonClickListener = new OnClickListener() {
        
        public void onClick(View v) {
        	 setResult(-1);
        	 finish();
        }
    };
    
    private final OnClickListener mShareButtonClickListener = new OnClickListener() {
        
        public void onClick(View v) {
        	//2014/01/24-33378-WendyWan,[AAP1302][Eyesight]Change share style from text format to image format
        	Window window = getWindow();
    		int width = getWindowManager().getDefaultDisplay().getWidth();
    		int height = getWindowManager().getDefaultDisplay().getHeight();
    		//2014/01/24-33378-WendyWan
        	Intent intent = new Intent(Intent.ACTION_SEND); 
        	//2014/01/24-33378-WendyWan,[AAP1302][Eyesight]Change share style from text format to image format
        	File file = ScreenShotForShare.GetScreenShotFile(window, width, height, mShareFileName); 
    		if (file != null)
    			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        	
        	//intent.setType("text/plain"); 
    		intent.setType("text/image/png"); 
    		//2014/01/24-33378-WendyWan
        	intent.putExtra(Intent.EXTRA_SUBJECT, "Share My Score");
        	intent.putExtra(Intent.EXTRA_TEXT, myScores);
        	startActivity(Intent.createChooser(intent, getString(R.string.share_score))); 
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();

    }
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wakeLock.release();
	}
	
	//2014/02/21-34133-WendyWan,[AAP1302][Eyesight]Fix bug 316 that it can still test while quit up sharing
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(ScoreActivity.this, StartActivity.class);
		startActivity(intent);
	}
	//2014/02/21-34133-WendyWan
}