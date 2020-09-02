package com.arima.healthyliving.bycicleriding;

import java.io.File;
import java.util.ArrayList;

import com.arima.healthyliving.R;
import com.arima.healthyliving.bycicleriding.BycridData.BycridImgInfo;
import com.arima.healthyliving.bycicleriding.TrackDataHelper.INDEX_ENUM;
import com.arima.healthyliving.bycicleriding.TrackDataHelper.LNG_MODE;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

//<2014/02/21-Task_34127-xiangrongji, [AAP1302][BycicleRiding]upload codes for Bycicle Riding screens
public class RidingActivity extends Activity {
	private final static String TAG = "RidingActivity";
	public final static int BYCRID_RIDING_OPT_FINISH = 1;
	public final static int BYCRID_RIDING_OPT_CAPTURE = 2;
	public final static int BYCRID_RIDING_OPT_ZOOMOUT = 3;
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	private final static int BYCRID_RIDING_INTENT_REQ_CAPTURE = 1;
	//>2014/03/08-Task_34590-xiangrongji
	
	//private OptionsView mOptsView;
	private TextView mCurrTimeTextView;
	private TextView mDistanceValTextView;
	private TextView mDurationValTextView;
	private ImageView mAltitudeIcon;
	private TextView mAltitudeValTextView;
	private TextView mVelocityValTextView;
	
	private TimeTickReceiver mTimeTickReceiver;
	//<2014/02/25-Task_34211-ZhiweiWang,[HealthyLiving][BycicleRide] BycicleRide app UI Modify.
	//<2014/03/08-Task_34628-xiangrongji, [AAP1302][BycicleRiding]disable zoomout
	private Button mOptBtnFinish = null;
	private Button mOptBtnCapture = null;
	private Button mOptBtnZoomout = null;
	//>2014/03/08-Task_34628-xiangrongji
	private SlidingDrawer mSlidingDrawer = null;
	private ImageView slidingImageView = null;
	//>2014/02/25-Task_34211-ZhiweiWang.

	//<2014/02/26-Task_34228-xiangrongji, [AAP1302][BycicleRiding]bycicle riding tracker
	private ImageView mTrack;
	
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	private long mDate;
	//>2014/03/08-Task_34590-xiangrongji
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private boolean isUpdating;
	private long mStartTimeMills;
	private long mLastDurationSec;
	private long mDuration;
	
	private float mRefAltitude;
	private float mMaxDeltaAlt;
	private float mMinDeltaAlt;
	private float mCurrDeltaAlt;
	
	private float mCurrSpeed;
	//>2014/03/06-Task_34540-xiangrongji

	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load	
	private ArrayList<BycridImgInfo> mImgInfoList;
	//>2014/03/08-Task_34590-xiangrongji
	
	private LocationCreater mLocationCreater;

	private LocationManager mLocationManager;
	private TrackThread mTrackThread;
	private BycridTracker mBycridTracker;
	private RidingMainHandler mRidingMainHandler;
	
	private final GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			mLocationManager.getGpsStatus(null);
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				BycLog.i(TAG, "onGpsStatusChanged(): GPS started");
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				BycLog.i(TAG, "onGpsStatusChanged(): GPS first fix");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				BycLog.i(TAG, "onGpsStatusChanged(): GPS satellite status");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				BycLog.i(TAG, "onGpsStatusChanged(): GPS stopped");
				break;
			}
		}
	};
	private final LocationListener mLocationListener = new LocationListener() {
		public void onLocationChanged(Location location) {

			//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
			if (mBycridTracker.getNodeNum() == 0) {
				startDurationUpdating();

				mRefAltitude = (float) location.getAltitude();
				mMinDeltaAlt = 0;
				mMaxDeltaAlt = 0;
			}
			
			mCurrSpeed = location.getSpeed();
			
			updateAltitude((float) location.getAltitude() - mRefAltitude);
			//>2014/03/06-Task_34540-xiangrongji
			
			if (mTrackThread != null) {
				mTrackThread.updateLocation(location);
			}
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			//toast("Status Changed");
			//updateGpsStatus(status);
			BycLog.d(TAG, "location listener onStatusChanged");
		}

		public void onProviderEnabled(String provider) {
			//toast("Provider Enabled");
		}

		public void onProviderDisabled(String provider) {
			//toast("Provider Disabled");
			//updateWithNewLocation(null);
		}
	};
	//>2014/02/26-Task_34228-xiangrongji

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bycrid_riding);
		
		mCurrTimeTextView = (TextView) findViewById(R.id.bycrid_riding_curr_time);
		mDistanceValTextView = (TextView) findViewById(R.id.bycrid_riding_distance_val);
		mDurationValTextView = (TextView) findViewById(R.id.bycrid_riding_duration_val);
		mAltitudeIcon = (ImageView) findViewById(R.id.bycrid_riding_altitude_icon);
		mAltitudeValTextView = (TextView) findViewById(R.id.bycrid_riding_altitude_val);
		mVelocityValTextView = (TextView) findViewById(R.id.bycrid_riding_velocity_val);
		
		mTimeTickReceiver = new TimeTickReceiver();
		
		//<2014/02/25-Task_34211-ZhiweiWang,[HealthyLiving][BycicleRide] BycicleRide app UI Modify.
		//mOptsView = (OptionsView) findViewById(R.id.bycrid_riding_opts);
		//initOptions();
		//<2014/03/08-Task_34628-xiangrongji, [AAP1302][BycicleRiding]disable zoomout
		mOptBtnFinish = (Button)findViewById(R.id.bycrid_finish);
		mOptBtnFinish.setOnClickListener(mOptBtnFinishClickListener);
		mOptBtnCapture = (Button)findViewById(R.id.bycrid_capture);
		mOptBtnCapture.setOnClickListener(mOptBtnCaptureClickListener);
		mOptBtnZoomout = (Button)findViewById(R.id.bycrid_zoomout);
		//mOptBtnZoomout.setOnClickListener(mOptBtnZoomoutClickListener);
		mOptBtnZoomout.setOnClickListener(null);
		//>2014/03/08-Task_34628-xiangrongji
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		slidingImageView = (ImageView)findViewById(R.id.bycrid_riding_option_sliding_handle_message_logo);
		slidingImageView.setImageResource(R.drawable.riding_upward_icon);
		mSlidingDrawer = (SlidingDrawer)findViewById(R.id.bycrid_riding_option_sliding);
		//>2014/03/06-Task_34540-xiangrongji
		mSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
			
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				slidingImageView.setImageResource(R.drawable.riding_downward_icon);
			}
		});
		mSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				slidingImageView.setImageResource(R.drawable.riding_upward_icon);
			}
		});
		//>2014/02/25-Task_34211-ZhiweiWang.
		
		//<2014/02/26-Task_34228-xiangrongji, [AAP1302][BycicleRiding]bycicle riding tracker
		mTrack = (ImageView) findViewById(R.id.bycrid_riding_track);
		
		mLocationCreater = new LocationCreater();
		
		mRidingMainHandler = new RidingMainHandler();

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.addGpsStatusListener(gpsListener);
		if(!isOPen()){
			showAskOpenGPSDialog();
		}
		
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		mBycridTracker = new BycridTracker(Double.NaN, Double.NaN, null, 0);
		//>2014/03/03-Task_34403-xiangrongji

		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		isUpdating = false;
		mLastDurationSec = -1;
		updateDurationAndView(0);
		
		mAltitudeValTextView.setText("0.0");
		
		mDistanceValTextView.setText("0.00");
		mVelocityValTextView.setText("0.00");
		//>2014/03/06-Task_34540-xiangrongji

		//startRiding();
		mRidingMainHandler.sendEmptyMessage(RidingMainHandler.RIDING_MSG_ID_START_RIDING);
	}
	
	public void startRiding() {
		//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
		mDate = System.currentTimeMillis();
		BycridDataHelper.getInstance().refreshTmpFolder();
		mImgInfoList = new ArrayList<BycridImgInfo>();
		//>2014/03/08-Task_34590-xiangrongji
		
		mTrackThread = new TrackThread();
		mTrackThread.start();

		mBycridTracker.clear();
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		mBycridTracker.initBitmapSize(484, 377, 8);
		//>2014/03/03-Task_34403-xiangrongji
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, mLocationListener);
		
		//For Test
		//startDurationUpdating();
		//mLocationCreater.start();
	}
	public void finishRiding() {
	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		DialogInterface.OnClickListener confirm_listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					//save data
					saveData();
					//go back
					finish();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					finish();
					break;
				default:
				}
			}
			
		};
		Builder builder = new Builder(this)
			.setTitle(R.string.bycrid_title_confirm)
			.setMessage(R.string.bycrid_save_when_finish_confirm)
			.setPositiveButton(R.string.bycrid_yes, confirm_listener)
			.setNegativeButton(R.string.bycrid_no, confirm_listener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
			if (mSlidingDrawer.isOpened()) {
				mSlidingDrawer.close();
			} else {
				finishRiding();
			}
			return true;
			//>2014/03/06-Task_34540-xiangrongji
		}
		return super.onKeyDown(keyCode, event);
		//>2014/03/03-Task_34403-xiangrongji
	}
	
	public boolean isOPen() { 
		boolean gps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
	    boolean network = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
	    if (gps || network) { 
	        return true; 
	    } 
	    return false; 
	}
	private void showAskOpenGPSDialog() {
		DialogInterface.OnClickListener confirm_listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent GPSIntent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS"); 
					startActivity(GPSIntent);
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				default:
				}
			}
			
		};
		Builder builder = new Builder(this)
			.setTitle(R.string.bycrid_title_confirm)
			.setMessage(R.string.bycrid_open_gps_data_confirm)
			.setPositiveButton(R.string.bycrid_ok, confirm_listener)
			.setNegativeButton(R.string.bycrid_cancel, confirm_listener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	//>2014/02/26-Task_34228-xiangrongji
	
	//<2014/02/25-Task_34211-ZhiweiWang,[HealthyLiving][BycicleRide] BycicleRide app UI Modify.
	 private OnClickListener mOptBtnFinishClickListener = new OnClickListener() {	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//<2014/02/26-Task_34228-xiangrongji, [AAP1302][BycicleRiding]bycicle riding tracker
				finishRiding();
				//>2014/02/26-Task_34228-xiangrongji
			}
		};
		private OnClickListener mOptBtnCaptureClickListener = new OnClickListener() {	
			public void onClick(View v) {
				//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
				takePhoto();
				//>2014/03/08-Task_34590-xiangrongji
			}
		};
		private OnClickListener mOptBtnZoomoutClickListener = new OnClickListener() {	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), BycridTrackViewerActivity.class);
				startActivity(intent);
			}
		};
		//>2014/02/25-Task_34211-ZhiweiWang.
	
	@Override
	protected void onResume() {
		BycLog.d(TAG, "onResume");
		super.onResume();
		updateCurrTimeTextView();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(mTimeTickReceiver, filter);
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		if (isUpdating) {
			updateDurationAndView(SystemClock.elapsedRealtime() - mStartTimeMills);
			requestNextDurationUpdate(SystemClock.elapsedRealtime() - mStartTimeMills);
		}
		//>2014/03/06-Task_34540-xiangrongji
	}
	@Override
	protected void onPause() {
		BycLog.d(TAG, "onPause");
		super.onPause();
		unregisterReceiver(mTimeTickReceiver);

		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		pauseDurationUpdating();
		//>2014/03/06-Task_34540-xiangrongji
	}
	
	//<2014/02/26-Task_34228-xiangrongji, [AAP1302][BycicleRiding]bycicle riding tracker
	@Override
	protected void onDestroy() {
		BycLog.d(TAG, "onDestroy.");
		if (mTrackThread != null) {
			mTrackThread.quit();
			mTrackThread = null;
		}
		if (mLocationManager != null){
			mLocationManager.removeUpdates(mLocationListener);
			mLocationManager.removeGpsStatusListener(gpsListener);
		}
		if (mLocationCreater != null) {
			mLocationCreater.quit();
		}
		
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		cancelDurationUpdating();
		//>2014/03/06-Task_34540-xiangrongji
		
		super.onDestroy();
	}
	//>2014/02/26-Task_34228-xiangrongji
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		BycLog.d(TAG, "onActivityResult, requestCode = " + requestCode);
		switch (requestCode) {
		case BYCRID_RIDING_INTENT_REQ_CAPTURE:
			if (data != null) {
				Uri uri = data.getData();
				String path = uri.getPath();
				File file = new File(path);
				String name = file.getName();
				
				BycridImgInfo info = new BycridImgInfo();
				info.name = name;
				info.index = mBycridTracker.getNodeNum() - 1;
				if (info.index > 0) {
					float[] node = mBycridTracker.getNode(info.index);
					info.longitude = node[INDEX_ENUM.LONGITUDE.ordinal()];
					info.latitude = node[INDEX_ENUM.LATITUDE.ordinal()];
				}
				mImgInfoList.add(info);
			}
			break;
		default:
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	//>2014/03/08-Task_34590-xiangrongji
	
	private void updateCurrTimeTextView() {
		long time = System.currentTimeMillis();
		//<2014/02/25-Task_34211-ZhiweiWang,[HealthyLiving][BycicleRide] BycicleRide app UI Modify.
		//CharSequence strTime =  DateFormat.format("yyyy-MM-dd kk:mm", time);
		CharSequence strTime =  DateFormat.format("yyyy/MM/dd kk:mm", time);
		//>2014/02/25-Task_34211-ZhiweiWang.
		mCurrTimeTextView.setText(strTime);
	}
	
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private void updateDurationAndView(long currDurationMills) {
		//update duration text view
		long sec = currDurationMills / 1000;
		if (sec > mLastDurationSec) {
			long min = sec / 60;
			long hour = min / 60;
			sec -= min * 60;
			min -= hour * 60;
			String str = String.format("%d:%02d:%02d", hour, min, sec);
			mDurationValTextView.setText(str);
			mLastDurationSec = sec;
		}
		mDuration = currDurationMills;
	}
	private void requestNextDurationUpdate(long currDurationMills) {
		//send msg for next update after one second
		long mills = 1001 + (currDurationMills / 1000) * 1000 - currDurationMills;
		mRidingMainHandler.sendEmptyMessageDelayed(RidingMainHandler.RIDING_MSG_ID_UPDATE_DURATION, mills);
	}
	private void startDurationUpdating() {
		isUpdating = true;
		mStartTimeMills = SystemClock.elapsedRealtime();
		requestNextDurationUpdate(0);
	}
	private void pauseDurationUpdating() {
		mRidingMainHandler.removeMessages(RidingMainHandler.RIDING_MSG_ID_UPDATE_DURATION);
	}
	private void cancelDurationUpdating() {
		isUpdating = false;
		mRidingMainHandler.removeMessages(RidingMainHandler.RIDING_MSG_ID_UPDATE_DURATION);
	}
	
	private void updateAltitude(float dalt) {
		mCurrDeltaAlt = dalt;
		if (dalt > mMaxDeltaAlt) {
			mMaxDeltaAlt = dalt;
		}
		if (dalt < mMinDeltaAlt) {
			mMinDeltaAlt = dalt;
		}
		
	}
	private void updateAltitudeTextView() {
		float val;
		if (mCurrDeltaAlt >= 0) {
			val = mCurrDeltaAlt;
			mAltitudeIcon.setImageResource(R.drawable.up_arrow);
		} else {
			val = -mCurrDeltaAlt;
			mAltitudeIcon.setImageResource(R.drawable.down_arrow);
		}
		String str = String.format("%.1f", val);
		mAltitudeValTextView.setText(str);
	}
	//>2014/03/06-Task_34540-xiangrongji

	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	private void saveData() {
		BycridData data = BycridDataHelper.getInstance().createData();
		//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
		data.date = mDate;
		//>2014/03/08-Task_34590-xiangrongji
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		data.distance = mBycridTracker.getDistance();
		data.duration = mDuration;
		data.altitude_rise = mMaxDeltaAlt;
		data.altitude_drop = -mMinDeltaAlt;
		//>2014/03/06-Task_34540-xiangrongji
		data.reference_longitude = mBycridTracker.getRefLongitude();
		data.reference_latitude = mBycridTracker.getRefLatitude();
		data.track_len = mBycridTracker.getNodeNum();
		data.track_list = mBycridTracker.getTrackList();
		
		//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
		data.img_info_list = mImgInfoList;
		
		BycridDataHelper.getInstance().save(data);
		//>2014/03/08-Task_34590-xiangrongji
	}
	//>2014/03/03-Task_34403-xiangrongji
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	private void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String name = generatePhotoName();
		String path = BycridDataHelper.getTmpFilePathByName(name);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
		startActivityForResult(intent, BYCRID_RIDING_INTENT_REQ_CAPTURE);
	}
	private String generatePhotoName() {
		String name = "capture";
		
		File file = new File(BycridDataHelper.getTmpFilePathByName(name + ".png"));
		int index = 1;
		while (file.exists()) {
			file = new File(BycridDataHelper.getTmpFilePathByName(name + "_" + index + ".png"));
			index++;
		}
		return file.getName();
	}
	//>2014/03/08-Task_34590-xiangrongji

	private class TimeTickReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateCurrTimeTextView();
		}
		
	}
	
	//<2014/02/26-Task_34228-xiangrongji, [AAP1302][BycicleRiding]bycicle riding tracker
	private class RidingMainHandler extends Handler {
		public final static int RIDING_MSG_ID_REDRAW_TRACK = 1;
		public final static int RIDING_MSG_ID_START_RIDING = 2;
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		public final static int RIDING_MSG_ID_UPDATE_DURATION = 3;
		//>2014/03/06-Task_34540-xiangrongji
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RIDING_MSG_ID_START_RIDING:
				startRiding();
				break;
			//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
			case RIDING_MSG_ID_REDRAW_TRACK:
				{
				//String str = (String) DateFormat.format("kk:mm:ss", System.currentTimeMillis());
				//BycLog.d(TAG, str + ", set track bitmap");
				double distance = mBycridTracker.getDistance();
				String str = String.format("%.2f", distance/1000);
				mDistanceValTextView.setText(str);
				
				str = String.format("%.2f", mCurrSpeed * 3.6);
				mVelocityValTextView.setText(str);
				
				updateAltitudeTextView();
				
				mTrack.setImageBitmap((Bitmap) msg.obj);
				}
				break;
			case RIDING_MSG_ID_UPDATE_DURATION:
				RidingMainHandler.this.removeMessages(RIDING_MSG_ID_UPDATE_DURATION);
				updateDurationAndView(SystemClock.elapsedRealtime() - mStartTimeMills);
				requestNextDurationUpdate(SystemClock.elapsedRealtime() - mStartTimeMills);
				break;
			//>2014/03/06-Task_34540-xiangrongji
			}
			super.handleMessage(msg);
		}
	}
	
	private class TrackThread extends Thread {
		private final static int TRACK_THREAD_MSG_ID_UPDATE_LOCATION = 1;
		
		TrackHandler mTrackHandler;
		Looper mTrackLooper;
		
		@Override
		public void run() {
			super.run();
			Looper.prepare();
			
			mTrackLooper = Looper.myLooper();
			mTrackHandler = new TrackHandler();
			
			Looper.loop();
		}
		
		public void quit() {
			mTrackHandler.removeMessages(TRACK_THREAD_MSG_ID_UPDATE_LOCATION);
			mTrackLooper.quit();
		}

		public void updateLocation(Location location) {
			if (isAlive() && mTrackHandler != null) {
				Message msg = mTrackHandler.obtainMessage(TRACK_THREAD_MSG_ID_UPDATE_LOCATION, location);
				mTrackHandler.sendMessage(msg);
			} else {
				BycLog.e(TAG, "updateLocation err: isAlive=" + isAlive() + ", mTrackHandler=" + mTrackHandler);
			}
		}
		
		public class TrackHandler extends Handler {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case TRACK_THREAD_MSG_ID_UPDATE_LOCATION:
					{
					BycLog.d(TAG, "update location");
					Location location = (Location) msg.obj;
					
					if (!hasMessages(TRACK_THREAD_MSG_ID_UPDATE_LOCATION)) {
						BycLog.d(TAG, "no more msg of update location, so redraw");
						//append node into data, and redraw track
						Bitmap bitmap = mBycridTracker.updateTrack(location, LNG_MODE.NORMAL, true);
						mRidingMainHandler.sendMessage(
								mRidingMainHandler.obtainMessage(RidingMainHandler.RIDING_MSG_ID_REDRAW_TRACK, bitmap));
					} else {
						BycLog.d(TAG, "has msg of update location");
						//append node into data, not redraw
						mBycridTracker.updateTrack(location, LNG_MODE.NORMAL, false);
					}
					break;
					}
				default:
				}
				super.handleMessage(msg);
				if (!hasMessages(TRACK_THREAD_MSG_ID_UPDATE_LOCATION)) {
					BycLog.d(TAG, "after super.handleMessage(msg), no more msg of update location");
				} else {
					BycLog.d(TAG, "after super.handleMessage(msg), has msg of update location");
				}
			}
		}
	}

	private class LocationCreater extends Thread {
		private boolean running = false;
		private final int TIME_STAMP_MS = 500;
		private double time;
		private double lng;
		private double lat;
		
		public void quit() {
			running = false;
		}
		@Override
		public void run() {
			super.run();
			running = true;
			time = 0;
			try {
				sleep(TIME_STAMP_MS);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (running) {
				try {
					time += (double)TIME_STAMP_MS / 1000;
					/*if (time >= 300) {
						quit();
					}*/
					makeHelixTrack();
					//makeLinearTrack();
					
					Location location = new Location("gps");
					location.setLongitude(lng);
					location.setLatitude(lat);
					location.setAltitude(12 * Math.sin(time));
					location.setSpeed(6);
					mLocationListener.onLocationChanged(location);
					sleep(TIME_STAMP_MS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		private void makeSinTrack() {
			lng = 180 + 8.1 / 1000.0 * (time / 60);
			if (lng > 180) lng -= 360;
			else if (lng <= -180) lng += 360;
			lat = 32 + 6.2 / 1000.0 * Math.sin(time * 2 * Math.PI / 30);
		}
		
		private final double helix_start_radius = 0;
		private final double helix_start_theta = Math.PI / 2;
		private final double helix_radial_velocity = 8.1 / 1000 / 300;
		private final double helix_angular_velocity = 2 * Math.PI / 60;
		private void makeHelixTrack() {
			double radius = helix_start_radius + helix_radial_velocity * time;
			double theta = helix_start_theta + helix_angular_velocity * time;
			lng = 180 + radius * Math.cos(theta);
			lat = 32 + radius * Math.sin(theta);
		}
		private final double linear_all_lng = 1;
		private final double linear_all_lat = 0;
		private final double linear_all_time = 60;
		private void makeLinearTrack() {
			lng = 180 + 0.001 * time;
			lat = 32;
		}
	}
	//>2014/02/26-Task_34228-xiangrongji
}
//>2014/02/21-Task_34127-xiangrongji
