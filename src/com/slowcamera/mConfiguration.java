package com.slowcamera;

import java.io.Serializable;

import android.util.Log;


public class mConfiguration implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public int Mode = 0;
	public int PreviewWidth = 0;
	public int PreviewHeight = 0;
	public int SurfaceWidth = 0;
	public int SurfaceHeight = 0;
	public long SPF = 0;
	public int FrameRate = 0;
	public boolean showparam = false;
	private int SW = 0;
	private int SH = 0;
	public void Default(int ScreenWidth,int ScreenHeight){
		SW = ScreenWidth;
		SH = ScreenHeight;
        //854¡Á480
		//800¡Á480
		//176x144,320x240,352x288,640x480,720x480,720x576,800x448,848x480,1280x720

		this.PreviewWidth = 1280;
		this.PreviewHeight = 720;

		this.SPF = 2000;
		this.FrameRate = 20;
		this.setDisplaySize();
		
	}
	public void setDisplaySize(){
		double tempw = SW - 300;
		double temph = SH;
		boolean flag = (this.PreviewWidth * (temph / this.PreviewHeight)) > tempw;
		
		if(flag){
			temph = this.PreviewHeight * (tempw / this.PreviewWidth);
		}else{
			tempw = this.PreviewWidth * (temph / this.PreviewHeight);
		}
		
			
		this.SurfaceWidth = (int)tempw;
		this.SurfaceHeight = (int)temph;
	}
	public String GetCFG(){
		String str = "";
		str = str.concat("Preview Size:".concat(String.valueOf(this.SurfaceWidth)+"x"+String.valueOf(this.SurfaceHeight))).concat("\r\n");
		str = str.concat("FPS:".concat(String.valueOf((double)(1/((double)this.SPF/1000))))).concat("\r\n\r\n\r\n");
		str = str.concat("Video Size:".concat(String.valueOf(this.PreviewWidth)+"x"+String.valueOf(this.PreviewHeight))).concat("\r\n");
		str = str.concat("Video FrameRate:"+String.valueOf(this.FrameRate));
		return str;
	}
}
