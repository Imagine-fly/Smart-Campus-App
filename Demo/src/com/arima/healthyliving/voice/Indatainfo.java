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
	public static final String FLAG = "id";//����һ��������������Ϊ������
	ListView lvinfo;//����ListView����
	String strType = "";//�����ַ�������¼��������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indatainfo1);//���ò����ļ�Ϊindatainfo.xml
		lvinfo = (ListView)findViewById(R.id.lvinaccountinfo);//��ȡListView���
		Showlnfo(R.id.action_settings);//��ʾ������Ϣ
	}
	//����һ��ShowInfo����
	private void Showlnfo(int intType) {
		String[] strlnfos = null;//�����ַ������飬��������������Ϣ
		ArrayAdapter<String>arrayAdapter = null;
		strType = "btnininfo";
		DB_regulate indatainfo = new DB_regulate(Indatainfo.this);
		//��ȡ���е�������Ϣ�����洢��List���ͼ�����
		List<My_inaccount>listinfos = indatainfo.getScrollData(0, (int)indatainfo.getCount());
		strlnfos = new String[listinfos.size()];
		int m = 0;
		for(My_inaccount my_inaccount:listinfos) {
			//�����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
			strlnfos[m] = my_inaccount.getid()+"|"+my_inaccount.getType()+" "+String.valueOf(my_inaccount.getMoney())+"Ԫ        "
					+my_inaccount.getTime()+"  "+my_inaccount.getMark()+"  "+my_inaccount.getHandler();
			m++;
		}
		//ʹ���ַ��������ʼ��ArrayAdapter����
		arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strlnfos);
		//ΪListView�б���������Դ
		lvinfo.setAdapter(arrayAdapter);
	}

}
