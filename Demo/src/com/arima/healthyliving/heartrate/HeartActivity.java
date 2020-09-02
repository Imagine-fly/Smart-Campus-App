package com.arima.healthyliving.heartrate;

//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
//<2014/01/24-33374-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify share heart method.
import java.io.File;
//>2014/01/24-33374-ZhiweiWang.
import java.io.IOException;
//>2013/01/19-33169-ZhiweiWang.
//>2013/01/23-33333-ZhiweiWang.
//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate ..
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//>2014/01/16-33088-ZhiweiWang.
//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
import java.util.concurrent.atomic.AtomicBoolean;

//<2014/01/24-33374-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify share heart method.
import com.arima.healthyliving.Util.ScreenShotForShare;
//>2014/01/24-33374-ZhiweiWang.
import com.arima.healthyliving.view.ImageProcessing;
//>2013/01/19-33169-ZhiweiWang.
//>2013/01/23-33333-ZhiweiWang.
import com.arima.healthyliving.view.RotateView;
import com.arima.healthyliving.R;

//<2014/01/24-33374-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify share heart method.
import android.net.Uri;
//>2014/01/24-33374-ZhiweiWang.
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
//import android.hardware.Camera;
//import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.LayoutInflater;
//import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
//>2013/01/19-33169-ZhiweiWang.
//>2013/01/23-33333-ZhiweiWang.
import android.view.SurfaceView;
import android.view.View;
//<2014/01/24-33374-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify share heart method.
import android.view.Window;
//>2014/01/24-33374-ZhiweiWang.
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate.
import android.widget.Toast;
//>2014/01/16-33088-ZhiweiWang.
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HeartActivity extends Activity {

	 private Button imageButton000;
	 private static ToggleButton imageButton001;
	 private Button imageButton002;
	 private RotateView imageButton100;
	 private RotateView imageButton102;
	 private Button imageButton200;
	 private Button imageButton201;
	 private Button imageButton202;
	//<2013/01/25-33394-ZhiweiWang,[HealthyLiving][InstantHeartRate]Help guide button replace.
	 private Button imageHelp;
	//>2013/01/25-33394-ZhiweiWange.
	
	 
	//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
	 //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	 private static final String TAG = "HeartActivity";
	 private static final AtomicBoolean processing = new AtomicBoolean(false);

	 private static SurfaceView preview = null;
	 private static SurfaceHolder previewHolder = null;
	 private static Camera camera = null;
	//>2013/01/19-33169-ZhiweiWang.
	//>2013/01/23-33333-ZhiweiWang.
	 private static View image = null;
	 //private static TextView text = null;
	 private static TextView textView = null;

	 private static WakeLock wakeLock = null;

	//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
	 //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	 //private static int averageIndex = 0;
	 private static final int averageArraySize = 2;
	 //private static final int[] averageArray = new int[averageArraySize];
	//>2013/01/19-33169-ZhiweiWang.
	//>2013/01/23-33333-ZhiweiWang.

	 public static enum TYPE {
			GREEN, RED
	 };

	 private static TYPE currentType = TYPE.GREEN;

	 public static TYPE getCurrent() {
			return currentType;
	 }

	//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
	 //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	 private static int beatsIndex = 0;
	 private static final int beatsArraySize = 8;
	 private static final int[] beatsArray = new int[beatsArraySize];
	 private static double beats = 0;
	//>2013/01/19-33169-ZhiweiWang.
	 private static long startTime = 0;

	 //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	 private static int preImgValue = 0;
	 private static int deltaImgValue = 0;
	 private static int preDeltaImgValue = 0;
	//>2013/01/19-33169-ZhiweiWang.
		
	 private static String instantHeartRate = null;
	//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate.
	 //private static boolean flashLightIsOn = true;
	 //private static boolean flashLightState = false;
	 private static String systemNowTime = null;
	//>2014/01/16-33088-ZhiweiWang.
	 private Camera.Parameters parameters = null;
	//>2013/01/23-33333-ZhiweiWang.
	 //<2014/02/16-33876-ZhiweiWang,[HealthyLiving][InstantHeartRate]Share data after store heartRate.
	 private boolean isFirstGoToActivity = false;
	 //>2014/02/16-33876-ZhiweiWang.
	//<2014/02/19-34006-ZhiweiWang,[HealthyLiving][InstantHeartRate]make it store the same data only once.
	 private static String nowHeartRate = null;
	//>2014/02/19-34006-ZhiweiWang
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainone);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imageButton000 = (Button)findViewById(R.id.button_000);
        imageButton001 = (ToggleButton)findViewById(R.id.button_001);
        imageButton002 = (Button)findViewById(R.id.button_002);
        imageButton100 = (RotateView)findViewById(R.id.button_100);
        imageButton102 = (RotateView)findViewById(R.id.button_102);
        imageButton200 = (Button)findViewById(R.id.button_200);
        imageButton201 = (Button)findViewById(R.id.button_201);
   	    imageButton202 = (Button)findViewById(R.id.button_202);
   	    //<2013/01/25-33394-ZhiweiWang,[HealthyLiving][InstantHeartRate]Help guide button replace.
   	    imageHelp = (Button)findViewById(R.id.imageButton_help);
   	    //<2013/01/25-33394-ZhiweiWang.
   	    textView = (TextView) findViewById(R.id.textView);
   	    
   	    preview = (SurfaceView) findViewById(R.id.preview);
   	    //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
   	    checkFlashLight();
   	    //<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
   	    //Ledserver.getSurfaceView(preview,textView);
   	    //>2013/01/23-33333-ZhiweiWang.
   		//>2013/01/19-33169-ZhiweiWang.
		/*previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);*/

		image = (View)findViewById(R.id.image);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
   	    
   	    imageButton001.setOnTouchListener(imageButton001TouchListener);
   	    imageButton100.setOnTouchListener(imageButton100TouchListener); 
   	    imageButton102.setOnTouchListener(imageButton102TouchListener); 
   	    imageButton201.setOnTouchListener(imageButton201TouchListener);  
   	    
   	    imageButton102.setOnClickListener(imageButton102ClickListener);
   	    imageButton201.setOnClickListener(imageButton201ClickListener);
   	    imageButton100.setOnClickListener(imageButton100ClickListener);
   	    imageHelp.setOnClickListener(helpGuideClickListener);
   	    //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
   	    //imageButton001.setOnClickListener(imageButton001ClickListener);
   	    //>2013/01/19-33169-ZhiweiWang.
   	    
   	    //camera = Camera.open();
   	    //camera.startPreview();
   	    imageButton001.setOnCheckedChangeListener(new ToggleListener()); 
    }
    
  //<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
    //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
    private void checkFlashLight(){
    	Camera cam = Camera.open();
   	    cam.startPreview();
   	    Parameters parm = cam.getParameters();
    	List<String> flashModes = parm.getSupportedFlashModes();
    	if (flashModes == null) {
    		  checkFlashDialog();
    	}
   	    cam.setParameters(parm);
   	    cam.release();
   	    cam = null;
    }
  //>2013/01/19-33169-ZhiweiWang.
  //>2013/01/23-33333-ZhiweiWang.
    
    private class ToggleListener implements OnCheckedChangeListener{

        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
         // TODO Auto-generated method stub
        	//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
           //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
           //Intent intent = new Intent(HeartActivity.this,Ledserver.class);
           //>2013/01/19-33169-ZhiweiWang.
        	//flashLightIsOn = true;
           if(isChecked){
        	   //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.    		
        	   //HeartActivity.this.startService(intent);
        	try {
				openFlashLight();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//>2013/01/19-33169-ZhiweiWang.
        	  imageButton000.setBackgroundResource(R.drawable.button_1_0_0); 
        	  imageButton001.setBackgroundResource(R.drawable.button_1_0_1);  
        	  imageButton002.setBackgroundResource(R.drawable.button_1_0_2); 
        	  imageButton001.setTextColor(getResources().getColor(R.color.textColorRed));
            }else{
              //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
              //HeartActivity.this.stopService(intent);
              //>2013/01/19-33169-ZhiweiWang.
              //>2013/01/23-33333-ZhiweiWang.
        	  imageButton000.setBackgroundResource(R.drawable.button_0_0_0); 
        	  imageButton001.setBackgroundResource(R.drawable.button_0_0_1);  
        	  imageButton002.setBackgroundResource(R.drawable.button_0_0_2); 
        	  imageButton001.setTextColor(getResources().getColor(R.color.textColorBlack));
        	  closeFlashLight();
           }
        }
      }
     
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

  //<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
    //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
    private void openFlashLight() throws IOException{
    	//<2014/02/16-33876-ZhiweiWang,[HealthyLiving][InstantHeartRate]Share data after store heartRate.
    	isFirstGoToActivity = true;
    	//>2014/02/16-33876-ZhiweiWang.
    	previewHolder = preview.getHolder();
		//previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		if(camera == null){
		   imageButton001.setTextColor(getResources().getColor(R.color.textColorRed));
		   camera = Camera.open();
	   	   camera.startPreview();
		   camera.setPreviewDisplay(previewHolder);
		   camera.setPreviewCallback(previewCallback);
		   camera.setDisplayOrientation(90);
		   parameters = camera.getParameters();
		   parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		   parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);	
		   parameters.setPreviewSize(640, 480);
		   camera.setParameters(parameters);
		}
    }
    
    private void closeFlashLight(){
        if(camera != null){
           imageButton001.setTextColor(getResources().getColor(R.color.textColorBlack));
           parameters = camera.getParameters();
           parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
           camera.setPreviewCallback(null);
		   camera.stopPreview();
		   camera.setParameters(parameters);
		   camera.release();
		   camera = null;
        }
    }
    //>2013/01/19-33169-ZhiweiWang.
   //<2013/01/23-33333-ZhiweiWang.
    
	@Override
	public void onResume() {
		super.onResume();

		wakeLock.acquire();
		//camera = Camera.open();
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onPause() {
		super.onPause();

		wakeLock.release();
		//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
		//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
        if(camera != null){
           parameters = camera.getParameters();
           parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		   camera.setPreviewCallback(null);
		   camera.stopPreview();
		   camera.setParameters(parameters);
		   camera.release();
		   camera = null;
        }
		//>2013/01/19-33169-ZhiweiWang.
        //>2013/01/23-33333-ZhiweiWang.
	}
	//<2013/01/23-33333-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify test method.
	//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	//<2014/02/23-34163-ZhiweiWang,[HealthyLiving][InstantHeartRate]Stop heart rate test when your finger not put on the camera.
	private  PreviewCallback previewCallback = new PreviewCallback() {
	//>2014/02/23-34163-ZhiweiWang.
		public void onPreviewFrame(byte[] data, Camera cam) {
			if (data == null)
				throw new NullPointerException();
			Camera.Size size = cam.getParameters().getPreviewSize();
			if (size == null)
				throw new NullPointerException();

			if (!processing.compareAndSet(false, true))
				return;

			int width = size.width;
			int height = size.height;

			int imgValue = ImageProcessing.decodeYUV420SPtoRed(data.clone(),height, width);
			Log.i(TAG, "imgAvg=" + imgValue);
				
			//<2014/02/23-34163-ZhiweiWang,[HealthyLiving][InstantHeartRate]Stop heart rate test when your finger not put on the camera.
			int averageRed = ImageProcessing.decodeYUV420SPtoRed(data.clone(), width, height)/(height*width);
			if(averageRed < 200 && ((System.currentTimeMillis()-startTime)/1000d)>=5){
				closeFlashLight();
				//<2014/02/23-34164-ZhiweiWang,[HealthyLiving][InstantHeartRate]Prepare toast for stop heart rate test when your finger not put on the camera.
				Toast.makeText(HeartActivity.this, R.string.put_finger_onCamera, Toast.LENGTH_SHORT).show();
				//>2014/02/23-34164-ZhiweiWang.
			}
			//>2014/02/23-34163-ZhiweiWang.
			if(preImgValue == 0){
				preImgValue = imgValue;
				processing.set(false);
				return ;
			}
			else{
				deltaImgValue = imgValue - preImgValue;
				preImgValue = imgValue;
			}

			if((preDeltaImgValue*deltaImgValue) < 0)
			{
				beats++;
			}
			preDeltaImgValue = deltaImgValue;
			
			long endTime = System.currentTimeMillis();
			double totalTimeInSecs = (endTime - startTime) / 1000d;
			//<2014/02/23-34163-ZhiweiWang,[HealthyLiving][InstantHeartRate]Stop heart rate test when your finger not put on the camera.
			if (totalTimeInSecs >= 5) {
			//>2014/02/23-34163-ZhiweiWang.
				double bps = (beats / totalTimeInSecs / 2);
				int dpm = (int) (bps * 60d);
				Log.i(TAG, "MY heart dpm" + dpm);
				if (dpm < 30 || dpm > 180) {
					startTime = System.currentTimeMillis();
					beats = 0;
					processing.set(false);
					return;
				}

				Log.e(TAG, "totalTimeInSecs=" + totalTimeInSecs + " beats="
						+ beats);

				if (beatsIndex == beatsArraySize)
					beatsIndex = 0;
				beatsArray[beatsIndex] = dpm;
				beatsIndex++;

				int beatsArrayAvg = 0;
				int beatsArrayCnt = 0;
				for (int i = 0; i < beatsArray.length; i++) {
					if (beatsArray[i] > 0) {
						beatsArrayAvg += beatsArray[i];
						beatsArrayCnt++;
					}
				}
				int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
				instantHeartRate = String.valueOf(beatsAvg);
				//<2014/02/19-34006-ZhiweiWang,[HealthyLiving][InstantHeartRate]make it store the same data only once.
				nowHeartRate = String.valueOf(beatsAvg);
				//>2014/02/19-34006-ZhiweiWang.
				//text.setText(String.valueOf(beatsAvg));
				textView.setText(String.valueOf(beatsAvg));
				startTime = System.currentTimeMillis();
				beats = 0;
			}
			processing.set(false);
		}
	};
	//>2013/01/19-33169-ZhiweiWang.
	//>2013/01/23-33333-ZhiweiWang.
	
	/*private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				if(camera == null)  { 
				   camera = Camera.open();
				   camera.startPreview();
				   camera.setPreviewDisplay(previewHolder);
				   camera.setPreviewCallback(previewCallback);
				}
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback","Exception in setPreviewDisplay()", t);
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
			   Camera.Parameters parameters = camera.getParameters();
			   parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			   parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);	
		       Camera.Size size = getSmallestPreviewSize(width, height, parameters);
			 if (size != null) {
				parameters.setPreviewSize(size.width, size.height);
				Log.d(TAG, "Using width=" + size.width + " height="+ size.height);
			 }
			 else{
				parameters.setPreviewSize(width, height);
			 }
			parameters.setPreviewSize(640, 480);
			camera.setDisplayOrientation(90);
			camera.setParameters(parameters);
			//camera.startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// Ignore
		}
	};*/

	//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	/*private static Camera.Size getSmallestPreviewSize(int width, int height,Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea < resultArea)
						result = size;
				}
			}
		}
		return result;
	}*/
	
   /* private OnClickListener imageButton001ClickListener = new OnClickListener() {	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this,Ledserver.class);
   	        MainActivity.this.startService(intent);
		}
	};*/
	
	public static void getNowHeartRate(String data){
		instantHeartRate = data;
	}
	//>2013/01/19-33169-ZhiweiWang.
	
    private OnClickListener imageButton102ClickListener = new OnClickListener() {	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(HeartActivity.this,HeartRateHistoryActivity.class);
			intent.putExtra("Instant heart rate", instantHeartRate);
			//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
			intent.putExtra("system now time", systemNowTime);
			//>2014/01/16-33088-ZhiweiWang>.
			startActivity(intent);
			//<2014/02/16-33876-ZhiweiWang,[HealthyLiving][InstantHeartRate]Share data after store heartRate.
			//<2014/02/19-34006-ZhiweiWang,[HealthyLiving][InstantHeartRate]make it store the same data only once.
			if(instantHeartRate != null && systemNowTime != null){
			   instantHeartRate = null;
			   systemNowTime = null;
			}
			//>2014/02/19-34006-ZhiweiWang.
			//>2014/02/16-33876-ZhiweiWang.
			//startActivityForResult(intent, 0x1001);
		}
	};

	
	private OnClickListener imageButton201ClickListener = new OnClickListener() {	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//closeFlashLight();
			//<2014/01/27-33432-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify heartRate history storage dialog title.
			if(instantHeartRate != null){
				dialog();
			}else{
				Toast.makeText(HeartActivity.this, R.string.no_data_to_store, Toast.LENGTH_SHORT).show();
			}
			//>2014/01/27-33432-ZhiweiWang¡£
		}
	};
	
	private OnClickListener imageButton100ClickListener = new OnClickListener() {	
		public void onClick(View v) {
			// TODO Auto-generated method stub
		  //<2014/01/24-33374-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify share heart method.
		//<2014/02/16-33876-ZhiweiWang,[HealthyLiving][InstantHeartRate]Share data after store heartRate.
		  //if(instantHeartRate != null){
		if(isFirstGoToActivity == true){
		//>2014/02/16-33876-ZhiweiWang.
			String mShareFileName = File.separator + "/HeartRateShare.png";
			Window window = getWindow();
			int width = getWindowManager().getDefaultDisplay().getWidth();
			int height = getWindowManager().getDefaultDisplay().getHeight();
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			File file = ScreenShotForShare.GetScreenShotFile(window, width, height, mShareFileName); 
			if (file != null)
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			shareIntent.setType("text/image/png");   
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, mShareFileName);
			//<2014/02/19-34006-ZhiweiWang,[HealthyLiving][InstantHeartRate]make it store the same data only once.
			//shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_data, Integer.parseInt(instantHeartRate)));
			shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_data, Integer.parseInt(nowHeartRate)));
			//>2014/02/19-34006-ZhiweiWang.
			startActivity(Intent.createChooser(shareIntent, getString(R.string.share))); 
		  }else{
				 Toast.makeText(HeartActivity.this, R.string.no_data_to_share, Toast.LENGTH_SHORT).show();
		  }
		  //>2014/01/24-33374-ZhiweiWang
		}
	};
	
	private OnClickListener helpGuideClickListener = new OnClickListener() {	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LayoutInflater li = LayoutInflater.from(HeartActivity.this); 
	        View view = li.inflate(R.layout.help_guide_introduce, null); 
	 
	        //get a builder and set the view 
	        AlertDialog.Builder builder = new AlertDialog.Builder(HeartActivity.this); 
	        builder.setView(view); 
	        builder.setNegativeButton(getString(R.string.start_using),null);   
	        builder.create(); 
	        builder.show(); 
		}
	};
	
    private OnTouchListener imageButton001TouchListener = new OnTouchListener() {
    	public boolean onTouch(View v, MotionEvent event) {     
            if(event.getAction() == MotionEvent.ACTION_DOWN){     
           	 imageButton000.setBackgroundResource(R.drawable.button_1_0_0); 
           	 imageButton001.setBackgroundResource(R.drawable.button_1_0_1);  
           	 imageButton002.setBackgroundResource(R.drawable.button_1_0_2); 
            }else if(event.getAction() == MotionEvent.ACTION_UP){     
           	 imageButton000.setBackgroundResource(R.drawable.button_0_0_0); 
           	 imageButton001.setBackgroundResource(R.drawable.button_0_0_1);  
           	 imageButton002.setBackgroundResource(R.drawable.button_0_0_2); 
            }     
            return false;     
        }     
    };
    private OnTouchListener imageButton100TouchListener = new OnTouchListener() {
    	 public boolean onTouch(View v, MotionEvent event) {     
             if(event.getAction() == MotionEvent.ACTION_DOWN){   
               imageButton000.setBackgroundResource(R.drawable.button_0_0_0); 
          	   imageButton001.setBackgroundResource(R.drawable.button_0_0_1);  
          	   imageButton002.setBackgroundResource(R.drawable.button_0_0_2); 
          	   imageButton001.setTextColor(getResources().getColor(R.color.textColorBlack));
            	 
         	   imageButton000.setBackgroundResource(R.drawable.button_4_0_0); 
         	   imageButton100.setBackgroundResource(R.drawable.button_4_1_0);  
         	   imageButton200.setBackgroundResource(R.drawable.button_4_2_0); 
             }else if(event.getAction() == MotionEvent.ACTION_UP){     
         	   imageButton000.setBackgroundResource(R.drawable.button_0_0_0); 
           	   imageButton100.setBackgroundResource(R.drawable.button_0_1_0);  
           	   imageButton200.setBackgroundResource(R.drawable.button_0_2_0);    
             }     
          return false;     
         }     
    };
    private OnTouchListener imageButton102TouchListener = new OnTouchListener() {
    	 public boolean onTouch(View v, MotionEvent event) {     
             if(event.getAction() == MotionEvent.ACTION_DOWN){ 
               imageButton000.setBackgroundResource(R.drawable.button_0_0_0); 
          	   imageButton001.setBackgroundResource(R.drawable.button_0_0_1);  
          	   imageButton002.setBackgroundResource(R.drawable.button_0_0_2); 
          	   //imageButton001.setTextColor(getResources().getColor(R.color.textColorBlack));
          	   //closeFlashLight();
            	 
         	   imageButton002.setBackgroundResource(R.drawable.button_2_0_2); 
         	   imageButton102.setBackgroundResource(R.drawable.button_2_1_2);  
         	   imageButton202.setBackgroundResource(R.drawable.button_2_2_2);          	   
             }else if(event.getAction() == MotionEvent.ACTION_UP){     
               imageButton002.setBackgroundResource(R.drawable.button_0_0_2); 
               imageButton102.setBackgroundResource(R.drawable.button_0_1_2);  
               imageButton202.setBackgroundResource(R.drawable.button_0_2_2);    
             }     
          return false;     
         }     
   };
   private OnTouchListener imageButton201TouchListener = new OnTouchListener() {
	   public boolean onTouch(View v, MotionEvent event) {     
           if(event.getAction() == MotionEvent.ACTION_DOWN){     
       	       imageButton200.setBackgroundResource(R.drawable.button_3_2_0); 
       	       imageButton201.setBackgroundResource(R.drawable.button_3_2_1);  
       	       imageButton202.setBackgroundResource(R.drawable.button_3_2_2); 
       	       
       	       imageButton000.setBackgroundResource(R.drawable.button_0_0_0); 
      	       imageButton001.setBackgroundResource(R.drawable.button_0_0_1);  
      	       imageButton002.setBackgroundResource(R.drawable.button_0_0_2); 
      	       imageButton001.setTextColor(getResources().getColor(R.color.textColorBlack));
           }else if(event.getAction() == MotionEvent.ACTION_UP){     
        	   imageButton200.setBackgroundResource(R.drawable.button_0_2_0); 
           	   imageButton201.setBackgroundResource(R.drawable.button_0_2_1);  
           	   imageButton202.setBackgroundResource(R.drawable.button_0_2_2); 
           }     
        return false;     
       }     
  };
    
    protected void dialog() {
  	     AlertDialog.Builder builder = new AlertDialog.Builder(HeartActivity.this);
  	     //<2014/01/27-33432-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify heartRate history storage dialog title.
  	     builder.setMessage(R.string.store_heart_rate);
  	     builder.setTitle(R.string.my_heart_rate);
  	     //>2014/01/27-33432-ZhiweiWang.

         builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
 	          public void onClick(DialogInterface dialog, int which) {
 	        	//<2014/01/16-33088-ZhiweiWang,[HealthyLiving][InstantHeartRate]add share function and system time to HeartRate .
 	        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd    HH:mm:ss");  
 	        	//<2014/01/21-33196-ZhiweiWang,[HealthyLiving][InstantHeartRate]Modify textView  position and remove Chinese note.
  	        	Date curDate = new Date(System.currentTimeMillis());
  	            //>2014/01/21-33196-ZhiweiWang.
  	        	String str = formatter.format(curDate);       
  	        	systemNowTime = str;
 	               //if(instantHeartRate == null)
 	            	  //instantHeartRate = "";
 	            //>2014/01/16-33088-ZhiweiWang.
 	        }
 	     });

 	     builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
 	          public void onClick(DialogInterface dialog, int which) {
 	              dialog.dismiss();
 	       }
 	    });
 	    builder.create().show();    
    }
    
  //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
    protected void checkFlashDialog() {
 	     AlertDialog.Builder builder = new AlertDialog.Builder(HeartActivity.this);
 	     builder.setMessage(R.string.suggest_donot_to_test);
 	     builder.setTitle(R.string.check_FlashLight);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	        	  onBackPressed();
	        }
	     });

	     builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	              dialog.dismiss();
	       }
	    });
	    builder.create().show();    
   }
    
    @Override
    public void onBackPressed() {
      super.onBackPressed();
    }
  //>2013/01/19-33169-ZhiweiWang.
}
