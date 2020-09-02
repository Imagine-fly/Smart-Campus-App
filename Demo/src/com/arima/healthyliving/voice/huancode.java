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


public class huancode extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huanshu);  
	 
        Button huanButton = (Button) this.findViewById(R.id.huanshu1);
        huanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent openCameraIntent1 = new Intent(huancode.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent1, 0);
			}
		});
        
        Button hqButton = (Button) this.findViewById(R.id.huanqu);
        hqButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				Intent intent4 = new Intent(huancode.this,BarCodeTestActivity.class);
				startActivity(intent4);
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
			db.execSQL("delete from book where name = ?", new String[] {scanResult});
			Context context = getApplicationContext();
		    CharSequence text = "还书成功";
		    int duration = Toast.LENGTH_SHORT;

		    Toast toast = Toast.makeText(context, text, duration);
		    toast.show();
			
			db.close();
		}
}
  
    
}