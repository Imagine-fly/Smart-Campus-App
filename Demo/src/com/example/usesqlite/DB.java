package com.example.usesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	// Context context��ʾ������
	//String  name��ʾ���ݿ�����
	// CursorFactory  factory��һ�����ݿ��ѯ�������Ҳ�����൱���α꣬һ��ָ��Ϊnull
	//version�����ʾ���ݿ�汾
	
	public DB(Context context) {
		
		super(context, "db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//�û���д�������ݵ��ڲ��ṹ
		//�˴�����һ����user,����ط���Ҫдsql���ſ���
		
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
