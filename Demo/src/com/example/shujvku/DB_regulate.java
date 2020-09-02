package com.example.shujvku;

import java.util.ArrayList;
import java.util.List;

import com.example.data_model.My_inaccount;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB_regulate {
	private DBAdapter adapter;//创建DBAdapter对象
	private SQLiteDatabase db;//创建SQLiteDatabase对象
	//初始化DBAdapter对象
	public DB_regulate(Context context){
		
		adapter = new DBAdapter(context);
	}
	/**
	 * 添加信息
	 * @param my_inaccount
	 */
	public void add(My_inaccount my_inaccount)
	{
		db = adapter.getWritableDatabase();
		//执行添加收入信息操作
		db.execSQL("insert into tb_inaccount (_id,money,time,type,handler,mark) values (?,?,?,?,?,?)",new Object[] {
				my_inaccount.getid(),my_inaccount.getMoney(),
				my_inaccount.getTime(),my_inaccount.getType(),my_inaccount.getHandler(),my_inaccount.getMark()
		});
	}
		/**
		 * 查找收入和支出信息
		 * 
		 * @param id
		 * @return
		 */
		public My_inaccount find(int id)
		{
			//初始化SQLiteDatabase对象
			db = adapter.getWritableDatabase();
			Cursor cursor = db.rawQuery("select_id,money,time,type,handler,mark,from tb_inaccount where_id = ?",new String[]
					{
							String.valueOf(id)});//根据编号查找收入和支出的信息，并储存到Cursor类中
			if(cursor.moveToNext())//遍历查找到的信息
				{
				return new My_inaccount(cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getDouble(cursor.getColumnIndex("money")),
						cursor.getString(cursor.getColumnIndex("time")),
						cursor.getString(cursor.getColumnIndex("type")),
						cursor.getString(cursor.getColumnIndex("handler")),
						cursor.getString(cursor.getColumnIndex("mark")));
				}
			return null;//如果没有信息，返回null
		}
		/**
		 * 获取信息
		 * 
		 * @param start
		 * @param count
		 * @return
		 */
		public List<My_inaccount>getScrollData(int start, int count)
		{
			List<My_inaccount>tb_inaccount = new ArrayList<My_inaccount>();//创建集合对象
			db = adapter.getWritableDatabase();//初始化SQLiteDatabase对象
			Cursor cursor = db.rawQuery("select * from tb_inaccount limit ?,?", new String[] {
					String.valueOf(start),String.valueOf(count)
			});//获取信息
			//遍历信息
			while (cursor.moveToNext())
			{
				tb_inaccount.add(new My_inaccount(cursor.getInt(cursor.getColumnIndex("_id")),
						cursor.getDouble(cursor.getColumnIndex("money")) ,
						cursor.getString(cursor.getColumnIndex("time")), 
						cursor.getString(cursor.getColumnIndex("type")),
						cursor.getString(cursor.getColumnIndex("handler")),
						cursor.getString(cursor.getColumnIndex("mark"))));
			}
			return tb_inaccount;//返回集合
		}
			/**
			 * 获取总记录数
			 * @return
			 */
			public long getCount()
			{
				db = adapter.getWritableDatabase();//初始化SQLiteDatabase对象
				Cursor cursor = db.rawQuery("select count(_id) from tb_inaccount", null);//获取信息的记录数
				if(cursor.moveToNext())
				{
					return cursor.getLong(0);
				}
				return 0;
			}
			/**
			 *获取收入和支出的最大编号
			 * @return
			 */
		public int getMaxld()
		{
			db = adapter.getWritableDatabase();//初始化SQLiteDatabase对象
			Cursor cursor = db.rawQuery("select max(_id) from tb_inaccount",null);//获取信息表中的最大编号
			//访问Cursor中的最后一条数据
			while(cursor.moveToLast()) {
				return cursor.getInt(0);//获取访问到的数据，就是最大的编号
			}
			return 0;//如果没有数据，返回0
		}
}
