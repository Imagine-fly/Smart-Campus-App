/**
 * 
 */
/**
 * @author 张朋
 *
 */
package com.example.shujvku;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper
{
	private static final int VERSION = 1;//定义数据库版本号
	private static final String DBNAME = "account.db";//定义数据库名
			public DBAdapter(Context context)
			{
				super(context,DBNAME,null,VERSION);
			}
			//创建数据库
			@Override
			public void onCreate(SQLiteDatabase db) {
				// TODO Auto-generated method stub
			//创建收入和支出的信息表	
				db.execSQL("create table tb_inaccount(_id integer primary key,money decimal,time varchar(10),"
						+ ""+"type varchar(10),handler varchar(100),mark varchar(200))");
			}
			//覆写基类的onUpgrade方法，以便数据库版本的更新
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				// TODO Auto-generated method stub
				
			}
	
}