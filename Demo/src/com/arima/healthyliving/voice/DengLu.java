package com.arima.healthyliving.voice;





import java.util.HashMap;

import com.arima.healthyliving.R;
import com.mob.MobApplication;
import com.mob.MobSDK;



import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DengLu extends Activity {
	private EditText userphone;
	private ImageView number;

	private EditText usercode;
	private ImageView password;
	private Button login;
	private Button login_error;
	private Button register;
	private Button yuyin;
	private DBTHelper dbHelper;
	
	int flag;
	int x;
	String zhiwu;
	private String phoneNumber="";

	
	
	private String userPhoneValue,psaawordValue;
	private SharedPreferences login_sp;
	
	private String username="";
	private String y;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.denglu);
		
		//注册AppKey AppSecret
		MobSDK.init(DengLu.this, "239ded9f00214", "6dba31ad329a2010379d0825a4le7b6c");

		
		
		
		
		//实例化数据库
		dbHelper=new DBTHelper(this, "jr.db", null, 2);
		
		//获得控件
		userphone=(EditText)findViewById(R.id.userphone);
		number=(ImageView)findViewById(R.id.number);

		usercode=(EditText)findViewById(R.id.usercode);
		password=(ImageView)findViewById(R.id.password);
		

		
		login=(Button)findViewById(R.id.login);
		login.setBackgroundColor(Color.GRAY);//指定背景颜色
		
		
		login_error=(Button)findViewById(R.id.login_error);
		register=(Button)findViewById(R.id.register);
		yuyin=(Button)findViewById(R.id.yuyin);
	
		
		yuyin.setOnClickListener(new YuYinListener());

		
		
		register.setOnClickListener(new RegisterListener());//注册按钮监听事件
		login.setOnClickListener(new LoginListener());//登录按钮监听事件
		login_error.setOnClickListener(new YanZhengListener());

		userphone.addTextChangedListener(new mywatcher(userphone));
		usercode.addTextChangedListener(new mywatcher(usercode));
		
		
		
		
		
	}

		
		 //获得RadioGroup的内容
		
	//跳转到注册界面
	class RegisterListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(DengLu.this,RgContent.class);
			DengLu.this.startActivity(intent);
			
		}
		
	}
	//登录按钮
	class LoginListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			String phone=userphone.getText().toString();//获得账号
			String key=usercode.getText().toString();//获得密码
			x=login(phone,key);
			if(x==1){
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				String iphone=userphone.getText().toString();//获得账号
				Check(iphone);
				username=Check(iphone);
				Intent intent=new Intent();
				intent.putExtra("username", username);
				intent.setClass(DengLu.this,Menu.class);
				DengLu.this.startActivity(intent);
			}//登录成功
			if(x==2){
				Toast.makeText(getApplicationContext(),"该用户名不存在",Toast.LENGTH_SHORT).show();
				userphone.setText("");
				usercode.setText("");
			}//用户不存在
			if(x==3){
				Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
				usercode.setText("");
			}//密码错误

		}
		
	}
	//登录时判断
	public int login(String mphone,String password){
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	String Query = "Select * from userData where phone =?";
    	Cursor cursor = db.rawQuery(Query,new String[] { mphone });
    	if (cursor.getCount()>0){
    		cursor.close();
    		String sql = "select * from userData where phone=? and password=?";
    		Cursor cu = db.rawQuery(sql, new String[] { mphone, password});
    		if (cu.moveToFirst()) {
                cu.close();
                return 1;
            }
    		else{
    			cu.close();
    			return 3;
    		}
    	}
    	cursor.close();
    	return 2;
    }
	
	//同时满足用户名和密码不为空，登录按钮才有用
	class mywatcher implements TextWatcher{
		private EditText EditId=null;
        private String str;
        public mywatcher(EditText id){
        EditId=id;
    }

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(EditId==userphone){
	            if(TextUtils.isEmpty(userphone.getText())){
	            	login.setEnabled(false);
	            	login.setBackgroundColor(Color.GRAY);
	            }
	            else
	            	flag=flag+1;
			}
			if(EditId==usercode){
	            if(TextUtils.isEmpty(usercode.getText())){
	            	login.setEnabled(false);
	            	login.setBackgroundColor(Color.GRAY);
	            }
	            else
	            	flag=flag+1;
			}
			if(flag==2){
				login.setEnabled(true);
				login.setBackgroundColor(Color.parseColor("#FFCC00"));
				
			}
			
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			if(EditId==userphone){
                if(TextUtils.isEmpty(userphone.getText())){
                	login.setEnabled(false);
                	login.setBackgroundColor(Color.GRAY);
                }
                else
                	flag=flag-1;
			}
			if(EditId==usercode){
                if(TextUtils.isEmpty(usercode.getText())){
                	login.setEnabled(false);
                	login.setBackgroundColor(Color.GRAY);
                }
                else
                	flag=flag-1;
			}
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	}
	

    class YanZhengListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(DengLu.this,YanZheng.class);
			DengLu.this.startActivity(intent);
			
		}

    	
    }
    
    public String Check(String value){
    	SQLiteDatabase db=dbHelper.getWritableDatabase();
    	Cursor cursor = db.query("userData", new String[] {"xuehao"}, "phone=?", new String[]{value}, null, null, null);
    	while (cursor.moveToNext()){
    		y=cursor.getString(cursor.getColumnIndex("xuehao"));
    	}
    	return y;
    }
    
    class YuYinListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(DengLu.this,LoginActivity.class);
			DengLu.this.startActivity(intent);
			
		}
		
	}

                    

	

	

		

	
    
	





}
