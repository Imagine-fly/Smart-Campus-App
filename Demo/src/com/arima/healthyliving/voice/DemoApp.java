package com.arima.healthyliving.voice;


import com.arima.healthyliving.R;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;

import android.app.Application;


public class DemoApp extends Application{
	private final String appKey="239ded9f00214";
	private final String appSecret="6dba31ad329a2010379d0825a41e7b6c";
	@Override
	public void onCreate() {
		super.onCreate();
		// 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
		// 设置你申请的应用appid
		SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id)); 
		MobSDK.init(this,appKey,appSecret);
	}
}
