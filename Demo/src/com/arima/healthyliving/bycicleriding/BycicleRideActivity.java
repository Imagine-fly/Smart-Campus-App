package com.arima.healthyliving.bycicleriding;

import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//<2014/02/21-Task_34127-xiangrongji, [AAP1302][BycicleRiding]upload codes for Bycicle Riding screens
public class BycicleRideActivity extends Activity implements OnClickListener {
	private final static String TAG = "BycicleRidingActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bycrid_activity);
		Button btn = (Button) findViewById(R.id.bycrid_btn_start);
		btn.setOnClickListener(this);
		btn = (Button) findViewById(R.id.bycrid_btn_history);
		btn.setOnClickListener(this);
		btn = (Button) findViewById(R.id.bycrid_btn_back);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bycrid_btn_start:
			{
			Intent intent = new Intent();
			//intent.setClassName(getApplicationContext(), "com.arima.healthyliving.bycicleriding.RidingActivity");
			intent.setClass(getApplicationContext(), RidingActivity.class);
			startActivity(intent);
			break;
			}
		case R.id.bycrid_btn_history:
			{
			Intent intent = new Intent();
			//intent.setClassName(getApplicationContext(), "com.arima.healthyliving.bycicleriding.BycridHistoryActivity");
			intent.setClass(getApplicationContext(), BycridHistoryActivity.class);
			startActivity(intent);
			break;
			}
		case R.id.bycrid_btn_back:
			finish();
			break;
		default:
		}
	}

}
//>2014/02/21-Task_34127-xiangrongji
