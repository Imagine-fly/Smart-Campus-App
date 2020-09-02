package com.arima.healthyliving.heartrate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.arima.healthyliving.R;
//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
import com.arima.healthyliving.view.Person;
//>2014/01/16-33088-ZhiweiWang.

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HeartRateHistoryActivity extends Activity{
	
	private ListView lv;
	private MemberListAdapter mMemberListAdapter;
	ArrayList<Map<String, Object>> list;
	Map<String,Object> map;
	private ArrayList<String> arrayList = new ArrayList<String>();
	//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
	ArrayList<Map<String, Object>> lists;
	Map<String,Object> maps;
	private ArrayList<String> arrayList1 = new ArrayList<String>();
	private String heartRate = null;
	private String time = null;
	private SQLiteDatabase db;
	//>2014/01/16-33088-ZhiweiWang.
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	    //<2014/01/21-33196-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify textView  position and remove Chinese note.
	     //set no titleBar
	    //>2014/01/21-33196-ZhiweiWang.
	     requestWindowFeature(Window.FEATURE_NO_TITLE); 
	     
	     Bundle bundle = getIntent().getExtras();
	   //<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
	     //String heartRate = bundle.getString("Instant heart rate");     
	     //if(heartRate != null)
	     //arrayList.add(heartRate);
	     //arrayList.add("65");
	     //arrayList.add("69");
	     heartRate = bundle.getString("Instant heart rate");
	     time = bundle.getString("system now time");
	     // create sql database
	     db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);  
	     //<2014/01/21-33196-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify textView  position and remove Chinese note.
	     //create person  table 
	     //>2014/01/21-33196-ZhiweiWang.
	     db.execSQL("CREATE TABLE IF NOT EXISTS person (_id INTEGER PRIMARY KEY AUTOINCREMENT, heartRate VARCHAR, time SMALLINT)");  
	     Person person = new Person();  
	     if(heartRate != null && time != null){
	        person.heartRate = heartRate;  
	        person.time = time; 
	        ContentValues cv = new ContentValues();  
		    cv.put("heartRate", person.heartRate);  
		    cv.put("time", person.time);  
		  //<2014/01/21-33196-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify textView  position and remove Chinese note.
		    //insert ContentValues data
		  //>2014/01/21-33196-ZhiweiWang.
		    db.insert("person", null, cv);  
	     }
	     Cursor c = db.query("person", new String[] { "time","heartRate" }, null, null, null, null, null);
	     while (c.moveToNext()) {  
	         //int _id = c.getInt(c.getColumnIndex("_id"));  
	         String heartRate = c.getString(c.getColumnIndex("heartRate"));           
	         String time = c.getString(c.getColumnIndex("time"));  
	         arrayList.add(heartRate);
	         arrayList1.add(time);
	     }  
	     c.close();  	          
	   //>2014/01/16-33088-ZhiweiWang.
	     setContentView(R.layout.listview_history);     
		 lv = (ListView)findViewById(R.id.heart_rate_list);
		 list = new ArrayList<Map<String, Object>>();  
		 
		 Iterator<String> it = arrayList.iterator();
    	 while(it.hasNext()){
    		 String heartRates = it.next();
    		 map = new HashMap<String, Object>();
 	         map.put("heartRate",heartRates); 
 			 list.add(map);
    	 }
    	 //<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
    	 lists = new ArrayList<Map<String, Object>>();  
    	 Iterator<String> its = arrayList1.iterator();
    	 while(its.hasNext()){
    		 String times = its.next();
    		 maps = new HashMap<String, Object>();
 	         maps.put("time",times); 
 			 lists.add(maps);
    	 }
    	 //>2014/01/16-33088-ZhiweiWang.
		 mMemberListAdapter = new MemberListAdapter(HeartRateHistoryActivity.this);
		 lv.setAdapter(mMemberListAdapter);
	 }
	 
	 @Override
	 public void onResume() {
		super.onResume();
	 }
	 
	 @Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
		}
	 
	 @Override
	 public void onPause() {
		super.onPause();
	 }
	 
	 public final class ViewHolder{          
	        public TextView heartRate;            
	        public ImageView deleteBtn; 
	        //<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
	        public TextView time;  
	        //>2014/01/16-33088-ZhiweiWang.
	 }
	 
	 private final class MemberListAdapter extends BaseAdapter {
	    	private LayoutInflater mInflater; 
	    	public MemberListAdapter(Context context){               
	    		this.mInflater = LayoutInflater.from(context);
	    	}
			public int getCount() {                
	    		// TODO Auto-generated method stub086    
	    		return list != null ?  list.size() : 0;  
	    	}                
	    	public Object getItem(int position) {           
	    		// TODO Auto-generated method stub092      
	    		return list != null ? list.get(position) : null;
	    	}              
	    	public long getItemId(int position) {         					
	    		// TODO Auto-generated method stub098             
	    		return position;
	    	}  

	        public View getView(int position, View convertView, ViewGroup parent) {
	        	 ViewHolder holder = null;
	        	 if (convertView == null) 
	        	 {                         
	        		 holder=new ViewHolder();                                    
					 convertView = mInflater.inflate(R.layout.listview_history_item, null);          
	        		 holder.heartRate = (TextView)convertView.findViewById(R.id.name); 
	        		 //<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
	        		 holder.time = (TextView)convertView.findViewById(R.id.time); 
	        		 //>2014/01/16-33088-ZhiweiWang.
	        		 holder.deleteBtn = (ImageView)convertView.findViewById(R.id.delete_button);               
	        		 convertView.setTag(holder);                                    
	        	 }else {      
	        			holder = (ViewHolder)convertView.getTag();               
	        	 }
	        	 holder.deleteBtn.setTag(position);
	        	 String heartRate = (String) list.get(position).get("heartRate");
	        	//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
	        	 String time = (String) lists.get(position).get("time");
	        	 if (time == null) {
	        		 time = getString(R.string.title_activity_main);
        		 }
	        	 //>2014/01/16-33088-ZhiweiWang.
	        	 if (heartRate == null) {
	        		 heartRate = getString(R.string.title_activity_main);
        		 }
	        	 final SpannableString spannable = new SpannableString(heartRate);
     	         spannable.setSpan(TruncateAt.MARQUEE, 0, spannable.length(),
     	                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        	 holder.heartRate.setText(spannable);    
	        	 //<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
	        	 final SpannableString spannables = new SpannableString(time);
     	         spannables.setSpan(TruncateAt.MARQUEE, 0, spannables.length(),
     	                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        	 holder.time.setText(spannables); 	
	        	 //>2014/01/16-33088-ZhiweiWang.
	        	 
	        	 
	        	 holder.deleteBtn.setOnClickListener(new View.OnClickListener() {                      
	        		 public void onClick(View v) {                       
	        			 int deletePosition = (Integer) v.getTag();
	        			 //String mHeartRate = (String) list.get(deletePosition).get("name");
	        			 list.remove(deletePosition);
	        			//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
	        			 //delete data from SQL
	        			 String time = (String)lists.get(deletePosition).get("time");
	        		     db.delete("person", "time = ?", new String[]{time});
	        			 //>2014/01/16-33088-ZhiweiWang.
	        	         if (mMemberListAdapter != null)
     			 	         mMemberListAdapter.notifyDataSetChanged();
	                }
	            });
	          return convertView; 
	     }
	 }
}
