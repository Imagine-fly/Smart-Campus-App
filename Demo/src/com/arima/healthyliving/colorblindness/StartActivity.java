package com.arima.healthyliving.colorblindness;

import com.arima.healthyliving.PopupActivity;
import com.arima.healthyliving.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity {
	
	private static final String TAG = "StartActivity";
	private Button mStartButton;
	private Button mAboutColorBlindnessButton;
	private Button mAboutThisSoftwareButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mAboutColorBlindnessButton = (Button) findViewById(R.id.about_Color_Blindness_Button);
        mAboutColorBlindnessButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "about color blindness button onClick");
				showAboutColorBlindnessDialog();
			}
		});
        mAboutThisSoftwareButton = (Button) findViewById(R.id.about_Software_Button);
        mAboutThisSoftwareButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "about this software button onClick");
				showAboutThisSoftwareDialog();
			}
		});
        mStartButton = (Button) findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Log.d(TAG, "start button onClick");
                Intent intent = new Intent();
                intent.setClass(StartActivity.this, TestActivity.class);
				startActivity(intent);
			}
		});
    }

    private void showAboutColorBlindnessDialog(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName("com.arima.healthyliving", "com.arima.healthyliving.PopupActivity");
        intent.putExtra("popup_title_string", getString(R.string.about_color_blindness));
        intent.putExtra("popup_message_string", getString(R.string.about_color_blindness_msg));
		intent.putExtra("popup_button_string",getString(R.string.label_ok));
        startActivity(intent);
    }
    
    private void showAboutThisSoftwareDialog(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName("com.arima.healthyliving", "com.arima.healthyliving.PopupActivity");
        intent.putExtra("popup_title_string", getString(R.string.about_this_software));
        intent.putExtra("popup_message_string", getString(R.string.about_this_software_msg));
		intent.putExtra("popup_button_string",getString(R.string.label_ok));
        startActivity(intent);
       
    }
    
    public void onResume(){
    	super.onResume();
    }
    
    public void onPause(){
    	super.onPause();
    }
    
}
