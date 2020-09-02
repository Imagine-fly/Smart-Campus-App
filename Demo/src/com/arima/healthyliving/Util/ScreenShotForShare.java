package com.arima.healthyliving.Util;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;
import android.view.Window;

public class ScreenShotForShare{
	
	@SuppressWarnings("unused")
	public static File GetScreenShotFile(Window window, int width, int height, String shareFileName)  
    { 
		File file = null;
		String shareFolder = getSDCardPath() + File.separator + "HealthyLiving";
		View view = window.getDecorView();
        view.setDrawingCacheEnabled(true);    
        view.buildDrawingCache();    
        Bitmap b1 = view.getDrawingCache();
    
        Rect frame = new Rect();    
        window.getDecorView().getWindowVisibleDisplayFrame(frame);    
        int statusBarHeight = frame.top;    
    
        //<2014/02/17-A123456-RubyJiang, [AAP1302][ColorBlindness]Change result screen style
        //Bitmap Bmp = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);  
        Bitmap Bmp;
        if (width > b1.getWidth())
        	Bmp = Bitmap.createBitmap(b1, 0, statusBarHeight, b1.getWidth(), b1.getHeight() - statusBarHeight);
        else
        	Bmp = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        //>2014/02/17-A123456-RubyJiang
        view.destroyDrawingCache();    
        try {  
            File path = new File(shareFolder);  
            file = new File(shareFolder + shareFileName);  
            if(!path.exists()){  
                path.mkdirs();  
            }  
            if (file.exists()) {
            	file.delete();
            }
            file.createNewFile();
              
            FileOutputStream fos = null;  
            fos = new FileOutputStream(file);  
            if (null != fos) {  
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);  
                fos.flush();  
                fos.close();    
            }  
            return file;
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;
        }  
    }  
  
    private static String getSDCardPath(){
        File sdcardDir = null;
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(!sdcardExist){
            sdcardDir = Environment.getExternalStorageDirectory();
            return sdcardDir.toString();
        }else return "storage/sdcard0";
    }
	
}
