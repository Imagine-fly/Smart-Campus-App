package com.example.usesqlite;



import com.arima.healthyliving.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public  class MainActivity1 extends Activity implements OnClickListener{

	private DBAdapter1 dbAdapter;
	private EditText addSnoEditText,addSnameEditText,addClassEditText,findBySnoEditText;
	private Button btnAdd,btnClearShowView,btnFindBySno;
	private TextView labelShow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        
		btnAdd = (Button)findViewById(R.id.btnAdd);
		addSnoEditText = (EditText)findViewById(R.id.addSno);
		addSnameEditText = (EditText)findViewById(R.id.addSname);
		addClassEditText = (EditText)findViewById(R.id.addClass);
		labelShow  = (TextView)findViewById(R.id.labelShow);
		btnClearShowView = (Button)findViewById(R.id.btnClearShowView);
		findBySnoEditText = (EditText)findViewById(R.id.findBySnoEditText);
		btnFindBySno = (Button)findViewById(R.id.btnFindBySno);
	    btnAdd.setOnClickListener(this);
		btnClearShowView.setOnClickListener(this);
		btnFindBySno.setOnClickListener(this);
	    dbAdapter = new DBAdapter1(this);
		dbAdapter.open();
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//�������
		case R.id.btnAdd:
		{
			Student s = new Student();
			String sno = addSnoEditText.getText().toString();
			String sname = addSnameEditText.getText().toString();
			String classes = addClassEditText.getText().toString();
			s.setSno(sno);
			s.setSname(sname);
			s.setClasses(classes);
			dbAdapter.insert(s);
			Toast.makeText(this, "��ӳɹ�",Toast.LENGTH_LONG).show();
			
			//�����һ�ε��ı������������
			addSnoEditText.setText("");
			addSnameEditText.setText("");
			addClassEditText.setText("");
			break;
		}
		//�����ʾ��Ļ�ϵ���������
		case R.id.btnClearShowView:
		{
            labelShow.setText("");
			break;
		}
		case R.id.btnFindBySno:
		{
			String sno = findBySnoEditText.getText().toString();
			
			if ("".equals(sno)) {
				
				Toast.makeText(this, "��������Ϊ��", Toast.LENGTH_LONG).show();
				return ;
				
			}
			findBySnoEditText.setText("");
			
			 Student []s =dbAdapter.findStudentsBySno(sno);
			 labelShow.setText("");
			 
			 if(s==null)
				{
					labelShow.setText("���ݿ��в���������");
					Toast.makeText(this, "���ݿ��в���������", Toast.LENGTH_LONG).show();
					return ;
				}
				else  
				{
					String msg="";
					for (int i = 0; i < s.length; i++)
					{
						msg += s[i].toString()+"\n";
					}
					labelShow.setText(msg);
				}
			 	
			break;	
		}
		default:

			break;
		}
	}
}
