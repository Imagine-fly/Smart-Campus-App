package com.arima.healthyliving.colorblindness;

import java.io.File;

import com.arima.healthyliving.R;
import com.arima.healthyliving.Util.ScreenShotForShare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

	private static final String TAG = "ResultActivity";
	private Button mShareButton;
	private Button mBackButton;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    	TextView correctRate = (TextView)findViewById(R.id.correct_rate);
    	final int i = displayCorrectRate();
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
			
    	mShareButton = (Button)findViewById(R.id.shareButton);
    	mShareButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addShareAction();
			}
		});
    	mBackButton = (Button)findViewById(R.id.backButton);
    	mBackButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            intent.setClass(ResultActivity.this, StartActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void addShareAction() {
		// TODO Auto-generated method stub
		Window window = getWindow();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		Log.d(TAG, "addShareAction(), width = " + width + "; height = " + height);
		String shareContent = "" + displayCorrectRate();
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
	
	public int displayCorrectRate() {
		int rightAnswer = getIntent().getIntExtra("rightAnswer", 0);
		int allImages = getIntent().getIntExtra("allImages", 10);
		Log.d(TAG, "displayCorrectRate(), rightAnswer = " + rightAnswer + "; allImages = " + allImages);
		int rightRate = 100 * rightAnswer / allImages;
		return rightRate;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(ResultActivity.this, StartActivity.class);
		startActivity(intent);
	}
}
