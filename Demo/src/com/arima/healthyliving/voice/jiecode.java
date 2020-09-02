package com.arima.healthyliving.voice;

import com.arima.healthyliving.R;
import com.shuju.shujuku.MyOpenHelper;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class jiecode extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jieshu);  
	 
        
        
        Button jieButton = (Button) this.findViewById(R.id.jieshu1);
        jieButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent openCameraIntent1 = new Intent(jiecode.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent1, 0);
			}
		});
        
        Button jqButton = (Button) this.findViewById(R.id.jiequ);
        jqButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				Intent intent3 = new Intent(jiecode.this,BarCodeTestActivity.class);
				startActivity(intent3);
				jiecode.this.finish();
			}
		});
	}
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			
			MyOpenHelper myopenhelper = new MyOpenHelper(getApplicationContext());
			SQLiteDatabase db = myopenhelper.getReadableDatabase();
			db.execSQL("insert into book(name) values(?) ", new String[] {scanResult});
			
			Context context = getApplicationContext();
		    CharSequence text = "ΩË È≥…π¶";
		    int duration = Toast.LENGTH_SHORT;

		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
			
			db.close();
		 
	
		}
	
    }
    

}