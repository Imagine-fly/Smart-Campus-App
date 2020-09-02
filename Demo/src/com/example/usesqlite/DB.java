package com.example.usesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	// Context context表示上下文
	//String  name表示数据库名字
	// CursorFactory  factory是一个数据库查询结果对象，也就是相当于游标，一般指定为null
	//version用足表示数据库版本
	
	public DB(Context context) {
		
		super(context, "db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//用户书写创建数据的内部结构
		//此处创建一个表user,这个地方需要写sql语句才可以
		
		db.execSQL("CREATE TABLE user("
				+ "name TEXT DEFAULT NONE,"
				+ "sex TEXT DEFAULT NONE "
				+ ")");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
