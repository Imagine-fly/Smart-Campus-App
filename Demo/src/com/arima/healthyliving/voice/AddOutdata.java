package com.arima.healthyliving.voice;

import java.util.Calendar;

import com.arima.healthyliving.R;
import com.example.data_model.My_inaccount;
import com.example.shujvku.DB_regulate;

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

public class AddOutdata extends Activity{

	protected static final int DATE_DIALOG_ID = 0;//�������ڶԻ�����
	EditText txtoutMoney,txtoutTime,txtoutHandler,txtoutMark;
	Spinner spoutType;
	Button btnoutSaveButton;
	Button btnoutCancelButton;
	private int mYear;
	private int mMonth;
	private int mDay;
	//��ʾ���õ�ʱ��
	private void updateDisplay()
	{
		txtoutTime.setText(new               StringBuilder().append(mYear).append("-").append(mMonth    + 1).append("-").append(mDay));
	}
	//��дonCreateDialog����
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id)
		{
		case DATE_DIALOG_ID://����ʱ��ѡ��Ի���
			return new DatePickerDialog(this,mDataSetListener,mYear,mMonth,mDay);
		}
		return null;
	}
	private DatePickerDialog.OnDateSetListener mDataSetListener = new DatePickerDialog.OnDateSetListener() {
		
		
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			mYear = year;//Ϊ��ݸ�ֵ
			mMonth = monthOfYear;//Ϊ�·ݸ�ֵ
			mDay = dayOfMonth;//Ϊ������ֵ
			updateDisplay();//��ʾ����ʱ��
		}
	};
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.addoutdata);//���ò����ļ�Ϊaddoutdata.xml
			//��ȡ��Ӧ���ı���
			txtoutMoney = (EditText)findViewById(R.id.txtoutMoney);
			txtoutTime = (EditText)findViewById(R.id.txtoutTime);
			txtoutHandler = (EditText)findViewById(R.id.txtoutHandler);
			txtoutMark = (EditText)findViewById(R.id.txtoutMark);
			spoutType = (Spinner)findViewById(R.id.spoutType);
			btnoutSaveButton = (Button)findViewById(R.id.btnoutSave);
			btnoutCancelButton = (Button)findViewById(R.id.btnoutCancel);
			//txtoutTime���ü�������
			txtoutTime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog(DATE_DIALOG_ID);//��ʾ����ѡ��Ի���
				}
			});
			//��ȡ��ǰ��ϵͳ����
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			updateDisplay();//��ʾ��ǰϵͳʱ��
			//Ϊ���水ť���ü�������
			btnoutSaveButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String stroutMoney = txtoutMoney.getText().toString();//��ȡ�����ı����ֵ
					if(!stroutMoney.isEmpty()) {
						DB_regulate db_regulate = new DB_regulate(AddOutdata.this);
						My_inaccount my_inaccount = new My_inaccount(db_regulate.getMaxld()+1, Double.parseDouble(stroutMoney),
								txtoutTime.getText().toString(),spoutType.getSelectedItem().toString(),txtoutHandler.getText().toString(),
								txtoutMark.getText().toString());//����My_inaccount����
						db_regulate.add(my_inaccount);//���֧����Ϣ
						Toast.makeText(AddOutdata.this, "������֧����������ӳɹ���", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(AddOutdata.this, "������֧�����", Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			//Ϊȡ����ť���ü�������
			btnoutCancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					txtoutMoney.setText("");//���á����ı���Ϊ��
					txtoutMoney.setHint("0.00");//Ϊ�����ı���������ʾ
					txtoutTime.setText("");
					txtoutTime.setHint("2018-01-01");
					txtoutHandler.setText("");
					txtoutMark.setText("");
					spoutType.setSelection(0);//���á���������б�Ĭ��ѡ���һ��
				}
			});
		}
}