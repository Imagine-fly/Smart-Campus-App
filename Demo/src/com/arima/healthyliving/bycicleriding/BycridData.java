package com.arima.healthyliving.bycicleriding;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class BycridData {
	long date;
	double distance;
	long duration;
	double altitude_rise;
	double altitude_drop;
	double reference_longitude;
	double reference_latitude;
	long track_len;
	List<float[][]> track_list;
	
	int picture_num;
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	String record_name;
	//>2014/03/06-Task_34540-xiangrongji
	
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	List<BycridImgInfo> img_info_list;
	
	public static class BycridImgInfo implements Parcelable {
		String name;
		int index;
		float longitude;
		float latitude;
		public BycridImgInfo(Parcel source) {
			name = source.readString();
			index = source.readInt();
			longitude = source.readFloat();
			latitude = source.readFloat();
		}
		public BycridImgInfo() {
			
		}
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(name);
			dest.writeInt(index);
			dest.writeFloat(longitude);
			dest.writeFloat(latitude);
		}
	    public static final Parcelable.Creator<BycridImgInfo> CREATOR
	        = new Parcelable.Creator<BycridImgInfo>() {
		    public BycridImgInfo createFromParcel(Parcel source) {
		        return new BycridImgInfo(source);
		    }
		    public BycridImgInfo[] newArray(int size) {
		        return new BycridImgInfo[size];
		    }
	    };

	}
	//>2014/03/08-Task_34590-xiangrongji
}
