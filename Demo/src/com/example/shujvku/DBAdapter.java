/**
 * 
 */
/**
 * @author ����
 *
 */
package com.example.shujvku;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper
{
	private static final int VERSION = 1;//�������ݿ�汾��
	private static final String DBNAME = "account.db";//�������ݿ���
			public DBAdapter(Context context)
			{
				super(context,DBNAME,null,VERSION);
			}
			//�������ݿ�
			@Override
			public void onCreate(SQLiteDatabase db) {
				// TODO Auto-generated method stub
			//���������֧������Ϣ��	
				db.execSQL("create table tb_inaccount(_id integer primary key,money decimal,time varchar(10),"
						+ ""+"type varchar(10),handler varchar(100),mark varchar(200))");
			}
			//��д�����onUpgrade�������Ա����ݿ�汾�ĸ���
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				// TODO Auto-generated method stub
				
			}
	
}