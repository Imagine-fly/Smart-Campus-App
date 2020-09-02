package com.arima.healthyliving.voice;


import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RgContent extends Activity{
	private EditText p_number;
	private EditText k_number;
	private EditText x_number;
	private Spinner yuanxi_item;
	private Spinner nianfen_item;
	private Spinner xueli_item;
	private Button register;
	private Button fanhui;
	private RadioGroup t_s=null;
	private RadioButton teacher=null;
	private RadioButton student=null;
	private DBTHelper dbHelper;
	int flag;
	int x;
	private Toast mToast;
	




	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rg_inf);
		//ʵ�������ݿ�
		dbHelper=new DBTHelper(getApplicationContext(), "jr.db", null, 2);
		//��ÿؼ�����
		p_number=(EditText)findViewById(R.id.p_number);
		k_number=(EditText)findViewById(R.id.k_number);
		x_number=(EditText)findViewById(R.id.x_number);
		yuanxi_item=(Spinner)findViewById(R.id.yuanxi_item);
		nianfen_item=(Spinner)findViewById(R.id.nianfen_item);
		xueli_item=(Spinner)findViewById(R.id.xueli_item);
		register=(Button)findViewById(R.id.register);
		register.setBackgroundColor(Color.parseColor("#AAAAAA"));
		
		fanhui=(Button)findViewById(R.id.fanhui);
		t_s=(RadioGroup)findViewById(R.id.t_s);
		teacher=(RadioButton)findViewById(R.id.teacher);
		student=(RadioButton)findViewById(R.id.student);
		teacher.setChecked(true);
		
		//��ť�����¼�
		fanhui.setOnClickListener(new FanhuiListener());
        register.setOnClickListener(new RegisterListener());
        //EditText����
        p_number.addTextChangedListener(new mywatcher(p_number));
        k_number.addTextChangedListener(new mywatcher(k_number));
        x_number.addTextChangedListener(new mywatcher(x_number));
        
        mToast = Toast.makeText(RgContent.this, "", Toast.LENGTH_SHORT);
        
        //���RadioGroup������
        t_s.setOnCheckedChangeListener(mChangeRadio);
        
		
	}
	
	
	private RadioGroup.OnCheckedChangeListener mChangeRadio =new RadioGroup.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			if(arg1==teacher.getId()){
				x=2;
			}
			else{
				x=1;
			}
			
		}
	};
	
	
	
	//���ص�¼����
	class FanhuiListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(RgContent.this,DengLu.class);
			RgContent.this.startActivity(intent);
			
		}
		
	}

    //ע�ᰴť
	class RegisterListener implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			int Len;
			
			
			
            //ʵ�������ݿ�
            SQLiteDatabase db=dbHelper.getWritableDatabase();
			//�绰
			String phone=p_number.getText().toString();
			//����
			String password=k_number.getText().toString();
			//����
			
			//Ժϵ
			String yuanxi=yuanxi_item.getSelectedItem().toString();
			//��ѧ���
			String nianfen=nianfen_item.getSelectedItem().toString();
			//ѧ��
			String xueli=xueli_item.getSelectedItem().toString();
			//ѧ��
			String xuehao=x_number.getText().toString();
			//ְ��
			int zhiwu=x;
			
			Len=k_number.getText().toString().length();
			
			if (CheckIsDataAlreadyInDBorNot(phone)) {
	            Toast.makeText(getApplicationContext(),"���û����ѱ�ע�ᣬע��ʧ��",Toast.LENGTH_SHORT).show();
	            p_number.setText("");
	            k_number.setText("");
	        }
			if(Len<6){
				showTip("���벻��С��6λ��");
				k_number.setText("");
				return;

			}
	        else {

	            if (register(phone, password,yuanxi,nianfen,xueli,zhiwu,xuehao)) {
	                Toast.makeText(getApplicationContext(), "ע��ɹ�", Toast.LENGTH_SHORT).show();
	                Intent intent=new Intent();
	    			intent.setClass(RgContent.this,DengLu.class);
	    			RgContent.this.startActivity(intent);
	            }
		
			}
			
			

		}

	}


	public boolean register(String userphone,String userpassword,String useryuan,String usernian,String userxue,int userwu,String userxuehao){
		    SQLiteDatabase db=dbHelper.getWritableDatabase();//������ݿ�
			ContentValues values=new ContentValues();
			values.put("phone",userphone);
			values.put("password", userpassword);
			values.put("yuanxi", useryuan);
			values.put("xueli", userxue);
			values.put("nianfen", usernian);
			values.put("zhiwu", userwu);
			values.put("xuehao", userxuehao);
			db.insert("userData",null,values);
			db.close();
			return true;
			
		
	}
	
	//�ж��û����Ƿ�ע���
	public boolean CheckIsDataAlreadyInDBorNot(String value){
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		String Query = "Select * from userData where phone =?";
		Cursor cursor = db.rawQuery(Query,new String[] { value });
		if (cursor.getCount()>0){
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}
	
	//EditText�����¼�
	class mywatcher implements TextWatcher{
		private EditText EditId=null;
        private String str;
        public mywatcher(EditText id){
        EditId=id;
    }

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(EditId==p_number){
	            if(TextUtils.isEmpty(p_number.getText())){
	            	register.setEnabled(false);
	            	register.setBackgroundColor(Color.parseColor("#AAAAAA"));
	            }
	            else
	            	flag=flag+1;
			}
			if(EditId==k_number){
	            if(TextUtils.isEmpty(k_number.getText())){
	            	register.setEnabled(false);
	            	register.setBackgroundColor(Color.parseColor("#AAAAAA"));
	            }
	            else
	            	flag=flag+1;
			}
			
			if(EditId==x_number){
	            if(TextUtils.isEmpty(x_number.getText())){
	            	register.setEnabled(false);
	            	register.setBackgroundColor(Color.parseColor("#AAAAAA"));
	            }
	            else
	            	flag=flag+1;
			}
			
			
			if(flag==3){
				register.setEnabled(true);
				register.setBackgroundColor(Color.parseColor("#FFCC00"));
				
			}
			
			
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			if(EditId==p_number){
                if(TextUtils.isEmpty(p_number.getText())){
                	register.setEnabled(false);
                	register.setBackgroundColor(Color.parseColor("#AAAAAA"));
                }
                else
                	flag=flag-1;
			}
			if(EditId==k_number){
                if(TextUtils.isEmpty(k_number.getText())){
                	register.setEnabled(false);
                	register.setBackgroundColor(Color.parseColor("#AAAAAA"));
                }
                else
                	flag=flag-1;
			}
			
			if(EditId==x_number){
                if(TextUtils.isEmpty(x_number.getText())){
                	register.setEnabled(false);
                	register.setBackgroundColor(Color.parseColor("#AAAAAA"));
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
	
	
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	
	

	

}
