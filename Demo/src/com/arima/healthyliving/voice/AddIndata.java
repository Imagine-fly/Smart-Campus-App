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
protected static final int DATE_DIALOG_ID = 0;//创建日期对话框常量
EditText txtlnMoney,txtlnTime,txtlnHandler,txtlnMark;
Spinner splnType;
Button btnlnSaveButton;//创建Button对象“保存”
Button btnlnCancelButton;//创建Button对象“取消”
private int mYear;//年
private int mMonth;//月
private int mDay;//日
//定义一个用来显示时间的方法
private void updateDisplay()
{
	txtlnTime.setText(new               StringBuilder().append(mYear).append("-").append(mMonth    + 1).append("-").append(mDay));
}
//覆写onCreateDialog()方法
@Override
protected Dialog onCreateDialog(int id) {
	// TODO Auto-generated method stub
	switch(id)
	{
	//弹出时间选择对话框
	case DATE_DIALOG_ID:
		return new DatePickerDialog(this,mDataSetListener,mYear,mMonth,mDay);
	}
	return null;
}
private DatePickerDialog.OnDateSetListener mDataSetListener = new DatePickerDialog.OnDateSetListener() {
	
	
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		
		mYear = year;//为年份赋值
		mMonth = monthOfYear;//为月份赋值
		mDay = dayOfMonth;//为天数赋值
		updateDisplay();//显示设置的日期
	}
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addindata);//设置布局文件为addindata.xml
		txtlnMoney = (EditText)findViewById(R.id.txtlnMoney);//获取“金额”的文本框
		txtlnTime = (EditText)findViewById(R.id.txtlnTime);//获取“时间”的文本框
		txtlnHandler = (EditText)findViewById(R.id.txtlnHandler);//获取“付款人”的文本框
		txtlnMark = (EditText)findViewById(R.id.txtlnMark);//获取“备注”的文本框
		splnType = (Spinner)findViewById(R.id.splnType);//获取“类别”的文本框
		btnlnSaveButton = (Button)findViewById(R.id.btnlnSave);//获取“保存”的文本框
		btnlnCancelButton = (Button)findViewById(R.id.btnlnCancel);//获取“取消”的文本框
		//为时间按钮设置监听器
		txtlnTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);//显示日期选择对话框
			}
		});
		//获取当前系统日期
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		//为保存按钮设置监听机制
		btnlnSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String strlnMoney = txtlnMoney.getText().toString();//获取“金额”文本框的值
				if(!strlnMoney.isEmpty()) {
					DB_regulate db_regulate = new DB_regulate(AddIndata.this);
					My_inaccount my_inaccount = new My_inaccount(db_regulate.getMaxld()+1, Double.parseDouble(strlnMoney),
							txtlnTime.getText().toString(),splnType.getSelectedItem().toString(),txtlnHandler.getText().toString(),
							txtlnMark.getText().toString());
					db_regulate.add(my_inaccount);//添加收入信息
					Toast.makeText(AddIndata.this, "【新增收入】数据添加成功！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(AddIndata.this, "请输入收入金额", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		//为取消按钮设置监听机制
		btnlnCancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				txtlnMoney.setText("");//设置“金额”文本框为空
				txtlnMoney.setHint("0.00");//为“金额”文本框设置提示
				txtlnTime.setText("");//设置“时间”文本框为空
				txtlnTime.setHint("2018-01-01");//为“时间”文本框设置提示
				txtlnHandler.setText("");//设置“付款人”文本框为空
				txtlnMark.setText("");//设置“类别”下拉列表默认选择的第一项
				splnType.setSelection(0);
			}
		});
	}
	}
	