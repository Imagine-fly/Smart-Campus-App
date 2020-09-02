package com.arima.healthyliving.voice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arima.healthyliving.R;




import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SignSearch extends Activity {
	ListView listView;
	private DBTHelper dbHelper;
	Cursor cursor;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.signsearch);
		//实例化数据库
		dbHelper=new DBTHelper(getApplicationContext(), "jr.db", null, 2);
		List<Map<String, String>> listItemsList=new ArrayList<Map<String,String>>();
		listView=(ListView)findViewById(R.id.listview);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		cursor=db.rawQuery("select  * from userData" , null);
		SimpleAdapter adapter=new SimpleAdapter(this, listItemsList, R.layout.item,
        		new String[]{"date","sign","time"}, 
        		new int[]{R.id.date,R.id.sign,R.id.time});
		while(cursor.moveToNext()){
        	Map<String,String> map=new HashMap<String, String>();
        	map.put("date",cursor.getString(cursor.getColumnIndex("date")));
        	map.put("sign",cursor.getString(cursor.getColumnIndex("sign")) );
        	map.put("time",cursor.getString(cursor.getColumnIndex("time")) );
        	listItemsList.add(map);
		
		
		
		
	}
		listView.setAdapter(adapter);
	}

}
