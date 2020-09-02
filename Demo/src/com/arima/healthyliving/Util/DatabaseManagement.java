package com.arima.healthyliving.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class DatabaseManagement{
	private static final String TAG = "HealtyLivingDatabase";
	private static final String DATABASE_NAME = "healty.db";
	private static final int DATABASE_VERSION = 1;
	public static final String AUTHORITY = "com.arima.healthyliving";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	
	public static final class HealthyLivingPedometerTable {
		public static final String DATABASE_TABLE_PEDOMETER = "pedometer";
		public static final String KEY_ID = "_id";
		public static final String KEY_START_TIME = "starttime";
		public static final String KEY_END_TIME = "endtime";
		public static final String KEY_STEP_COUNT = "stepcount";
		public static final String KEY_STEP_DISTANCE = "distance";
		public static final String KEY_STEP_SPEED = "speed";
		public static final String KEY_STEP_CALORIE = "calorie";

		public static final String DATABASE_TABLE_PEDOMETER_SETTINGS = "pSettings";
		public static final String KEY_SPORT_MODE = "mode";
		public static final String KEY_WEIGHT = "weight";
		public static final String KEY_STEP_WIDTH = "width";
		public static final String KEY_SCREEN_ON = "screenon";
		
		private static final String PEDOMETER_TABLE_CREATE =
		"CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_PEDOMETER +" (" + KEY_ID + " integer primary key autoincrement, " 
			+ KEY_START_TIME + " text null, " + KEY_END_TIME + " text null," + KEY_STEP_COUNT + " text null," 
			+ KEY_STEP_DISTANCE + " text null," + KEY_STEP_SPEED + " text null," + KEY_STEP_CALORIE + " text null)";
		
		private static final String PEDOMETER_SETTINGS =
				"CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_PEDOMETER_SETTINGS +" (" + KEY_SPORT_MODE + " integer null, "
					+ KEY_WEIGHT + " integer null," + KEY_SCREEN_ON + " integer null," + KEY_STEP_WIDTH + " integer null)";
	}
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	private Context context = null;
	
	public DatabaseManagement(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(HealthyLivingPedometerTable.PEDOMETER_TABLE_CREATE);
			db.execSQL(HealthyLivingPedometerTable.PEDOMETER_SETTINGS);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion
			+ " to "
			+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}
	
	public DatabaseManagement open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		DBHelper.close();
	}
	
	public long insert(String insertTable, ContentValues values)
	{
		long insertID = 0;
		try { 
			insertID = db.insert(insertTable, null, values);
			return insertID;
       } catch (android.database.sqlite.SQLiteDiskIOException ex) {
           Log.w(TAG, "[insert]catch SQLiteDiskIOException!");
           return insertID;
       }
	}
	
	public int delete(String deleteTable, String selection, String[] selectionArgs)
	{
		int count = 0;
        try { 
        	 count = db.delete(deleteTable, selection, selectionArgs);
        	 return count;
        } catch (android.database.sqlite.SQLiteDiskIOException ex) {
            Log.w(TAG, "[delete]catch SQLiteDiskIOException!");
            return count;
        }
	}
	
	public Cursor query(String queryTable, String[] projection, String selection, String[] selectionArgs,
            String sortOrder)
	{
		Cursor cursor = null;
		try { 
			cursor = db.query(queryTable, projection, selection, selectionArgs, sortOrder, null, null);
       	 	return cursor;
       } catch (android.database.sqlite.SQLiteDiskIOException ex) {
           Log.w(TAG, "[query]catch SQLiteDiskIOException!");
           return cursor;
       }
	}
	
	public int update(String updateTable, ContentValues values, String selection, String[] selectionArgs)
	{
		int count = 0;
		try { 
			count = db.update(updateTable, values, selection, selectionArgs);
       	 	return count;
       } catch (android.database.sqlite.SQLiteDiskIOException ex) {
           Log.w(TAG, "[update]catch SQLiteDiskIOException!");
           return count;
       } 
	}
}