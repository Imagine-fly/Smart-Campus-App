 package com.arima.healthyliving.voice;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 用户名输入页面
 *
 * @author iFlytek &nbsp;&nbsp;&nbsp;<a href="http://http://www.xfyun.cn/">讯飞语音云</a>
 */
public class LoginActivity extends Activity {
	private Button btn_confirm;
	private Button register;
	private EditText edt_uname;
	
	private DBTHelper dbHelper;
	private Toast mToast;
	int flag;
	String y;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		//实例化数据库
		dbHelper=new DBTHelper(getApplicationContext(), "jr.db", null, 2);
		
		register=(Button)findViewById(R.id.register);
		register.setOnClickListener(new RegisterListener());
		
	    btn_confirm=(Button)findViewById(R.id.btn_confirm);
	    btn_confirm.setOnClickListener(new NextListener());
		btn_confirm.setBackgroundColor(Color.GRAY);//指定背景颜色
		
		edt_uname=(EditText)findViewById(R.id.edt_uname);
		edt_uname.addTextChangedListener(new mywatcher(edt_uname));
		
		mToast = Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT);
	}
	
	class NextListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			//实例化数据库
			SQLiteDatabase db=dbHelper.getWritableDatabase();
	        
	        String userphone=edt_uname.getText().toString();
	        
	        if (CheckIsDataAlreadyInDBorNot(userphone)) {
	            Toast.makeText(getApplicationContext(),"该用户名不存在，请先进行账号注册",Toast.LENGTH_SHORT).show();
	            edt_uname.setText("");
	           
	        }
	        else{
	        	String iphone = ((EditText) findViewById(R.id.edt_uname)).getText().toString();
	        	String uname=Check(iphone);
				Intent intent = new Intent(LoginActivity.this, IsvDemo.class);
				intent.putExtra("uname", uname);
				startActivity(intent);

	        }
		}
	}
	
	class RegisterListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(LoginActivity.this,RgContent.class);
			LoginActivity.this.startActivity(intent);
			
			
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
	
	
			
	

	

	
		

	
	//判断用户名是否被注册过
		public boolean CheckIsDataAlreadyInDBorNot(String userphone){
			SQLiteDatabase db=dbHelper.getWritableDatabase();
			String Query = "Select * from userData where phone =?";
			Cursor cursor = db.rawQuery(Query,new String[] { userphone });
			if (cursor.getCount()>0){
				cursor.close();
				return false;
			}
			cursor.close();
			return true;
		}
	
	
	
	
	
	
	
	
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	
	
	class mywatcher implements TextWatcher{
		private EditText EditId=null;
        private String str;
        public mywatcher(EditText id){
        	EditId=id;
        }

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			if(EditId==edt_uname){
				if(TextUtils.isEmpty(edt_uname.getText())){
					btn_confirm.setEnabled(false);
					btn_confirm.setBackgroundColor(Color.parseColor("#AAAAAA"));
	            }
	            else
	            	flag=flag+1;
			}
			if(flag==1){
				btn_confirm.setEnabled(true);
				btn_confirm.setBackgroundColor(Color.parseColor("#FFCC00"));
				
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			if(EditId==edt_uname){
                if(TextUtils.isEmpty(edt_uname.getText())){
                	btn_confirm.setEnabled(false);
                	btn_confirm.setBackgroundColor(Color.parseColor("#AAAAAA"));
                }
                else
                	flag=flag-1;
			}
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
	}
}
	

		
	


	
	

