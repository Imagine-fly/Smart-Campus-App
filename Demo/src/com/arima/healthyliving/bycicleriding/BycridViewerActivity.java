package com.arima.healthyliving.bycicleriding;

import java.io.File;
import java.util.ArrayList;

import com.arima.healthyliving.R;
import com.arima.healthyliving.Util.ScreenShotForShare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

//<2014/02/21-Task_34127-xiangrongji, [AAP1302][BycicleRiding]upload codes for Bycicle Riding screens
public class BycridViewerActivity extends Activity {
	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	private final static String TAG = "BycridViewer";
	private final static int LOADER_ID_BYCRID_VIEWER = 0x2001;
	
	private BycridTracker mBycridTracker;
	//<2014/03/06-Task_34548-xiangrongji, [AAP1302][BycicleRiding]update share function
	private ViewerMainHandler mViewerMainHandler;
	//>2014/03/06-Task_34548-xiangrongji
	
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private TextView mDateTextView;
	private TextView mDistanceValTextView;
	private TextView mDurationValTextView;
	private TextView mAltitudeUpValTextView;
	private TextView mAltitudeDownValTextView;
	private TextView mVelocityValTextView;
	private ImageView mTrack;
	
	private Button mOptBtnShare = null;
	private Button mOptBtnDelete = null;
	private SlidingDrawer mSlidingDrawer = null;
	private ImageView slidingImageView = null;
	
	private String mRecordName;
	protected double mDistance;
	protected long mDuration;
	protected long mDate;
	protected double mAvgSpeed;
	protected double mAltitudeRise;
	protected double mAltitudeDrop;
	//>2014/03/06-Task_34540-xiangrongji
	
	private LoaderManager.LoaderCallbacks<BycridData> mViewerLoaderListener =
			new LoaderManager.LoaderCallbacks<BycridData>() {

		@Override
		public Loader<BycridData> onCreateLoader(int id, Bundle args) {
			//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
			String record_name = args.getString("record_name");
			return new BycridLoader(getApplicationContext(), record_name);
			//>2014/03/06-Task_34540-xiangrongji
		}

		@Override
		public void onLoadFinished(Loader<BycridData> loader, BycridData data) {
			// TODO Auto-generated method stub
			getLoaderManager().destroyLoader(LOADER_ID_BYCRID_VIEWER);
			if (data != null) {
				//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
				mDistance = data.distance;
				mDuration = data.duration;
				mDate = data.date;
				//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
				if (mDuration != 0) {
					mAvgSpeed = mDistance * 1000 / mDuration;
				} else {
					mAvgSpeed = 0;
				}
				//>2014/03/08-Task_34590-xiangrongji
				mAltitudeRise = data.altitude_rise;
				mAltitudeDrop = data.altitude_drop;
				
				//date
				updateDateView();
				//distance
				updateDistanceView();
				//duration
				updateDurationView();
				//altitude rise and drop
				updateAltitudeView();
				//average speed
				updateAvgSpeedView();
				//>2014/03/06-Task_34540-xiangrongji

				mBycridTracker = new BycridTracker(data.reference_longitude, data.reference_latitude, (ArrayList<float[][]>) data.track_list, (int)data.track_len);
				mBycridTracker.initBitmapSize(484, 377, 8);
				Bitmap bitmap = mBycridTracker.redrawTrack();
				mTrack.setImageBitmap(bitmap);
				
			}
		}

		@Override
		public void onLoaderReset(Loader<BycridData> arg0) {
			// TODO Auto-generated method stub
		}
		
	};
	//>2014/03/03-Task_34403-xiangrongji
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private OnClickListener mOptBtnShareClickListener = new OnClickListener() {	
		public void onClick(View v) {
			//<2014/03/06-Task_34548-xiangrongji, [AAP1302][BycicleRiding]update share function
			mViewerMainHandler.sendEmptyMessageDelayed(ViewerMainHandler.VIEWER_MSG_ID_ACTION_SHARE, 300);
			mSlidingDrawer.close();
			//>2014/03/06-Task_34548-xiangrongji
		}
	};
	private OnClickListener mOptBtnDeleteClickListener = new OnClickListener() {	
		public void onClick(View v) {
			deleteRecord();
		}
	};
	//>2014/03/06-Task_34540-xiangrongji
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		mRecordName = getIntent().getStringExtra("record_name");
		//>2014/03/03-Task_34403-xiangrongji
		//Toast.makeText(getApplicationContext(), "view history of " + position, Toast.LENGTH_SHORT).show();
		//<2014/02/25-Task_34211-ZhiweiWang,[HealthyLiving][BycicleRide] BycicleRide app UI Modify.
		setContentView(R.layout.bycrid_viewer);
		//setContentView(R.layout.bycrid_riding);
		//>2014/02/25-Task_34211-ZhiweiWang.
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		mDateTextView = (TextView) findViewById(R.id.bycrid_viewer_date);
		mDistanceValTextView = (TextView) findViewById(R.id.bycrid_viewer_distance_val);
		mDurationValTextView = (TextView) findViewById(R.id.bycrid_viewer_duration_val);
		mAltitudeUpValTextView = (TextView) findViewById(R.id.bycrid_viewer_altitude_up_val);
		mAltitudeDownValTextView = (TextView) findViewById(R.id.bycrid_viewer_altitude_down_val);
		mVelocityValTextView = (TextView) findViewById(R.id.bycrid_viewer_velocity_val);
		mTrack = (ImageView) findViewById(R.id.bycrid_viewer_track);
		
		//<2014/03/06-Task_34548-xiangrongji, [AAP1302][BycicleRiding]update share function
		mViewerMainHandler = new ViewerMainHandler();
		//>2014/03/06-Task_34548-xiangrongji
		
		setupOptionsSliding();
		
		Bundle bundle = new Bundle();
		bundle.putString("record_name", mRecordName);
		//>2014/03/06-Task_34540-xiangrongji
		getLoaderManager().initLoader(LOADER_ID_BYCRID_VIEWER, bundle, mViewerLoaderListener);
		//>2014/03/03-Task_34403-xiangrongji
	}
	
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mSlidingDrawer.isOpened()) {
				mSlidingDrawer.close();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setupOptionsSliding() {
		mOptBtnShare = (Button)findViewById(R.id.bycrid_viewer_share);
		mOptBtnShare.setOnClickListener(mOptBtnShareClickListener);
		mOptBtnDelete = (Button)findViewById(R.id.bycrid_viewer_delete);
		mOptBtnDelete.setOnClickListener(mOptBtnDeleteClickListener);
		slidingImageView = (ImageView)findViewById(R.id.bycrid_viewer_option_sliding_handle_message_logo);
		slidingImageView.setImageResource(R.drawable.riding_upward_icon);
		mSlidingDrawer = (SlidingDrawer)findViewById(R.id.bycrid_viewer_option_sliding);
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
	}
	
	private void updateDateView() {
		String str = (String) DateFormat.format("yyyy/MM/dd", mDate);
		mDateTextView.setText(str);
	}
	private void updateDistanceView() {
		String str = String.format("%.2f", mDistance / 1000); // km
		mDistanceValTextView.setText(str);

	}

	private void updateAltitudeView() {
		String str = String.format("%.1f", mAltitudeRise);
		mAltitudeUpValTextView.setText(str);
		
		str = String.format("%.1f", mAltitudeDrop);
		mAltitudeDownValTextView.setText(str);
	}

	private void updateAvgSpeedView() {
		double speed = mAvgSpeed * 3.6;
		String str = String.format("%.1f", speed); // km/h
		mVelocityValTextView.setText(str);
	}

	private void updateDurationView() {
		long sec = mDuration / 1000;
		long min = sec / 60;
		long hour = min / 60;
		sec -= min * 60;
		min -= hour * 60;
		String str = String.format("%d:%02d:%02d", hour, min, sec);
		mDurationValTextView.setText(str);
	}
	
	private void deleteRecord() {
		DialogInterface.OnClickListener confirm_listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					BycridDataHelper.getInstance().delete(mRecordName);
					//go back
					finish();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				default:
				}
			}
				
		};
		Builder builder = new Builder(this)
			.setTitle(R.string.bycrid_title_confirm)
			.setMessage(R.string.bycrid_delete_record_confirm)
			.setPositiveButton(R.string.bycrid_yes, confirm_listener)
			.setNegativeButton(R.string.bycrid_no, confirm_listener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	//>2014/03/06-Task_34540-xiangrongji
	//<2014/03/06-Task_34548-xiangrongji, [AAP1302][BycicleRiding]update share function
	private void startShareAction() {
		String share_name = "_bycicle_" + mRecordName + ".png";
		
		Window window = getWindow();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		File file = ScreenShotForShare.GetScreenShotFile(window, width, height, share_name); 
		if (file != null)
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("text/image/png");
		shareIntent.putExtra(Intent.EXTRA_TEXT, share_name);
    	startActivity(Intent.createChooser(shareIntent, getString(R.string.share_text))); 
	}
	private class ViewerMainHandler extends Handler {
		public final static int VIEWER_MSG_ID_ACTION_SHARE = 1;
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case VIEWER_MSG_ID_ACTION_SHARE:
				startShareAction();
				break;
			}
			super.handleMessage(msg);
		}
	}
	//>2014/03/06-Task_34548-xiangrongji
}
//>2014/02/21-Task_34127-xiangrongji
