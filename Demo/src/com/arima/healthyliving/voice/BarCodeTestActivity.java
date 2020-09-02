package com.arima.healthyliving.voice;



import com.arima.healthyliving.R;
import com.shuju.shujuku.MyOpenHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BarCodeTestActivity extends Activity {
	
 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
      
        MyOpenHelper myopenhelper = new MyOpenHelper(getApplicationContext());
		myopenhelper.getReadableDatabase();
        
        Button jieButton = (Button) this.findViewById(R.id.jie);
        jieButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent intent1 = new Intent(BarCodeTestActivity.this,jiecode.class);
				startActivity(intent1);
				BarCodeTestActivity.this.finish();
			}
		});
        
        Button huanButton = (Button) this.findViewById(R.id.huan);
        huanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent intent2 = new Intent(BarCodeTestActivity.this,huancode.class);
				startActivity(intent2);
				BarCodeTestActivity.this.finish();
			}
		});
        
        
        Button Button1 = (Button) this.findViewById(R.id.button1);
        Button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				MyOpenHelper myopenhelper = new MyOpenHelper(getApplicationContext());
				SQLiteDatabase db = myopenhelper.getReadableDatabase();
 
				  
				
				  
	
				Cursor cursor = db.query("book", null, null, null, null, null, null);
				if (cursor != null && cursor.getCount()>0) {
					while(cursor.moveToNext()) {
						//String id = cursor.getString(0);
						String name = cursor.getString(1);
					
						Context context = getApplicationContext();
					    //CharSequence text = "name";
					    int duration = Toast.LENGTH_SHORT;

					    Toast toast = Toast.makeText(context, name, duration);
					    toast.show();
					}
				}  
				
			}
		});
        
    }

	
}