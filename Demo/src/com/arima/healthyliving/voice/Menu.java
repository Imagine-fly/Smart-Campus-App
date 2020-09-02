 package com.arima.healthyliving.voice;


import com.arima.healthyliving.HealthyLivingActivity;
import com.arima.healthyliving.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Menu extends Activity{
	private Button sign;
	private Button function_button;
	private Button health;
	private Button qiehuan;
	private TextView userId;
	private DBTHelper dbHelper;
	
	
	int y;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		//实例化数据库
		dbHelper=new DBTHelper(getApplicationContext(), "jr.db", null, 2);
		
		sign=(Button)findViewById(R.id.sign);
		
		function_button=(Button)findViewById(R.id.function_button);


		health=(Button)findViewById(R.id.health);
		qiehuan=(Button)findViewById(R.id.qiehuan );
		userId=(TextView)findViewById(R.id.txt_authorid);
		
		// 将上个页面输入的用户名作为AuthId
		String mAuthId = getIntent().getStringExtra("username");
		userId.setText(mAuthId);
		
		sign.setOnClickListener(new SignListener());
		
		function_button.setOnClickListener(new FunctionListener());
		
		qiehuan.setOnClickListener(new ExitListener());
		health.setOnClickListener(new TwoListener());
		
		
		
	}
	class SignListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			 //实例化数据库
			SQLiteDatabase db=dbHelper.getWritableDatabase();
            String mxuehao=userId.getText().toString();
            if( CheckIsStudent(mxuehao)){
            	 
    			String uname=userId.getText().toString();
        		Intent intent=new Intent();
        		intent.putExtra("uname", uname);
        		intent.setClass(Menu.this,Sign.class);
        		Menu.this.startActivity(intent);
            	
            }
            else{
            	Intent intent=new Intent();
            	intent.setClass(Menu.this,SignSearch.class);
        		Menu.this.startActivity(intent);
            	
            }	
		}
		
	}
	
	
public boolean CheckIsStudent(String value){
		
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		Cursor cursor = db.query("userData", new String[] {"zhiwu"}, "xuehao=?", new String[]{value}, null, null, null);
		while (cursor.moveToNext()) {
			y=cursor.getInt(cursor.getColumnIndex("zhiwu"));
			
		}
		if(y==1){
			cursor.close();
			return true;
			
		}
		cursor.close();
		return false;
	  
		
		
		
	}

	class FunctionListener implements OnClickListener{
    @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
    	Intent intent = new Intent();
		
		intent.setClass(Menu.this, FunctionButton.class);
		Menu.this.startActivity(intent);
		}
	}
	class ExitListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Dialog dialog=new AlertDialog.Builder(Menu.this).setTitle("退出账号").setMessage("您确定要切换吗").setIcon(R.drawable.ic_launcher).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
	            	intent.setClass(Menu.this,DengLu.class);
	        		Menu.this.startActivity(intent);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
	
				}
			}).create();
			 dialog.show();//显示对话框
			 
			
		}
    	
    }
    class TwoListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
        	intent.setClass(Menu.this,HealthyLivingActivity.class);
        	Menu.this.startActivity(intent);
    		

	}
    	
    }
    
   


	}
			
			
		
		

    
    



	
	
	


