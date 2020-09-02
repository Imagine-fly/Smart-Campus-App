package com.arima.healthyliving.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

   public class RotateView extends TextView {
	    private  static  final  String  NAMESPACE = "http://myrotate.com/apk/res/myrotate";
	    private  static  final  String  ATTR_ROTATE = "rotate";
	    private  static  final  int  DEFAULTVALUE_DEGREES = 0;
	    private  int  degrees ;
	    
	    public RotateView(Context context){
	          super(context);
	    }
	    
	    public  RotateView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        degrees = attrs.getAttributeIntValue(NAMESPACE, ATTR_ROTATE, DEFAULTVALUE_DEGREES);
	    }
	    
	    public RotateView(Context context, AttributeSet attrs,int defStyle) {
	        super(context, attrs,defStyle);                        
	    }
	    
	    @Override
	    protected  void  onDraw(Canvas canvas) {
	    	//canvas.save();
	        canvas.rotate(degrees,getMeasuredWidth()/2,getMeasuredHeight()/2);
	        super.onDraw(canvas);
	        //canvas.restore();
	    }
	}
