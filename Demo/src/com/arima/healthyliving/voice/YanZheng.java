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
	
	//����ʱ

	//��֤��
	private final String appKey="239ded9f00214";
	private final String appSecret="6dba31ad329a2010379d0825a41e7b6c";
	private final String TAG="--YanZheng--";
    private boolean isChange;
	    //���ư�ť��ʽ�Ƿ�ı�
    private boolean tag = true;
	    //ÿ����֤������Ҫ���60S
    private int i=60;
    private String phone;
    private String code;
    private boolean flag;
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yanzheng);
		
		//����������֤SDK
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
		
		 SMSSDK.registerEventHandler(eh); //ע����Żص�
		
	        
		 
		 

		
		//��ÿؼ�
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
                Toast.makeText(YanZheng.this,"��֤�벻��Ϊ��",Toast.LENGTH_SHORT).show();
            }else{
                //��д����֤�룬������֤
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
				Toast.makeText(YanZheng.this,"�ֻ��Ų���Ϊ��",Toast.LENGTH_SHORT).show();
			}
			else{
				//��д�ֻ���
				if(isMobileNO(phone)){
					//����ֻ���������������
					changeBtnGetCode();
                    SMSSDK.getVerificationCode("86", phone);
                    flag=false;
				}
				else{
					//�ֻ��Ÿ�ʽ����
                    Toast.makeText(YanZheng.this,"�ֻ��Ÿ�ʽ��������",Toast.LENGTH_SHORT).show();
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
	                        //����Ϊ��
	                        if (YanZheng.this == null) {
	                            break;
	                        }

	                        YanZheng.this.runOnUiThread(new Runnable() {
	                            @Override
	                            public void run() {
	                            	btn_yzm.setText("��ȡ��֤��(" + i + ")");
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
	                        	btn_yzm.setText("��ȡ��֤��");
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
	    �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
	    ��ͨ��130��131��132��152��155��156��185��186
	    ���ţ�133��153��180��189����1349��ͨ��
	    �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
	    */
	        String telRegex = "[1][358]\\d{9}";//"[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
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
					//��������ɹ�
					if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
						//��֤У���룬����У���ֻ��͹���
						
						Toast.makeText(YanZheng.this, "��֤�ɹ�",Toast.LENGTH_SHORT).show();
						String uname=p_number.getText().toString();
						Intent intent=new Intent(YanZheng.this,RePassword.class);
						intent.putExtra("uname", uname);
						startActivity(intent);
					}else if(event==SMSSDK.EVENT_GET_VERIFICATION_CODE){
						//��ȡ��֤��ɹ�
						Toast.makeText(YanZheng.this,"��֤���ѷ���", Toast.LENGTH_SHORT).show();
						
					}else if(event==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
						//���ط�����֤��Ĺ����б�
					}
				}
				else{
					//�������ʧ��
					if(flag){
						Toast.makeText(YanZheng.this,"��֤���ȡʧ�ܣ������»�ȡ", Toast.LENGTH_SHORT).show();
					}else{
						((Throwable)data).printStackTrace();
						Toast.makeText(YanZheng.this,"��֤�����", Toast.LENGTH_SHORT).show();
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




  

 
	   
  