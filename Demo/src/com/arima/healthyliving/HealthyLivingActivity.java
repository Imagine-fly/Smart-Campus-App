package com.arima.healthyliving;

import com.arima.healthyliving.bycicleriding.BycicleRideActivity;
import com.arima.healthyliving.heartrate.HeartActivity;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HealthyLivingActivity extends Activity {
	RelativeLayout appC1;
	RelativeLayout appC2;
	RelativeLayout appC3;
	RelativeLayout appC4;
	RelativeLayout appC5;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_living);
        appC1=(RelativeLayout) findViewById(R.id.app1);
        appC2=(RelativeLayout) findViewById(R.id.app2);
        appC3=(RelativeLayout) findViewById(R.id.app3);
        appC4=(RelativeLayout) findViewById(R.id.app4);
        appC5=(RelativeLayout) findViewById(R.id.app5);
        
        appC1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Intent intent=new Intent();
			    intent.setClassName("com.arima.healthyliving", "com.arima.healthyliving.colorblindness.StartActivity");
				startActivity(intent);
			}
		});
        appC2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Intent intent=new Intent();
			    intent.setClassName("com.arima.healthyliving", "com.arima.healthyliving.heartrate.HeartActivity");
				startActivity(intent);
				
			}
		});
        appC3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			    Intent intent=new Intent();
			    intent.setClassName("com.arima.healthyliving", "com.arima.healthyliving.eyesighttest.StartActivity");
				startActivity(intent);
			}
		});
        appC4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//<2014/02/21-Task_34127-xiangrongji, [AAP1302][BycicleRiding]upload codes for Bycicle Riding screens
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), BycicleRideActivity.class);
				startActivity(intent);
				//>2014/02/21-Task_34127-xiangrongji
			}
		});
        appC5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    Intent intent=new Intent();
			    intent.setClassName("com.arima.healthyliving", "com.arima.healthyliving.pedometer.PedometerActivity");
				startActivity(intent);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_healthy_living, menu);
        return true;
    }
}
