package com.arima.healthyliving.eyesighttest;


import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadyActivity extends Activity{
	private Button backBtn = null;
	private Button goOnBtn = null;
	private ImageView img_photo = null;
	private TextView titleText = null;
	private boolean on_right_eye = true;
	//private boolean witchEye = false;
	private int witchEye = 0;
	private PowerManager powerManager = null;
	private WakeLock wakeLock = null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);       
	    wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        titleText = (TextView)findViewById(R.id.title_ready);
        backBtn = (Button)findViewById(R.id.button_back);
        goOnBtn = (Button)findViewById(R.id.button_go_on);
        img_photo = (ImageView)findViewById(R.id.img_photo);
        titleText.setText(getString(R.string.title_ready_text));
        backBtn.setText(getString(R.string.back_text));
        backBtn.setOnClickListener(mBackButtonClickListener);
        goOnBtn.setOnClickListener(mGoOnButtonClickListener);
        updateComponets();
    }

	
    private final OnClickListener mBackButtonClickListener = new OnClickListener() {
       
        public void onClick(View v) {
        	finish();
        }
    };
    private final OnClickListener mGoOnButtonClickListener = new OnClickListener() {
        
        public void onClick(View v) {
        	Intent goOnIntent = new Intent(ReadyActivity.this, EyesightTestActivity.class);
        	goOnIntent.putExtra("witchEye", witchEye);
			startActivityForResult(goOnIntent, 1001);
        }
    };
    private void updateComponets()
    {
    	if(on_right_eye){
    		goOnBtn.setText(getString(R.string.test_right_text));
    		img_photo.setImageResource(R.drawable.photo_face_down_right_eye);
    	}else{
    		goOnBtn.setText(getString(R.string.test_left_text));
    		img_photo.setImageResource(R.drawable.photo_face_down_left_eye);
    	}
    }
    
    /*private void startResultActivity()
    {
    		goOnBtn.setText("重新测试");
    		img_photo.setImageResource(R.drawable.photo_face_down_right_eye);
    }
    
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        if (paramInt1 == 1001)
        {
			if (paramInt2 != -1)
				return;
			else{
				boolean isResult = true;
				if (paramIntent != null)
					isResult = paramIntent.getBooleanExtra("result", false);
				if (!isResult)
				{
					on_right_eye = true;
					System.out.println("重新测试");
					startResultActivity();
					witchEye = false;
				}  
				else{
					on_right_eye = false;
					updateComponets();
					witchEye = true;
				}
				
			}
        }
    }*/
    
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
		if (paramInt1 == 1001) {
			if (paramInt2 == -1){
			on_right_eye = false;
			updateComponets();
			witchEye = 1;
			}
			else if(paramInt2 == 1) {
				on_right_eye = true;
				System.out.println("重新测试");
				updateComponets();
				witchEye = 0;
			}
		}
    }

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
	
}