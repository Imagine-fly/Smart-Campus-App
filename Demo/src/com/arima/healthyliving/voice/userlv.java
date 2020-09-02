package com.arima.healthyliving.voice;

public class userlv {
	private String date;
	private String time;
	private String sign;
	public String getdate(){
		return date;
	}
	public void setdate(){
		this.date=date;
	}
	
	
	public String getsign(){
		return sign;
	}
	public void setsign(){
		this.sign=sign;
	}
	
	public String gettime(){
		return time;
	}
	public void settime(){
		this.time=time;
	}
	
	public userlv(String date,String sign,String time){
		super();
		this.date=date;
		this.sign=sign;
		this.time=time;
		
	}

}
