package com.arima.healthyliving.voice;

import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;

public class RePassword extends Activity{
	private EditText  rekey;
	private EditText  newkey;
	private Button    next;
	private TextView  user;
	private Toast mToast;
	
	private DBTHelper dbHelper;
	private String mAuthId = "";
	int x;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repassword);
		
		//实例化数据库
	    dbHelper=new DBTHelper(this, "jr.db", null, 2);
	    
	   //获得控件
	    newkey=(EditText)findViewById(R.id.newkey);
	    rekey=(EditText)findViewById(R.id.rekey);
	    next=(Button)findViewById(R.id.next);
	    user=(TextView)findViewById(R.id.user);
	    next.setOnClickListener(new NextListener());
	    
	    mAuthId = getIntent().getStringExtra("uname");
		user.setText(mAuthId);
		
	}
	
	class NextListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int Len;
					
			
            //获得新密码
            String password=newkey.getText().toString();
            //确定密码
            String repassword=rekey.getText().toString();
            //电话号码
            String phone=user.getText().toString();
            
            Len=newkey.getText().toString().length();
          
            if(password!=repassword){
            	Toast.makeText(getApplicationContext(), "请确认密码", Toast.LENGTH_SHORT).show();
            	rekey.setText("");
            }
            if(Len<6){
            	 Toast.makeText(getApplicationContext(), "密码不能小于6位数", Toast.LENGTH_SHORT).show();
            	 rekey.setText("");
            	 newkey.setText("");
            	
            }
            else{
            	if(Repassword(phone,password)){
            		 Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
            		 Intent intent=new Intent();
            		 intent.setClass(RePassword.this,DengLu.class);
 	    			RePassword.this.startActivity(intent);
            	}
            }
            

	}
           
	
		public boolean Repassword(String userphone,String userpassword){
			SQLiteDatabase db=dbHelper.getWritableDatabase();//获得数据库
			ContentValues values=new ContentValues();
			values.put("password", userpassword);
			String whereClause = "phone=?";  
			String[] whereArgs = new String[] { String.valueOf(userphone) };  
			db.update("userData", values, whereClause, whereArgs);
			db.close();
			return true;
		
	
		
	}
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

}
}
