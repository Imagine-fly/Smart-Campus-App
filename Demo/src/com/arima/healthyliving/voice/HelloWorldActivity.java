package com.arima.healthyliving.voice;

import com.arima.healthyliving.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.widget.*;

public class HelloWorldActivity extends Activity {
    /** Called when the activity is first created. */
	private Button yuyinButton;
	private Button accountButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        yuyinButton=(Button)findViewById(R.id.yuyinButton);
        accountButton=(Button)findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new AccountListener());
        yuyinButton.setOnClickListener(new YuyinListener());
	}
        
  
    
	
	
    class AccountListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(HelloWorldActivity.this,DengLu.class);
			HelloWorldActivity.this.startActivity(intent);
			
			
		}
    	
    }
    class YuyinListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(HelloWorldActivity.this, LoginActivity.class);
			HelloWorldActivity.this.startActivity(intent);
		}
    	
    }
    
    
    
 
    	
}