package com.arima.healthyliving;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopupActivity extends Activity {
	TextView title;
	TextView message;
	Button back;
	Intent intent;
	
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.healthyliving_popup);
     title = (TextView)findViewById(R.id.title);
     message=(TextView)findViewById(R.id.diagmessage);
     back=(Button)findViewById(R.id.backbutton);
     if(title!=null)
     title.setText(getIntent().getStringExtra("popup_title_string"));
     if(message!=null)
     message.setText(getIntent().getStringExtra("popup_message_string"));
     if(back!=null)
     { 
      if(getIntent().getStringExtra("popup_button_string")!=null)
      back.setText(getIntent().getStringExtra("popup_button_string"));
      back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
             onBackPressed(); 
			}
		});
     }
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
}
