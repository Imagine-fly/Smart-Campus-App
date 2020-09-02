package com.iflytek.voicedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SpeechdemoActivity extends Activity implements OnClickListener {

	private Toast mToast;
	/*private Button sun;*/

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.speechdemo);
		/*sun=(Button)findViewById(R.id.button1);
		sun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "hello!", Toast.LENGTH_LONG).show();
			}
		});*/
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		SimpleAdapter listitemAdapter = new SimpleAdapter();
		((ListView) findViewById(R.id.listview_main)).setAdapter(listitemAdapter);
	}

	@Override
	public void onClick(View view) {
		int tag = Integer.parseInt(view.getTag().toString());
		Intent intent = null;
		switch (tag) {
		case 0:
			// 语音听写
			intent = new Intent(SpeechdemoActivity.this, IatDemo.class);
			break;
		/*case 1:
			// 语法识别
			intent = new Intent(MainActivity.this, AsrDemo.class);
			break;*/
		case 1:
			// 语义理解
			intent = new Intent(SpeechdemoActivity.this, UnderstanderDemo.class);
			break;
		case 2:
			// 语音合成
			intent = new Intent(SpeechdemoActivity.this, TtsDemo.class);
			break;
		/*case 4:
			// 语音评测
			intent = new Intent(MainActivity.this, IseDemo.class);
			break;
		case 5:
			// 唤醒
			showTip("请登录：http://www.xfyun.cn/ 下载体验吧！");
			break;
		case 6:
			// 声纹*/		
			default:
			showTip("在IsvDemo中哦，为了代码简洁，就不放在一起啦，^_^");
			break;
		}
		
		if (intent != null) {
			startActivity(intent);
		}
	}

	// Menu 列表
	String items[] = { "语音听写", "语义理解", "语音合成",};

	private class SimpleAdapter extends BaseAdapter {
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				LayoutInflater factory = LayoutInflater.from(SpeechdemoActivity.this);
				View mView = factory.inflate(R.layout.list_items, null);
				convertView = mView;
			}
			
			Button btn = (Button) convertView.findViewById(R.id.btn);
			btn.setOnClickListener(SpeechdemoActivity.this);
			btn.setTag(position);
			btn.setText(items[position]);
			
			return convertView;
		}

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	
}
