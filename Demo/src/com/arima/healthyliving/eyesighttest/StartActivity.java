package com.arima.healthyliving.eyesighttest;

import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
public class StartActivity extends Activity{
	private Button helpBtn = null;
	private Button startBtn = null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        helpBtn = (Button)findViewById(R.id.button_help);
        startBtn = (Button)findViewById(R.id.button_start);
        helpBtn.setText(getString(R.string.help_text));
        startBtn.setText(getString(R.string.start_text));
        helpBtn.setOnClickListener(mHelpButtonClickListener);
        startBtn.setOnClickListener(mStartButtonClickListener);
    }

	
    private final OnClickListener mHelpButtonClickListener = new OnClickListener() {
       
        public void onClick(View v) {
        	Intent helpIntent = new Intent(StartActivity.this, HelpActivity.class);
        	startActivity(helpIntent);
        }
    };
    private final OnClickListener mStartButtonClickListener = new OnClickListener() {
        
        public void onClick(View v) {
        	Intent helpIntent = new Intent(StartActivity.this, ReadyActivity.class);
        	startActivity(helpIntent);
        }
    };
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
	
}