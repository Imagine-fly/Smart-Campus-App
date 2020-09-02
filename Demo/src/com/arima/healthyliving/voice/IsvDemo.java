 package com.arima.healthyliving.voice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.arima.healthyliving.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;
import com.iflytek.voicedemo.SpeechdemoActivity;

/**
 * ��������ʾ��
 *
 * @author iFlytek &nbsp;&nbsp;&nbsp;<a href="http://http://www.xfyun.cn/">Ѷ��������</a>
 */
public class IsvDemo extends Activity implements OnClickListener {
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
	private TextView mRecordTimeTextView;
	private AlertDialog mTextPwdSelectDialog;
	private Toast mToast;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_isv_demo);
		
		
		
		
		initUi();
		// ���ϸ�ҳ��������û�����ΪAuthId
		mAuthId = getIntent().getStringExtra("uname");
		mAuthIdTextView.setText(mAuthId);
		
		// ��ʼ��SpeakerVerifier��InitListenerΪ��ʼ����ɺ�Ļص��ӿ�
		mVerifier = SpeakerVerifier.createVerifier(IsvDemo.this, new InitListener() {
			
			@Override
			public void onInit(int errorCode) {
				if (ErrorCode.SUCCESS == errorCode) {
					showTip("�����ʼ���ɹ�");
				} else {
					showTip("�����ʼ��ʧ�ܣ������룺" + errorCode);
				}
			}
		});
	}
	
	@SuppressLint("ShowToast")
	private void initUi() {
		mResultEditText = (EditText) findViewById(R.id.edt_result);
		mAuthIdTextView = (TextView) findViewById(R.id.txt_authorid);
		mShowPwdTextView = (TextView) findViewById(R.id.showPwd);
		mShowMsgTextView = (TextView) findViewById(R.id.showMsg);
		mShowRegFbkTextView = (TextView) findViewById(R.id.showRegFbk);
		mRecordTimeTextView = (TextView) findViewById(R.id.recordTime);

		findViewById(R.id.isv_register).setOnClickListener(IsvDemo.this);
		findViewById(R.id.isv_verify).setOnClickListener(IsvDemo.this);
		findViewById(R.id.isv_stop_record).setOnClickListener(IsvDemo.this);
		findViewById(R.id.isv_cancel).setOnClickListener(IsvDemo.this);
		findViewById(R.id.isv_getpassword).setOnClickListener(IsvDemo.this);
		findViewById(R.id.isv_search).setOnClickListener(IsvDemo.this);
		findViewById(R.id.isv_delete).setOnClickListener(IsvDemo.this);
		
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
		
		mToast = Toast.makeText(IsvDemo.this, "", Toast.LENGTH_SHORT);
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
		mRecordTimeTextView.setText("");
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
	
	@Override
	public void onClick(View v){
		if( !checkInstance() ){
			return;
		}
	
		
		
		switch (v.getId()) {
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
		case R.id.isv_search:
			performModelOperation("que", mModelOperationListener);
			break;
		case R.id.isv_delete:
			performModelOperation("del", mModelOperationListener);
			break;
		case R.id.isv_register:
			// ��ղ���
			mVerifier.setParameter(SpeechConstant.PARAMS, null);
			mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
					Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test.pcm");
			// ����ĳЩ��˷�ǳ������Ļ�������nexus��samsung i9300�ȣ���������������ö�¼���������봦��
//			mVerify.setParameter(SpeechConstant.AUDIO_SOURCE, "" + MediaRecorder.AudioSource.VOICE_RECOGNITION);
			if (mPwdType == PWD_TYPE_TEXT) {
//				// �ı�����ע����Ҫ��������
				if (TextUtils.isEmpty(mTextPwd)) {
				return;
			}
				mVerifier.setParameter(SpeechConstant.ISV_PWD, mTextPwd);
				mShowPwdTextView.setText("�������" + mTextPwd);
				mShowMsgTextView.setText("ѵ�� ��" + 1 + "�飬ʣ��4��");
			} else if (mPwdType == PWD_TYPE_NUM) {
				// ��������ע����Ҫ��������
				if (TextUtils.isEmpty(mNumPwd)) {
					showTip("���ȡ�������в���");
					return;
				}
				mVerifier.setParameter(SpeechConstant.ISV_PWD, mNumPwd);
				((TextView) findViewById(R.id.showPwd)).setText("�������"
						+ mNumPwd.substring(0, 8));
				mShowMsgTextView.setText("ѵ�� ��" + 1 + "�飬ʣ��4��");
			}else if (mPwdType==PWD_TYPE_FREE){
				//����˵��ע�����֮���� ����Ϊ��1�� ���ʵĵ����á�8000��
				mVerifier.setParameter(SpeechConstant.ISV_RGN,"1");
                mVerifier.setParameter(SpeechConstant.SAMPLE_RATE,"8000");

			}
			
			 setRadioClickable(false);

			// ����auth_id����������Ϊ��
			mVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
			// ����ҵ������Ϊע��
			mVerifier.setParameter(SpeechConstant.ISV_SST, "train");
			// ����������������
			mVerifier.setParameter(SpeechConstant.ISV_PWDT, "" + mPwdType);
			// ��ʼע��
			mVerifier.startListening(mRegisterListener);
			break;
			 
			// ��������֤������һ��ע�Ἣ������
		case R.id.isv_verify:
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
		case R.id.isv_stop_record:
			mVerifier.stopListening();
			break;
		case R.id.isv_cancel:
			setRadioClickable(true);
			mVerifier.cancel();
			initTextView();
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
							IsvDemo.this)
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
	
	private SpeechListener mModelOperationListener = new SpeechListener() {
		
		@Override
		public void onEvent(int eventType, Bundle params) {
		}
		
		@Override
		public void onBufferReceived(byte[] buffer) {
			setRadioClickable(true);
			
			String result = new String(buffer);
			try {
				JSONObject object = new JSONObject(result);
				String cmd = object.getString("cmd");
				int ret = object.getInt("ret");
				
				if ("del".equals(cmd)) {
					if (ret == ErrorCode.SUCCESS) {
						showTip("ɾ���ɹ�");
						mResultEditText.setText("");
					} else if (ret == ErrorCode.MSP_ERROR_FAIL) {
						showTip("ɾ��ʧ�ܣ�ģ�Ͳ�����");
					}
				} else if ("que".equals(cmd)) {
					if (ret == ErrorCode.SUCCESS) {
						showTip("ģ�ʹ���");
					} else if (ret == ErrorCode.MSP_ERROR_FAIL) {
						showTip("ģ�Ͳ�����");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void onCompleted(SpeechError error) {
			setRadioClickable(true);
			
			if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
				showTip("����ʧ�ܣ�" + error.getPlainDescription(true));
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
				mAuthId = getIntent().getStringExtra("uname");
				String username=mAuthId;
				
				Intent intent = new Intent(IsvDemo.this, Menu.class);
				//Intent intent = new Intent(IsvDemo.this, SuccessActivity.class);
				intent.putExtra("username", username);
				startActivity(intent);
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
		
		
		
		// �����������ݲ���
		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle arg3) {
			// ���´������ڻ�ȡ���ƶ˵ĻỰid����ҵ�����ʱ���Ựid�ṩ������֧����Ա�������ڲ�ѯ�Ự��־����λ����ԭ��
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}

		@Override
		public void onError(SpeechError error) {
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
		public void onEndOfSpeech() {
			// �˻ص���ʾ����⵽��������β�˵㣬�Ѿ�����ʶ����̣����ٽ�����������
			showTip("����˵��");
		}

		@Override
		public void onBeginOfSpeech() {
			// �˻ص���ʾ��sdk�ڲ�¼�����Ѿ�׼�����ˣ��û����Կ�ʼ��������
			showTip("��ʼ˵��");
		}
	};
	
	private VerifierListener mRegisterListener = new VerifierListener() {

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("��ǰ����˵����������С��" + volume);
			Log.d(TAG, "������Ƶ���ݣ�"+data.length);
		}

		@Override
		public void onResult(VerifierResult result) {
			((TextView)findViewById(R.id.showMsg)).setText(result.source);
			
			if (result.ret == ErrorCode.SUCCESS) {
				switch (result.err) {
				case VerifierResult.MSS_ERROR_IVP_GENERAL:
					mShowMsgTextView.setText("�ں��쳣");
					break;
				case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
					mShowRegFbkTextView.setText("ѵ���ﵽ������");
					break;
				case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
					mShowRegFbkTextView.setText("���ֽط�");
					break;
				case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
					mShowRegFbkTextView.setText("̫������");
					break;
				case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
					mShowRegFbkTextView.setText("¼��̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
					mShowRegFbkTextView.setText("ѵ��ʧ�ܣ����������ı���һ��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
					mShowRegFbkTextView.setText("����̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
					mShowMsgTextView.setText("��Ƶ���ﲻ������˵��Ҫ��");
				default:
					mShowRegFbkTextView.setText("");
					break;
				}
				
				if (result.suc == result.rgn) {
					setRadioClickable(true);
					mShowMsgTextView.setText("ע��ɹ�");
					
					if (PWD_TYPE_TEXT == mPwdType) {
						mResultEditText.setText("�����ı���������ID��\n" + result.vid);
						Log.e(TAG, "onResult:���� "+result.vid );     
					} else if (PWD_TYPE_NUM == mPwdType) {
						mResultEditText.setText("����������������ID��\n" + result.vid);
						Log.e(TAG, "onResult:���� "+result.vid );
					}else if (mPwdType==PWD_TYPE_FREE){
                        mResultEditText.setText("����������������ID��\n" + result.vid);
                        Log.e(TAG, "onResult: ����"+result.vid );

                    }

					
				} else {
					int nowTimes = result.suc + 1;
					int leftTimes = result.rgn - nowTimes;
					
					if (PWD_TYPE_TEXT == mPwdType) {
						mShowPwdTextView.setText("�������" + mTextPwd);
					} else if (PWD_TYPE_NUM == mPwdType) {
						mShowPwdTextView.setText("�������" + mNumPwdSegs[nowTimes - 1]);
					}
					
					mShowMsgTextView.setText("ѵ�� ��" + nowTimes + "�飬ʣ��" + leftTimes + "��");
				}

			}else {
				setRadioClickable(true);
				
				mShowMsgTextView.setText("ע��ʧ�ܣ������¿�ʼ��");	
			}
		}
		// �����������ݲ���
		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle arg3) {
			// ���´������ڻ�ȡ���ƶ˵ĻỰid����ҵ�����ʱ���Ựid�ṩ������֧����Ա�������ڲ�ѯ�Ự��־����λ����ԭ��
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}

		@Override
		public void onError(SpeechError error) {
			setRadioClickable(true);
			
			if (error.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
				showTip("ģ���Ѵ��ڣ���������ע�ᣬ����ɾ��");
			} else {
				showTip("onError Code��" + error.getPlainDescription(true));
			}
		}

		@Override
		public void onEndOfSpeech() {
			showTip("����˵��");
		}

		@Override
		public void onBeginOfSpeech() {
			showTip("��ʼ˵��");
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

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
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
