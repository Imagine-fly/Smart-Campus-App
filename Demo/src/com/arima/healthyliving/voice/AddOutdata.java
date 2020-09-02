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

	protected static final int DATE_DIALOG_ID = 0;//创建日期对话框常量
	EditText txtoutMoney,txtoutTime,txtoutHandler,txtoutMark;
	Spinner spoutType;
	Button btnoutSaveButton;
	Button btnoutCancelButton;
	private int mYear;
	private int mMonth;
	private int mDay;
	//显示设置的时间
	private void updateDisplay()
	{
		txtoutTime.setText(new               StringBuilder().append(mYear).append("-").append(mMonth    + 1).append("-").append(mDay));
	}
	//覆写onCreateDialog方法
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id)
		{
		case DATE_DIALOG_ID://弹出时间选择对话框
			return new DatePickerDialog(this,mDataSetListener,mYear,mMonth,mDay);
		}
		return null;
	}
	private DatePickerDialog.OnDateSetListener mDataSetListener = new DatePickerDialog.OnDateSetListener() {
		
		
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			mYear = year;//为年份赋值
			mMonth = monthOfYear;//为月份赋值
			mDay = dayOfMonth;//为天数赋值
			updateDisplay();//显示设置时间
		}
	};
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.addoutdata);//设置布局文件为addoutdata.xml
			//获取相应的文本框
			txtoutMoney = (EditText)findViewById(R.id.txtoutMoney);
			txtoutTime = (EditText)findViewById(R.id.txtoutTime);
			txtoutHandler = (EditText)findViewById(R.id.txtoutHandler);
			txtoutMark = (EditText)findViewById(R.id.txtoutMark);
			spoutType = (Spinner)findViewById(R.id.spoutType);
			btnoutSaveButton = (Button)findViewById(R.id.btnoutSave);
			btnoutCancelButton = (Button)findViewById(R.id.btnoutCancel);
			//txtoutTime设置监听机制
			txtoutTime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog(DATE_DIALOG_ID);//显示日期选择对话框
				}
			});
			//获取当前的系统日期
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			updateDisplay();//显示当前系统时间
			//为保存按钮设置监听机制
			btnoutSaveButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String stroutMoney = txtoutMoney.getText().toString();//获取“金额”文本框的值
					if(!stroutMoney.isEmpty()) {
						DB_regulate db_regulate = new DB_regulate(AddOutdata.this);
						My_inaccount my_inaccount = new My_inaccount(db_regulate.getMaxld()+1, Double.parseDouble(stroutMoney),
								txtoutTime.getText().toString(),spoutType.getSelectedItem().toString(),txtoutHandler.getText().toString(),
								txtoutMark.getText().toString());//创建My_inaccount对象
						db_regulate.add(my_inaccount);//添加支出信息
						Toast.makeText(AddOutdata.this, "【新增支出】数据添加成功！", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(AddOutdata.this, "请输入支出金额", Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			//为取消按钮设置监听机制
			btnoutCancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					txtoutMoney.setText("");//设置“金额”文本框为空
					txtoutMoney.setHint("0.00");//为“金额”文本框设置提示
					txtoutTime.setText("");
					txtoutTime.setHint("2018-01-01");
					txtoutHandler.setText("");
					txtoutMark.setText("");
					spoutType.setSelection(0);//设置“类别”下拉列表默认选择第一项
				}
			});
		}
}