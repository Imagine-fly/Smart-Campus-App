package com.arima.healthyliving.voice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTHelper extends SQLiteOpenHelper {
	public static final String CREATE_USERDATA="create table userData(" +"id integer primary key autoincrement,"+"phone text,"+"password text,"+"yuanxi text,"+"nianfen text,"+"xueli text,"+"zhiwu integer,"+"xuehao text,"+"sign text,"+"date text,"+"time text)";
	


	private Context mContext;


	public DBTHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mContext=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USERDATA);
		
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
