package com.arima.healthyliving.voice;

import java.util.Calendar;
import java.util.TimeZone;

import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SureSign extends Activity {
	private TextView txt_authorid;
	private Button sure;
	
	private String userId;
	private DBTHelper dbtHelper;
	private TextView yy;
	
	String year;
	String month;
	String day;
	String hour;
	String minute;
	String second;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suresign);
		
		//实例化数据库
		dbtHelper=new DBTHelper(this, "jr.db", null, 2);
		
		txt_authorid=(TextView)findViewById(R.id.txt_authorid);
		sure=(Button)findViewById(R.id.sure);
		yy=(TextView)findViewById(R.id.yy);
		
		userId = getIntent().getStringExtra("username");
		txt_authorid.setText(userId);
		sure.setOnClickListener(new SureListener());
		
	}
	class SureListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 //实例化数据库
            SQLiteDatabase db=dbtHelper.getWritableDatabase();
            
            Calendar c=Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00")); 
             year = String.valueOf(c.get(Calendar.YEAR));    
             month = String.valueOf(c.get(Calendar.MONTH));
             int n=Integer.parseInt(month)+1;
             month=Integer.toString(n);
             day = String.valueOf(c.get(Calendar.DATE));
            
            if (c.get(Calendar.AM_PM) == 0){
            	
            	String hour = String.valueOf(c.get(Calendar.HOUR));
            }
            else{
            hour = String.valueOf(c.get(Calendar.HOUR)+12);
            }
            minute = String.valueOf(c.get(Calendar.MINUTE));
            second = String.valueOf(c.get(Calendar.SECOND));
            
            String date= year + "-" + month + "-" + day;
            String time= hour + ":" + minute + ":" + second;
            
            yy.setText(time);
            
            String xuehao=txt_authorid.getText().toString();
            String signornot=xuehao;
            
            if(record(signornot,date,time)){
            	 Toast.makeText(getApplicationContext(), "签到成功", Toast.LENGTH_SHORT).show();
            	 Intent intent=new Intent();
            	 intent.putExtra("username", xuehao);
	    		 intent.setClass(SureSign.this,Menu.class);
	    		 SureSign.this.startActivity(intent);
            }
            
			
		}
		
	}
	
	public boolean record(String usersign,String userdate,String usertime){
		SQLiteDatabase db=dbtHelper.getWritableDatabase();//获得数据库
		ContentValues values=new ContentValues();
		values.put("sign", usersign);
		values.put("date", userdate);
		values.put("time", usertime);
		db.insert("userData" ,null,values);
		db.close();
		return true;
		
		
	}
	


}
