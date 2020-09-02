package com.arima.healthyliving.voice;

import java.util.Calendar;

import com.arima.healthyliving.R;
import com.example.data_model.My_inaccount;
import com.example.shujvku.DB_regulate;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@TargetApi(24)
@SuppressLint("NewApi")
public class AddIndata extends Activity {
protected static final int DATE_DIALOG_ID = 0;//�������ڶԻ�����
EditText txtlnMoney,txtlnTime,txtlnHandler,txtlnMark;
Spinner splnType;
Button btnlnSaveButton;//����Button���󡰱��桱
Button btnlnCancelButton;//����Button����ȡ����
private int mYear;//��
private int mMonth;//��
private int mDay;//��
//����һ��������ʾʱ��ķ���
private void updateDisplay()
{
	txtlnTime.setText(new               StringBuilder().append(mYear).append("-").append(mMonth    + 1).append("-").append(mDay));
}
//��дonCreateDialog()����
@Override
protected Dialog onCreateDialog(int id) {
	// TODO Auto-generated method stub
	switch(id)
	{
	//����ʱ��ѡ��Ի���
	case DATE_DIALOG_ID:
		return new DatePickerDialog(this,mDataSetListener,mYear,mMonth,mDay);
	}
	return null;
}
private DatePickerDialog.OnDateSetListener mDataSetListener = new DatePickerDialog.OnDateSetListener() {
	
	
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		
		mYear = year;//Ϊ��ݸ�ֵ
		mMonth = monthOfYear;//Ϊ�·ݸ�ֵ
		mDay = dayOfMonth;//Ϊ������ֵ
		updateDisplay();//��ʾ���õ�����
	}
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addindata);//���ò����ļ�Ϊaddindata.xml
		txtlnMoney = (EditText)findViewById(R.id.txtlnMoney);//��ȡ�������ı���
		txtlnTime = (EditText)findViewById(R.id.txtlnTime);//��ȡ��ʱ�䡱���ı���
		txtlnHandler = (EditText)findViewById(R.id.txtlnHandler);//��ȡ�������ˡ����ı���
		txtlnMark = (EditText)findViewById(R.id.txtlnMark);//��ȡ����ע�����ı���
		splnType = (Spinner)findViewById(R.id.splnType);//��ȡ����𡱵��ı���
		btnlnSaveButton = (Button)findViewById(R.id.btnlnSave);//��ȡ�����桱���ı���
		btnlnCancelButton = (Button)findViewById(R.id.btnlnCancel);//��ȡ��ȡ�������ı���
		//Ϊʱ�䰴ť���ü�����
		txtlnTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);//��ʾ����ѡ��Ի���
			}
		});
		//��ȡ��ǰϵͳ����
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		//Ϊ���水ť���ü�������
		btnlnSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String strlnMoney = txtlnMoney.getText().toString();//��ȡ�����ı����ֵ
				if(!strlnMoney.isEmpty()) {
					DB_regulate db_regulate = new DB_regulate(AddIndata.this);
					My_inaccount my_inaccount = new My_inaccount(db_regulate.getMaxld()+1, Double.parseDouble(strlnMoney),
							txtlnTime.getText().toString(),splnType.getSelectedItem().toString(),txtlnHandler.getText().toString(),
							txtlnMark.getText().toString());
					db_regulate.add(my_inaccount);//���������Ϣ
					Toast.makeText(AddIndata.this, "���������롿������ӳɹ���", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(AddIndata.this, "������������", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		//Ϊȡ����ť���ü�������
		btnlnCancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				txtlnMoney.setText("");//���á����ı���Ϊ��
				txtlnMoney.setHint("0.00");//Ϊ�����ı���������ʾ
				txtlnTime.setText("");//���á�ʱ�䡱�ı���Ϊ��
				txtlnTime.setHint("2018-01-01");//Ϊ��ʱ�䡱�ı���������ʾ
				txtlnHandler.setText("");//���á������ˡ��ı���Ϊ��
				txtlnMark.setText("");//���á���������б�Ĭ��ѡ��ĵ�һ��
				splnType.setSelection(0);
			}
		});
	}
	}
	