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
		
		//ʵ�������ݿ�
	    dbHelper=new DBTHelper(this, "jr.db", null, 2);
	    
	   //��ÿؼ�
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
					
			
            //���������
            String password=newkey.getText().toString();
            //ȷ������
            String repassword=rekey.getText().toString();
            //�绰����
            String phone=user.getText().toString();
            
            Len=newkey.getText().toString().length();
          
            if(password!=repassword){
            	Toast.makeText(getApplicationContext(), "��ȷ������", Toast.LENGTH_SHORT).show();
            	rekey.setText("");
            }
            if(Len<6){
            	 Toast.makeText(getApplicationContext(), "���벻��С��6λ��", Toast.LENGTH_SHORT).show();
            	 rekey.setText("");
            	 newkey.setText("");
            	
            }
            else{
            	if(Repassword(phone,password)){
            		 Toast.makeText(getApplicationContext(), "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
            		 Intent intent=new Intent();
            		 intent.setClass(RePassword.this,DengLu.class);
 	    			RePassword.this.startActivity(intent);
            	}
            }
            

	}
           
	
		public boolean Repassword(String userphone,String userpassword){
			SQLiteDatabase db=dbHelper.getWritableDatabase();//������ݿ�
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
