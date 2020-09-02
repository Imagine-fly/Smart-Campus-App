package com.arima.healthyliving.bycicleriding;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

public class TrackDataHelper {
	
	/*public class TrackNode {
		double longitude; // 經度
		double latitude; // 緯度
		//double altitude;
	}*/
	public static enum INDEX_ENUM {
		LONGITUDE, // 經度
		LATITUDE, // 緯度
	}
	public static enum LNG_MODE {
		NORMAL, //not deleted with ref
		REF,	//has been deleted with ref
	}

	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	//> modify double to float
	private ArrayList<float[][]> mBufList;
	private int mNodeNum;
	private double mRefLongitude = Double.NaN; //reference longitude
	private double mRefLatitude = Double.NaN; //reference latitude
	
	private float mMaxLongitude = Float.NEGATIVE_INFINITY;
	private float mMaxLatitude  = Float.NEGATIVE_INFINITY;
	private float mMinLongitude = Float.POSITIVE_INFINITY;
	private float mMinLatitude  = Float.POSITIVE_INFINITY;

	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private DistanceCompute mDistanceCompute;
	//>2014/03/06-Task_34540-xiangrongji

	/*
	 * when no data, the default ref_lng and ref_lat should be Double.Nan, track_list should be null,
	 * node_num should be 0
	 */
	public TrackDataHelper(double ref_lng, double ref_lat, ArrayList<float[][]> track_list, int node_num) {
		mRefLongitude = ref_lng;
		mRefLatitude = ref_lat;
		if (track_list != null) {
			mBufList = track_list;
			mNodeNum = node_num;
			refreshMinMaxLngLat();
		} else {
			mBufList = new ArrayList<float[][]>();
			mNodeNum = 0;
		}
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		mDistanceCompute = new DistanceCompute();
		//>2014/03/06-Task_34540-xiangrongji
	}
	
	public void setReferenceLngLatValue(double ref_lng, double ref_lat) {
		mRefLongitude = ref_lng;
		mRefLatitude = ref_lat;
	}
	public void append(double[][] source, LNG_MODE mode) {
		int leave_len = addOnceInternal(source, 0, mode);
		while (leave_len > 0) {
			leave_len = addOnceInternal(source, source.length - leave_len, mode);
		}
	}
	public void append(double[] node, LNG_MODE mode) {
		if (mNodeNum >= mBufList.size() * 1024) {
			mBufList.add(new float[1024][INDEX_ENUM.values().length]);
		}
		float[][] curr_buffer = mBufList.get(mBufList.size() - 1);
		int curr_offset = mNodeNum - (mBufList.size() - 1) * 1024;
		setNode(curr_buffer[curr_offset], node, mode);
		
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		mDistanceCompute.append(curr_buffer[curr_offset]);
		//>2014/03/06-Task_34540-xiangrongji
	}
	public void append(Location node, LNG_MODE mode) {
		if (mNodeNum >= mBufList.size() * 1024) {
			mBufList.add(new float[1024][INDEX_ENUM.values().length]);
		}
		float[][] curr_buffer = mBufList.get(mBufList.size() - 1);
		int curr_offset = mNodeNum - (mBufList.size() - 1) * 1024;
		setNode(curr_buffer[curr_offset], node, mode);
		
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		mDistanceCompute.append(curr_buffer[curr_offset]);
		//>2014/03/06-Task_34540-xiangrongji
	}
	public float[] getNode(int index) {
		int list_index = index / 1024;
		int offset = index - list_index * 1024;
		return mBufList.get(list_index)[offset];
	}
	
	public void clear() {
		mBufList.clear();
		mNodeNum = 0;
	}
	
	public int getNodeNum() {
		return mNodeNum;
	}
	public double getRefLongitude() {
		return mRefLongitude;
	}
	public double getRefLatitude() {
		return mRefLatitude;
	}
	public float getMaxLongitude() {
		return mMaxLongitude;
	}
	public float getMinLongitude() {
		return mMinLongitude;
	}
	public float getMaxLatitude() {
		return mMaxLatitude;
	}
	public float getMinLatitude() {
		return mMinLatitude;
	}
	public List<float[][]> getTrackList() {
		return mBufList;
	}
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	public float getDistance() {
		return mDistanceCompute.getDistance();
	}
	//>2014/03/06-Task_34540-xiangrongji
	
	private int addOnceInternal(double[][] source, int offset, LNG_MODE mode) {
		if (mNodeNum >= mBufList.size() * 1024) {
			mBufList.add(new float[1024][INDEX_ENUM.values().length]);
		}
		
		float[][] curr_buffer = mBufList.get(mBufList.size() - 1);
		int len_append = source.length - offset < mBufList.size() * 1024 - mNodeNum ? 
				source.length - offset : mBufList.size() * 1024 - mNodeNum;
		
		int curr_offset = mNodeNum - (mBufList.size() - 1) * 1024;
		for (int i = 0; i < len_append; i++) {
			setNode(curr_buffer[curr_offset + i], source[offset + i], mode);
		}
		
		return source.length - offset - len_append;
	}
	
	public float[] setNode(float[] to, Location node, LNG_MODE mode) {
		double lng = node.getLongitude();
		double lat = node.getLatitude();
		if (LNG_MODE.NORMAL.equals(mode)) {
			lng -= mRefLongitude;
			lat -= mRefLatitude;
		}
		if (lng > 180) lng -= 180;
		else if (lng < -180) lng += 180;
		to[INDEX_ENUM.LONGITUDE.ordinal()] = (float) lng;
		to[INDEX_ENUM.LATITUDE.ordinal()] = (float) lat;
		
		updateMinMax(to);
		mNodeNum++;
		
		return to;
	}
	public float[] setNode(float[] to, double[] from, LNG_MODE mode) {
		double lng = from[INDEX_ENUM.LONGITUDE.ordinal()];
		double lat = from[INDEX_ENUM.LATITUDE.ordinal()];
		if (LNG_MODE.NORMAL.equals(mode)) {
			lng -= mRefLongitude;
			lat -= mRefLatitude;
		}
		if (lng > 180) lng -= 360;
		else if (lng <= -180) lng += 360;

		to[INDEX_ENUM.LONGITUDE.ordinal()] = (float) lng;
		to[INDEX_ENUM.LATITUDE.ordinal()] = (float) lat;

		updateMinMax(to);
		mNodeNum++;
		
		return to;
	}
	
	private void updateMinMax(float[] to) {
		updateMinMaxLongitude(to[INDEX_ENUM.LONGITUDE.ordinal()]);
		updateMinMaxLatitude(to[INDEX_ENUM.LATITUDE.ordinal()]);
	}
	private void updateMinMaxLongitude(float longitude) {
		if (longitude > mMaxLongitude) {
			mMaxLongitude = longitude;
		}
		if (longitude < mMinLongitude) {
			mMinLongitude = longitude;
		}
	}
	private void updateMinMaxLatitude(float latitude) {
		if (latitude > mMaxLatitude) {
			mMaxLatitude = latitude;
		}
		if (latitude < mMinLatitude) {
			mMinLatitude = latitude;
		}
	}
	private void refreshMinMaxLngLat() {
		float[] node;
		
		mMaxLongitude = Float.NEGATIVE_INFINITY;
		mMaxLatitude  = Float.NEGATIVE_INFINITY;
		mMinLongitude = Float.POSITIVE_INFINITY;
		mMinLatitude  = Float.POSITIVE_INFINITY;
		
		for (int i = 0; i < mNodeNum; i++) {
			node = getNode(i);
			updateMinMax(node);
		}
	}

	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private class DistanceCompute {
		private float mLastLongitude;
		private float mLastLatitude;
		private float mDistance;
		private final float MIN_GAP_LNG = 1.0f / 1000 / 111;
		private final float MIN_GAP_LAT = 1.0f / 1000 / 111;
		private final double EARTH_RADIUS = 6371004;
		
		public DistanceCompute() {
			mLastLongitude = 0;
			mLastLatitude = 0;
			mDistance = 0;
		}
		public void append(float[] node) {
			float dlng = node[INDEX_ENUM.LONGITUDE.ordinal()] - mLastLongitude;
			float dlat = node[INDEX_ENUM.LATITUDE.ordinal()] - mLastLatitude;
			if ((dlng < -MIN_GAP_LNG) || (dlng > MIN_GAP_LNG) ||
				(dlat < -MIN_GAP_LAT) || (dlat > MIN_GAP_LAT)) {
				
				float d = (float) compute(
						getRadianByDegree(mRefLatitude + mLastLatitude),
						getRadianByDegree(dlng),
						getRadianByDegree(dlat));
				mDistance += d;
				
				mLastLongitude = node[INDEX_ENUM.LONGITUDE.ordinal()];
				mLastLatitude = node[INDEX_ENUM.LATITUDE.ordinal()];
			}
		}
		public float getDistance() {
			return mDistance;
		}
		private double getRadianByDegree(double degree) {
			return degree * Math.PI / 180;
		}
		/*
		 * the input lat & dlng & dlat should be in radian, not degree
		 */
		private double compute(double lat, double dlng, double dlat) {
			double sin_half_dlng = Math.sin(dlng * 0.5);
			double sin_half_dlat = Math.sin(dlat * 0.5);
			double cos_2lat = Math.cos(2 * dlat);
			double sin_2lat_half_dlat = Math.sin(2 * lat + dlat * 0.5);
			
			double base_val = sin_half_dlat * sin_half_dlat + (1 + cos_2lat) * 0.5 * sin_half_dlng * sin_half_dlng;
			double less_val1 = sin_2lat_half_dlat * sin_half_dlat * sin_half_dlng * sin_half_dlng;
			double less_val2 = sin_half_dlat * sin_half_dlat * sin_half_dlng * sin_half_dlng;
			
			double square_sin_half_angle = base_val - less_val1 - less_val2;
			//protect
			//if (square_sin_half_angle < 0) square_sin_half_angle = 0;
			//else if (square_sin_half_angle > 1) square_sin_half_angle = 1;
			
			double half_angle = Math.asin(Math.sqrt(square_sin_half_angle));
			return EARTH_RADIUS * 2 * half_angle;
			//return (float) (base_val - less_val1 - less_val2);
		}
	}
	//>2014/03/06-Task_34540-xiangrongji
}
