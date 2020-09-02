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
	//储存系统功能文本
	String[] titles=new String[] {"新增支出","新增收入","我的收入和支出","退出"};
	//定义数值，使用系统自带的图标
	int[] images = new int[] {R.drawable.appone5,R.drawable.appone6,R.drawable.appone7,R.drawable.appone8};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);//布局文件选用main.xml
		gvlnfo = (GridView)findViewById(R.id.gvlnfo);//获取main.xml的gvlngo组件
		pictureAdapter adapter = new pictureAdapter(titles,images,this);
		gvlnfo.setAdapter(adapter);
		//为gvlngo设置监听器
		gvlnfo.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = null;
				//通过switch语句实现Activity之间的跳转
				switch(arg2) {
				case 0:
					intent = new Intent(MainActivity.this,AddOutdata.class);//跳转到AddOutdata
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(MainActivity.this,AddIndata.class);//跳转到AddIndata
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(MainActivity.this,Indatainfo.class);//跳转到Indatainfo
				startActivity(intent);
					break;
				case 3:
					finish();//关闭当前的窗口
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
//定义一个内部类，用来定义文本组件及图片组件
class ViewHolder
{
	public TextView title;
	public ImageView image;
}
//该内部类用来定义图标及说明文字的实体
class Picture
{
	private String title;//定义title字符串，表示图标题目
	private int imageld;//定义imageld整型变量，表示图像的二进制数
	public Picture()
	{
		super();
	}
	public Picture(String title,int imageld)
	{
		super();
		this.title=title;//为图像标题赋值
		this.imageld=imageld;//为图像的二进制值赋值
	}
	public String getTitle()//使图像标题可读
	{
		return title;
	}
	public void setTitle(String title)//使图像标题可写
	{
		this.title = title;
	}
	public int getmageld()//使图像二进制值可读
	{
		return imageld;
	}
	public void setimageld(int imageld)//使图像二进制值可写
	{
		this.imageld = imageld;
	}
}
//定义pictureAdapter类并继承BaseAdapter类
class pictureAdapter extends BaseAdapter
{
	private LayoutInflater inflater;//创建LayoutInflater对象
	private List<Picture> pictures;
	public pictureAdapter(String[] titles, int[] images, Context context) {
		super();
		pictures = new ArrayList<Picture>();//初始化泛型集合对象
		inflater = LayoutInflater.from(context);//初始化LayoutInflater对象
		for(int i=0;i<images.length;i++)
		{
			Picture picture = new Picture(titles[i],images[i]);//使标题和图像生成Picture对象
			pictures.add(picture);//是Picture对象添加到泛型集合中
		}
	}
	//获取泛型集合的长度
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
	//获取泛型集合指定索引处的项
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pictures.get(arg0);
	}
	//返回泛型集合的索引
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	//设置图像的各种属性
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(arg1 == null)
		{
			arg1 = inflater.inflate(R.layout.gvitem,null);//设置图像的标识
			viewHolder = new ViewHolder();//初始化ViewHolder对象
			viewHolder.title = (TextView)arg1.findViewById(R.id.ItemTitle);//设置图像标题
			viewHolder.image = (ImageView)arg1.findViewById(R.id.Itemlmage);//设置图像的二进制值
			arg1.setTag(viewHolder);//设置提示
		}
		else {
			viewHolder = (ViewHolder)arg1.getTag();
		}
		viewHolder.title.setText(pictures.get(arg0).getTitle());//设置图像的标题
		viewHolder.image.setImageResource(pictures.get(arg0).getmageld());//设置图像的二进制值
		return arg1;//返回图像的标识
	}
}
