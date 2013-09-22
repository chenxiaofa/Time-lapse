package com.H264;


import android.util.Log;
public class H264Encoder{
	long encoder=0;
	private mQueue queue = null;
	byte[] h264Buff =null;
	byte[] sdata =null;
	private boolean isRecording = false;
	private int FrameCounter = 0;
	private int CodedCounter = 0;
	private EncodeThread encode=null;
	private boolean flagDone = false;
	private boolean isDestroy = false;
	private EncoderCallBack cb = null;
	static {
		System.loadLibrary("Encoder");
	}
	public void Destroy(){
		isDestroy = true;
		Log.w("Control","H264Encoder Destory");
		if(encode!=null){
			Log.w("Control","encode Destory");
			Log.w("Running", encode.getState().toString());
			while(!flagDone)
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//encode.interrupt();
			encode = null;
		}
		
		if(queue!=null){
			Log.w("Control","queue Destory");
			queue.Destroy();
			queue=null;
		}
		h264Buff=null;
		sdata=null;

	}
	public void addCallBack(EncoderCallBack CallBack){
		cb = CallBack;
	}
	
	private class EncodeThread extends Thread { 
		public EncodeThread(){
			Log.w("Control","Thread Create");
		}
		public void run(){
			//long ts=0;
			//long fc = 0;
			Log.w("Control","Thread Start");
			while(true){
				if((sdata = queue.Qout())!=null){
					try{
						CompressBuffer(encoder, -1, sdata, sdata.length,h264Buff);
						cb.OnEncodeCompleted(++CodedCounter);


					}catch(Exception e){
						Log.w("Error",e.getMessage());
					}
				}else{
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				}
				if(!isRecording&&queue.Qempry()){
					Log.w("Encoder","Thread end");
					CompressEnd(encoder);
					Log.w("Control","ended");
					flagDone = true;
					cb.OnFinished();
					return;
				}
				if(isDestroy){
					flagDone = true;
					return;
				}
				
			}
		}
	};
	
	public void StartRecord(String flvpath,int width, int height,int FrameRate) {
		byte[] fname = new byte[flvpath.length()+1];
		System.arraycopy(flvpath.getBytes(),0 , fname, 0, flvpath.length());
		queue= new mQueue(width,height);
		encoder = CompressBegin(width, height,FrameRate,fname);
		if(encoder<=0)
			Log.e("encoder","begin faild");
		h264Buff = new byte[width * height *8];
		Thread encode = new EncodeThread();
		isRecording = true;
		encode.start();

	};
	
	public void stopRecord(){
		isRecording = false;
	}
	protected void finalize()
    {
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private native long CompressBegin(int width,int height,int FrameRate,byte[] filepath);
	private native int CompressBuffer(long encoder, int type,byte[] in, int insize,byte[] out);
	private native int CompressEnd(long encoder);

	public void PutFrame(byte[] data) {	
			queue.Qin(data);	
			FrameCounter++;

	}
}