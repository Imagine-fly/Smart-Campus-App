package com.arima.healthyliving.voice;

import java.util.ArrayList;
import java.util.List;

import com.arima.healthyliving.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	GridView gvlnfo;
	//����ϵͳ�����ı�
	String[] titles=new String[] {"����֧��","��������","�ҵ������֧��","�˳�"};
	//������ֵ��ʹ��ϵͳ�Դ���ͼ��
	int[] images = new int[] {R.drawable.appone5,R.drawable.appone6,R.drawable.appone7,R.drawable.appone8};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);//�����ļ�ѡ��main.xml
		gvlnfo = (GridView)findViewById(R.id.gvlnfo);//��ȡmain.xml��gvlngo���
		pictureAdapter adapter = new pictureAdapter(titles,images,this);
		gvlnfo.setAdapter(adapter);
		//Ϊgvlngo���ü�����
		gvlnfo.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = null;
				//ͨ��switch���ʵ��Activity֮�����ת
				switch(arg2) {
				case 0:
					intent = new Intent(MainActivity.this,AddOutdata.class);//��ת��AddOutdata
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(MainActivity.this,AddIndata.class);//��ת��AddIndata
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(MainActivity.this,Indatainfo.class);//��ת��Indatainfo
				startActivity(intent);
					break;
				case 3:
					finish();//�رյ�ǰ�Ĵ���
				}
			}
		});
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
}
//����һ���ڲ��࣬���������ı������ͼƬ���
class ViewHolder
{
	public TextView title;
	public ImageView image;
}
//���ڲ�����������ͼ�꼰˵�����ֵ�ʵ��
class Picture
{
	private String title;//����title�ַ�������ʾͼ����Ŀ
	private int imageld;//����imageld���ͱ�������ʾͼ��Ķ�������
	public Picture()
	{
		super();
	}
	public Picture(String title,int imageld)
	{
		super();
		this.title=title;//Ϊͼ����⸳ֵ
		this.imageld=imageld;//Ϊͼ��Ķ�����ֵ��ֵ
	}
	public String getTitle()//ʹͼ�����ɶ�
	{
		return title;
	}
	public void setTitle(String title)//ʹͼ������д
	{
		this.title = title;
	}
	public int getmageld()//ʹͼ�������ֵ�ɶ�
	{
		return imageld;
	}
	public void setimageld(int imageld)//ʹͼ�������ֵ��д
	{
		this.imageld = imageld;
	}
}
//����pictureAdapter�ಢ�̳�BaseAdapter��
class pictureAdapter extends BaseAdapter
{
	private LayoutInflater inflater;//����LayoutInflater����
	private List<Picture> pictures;
	public pictureAdapter(String[] titles, int[] images, Context context) {
		super();
		pictures = new ArrayList<Picture>();//��ʼ�����ͼ��϶���
		inflater = LayoutInflater.from(context);//��ʼ��LayoutInflater����
		for(int i=0;i<images.length;i++)
		{
			Picture picture = new Picture(titles[i],images[i]);//ʹ�����ͼ������Picture����
			pictures.add(picture);//��Picture������ӵ����ͼ�����
		}
	}
	//��ȡ���ͼ��ϵĳ���
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(null != pictures)
		{
			return pictures.size();
		}
		else
		{
			return 0;
		}
	}
	//��ȡ���ͼ���ָ������������
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pictures.get(arg0);
	}
	//���ط��ͼ��ϵ�����
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	//����ͼ��ĸ�������
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(arg1 == null)
		{
			arg1 = inflater.inflate(R.layout.gvitem,null);//����ͼ��ı�ʶ
			viewHolder = new ViewHolder();//��ʼ��ViewHolder����
			viewHolder.title = (TextView)arg1.findViewById(R.id.ItemTitle);//����ͼ�����
			viewHolder.image = (ImageView)arg1.findViewById(R.id.Itemlmage);//����ͼ��Ķ�����ֵ
			arg1.setTag(viewHolder);//������ʾ
		}
		else {
			viewHolder = (ViewHolder)arg1.getTag();
		}
		viewHolder.title.setText(pictures.get(arg0).getTitle());//����ͼ��ı���
		viewHolder.image.setImageResource(pictures.get(arg0).getmageld());//����ͼ��Ķ�����ֵ
		return arg1;//����ͼ��ı�ʶ
	}
}
