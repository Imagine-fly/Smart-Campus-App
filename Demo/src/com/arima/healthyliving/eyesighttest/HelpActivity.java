package com.arima.healthyliving.eyesighttest;
import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends Activity{
	private Button backBtn = null;
    private TextView helpTitle = null;
    private TextView methodText = null;
    private TextView timeText = null;
    private TextView remindText = null;
	//private boolean mIsRightEyeTest = true;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        helpTitle = (TextView)findViewById(R.id.help_title);
        helpTitle.setText(getString(R.string.title_help_text));
        backBtn = (Button)findViewById(R.id.button_back);
        backBtn.setText(getString(R.string.back_text));
        backBtn.setOnClickListener(mBackButtonClickListener);
        methodText = (TextView)findViewById(R.id.test_method_text);
        timeText = (TextView)findViewById(R.id.test_time_text);
        remindText = (TextView)findViewById(R.id.remind_text);
        methodText.setText( getString(R.string.title_method_text) + "\n" +  getString(R.string.test_method_text));
        timeText.setText(getString(R.string.title_time_text) + "\n" + getString(R.string.test_time_text));
        remindText.setText(getString(R.string.title_remind_text) + "\n" + getString(R.string.test_remind_text));
        //Intent intent = getIntent();
        //mIsRightEyeTest = intent.getBooleanExtra("witchEye", false);
    }

    private final OnClickListener mBackButtonClickListener = new OnClickListener() {
        
        public void onClick(View v) {
        	/*Intent intent = new Intent();
        	if (mIsRightEyeTest){
        		intent.putExtra("result", false);
        	}
        	else intent.putExtra("result", true);*/
        	setResult(-1);
        	finish();
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