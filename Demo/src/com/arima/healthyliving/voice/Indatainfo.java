package com.arima.healthyliving.voice;

import java.util.List;

import com.arima.healthyliving.R;
import com.example.data_model.My_inaccount;
import com.example.shujvku.DB_regulate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Indatainfo extends Activity {
	public static final String FLAG = "id";//设置一个常量，用来作为请求码
	ListView lvinfo;//创建ListView对象
	String strType = "";//创建字符串，记录管理类型
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indatainfo1);//设置布局文件为indatainfo.xml
		lvinfo = (ListView)findViewById(R.id.lvinaccountinfo);//获取ListView组件
		Showlnfo(R.id.action_settings);//显示收入信息
	}
	//创建一个ShowInfo方法
	private void Showlnfo(int intType) {
		String[] strlnfos = null;//定义字符串数组，用来储存收入信息
		ArrayAdapter<String>arrayAdapter = null;
		strType = "btnininfo";
		DB_regulate indatainfo = new DB_regulate(Indatainfo.this);
		//获取所有的收入信息，并存储到List泛型集合中
		List<My_inaccount>listinfos = indatainfo.getScrollData(0, (int)indatainfo.getCount());
		strlnfos = new String[listinfos.size()];
		int m = 0;
		for(My_inaccount my_inaccount:listinfos) {
			//将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
			strlnfos[m] = my_inaccount.getid()+"|"+my_inaccount.getType()+" "+String.valueOf(my_inaccount.getMoney())+"元        "
					+my_inaccount.getTime()+"  "+my_inaccount.getMark()+"  "+my_inaccount.getHandler();
			m++;
		}
		//使用字符串数组初始化ArrayAdapter对象
		arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strlnfos);
		//为ListView列表设置数据源
		lvinfo.setAdapter(arrayAdapter);
	}

}
