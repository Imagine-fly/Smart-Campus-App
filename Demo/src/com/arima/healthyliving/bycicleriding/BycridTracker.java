package com.arima.healthyliving.bycicleriding;

import java.util.ArrayList;
import java.util.List;

import com.arima.healthyliving.bycicleriding.TrackDataHelper.INDEX_ENUM;
import com.arima.healthyliving.bycicleriding.TrackDataHelper.LNG_MODE;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;

public class BycridTracker {
	private final static String TAG = "BycridTracker";
	
	private int mWidth;
	private int mHeight;
	private int mPadding;
	private double mMinLongitude; // compared to reference longitude
	private double mMaxLongitude; // compared to reference longitude
	private double mMinLatitude;
	private double mMaxLatitude;
	private int lastX;
	private int lastY;
	
	private TrackDataHelper mDataHelper;
	
	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	public BycridTracker(double ref_lng, double ref_lat, ArrayList<float[][]> track_list, int node_num) {
		mDataHelper = new TrackDataHelper(ref_lng, ref_lat, track_list, node_num);
		initTrackRange();
		if (node_num > 0) {
			updateRangeIfNeed();
		}
	}
	//>2014/03/03-Task_34403-xiangrongji
	
	public void clear() {
		mDataHelper.clear();
	}
	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	public void initBitmapSize(int width, int height, int padding) {
		mWidth = width;
		mHeight = height;
		mPadding = padding;
	}
	private void initTrackRange() {
		//<2014/02/27-Task_34316-xiangrongji, [AAP1302][BycicleRiding]modify initial area of track
		mMaxLongitude = 5.0 / 1000.0;
		mMinLongitude = -5.0 / 1000.0;
		mMaxLatitude = 5.0 / 1000.0;
		mMinLatitude = -5.0 / 1000.0;
		//>2014/02/27-Task_34316-xiangrongji
	}
	
	public double getRefLongitude() {
		return mDataHelper.getRefLongitude();
	}
	public double getRefLatitude() {
		return mDataHelper.getRefLatitude();
	}
	public int getNodeNum() {
		return mDataHelper.getNodeNum();
	}
	public List<float[][]> getTrackList() {
		return mDataHelper.getTrackList();
	}
	//>2014/03/03-Task_34403-xiangrongji
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	public float[] getNode(int index) {
		return mDataHelper.getNode(index);
	}
	//>2014/03/08-Task_34590-xiangrongji
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	public float getDistance() {
		return mDataHelper.getDistance();
	}
	//>2014/03/06-Task_34540-xiangrongji
	
	public Bitmap updateTrack(Location node, LNG_MODE mode, boolean redraw) {
		if (mDataHelper.getNodeNum() == 0) {
			//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
			//initRange(node);
			mDataHelper.setReferenceLngLatValue(node.getLongitude(), node.getLatitude());
			//>2014/03/03-Task_34403-xiangrongji
		}
		
		mDataHelper.append(node, mode);
		updateRangeIfNeed();
		
		if (redraw) {
			return redrawTrack();
		} else {
			return null;
		}
	}
	
	public Bitmap redrawTrack() {
		BycLog.d(TAG, "redraw track");
		Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		
		canvas.save();
		Paint paint = new Paint();
		paint.setColor(0xFF00FF00);
		
		//start point
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		if (mDataHelper.getNodeNum()> 0) {
			float[] nodebuf = mDataHelper.getNode(0);
			int x = (int) computeX(nodebuf[INDEX_ENUM.LONGITUDE.ordinal()]);
			int y = (int) computeY(nodebuf[INDEX_ENUM.LATITUDE.ordinal()]);
			canvas.drawCircle(x, y, 3, paint);
			lastX = x;
			lastY = y;
			
			//in process
			for (int i = 1; i < mDataHelper.getNodeNum() - 1; i++) {
				nodebuf = mDataHelper.getNode(i);
				x = (int) computeX(nodebuf[INDEX_ENUM.LONGITUDE.ordinal()]);
				y = (int) computeY(nodebuf[INDEX_ENUM.LATITUDE.ordinal()]);
				if ((x != lastX) || (y != lastY)) {
					canvas.drawLine(lastX, lastY, x, y, paint);
					lastX = x;
					lastY = y;
				}
			}
			
			//end point
			if (mDataHelper.getNodeNum() > 1) {
				nodebuf = mDataHelper.getNode(mDataHelper.getNodeNum() - 1);
				x = (int) computeX(nodebuf[INDEX_ENUM.LONGITUDE.ordinal()]);
				y = (int) computeY(nodebuf[INDEX_ENUM.LATITUDE.ordinal()]);
				canvas.drawLine(lastX, lastY, x, y, paint);
				canvas.drawCircle(x, y, 3, paint);
				lastX = x;
				lastY = y;
			}
		}
		//>2014/03/03-Task_34403-xiangrongji
		
		canvas.restore();
		return bitmap;
	}
	
	private void updateRangeIfNeed() {
		double scale_lng = (mDataHelper.getMaxLongitude() - mDataHelper.getMinLongitude()) / (mMaxLongitude - mMinLongitude);
		double scale_lat = (mDataHelper.getMaxLatitude() - mDataHelper.getMinLatitude()) / (mMaxLatitude - mMinLatitude);
		
		if (scale_lng < 1) scale_lng = 1;
		if (scale_lat < 1) scale_lat = 1;
		
		double scale = scale_lng > scale_lat ? scale_lng : scale_lat;
		double w_lng = (mMaxLongitude - mMinLongitude) * scale;
		double h_lat = (mMaxLatitude - mMinLatitude) * scale;
		
		double center_lng = (mDataHelper.getMinLongitude() + mDataHelper.getMaxLongitude()) / 2;
		double center_lat = (mDataHelper.getMinLatitude() + mDataHelper.getMaxLatitude()) / 2;
		mMinLongitude = center_lng - w_lng / 2;
		mMaxLongitude = center_lng + w_lng / 2;
		mMinLatitude = center_lat - h_lat / 2;
		mMaxLatitude = center_lat + h_lat / 2;
	}
	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	private double computeX(float lng) {
		//return  (double)mWidth * (lng - mMinLongitude) / (mMaxLongitude - mMinLongitude);
		return mPadding + (mWidth - 2 * mPadding) * (lng - mMinLongitude) / (mMaxLongitude - mMinLongitude);
	}
	private double computeY(float lat) {
		//return (double)mHeight * (lat - mMinLatitude) / (mMaxLatitude - mMinLatitude);
		return mHeight - mPadding - (mHeight - 2 * mPadding) * (lat - mMinLatitude) / (mMaxLatitude - mMinLatitude);
	}
	//>2014/03/03-Task_34403-xiangrongji
	
}
