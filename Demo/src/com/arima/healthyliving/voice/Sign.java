package com.arima.healthyliving.voice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.arima.healthyliving.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Sign extends Activity implements OnClickListener {	
    private static final String TAG = IsvDemo.class.getSimpleName();
	
	private static final int PWD_TYPE_TEXT = 1;
	// ����˵����Ч�����⣬�ݲ�����
	private static final int PWD_TYPE_FREE = 2;
	private static final int PWD_TYPE_NUM = 3;
	// ��ǰ�����������ͣ�1��2��3�ֱ�Ϊ�ı�������˵����������
	private int mPwdType = PWD_TYPE_TEXT;
	// ����ʶ�����
	private SpeakerVerifier mVerifier;
	// ����AuthId���û�����ƽ̨����ݱ�ʶ��Ҳ������ģ�͵ı�ʶ
	// ��ʹ��Ӣ����ĸ������ĸ�����ֵ���ϣ���ʹ�������ַ�
	private String mAuthId = "";
	// �ı���������
	private String mTextPwd = "";
	// ������������
	private String mNumPwd = "";
	// ������������Σ�Ĭ����5��
	private String[] mNumPwdSegs;
	
	private EditText mResultEditText;
	private TextView mAuthIdTextView;
	private RadioGroup mPwdTypeGroup;
	private TextView mShowPwdTextView;
	private TextView mShowMsgTextView;
	private TextView mShowRegFbkTextView;
	private AlertDialog mTextPwdSelectDialog;
	private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sign);
		
		
		initUi();
		// ���ϸ�ҳ��������û�����ΪAuthId
		mAuthId = getIntent().getStringExtra("uname");
		mAuthIdTextView.setText(mAuthId);
		
		// ��ʼ��SpeakerVerifier��InitListenerΪ��ʼ����ɺ�Ļص��ӿ�
				mVerifier = SpeakerVerifier.createVerifier(Sign.this, new InitListener(){

					@Override
					public void onInit(int errorCode) {
						// TODO Auto-generated method stub
						if (ErrorCode.SUCCESS == errorCode) {
							showTip("�����ʼ���ɹ�");
						} else {
							showTip("�����ʼ��ʧ�ܣ������룺" + errorCode);
						}
						
					}
					
				});	
	}
	@SuppressLint("ShowToast")
	private void initUi(){
		mResultEditText = (EditText) findViewById(R.id.edt_result);
		mAuthIdTextView = (TextView) findViewById(R.id.txt_authorid);
		mShowPwdTextView = (TextView) findViewById(R.id.showPwd);
		mShowMsgTextView = (TextView) findViewById(R.id.showMsg);
		mShowRegFbkTextView = (TextView) findViewById(R.id.showRegFbk);
		
		findViewById(R.id.isv_getpassword).setOnClickListener(Sign.this);
		findViewById(R.id.sign).setOnClickListener(Sign.this);
		
		// ����ѡ��RadioGroup��ʼ��
		mPwdTypeGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mPwdTypeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				initTextView();
				switch (checkedId) {
				case R.id.radioText:
					mPwdType = PWD_TYPE_TEXT;
					break;
				case R.id.radioNumber:
					mPwdType = PWD_TYPE_NUM;
					break;
				case R.id.radioFree:
					mPwdType = PWD_TYPE_FREE;
				default:
					break;
				}
			}
		});
		
		mToast = Toast.makeText(Sign.this, "", Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	
	
	/**
	 * ��ʼ��TextView�������ı�
	 */
	private void initTextView(){
		mTextPwd = null;
		mNumPwd = null;
		mResultEditText.setText("");
		mShowPwdTextView.setText("");
		mShowMsgTextView.setText("");
		mShowRegFbkTextView.setText("");
	}
	
	
	/**
	 * ����radio��״̬
	 */
	private void setRadioClickable(boolean clickable){
		// ����RaioGroup״̬Ϊ�ǰ���״̬
		mPwdTypeGroup.setPressed(false);
		findViewById(R.id.radioText).setClickable(clickable);
		findViewById(R.id.radioNumber).setClickable(clickable);
		findViewById(R.id.radioFree).setClickable(clickable);
	}
	
	
	
	/**
	 * ִ��ģ�Ͳ���
	 *  
	 * @param operation ��������
	 * @param listener  ��������ص�����
	 */
	private void performModelOperation(String operation, SpeechListener listener) {
		// ��ղ���
		mVerifier.setParameter(SpeechConstant.PARAMS, null);
		
		 //������������(������Ҫ����)
		mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
		
		if (mPwdType == PWD_TYPE_TEXT) {
			// �ı�����ɾ����Ҫ��������
			if (TextUtils.isEmpty(mTextPwd)) {
				showTip("���ȡ�������в���");
				return;
			}
			mVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);
		}  else if (mPwdType == PWD_TYPE_NUM) {
			
		}  else if (mPwdType==PWD_TYPE_FREE){
			
		} 
		setRadioClickable(false);
		// ����auth_id����������Ϊ��
		mVerifier.sendRequest(operation, mAuthId, listener);
	}
	
	

	
	
	
	
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if( !checkInstance() ){
			return;
		}
		
		switch (v.getId()){
		case R.id.isv_getpassword:
			// ��ȡ����֮ǰ����ֹ֮ǰ��ע�����֤����
			mVerifier.cancel();
			initTextView();
			setRadioClickable(false);
			// ��ղ���
			mVerifier.setParameter(SpeechConstant.PARAMS, null);
			mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
			if (mPwdType!=PWD_TYPE_FREE)

			//���صļ�������
			mVerifier.getPasswordList(mPwdListenter);
			break;
			
		case R.id.sign:
			// �����ʾ��Ϣ
			((TextView) findViewById(R.id.showMsg)).setText("");
			// ��ղ���
			mVerifier.setParameter(SpeechConstant.PARAMS, null);
			mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
					Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/verify.pcm");
			mVerifier = SpeakerVerifier.getVerifier();
			// ����ҵ������Ϊ��֤
			mVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
			// ����ĳЩ��˷�ǳ������Ļ�������nexus��samsung i9300�ȣ���������������ö�¼���������봦��
//			mVerify.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
			
			if (mPwdType == PWD_TYPE_TEXT) {
				// �ı�����ע����Ҫ��������
				if (TextUtils.isEmpty(mTextPwd)) {
					showTip("���ȡ�������в���");
					return;
				}
				mVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);
				((TextView) findViewById(R.id.showPwd)).setText("�������"
						+ mTextPwd);
			} else if (mPwdType == PWD_TYPE_NUM) {
				// ��������ע����Ҫ��������
				String verifyPwd = mVerifier.generatePassword(8);
				mVerifier.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
				((TextView) findViewById(R.id.showPwd)).setText("�������"
						+ verifyPwd);
			}else if (mPwdType==PWD_TYPE_FREE){
                mVerifier.setParameter(SpeechConstant.SAMPLE_RATE,"8000");
                ((TextView)findViewById(R.id.showPwd)).setText("�����˵Щ������֤");
            }
			
			 setRadioClickable(false);


			// ����auth_id����������Ϊ��
			mVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
			mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
			// ��ʼ��֤
			mVerifier.startListening(mVerifyListener);
			break;
			
		default:
			break;
		
		}
		
		
	}
	
	
	private String[] items;
	private SpeechListener mPwdListenter = new SpeechListener() {
		@Override
		public void onEvent(int eventType, Bundle params) {
		}
		
		@Override
		public void onBufferReceived(byte[] buffer) {
			setRadioClickable(true);
			
			String result = new String(buffer);
			switch (mPwdType) {
			case PWD_TYPE_TEXT:
				try {
					JSONObject object = new JSONObject(result);
					if (!object.has("txt_pwd")) {
						initTextView();
						return;
					}
					
					JSONArray pwdArray = object.optJSONArray("txt_pwd");
					items = new String[pwdArray.length()];
					for (int i = 0; i < pwdArray.length(); i++) {
						items[i] = pwdArray.getString(i);
					}
					mTextPwdSelectDialog = new AlertDialog.Builder(
							Sign.this)
							.setTitle("��ѡ�������ı�")
							.setItems(items,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface arg0, int arg1) {
										mTextPwd = items[arg1];
										mResultEditText.setText("�������룺" + mTextPwd);
									}
								}).create();
					mTextPwdSelectDialog.show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case PWD_TYPE_NUM:
				StringBuffer numberString = new StringBuffer();
				try {
					JSONObject object = new JSONObject(result);
					if (!object.has("num_pwd")) {
						initTextView();
						return;
					}
					
					JSONArray pwdArray = object.optJSONArray("num_pwd");
					numberString.append(pwdArray.get(0));
					for (int i = 1; i < pwdArray.length(); i++) {
						numberString.append("-" + pwdArray.get(i));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mNumPwd = numberString.toString();
				mNumPwdSegs = mNumPwd.split("-");
				mResultEditText.setText("�������룺\n" + mNumPwd);
				break;
			default:
				break;
			}

		}
		
		@Override
		public void onCompleted(SpeechError error) {
			setRadioClickable(true);
			
			if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
				showTip("��ȡʧ�ܣ�" + error.getErrorCode());
			}
		}
	};
	
	private VerifierListener mVerifyListener = new VerifierListener() {

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("��ǰ����˵����������С��" + volume);
			Log.d(TAG, "������Ƶ���ݣ�"+data.length);
		}

		@Override
		public void onResult(VerifierResult result) {
			setRadioClickable(true);
			mShowMsgTextView.setText(result.source);
			
			if (result.ret == 0) {
				// ��֤ͨ��
				mShowMsgTextView.setText("��֤ͨ��");
				
				String username=mAuthIdTextView.getText().toString();
				Intent intent=new Intent();
				intent.putExtra("username", username);
        		intent.setClass(Sign.this,SureSign.class);
        		Sign.this.startActivity(intent);
				
				/***************************************************************************************/
			}
			else{
				// ��֤��ͨ��
				switch (result.err) {
				case VerifierResult.MSS_ERROR_IVP_GENERAL:
					mShowMsgTextView.setText("�ں��쳣");
					break;
				case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
					mShowMsgTextView.setText("���ֽط�");
					break;
				case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
					mShowMsgTextView.setText("̫������");
					break;
				case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
					mShowMsgTextView.setText("¼��̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
					mShowMsgTextView.setText("��֤��ͨ�������������ı���һ��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
					mShowMsgTextView.setText("����̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
					mShowMsgTextView.setText("��Ƶ���ﲻ������˵��Ҫ��");
					break;
				default:
					mShowMsgTextView.setText("��֤��ͨ��");
					break;
				}
			}
		}
		
		@Override
		public void onEndOfSpeech() {
			// �˻ص���ʾ����⵽��������β�˵㣬�Ѿ�����ʶ����̣����ٽ�����������
			showTip("����˵��");
		}

		@Override
		public void onBeginOfSpeech() {
			// �˻ص���ʾ��sdk�ڲ�¼�����Ѿ�׼�����ˣ��û����Կ�ʼ��������
			showTip("��ʼ˵��");
		}

		@Override
		public void onError(SpeechError error) {
			// TODO Auto-generated method stub
            setRadioClickable(true);
			
			switch (error.getErrorCode()) {
			case ErrorCode.MSP_ERROR_NOT_FOUND:
				mShowMsgTextView.setText("ģ�Ͳ����ڣ�����ע��");
				break;

			default:
				showTip("onError Code��"	+ error.getPlainDescription(true));
				break;
			}
			
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	public void finish() {
		if (null != mTextPwdSelectDialog) {
			mTextPwdSelectDialog.dismiss();
		}
		super.finish();
	}
	
	@Override
	protected void onDestroy() {		
		if (null != mVerifier) {
			mVerifier.stopListening();
			mVerifier.destroy();
		}
		super.onDestroy();
	}
	
	
	 private boolean checkInstance(){
		 if( null == mVerifier ){
			 this.showTip( "��������ʧ�ܣ���ȷ�� libmsc.so ������ȷ��\n ���е��� createUtility ���г�ʼ��" );
			 return false;

		 }
		 else{
	            return true;
	        }

		 
	            
	 }

	

				
				
				
	
	

}
