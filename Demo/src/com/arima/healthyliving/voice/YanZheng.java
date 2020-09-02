package com.arima.healthyliving.voice;

import com.arima.healthyliving.R;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;



public class YanZheng extends Activity {
	public static Object map;
	private EditText p_number;
	private Button btn_yzm;
	private EditText yz_number;
	private Button next;
	
	//倒计时

	//验证码
	private final String appKey="239ded9f00214";
	private final String appSecret="6dba31ad329a2010379d0825a41e7b6c";
	private final String TAG="--YanZheng--";
    private boolean isChange;
	    //控制按钮样式是否改变
    private boolean tag = true;
	    //每次验证请求需要间隔60S
    private int i=60;
    private String phone;
    private String code;
    private boolean flag;
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yanzheng);
		
		//启动短信验证SDK
		MobSDK.init(this,appKey,appSecret);
		
		
		EventHandler eh=new EventHandler(){
			public void afterEvent(int event,int result,Object data){
				Message msg=new Message();
				msg.arg1=event;
				msg.arg2=result;
				msg.obj=data;
				handler.sendMessage(msg);
			}
		};
		
		 SMSSDK.registerEventHandler(eh); //注册短信回调
		
	        
		 
		 

		
		//获得控件
		p_number=(EditText)findViewById(R.id.p_number);
		btn_yzm=(Button)findViewById(R.id.btn_yzm);
		yz_number=(EditText)findViewById(R.id.yz_number);
		next=(Button)findViewById(R.id.next);

		btn_yzm.setBackgroundColor(Color.parseColor("#FFCC00"));
		next.setOnClickListener(new NextListener());
		btn_yzm.setOnClickListener(new YanZhengListener());
		

	}
	
	
	


	
	
	
	
	class NextListener implements OnClickListener{
		

		public void onClick(View arg0) { 
			// TODO Auto-generated method stub
			code=yz_number.getText().toString();
			if (code.equals("")){
                Toast.makeText(YanZheng.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            }else{
                //填写了验证码，进行验证
                SMSSDK.submitVerificationCode("86", phone, code);

            }

			
		}
		
	}
	
	class YanZhengListener implements OnClickListener{
	
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			phone=p_number.getText().toString();
			if(phone.equals("")){
				Toast.makeText(YanZheng.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
			}
			else{
				//填写手机号
				if(isMobileNO(phone)){
					//如果手机号无误，则发送请求
					changeBtnGetCode();
                    SMSSDK.getVerificationCode("86", phone);
                    flag=false;
				}
				else{
					//手机号格式有误
                    Toast.makeText(YanZheng.this,"手机号格式错误，请检查",Toast.LENGTH_SHORT).show();
				}         
			}
		}
	}
	
	 private void changeBtnGetCode() {

	        Thread thread = new Thread() {
	            @Override
	            public void run() {
	                if (tag) {
	                    while (i > 0) {
	                        i--;
	                        //如果活动为空
	                        if (YanZheng.this == null) {
	                            break;
	                        }

	                        YanZheng.this.runOnUiThread(new Runnable() {
	                            @Override
	                            public void run() {
	                            	btn_yzm.setText("获取验证码(" + i + ")");
	                            	btn_yzm.setClickable(false);
	                            }
	                        });

	                        try {
	                            Thread.sleep(1000);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                    tag = false;
	                }
	                i = 60;
	                tag = true;

	                if (YanZheng.this != null) {
	                	YanZheng.this.runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                        	btn_yzm.setText("获取验证码");
	                        	btn_yzm.setClickable(true);
	                        }
	                    });
	                }
	            }
	        };
	        thread.start();
	    }
	 
	 
	 private boolean isMobileNO(String phone) {
	       /*
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、180、189、（1349卫通）
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	    */
	        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
	        if (TextUtils.isEmpty(phone)) return false;
	        else return phone.matches(telRegex);
	    }

	 
	 
	 private Handler handler=new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				int event=msg.arg1;
				int result=msg.arg2;
				Object data=msg.obj;
				
				if(result==SMSSDK.RESULT_COMPLETE){
					//如果操作成功
					if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
						//验证校验码，返回校验手机和国家
						
						Toast.makeText(YanZheng.this, "验证成功",Toast.LENGTH_SHORT).show();
						String uname=p_number.getText().toString();
						Intent intent=new Intent(YanZheng.this,RePassword.class);
						intent.putExtra("uname", uname);
						startActivity(intent);
					}else if(event==SMSSDK.EVENT_GET_VERIFICATION_CODE){
						//获取验证码成功
						Toast.makeText(YanZheng.this,"验证码已发送", Toast.LENGTH_SHORT).show();
						
					}else if(event==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
						//返回发送验证码的国家列表
					}
				}
				else{
					//如果发送失败
					if(flag){
						Toast.makeText(YanZheng.this,"验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
					}else{
						((Throwable)data).printStackTrace();
						Toast.makeText(YanZheng.this,"验证码错误", Toast.LENGTH_SHORT).show();
						yz_number.setText("");
					}
				}
			}
		
		};
		


	 protected void onDestroy(){
		 super.onDestroy();
		 SMSSDK.unregisterAllEventHandler();
	 }
	
	


}




  

 
	   
  