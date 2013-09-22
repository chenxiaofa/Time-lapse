package com.H264;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.os.Environment;
import android.util.Log;
//Java 1970年1月1日0时起的毫秒数
public class mQueue {
	private String SDCardPath = null;
	private String CachePath = "/cache/SlowCamera/";
	private File fout=null,fin=null;
	private RandomAccessFile rfout=null,rfin=null;
	private long h=0,r=0;
	private byte writercount = 0;
	private byte[] b = null;
	private byte[][] cache    = {null ,null ,null ,null ,null };
	private boolean[] isusing = {false,false,false,false,false};
	private byte ch=0,cr=0;
	public mQueue(int w,int h){
		SDCardPath = Environment.getExternalStorageDirectory().getPath();
		File Cache = new File(SDCardPath + CachePath); 
		if(!Cache.exists())
			Cache.mkdir();
		CachePath = Cache.getPath() + "/";
		for(int i=0;i<5;i++)
			cache[i] = new byte[((w*h)/2)*3];
		b = new byte[((w*h)/2)*3];
		
	}
	private void printstatus(){
		Log.i("h",String.valueOf(h));
		Log.i("r",String.valueOf(r));
		Log.i("ch",String.valueOf(ch));
		Log.i("cr",String.valueOf(cr));
		Log.i("Count",String.valueOf(writercount));
	}
	public void Qin(byte[] data){

		if(!isusing[ch]&&h==r){
			isusing[ch] = true;
			System.arraycopy(data, 0, cache[ch], 0, data.length );
			ch = (byte) (++ch % 5);
		}
		else{
				try {
					fout = new File(CachePath + "f"+String.valueOf(h));
					if(fout.exists())
						fout.delete();
					rfout = new RandomAccessFile(fout,"rw");
					rfout.write(data);
					rfout.close();
					h++;
				}catch(Exception e){		
				}
				fout=null;
				rfout=null;
		}
		printstatus();
	}
	
	public boolean Qempry(){
		return (!isusing[cr])&&(h==r);
	}

	public byte[] Qout(){		
		
		if(!isusing[cr]){
			return null;
		}else{
			Log.i("","ReadCache");
			byte[] returning = cache[cr];
			isusing[cr] = false;
			cr = (byte)(++cr%5);
			if(r!=h){
				fin = new File(CachePath + "f"+String.valueOf(r));
				r++;
				if(fin.exists()){
					try {
						rfin = new RandomAccessFile(fin,"r");
						isusing[ch] = true;
						rfin.read(cache[ch]);
						ch = (byte) (++ch % 5);
						fin.delete();
						fin=null;
						rfin=null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
			}
			printstatus();
			return returning;
			
		}			

	}
	class cacheWriter extends Thread{
		private byte[] data = null;
		private String filepath = null;
		cacheWriter(byte[] d,String path){
			
			data = d;
			filepath = path;
			
		}
		public void run(){
			File fout = new File(filepath);
			if(fout.exists())
				fout.delete();
			try {
				RandomAccessFile rfout = new RandomAccessFile(fout,"rw");
				rfout.write(data);
				rfout.close();
				fout=null;
				rfout=null;
				data=null;
				writercount--;
				return;
			}catch(Exception e){
				Log.w("Error","WriteCache"+e.getMessage());
			}

		}
		
		
	}
	
	
	public void Destroy(){
		Log.w("QU","Destory");
		rfout=null;
		rfin=null;
		for(int i=0;i<5;i++)
			cache[i] = null;
		
	}
	 
}
