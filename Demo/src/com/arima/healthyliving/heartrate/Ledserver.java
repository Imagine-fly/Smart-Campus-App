package com.arima.healthyliving.heartrate;

//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import com.arima.healthyliving.view.ImageProcessing;
//>2013/01/19-33169-ZhiweiWang.

import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.IBinder;
//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
import android.hardware.Camera.PreviewCallback;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
//>2013/01/19-33169-ZhiweiWang.

public class Ledserver extends Service {
	 private Camera cam = null;
	 private Parameters parm = null;
	//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	 private static final String TAG = "Ledserver";
	 private static final AtomicBoolean processing = new AtomicBoolean(false);
	 private static SurfaceView preview = null;
	 private static SurfaceHolder previewHolder = null;
	 private static TextView text = null;

	 private static WakeLock wakeLock = null;

	 private static int averageIndex = 0;
	 private static final int averageArraySize = 2;
	 private static final int[] averageArray = new int[averageArraySize];

	 public static enum TYPE {
			GREEN, RED
	 };

	 private static TYPE currentType = TYPE.GREEN;

	 public static TYPE getCurrent() {
			return currentType;
	 }

	 private static int beatsIndex = 0;
	 private static final int beatsArraySize = 9;
	 private static final int[] beatsArray = new int[beatsArraySize];
	 private static double beats = 0;
	 private static long startTime = 0;

	 private static int preImgValue = 0;
	 private static int deltaImgValue = 0;
	 private static int preDeltaImgValue = 0;
	 //>2013/01/19-33169-ZhiweiWang.
	 @Override
	 public IBinder onBind(Intent arg0) {
	  // TODO Auto-generated method stub
	  parm = cam.getParameters();
	  parm.setFlashMode(Parameters.FLASH_MODE_TORCH);
	  cam.setParameters(parm);
	  return this.onBind(arg0);
	 }
	 
	 @Override
	 public void onCreate() {
	  // TODO Auto-generated method stub
		//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
		 previewHolder = preview.getHolder();
		 previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);	
		 //>2013/01/19-33169-ZhiweiWang.
	  cam = Camera.open();
	  cam.startPreview();
	//<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	   try {
			cam.setPreviewDisplay(previewHolder);
			cam.setPreviewCallback(previewCallback);
			cam.setDisplayOrientation(90);
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }
		 //>2013/01/19-33169-ZhiweiWang.
	 }
	 
	 @Override
	 public void onDestroy() {
	  // TODO Auto-generated method stub
	  parm = cam.getParameters();
	  parm.setFlashMode(Parameters.FLASH_MODE_OFF);
	  //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	  cam.setPreviewCallback(null);
	  cam.stopPreview();
	  //>2013/01/19-33169-ZhiweiWang.
	  cam.setParameters(parm);
	  cam.release();
	  cam = null;
	 }
	 
	 @Override
	 public void onStart(Intent intent, int startId) {
	  // TODO Auto-generated method stub
	  parm = cam.getParameters();
	  //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	  parm.setFlashMode(Parameters.FLASH_MODE_TORCH);
	  parm.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);	
	  parm.setPreviewSize(640, 480);
	  cam.setParameters(parm);
	  //>2013/01/19-33169-ZhiweiWang.
	  //parm.setFlashMode(Parameters.FLASH_MODE_TORCH);
	  //cam.setParameters(parm);
	  
	 }
	 
	 @Override
	 public boolean onUnbind(Intent intent) {
	  // TODO Auto-generated method stub
	  parm = cam.getParameters();
	  parm.setFlashMode(Parameters.FLASH_MODE_OFF);
	  cam.setParameters(parm);
	  cam.release();
	  cam = null;
	  return super.onUnbind(intent);
	 }
	 
	 //<2013/01/19-33169-ZhiweiWang,[HealthyLiving][InstantHeartRate]Check the HW info of FlashLight before test and debug test method.
	 public static void getSurfaceView(SurfaceView surfaceView,TextView textView){
		 preview = surfaceView;
		 text = textView;
	 }
	 
	 public static PreviewCallback previewCallback = new PreviewCallback() {

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
				if (totalTimeInSecs >= 3) {
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
					//instantHeartRate = String.valueOf(beatsAvg);
					text.setText(String.valueOf(beatsAvg));
					HeartActivity.getNowHeartRate(String.valueOf(beatsAvg));
					//textView.setText(String.valueOf(beatsAvg));
					startTime = System.currentTimeMillis();
					beats = 0;
				}
				processing.set(false);
			}
		};
	 //>2013/01/19-33169-ZhiweiWang.
}
