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
		// Ӧ�ó�����ڴ�����,�����ֻ��ڴ��С��ɱ����̨����,���SpeechUtility����Ϊnull
		// �����������Ӧ��appid
		SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id)); 
		MobSDK.init(this,appKey,appSecret);
	}
}
