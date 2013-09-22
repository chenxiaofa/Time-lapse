package com.slowcamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.H264.EncoderCallBack;
import com.H264.H264Encoder;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class VideoRecoder implements SurfaceHolder.Callback {
	public int status = 0;
	private int iMode = 0;
	private VRCallBack cb = null;
	private Camera camera = null;
	private SurfaceView surface = null;
	private SurfaceHolder mHolder = null;
	private H264Encoder Encoder = null;
	private int previrewidth = 0;
	private int previreheight = 0;
	private int surfacewidth = 0;
	private int surfaceheight=0;
	private boolean Previewing = false;
	private int FrameCounter = 0;
	private RelativeLayout layout=null;
	private FrameCapturer framecapturer = null;
	private boolean enableExposure = false;
	public VideoRecoder(RelativeLayout l,SurfaceView s){
		surface = s;		
		layout = l;
	}
	public void SetSurfaceSize(int width,int height){
		surfacewidth = width;
		surfaceheight = height;
	}
	public void SetPreviewSize(int width,int height){
		previrewidth = width;
		previreheight = height;
	}
	public void addCallBack(VRCallBack CallBack){
		cb = CallBack;
	}
	public boolean Initialize(){
		Log.i("flag","On Initialize");
		if(!SDCard_is_ready()){
			status=-1;
			//#error 6#
			onError(6 , "SDCard Not Ready");
			return false;
		}
		
		try{
			camera = Camera.open();
			
	    	mHolder = surface.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
	    	mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    	mHolder.addCallback(this); // SurfaceHolder加入回调接口
		}catch(Exception e){
			Log.e("Error",e.getMessage());
			//#error 1#
			onError(1,e.getMessage());
			e.printStackTrace();
			return false;
		}
/**/
		return true;
		
	}
	public void AutoFocus(){
		if(camera!=null)
			this.exposureunlock();
			camera.autoFocus(new Camera.AutoFocusCallback() {
				
				public void onAutoFocus(boolean success, Camera camera) {
					// TODO Auto-generated method stub
					exposurelock();
				}
			});
	}
	public boolean StartRecord(long SPF,int FrameRate,int Mode){
		if(!SDCard_is_ready()){
			status=-1;
			cb.onStatusChanged(status);
			return false;
		}
		if(this.status==1){
			this.iMode=Mode;
			FrameCounter = 0;
			Encoder = new H264Encoder();
			Encoder.addCallBack(encodercb);
			Encoder.StartRecord(getOutputFLVPath(), previrewidth, previreheight,FrameRate);
			framecapturer=new FrameCapturer(Mode,SPF);
			if(Mode==0)
				framecapturer.start();
			this.status = 2;
			cb.onStatusChanged(this.status);
			return true;
		}else{
			return false;
		}
		
	}

	public boolean EnableDisplay(){
		if(camera!=null)
			try {
				//camera.setPreviewDisplay(this.mHolder);
				Camera.Parameters param = camera.getParameters();
				param.setPreviewFrameRate(25);
				camera.setParameters(param);
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//#error 2#
				onError(2,e.getMessage());
				e.printStackTrace();
				return false;
			}
		return false;
	}
	public boolean StopRecord(){
		
		if(this.status==2){
			camera.setPreviewCallback(null);
			framecapturer.StopCaputure();
			status = 3;
			cb.onStatusChanged(status);
			camera.stopPreview();
			Encoder.stopRecord();
			
			return true;
		}else{
			return false;
		}
		
	}
	public void StopPreview(){
		if(this.status!=2)
			camera.stopPreview();
	}
	public void StartPreview(){
		try{
		camera.startPreview();
		exposureinit();
		}catch(Exception e)
		{
			//#error 3#
			onError(3,e.getMessage());
			Log.e("Error",e.getMessage());
		}
	}
	
	private EncoderCallBack encodercb = new EncoderCallBack(){

		public void OnEncodeCompleted(int Counter) {
			// TODO Auto-generated method stub
			Log.i("encoded",String.valueOf(Counter));
			cb.CodedFrameCounter(Counter);
		}

		public void OnFinished() {
			// TODO Auto-generated method stub
			status = 1;
			cb.onStatusChanged(5);
		}

		public void OnError(int errid, String errmsg) {
			// TODO Auto-generated method stub
			onError(errid,errmsg);
		}
		
	};
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.i("flag","surfaceChanged");
		Log.i("flag",String.valueOf(Previewing));
		Log.i("SizeChange",String.valueOf(width)+"x"+String.valueOf(height));
		if(Previewing){
			
		    //cb.onStatusChanged(10);

			return;
		}
			
		
    	try{
	    	if(camera != null){
				camera.setPreviewDisplay(holder);
		    	Camera.Parameters param = camera.getParameters();
		    	//param.setPreviewFrameRate(20);
		    	param.setPreviewSize(previrewidth,previreheight);
		    	param.setPreviewFormat(PixelFormat.YCbCr_420_SP);
		    	//param.set("auto-exposure-lock", "true");
				camera.setParameters(param);
				camera.startPreview();
				Previewing = true;
		    	this.status = 1;
		    	cb.onStatusChanged(status);
				
	    	}
    	}catch(Exception e){
    		e.printStackTrace();
			//#error 4#
			onError(4,e.getMessage());
    		Log.e("Error",e.getMessage());
    	}
	}
	public String ErrorMSG = null;
	public void onError(int errid,String errmsg){
		this.ErrorMSG = errmsg;
		this.cb.onStatusChanged(-1);
		this.onDestroy();
	}
	public void surfaceCreated(SurfaceHolder holder) {

		Log.i("flag","surfaceCreated");
		this.Resize(surfacewidth, surfaceheight);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i("flag","surfaceDestroyed");
		this.onDestroy();
		
	}
	public void switchsize(){
		this.Resize(previreheight, previrewidth);
	}
	
    public void Resize( int width, int height){  
        //layout.getLayoutParams().width=width;
        //layout.getLayoutParams().height=height;
    	//RelativeLayout.LayoutParams params  =   
    	//new RelativeLayout.LayoutParams(width, height);  
    	
    	       Log.i("Resize",String.valueOf(width)+"x"+String.valueOf(height));
    	       
    	       
    	//params.leftMargin = 0;  
    	//params.topMargin  = 0;  
    	//surface.setLayoutParams(params);  
    	surface.getHolder().setFixedSize(width, height);   
    } 
    public void Resize(){
    	Resize(this.surfacewidth,this.surfaceheight);
    }
    public void onDestroy(){
    	if(camera!=null){
    		camera.setPreviewCallback(null);
    		camera.stopPreview();
    		camera.release();
    		camera=null;
    	}
    	if(Encoder!=null)
    		Encoder.Destroy();
    	if(framecapturer!=null){
    		framecapturer.StopCaputure();
    	}
    		
    }
	private static String getOutputFLVPath(){ 
        // 安全起见，在使用前应该
        // 用Environment.getExternalStorageState()检查SD卡是否已装入
		if(!SDCard_is_ready())
			return null;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/"); 
        Log.w("FileDir", mediaStorageDir.getPath());
        // 如果期望图片在应用程序卸载后还存在、且能被其它应用程序共享，

        if (! mediaStorageDir.exists()){ 
            if (! mediaStorageDir.mkdirs()){ 
                Log.d("MyCameraApp", "failed to create directory"); 
                return null; 
            } 
        } 

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); 
        return mediaStorageDir.getPath() + File.separator + 
        		"Video"+ timeStamp + ".mp4"; 
                   // "Video"+ timeStamp + ".flv"; 

    }
    private static boolean SDCard_is_ready(){
    	if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
    	{
    		Log.e("Error", "SD Card not ready");
    		return false;
    	}
    	return true;
    }
    public boolean CaptureFrame(){
    	if(this.status == 2&&this.iMode==1){
    		camera.setPreviewCallback(framecapturer);
    		return true;
    	}
    	else
    		return false;
    }
    class FrameCapturer extends Thread implements Camera.PreviewCallback{
    	private long ltSPF = 0;
    	private boolean isCapturu = false;
    	private int iMode = 0;
    	public FrameCapturer(int Mode,long t){
    		iMode = Mode;
    		ltSPF = t;
    		isCapturu = true;
    	}
		public void onPreviewFrame(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			camera.setPreviewCallback(null);
			Encoder.PutFrame(data);
			cb.Framecounter(++FrameCounter);
		}
		public void run(){
			while(isCapturu){
				camera.setPreviewCallback(this);
				if(iMode==0)
					try {
						Thread.sleep(ltSPF);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//#error 5#
						onError(5,e.getMessage());
						e.printStackTrace();
					}
			}
		}
		public void StopCaputure(){
			isCapturu = false;
		}
    	
    }
    private int zoom = 0;
    public boolean zoomout(){
    	Camera.Parameters param = camera.getParameters();
    	param.set("zoom", zoom==0?0:--zoom);
    	camera.setParameters(param);
		return false;
    	
    }
    public boolean zoomin(){
    	Camera.Parameters param = camera.getParameters();
    	param.set("zoom", zoom==5?5:++zoom);
    	camera.setParameters(param);
		return false;
    	
    }
    public void ShowParam(){
    	Camera.Parameters param = camera.getParameters();
    	Log.i("flatten",param.flatten());
    	
    }
    private String[] exposureValues = null;
    private int exposureoffsetindex = 0; 
    private int maxindex = 0;
    private void exposureinit(){
    	

    	Camera.Parameters param = camera.getParameters();
    	String str = param.get("exposure-offset-values");
    	String nowvalue = param.get("exposure-offset");
    	camera.setParameters(param);
    	if(str!=null){
    		this.enableExposure=true;
	    	exposureValues = str.split(",");
	    	for(int i = 0;i<exposureValues.length;i++){
	    		if(nowvalue.equals(exposureValues[i]))
	    			exposureoffsetindex = i;
	    	}
	    	maxindex = exposureValues.length-1;
    	}
    	/**/
    }
    public void exposurelock(){
    	//Camera.Parameters param = camera.getParameters();
    	//param.set("auto-exposure-lock", "true");
    	//camera.setParameters(param);
    }
    public void exposureunlock(){
    	//Camera.Parameters param = camera.getParameters();
    	//param.set("auto-exposure-lock", "false");
    	//camera.setParameters(param);
    }
    public void exposureinc(){
    	ShowParam();
    	if(exposureoffsetindex < maxindex&&this.enableExposure){
    		
    		Camera.Parameters param = camera.getParameters();
    		exposureoffsetindex++;
    		param.set("exposure-offset", exposureValues[exposureoffsetindex]);
    		camera.setParameters(param);
        	Log.i("exposureoffset", exposureValues[exposureoffsetindex]);
    	}
    }
    public void exposuredec(){
    	exposureinit();
    	if(exposureoffsetindex > 0&&this.enableExposure){
    		Camera.Parameters param = camera.getParameters();
    		exposureoffsetindex--;
    		param.set("exposure-offset", exposureValues[exposureoffsetindex]);
    		camera.setParameters(param);
        	Log.i("exposureoffset", exposureValues[exposureoffsetindex]);
    	}
    }
}
