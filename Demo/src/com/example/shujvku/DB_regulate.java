package com.example.shujvku;

import java.util.ArrayList;
import java.util.List;

import com.example.data_model.My_inaccount;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB_regulate {
	private DBAdapter adapter;//����DBAdapter����
	private SQLiteDatabase db;//����SQLiteDatabase����
	//��ʼ��DBAdapter����
	public DB_regulate(Context context){
		
		adapter = new DBAdapter(context);
	}
	/**
	 * �����Ϣ
	 * @param my_inaccount
	 */
	public void add(My_inaccount my_inaccount)
	{
		db = adapter.getWritableDatabase();
		//ִ�����������Ϣ����
		db.execSQL("insert into tb_inaccount (_id,money,time,type,handler,mark) values (?,?,?,?,?,?)",new Object[] {
				my_inaccount.getid(),my_inaccount.getMoney(),
				my_inaccount.getTime(),my_inaccount.getType(),my_inaccount.getHandler(),my_inaccount.getMark()
		});
	}
		/**
		 * ���������֧����Ϣ
		 * 
		 * @param id
		 * @return
		 */
		public My_inaccount find(int id)
		{
			//��ʼ��SQLiteDatabase����
			db = adapter.getWritableDatabase();
			Cursor cursor = db.rawQuery("select_id,money,time,type,handler,mark,from tb_inaccount where_id = ?",new String[]
					{
							String.valueOf(id)});//���ݱ�Ų��������֧������Ϣ�������浽Cursor����
			if(cursor.moveToNext())//�������ҵ�����Ϣ
				{
				return new My_inaccount(cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getDouble(cursor.getColumnIndex("money")),
						cursor.getString(cursor.getColumnIndex("time")),
						cursor.getString(cursor.getColumnIndex("type")),
						cursor.getString(cursor.getColumnIndex("handler")),
						cursor.getString(cursor.getColumnIndex("mark")));
				}
			return null;//���û����Ϣ������null
		}
		/**
		 * ��ȡ��Ϣ
		 * 
		 * @param start
		 * @param count
		 * @return
		 */
		public List<My_inaccount>getScrollData(int start, int count)
		{
			List<My_inaccount>tb_inaccount = new ArrayList<My_inaccount>();//�������϶���
			db = adapter.getWritableDatabase();//��ʼ��SQLiteDatabase����
			Cursor cursor = db.rawQuery("select * from tb_inaccount limit ?,?", new String[] {
					String.valueOf(start),String.valueOf(count)
			});//��ȡ��Ϣ
			//������Ϣ
			while (cursor.moveToNext())
			{
				tb_inaccount.add(new My_inaccount(cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getDouble(cursor.getColumnIndex("money")) ,
						cursor.getString(cursor.getColumnIndex("time")), 
						cursor.getString(cursor.getColumnIndex("type")),
						cursor.getString(cursor.getColumnIndex("handler")),
						cursor.getString(cursor.getColumnIndex("mark"))));
			}
			return tb_inaccount;//���ؼ���
		}
			/**
			 * ��ȡ�ܼ�¼��
			 * @return
			 */
			public long getCount()
			{
				db = adapter.getWritableDatabase();//��ʼ��SQLiteDatabase����
				Cursor cursor = db.rawQuery("select count(_id) from tb_inaccount", null);//��ȡ��Ϣ�ļ�¼��
				if(cursor.moveToNext())
				{
					return cursor.getLong(0);
				}
				return 0;
			}
			/**
			 *��ȡ�����֧���������
			 * @return
			 */
		public int getMaxld()
		{
			db = adapter.getWritableDatabase();//��ʼ��SQLiteDatabase����
			Cursor cursor = db.rawQuery("select max(_id) from tb_inaccount",null);//��ȡ��Ϣ���е������
			//����Cursor�е����һ������
			while(cursor.moveToLast()) {
				return cursor.getInt(0);//��ȡ���ʵ������ݣ��������ı��
			}
			return 0;//���û�����ݣ�����0
		}
}
