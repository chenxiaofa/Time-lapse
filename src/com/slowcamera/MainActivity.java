package com.slowcamera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
    /** Called when the activity is first created. */   

	private boolean Started = false;
	private boolean Focusing = false;
	private boolean isRecording = false;

	
	private RelativeLayout layout =null;
	private RelativeLayout Mainlayout =null;
    

	private GestureDetector mGestureDetector = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("Avtivity","onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏           
        
        setContentView(R.layout.main);        
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
        	Initialize();
        	InitTouchEvent();
        }
    }

    private void Initialize(){
    	
    	InitPowermanager();
    	Inittoast();
    	InitViews();
    	InitConf();
    	InitRecorder();
    	InitLEDNotic();
    }
    
    private mConfiguration cfg = null;
    private void InitConf(){
    	Context cont = this.getApplicationContext();
    	File configFile = new File(cont.getFilesDir().getPath()+"config.cfg");
    	
    	Log.i("CFG Path",configFile.getPath());
    	
    	if(!configFile.exists()){
    		ShowMsg("WritingCFG");
	        DisplayMetrics dm = new DisplayMetrics();  
	        getWindowManager().getDefaultDisplay().getMetrics(dm);  
	        cfg = new mConfiguration();
	        cfg.Default(dm.widthPixels, dm.heightPixels);
	        try {
	        	configFile.createNewFile();
	        	ObjectOutputStream  objoutput = new ObjectOutputStream (new FileOutputStream(configFile));
	        	objoutput.writeObject(cfg);
	        	objoutput.flush();
	        	objoutput.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

    	}else{
    		Log.i("CFG","ReadingCFG");
    		try {
				ObjectInputStream objinput = new ObjectInputStream(new FileInputStream(configFile));
				cfg = (mConfiguration)objinput.readObject();
				objinput.close();
    		} catch (Exception e) {
    			Log.e("Error",e.getMessage());
				e.printStackTrace();
			} 
    	}

    }
    
	private NotificationManager nm=null;
	private Notification notification=null;
	private int NoticID = 0;
    private void InitLEDNotic(){
    	if(this.nm!=null)
    		return;
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); 
        notification = new Notification(); 

        notification.defaults |= Notification.DEFAULT_LIGHTS; 
        notification.ledARGB = 0xFF0000;  //这里是颜色，我们可以尝试改变，理论上0xFF0000是红色，0x00FF00是绿色
        notification.ledOnMS = 100; 
        notification.ledOffMS = 100; 
        notification.flags = Notification.FLAG_SHOW_LIGHTS; 
        NoticID = (int) (Math.random()*1000);
    }
    private void StartLED(){
    	nm.notify(NoticID,notification);
    }
    private void StopLED(){
    	nm.cancel(NoticID);
    }
    
	private SurfaceView mSurfaceview = null;
	private ImageButton btStart=null;
	private ImageButton btStop=null;
	/*private TextView 	txtstatus = null,
						txtFramecounter=null,
						txtcodedcount=null,
						txtcfg=null;*/
    private void InitViews(){
        layout = (RelativeLayout)this.findViewById(R.id.mainlaout);
        btStart = (ImageButton)this.findViewById(R.id.btStart);
        btStart.setOnClickListener(new btStartListener());
        btStart.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() ==  MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(R.drawable.btn_video_shutter_pressed_holo);
				else if(event.getAction() == MotionEvent.ACTION_UP)
					v.setBackgroundResource(R.drawable.btn_video_shutter_holo);
				return false;
			}
		});
        btStop = (ImageButton)this.findViewById(R.id.btStop);
        btStop.setOnClickListener(new btStopListener());
        btStop.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() ==  MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(R.drawable.btn_video_shutter_recording_holo);
				else if(event.getAction() == MotionEvent.ACTION_UP)
					v.setBackgroundResource(R.drawable.btn_video_shutter_recording_pressed_holo);
				return false;
			}
		});
    	mSurfaceview = (SurfaceView) this.findViewById(R.id.surfaceView1);
    	mSurfaceview.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					if(vr!=null)
						vr.AutoFocus();
				}
				return true;
			}
		});
    	Mainlayout =(RelativeLayout)MainActivity.this.findViewById(R.id.screen);

    }
    
    
    private Toast toast = null;
    private void Inittoast(){
    	if(this.toast!=null)
    		return;
    	toast = Toast.makeText(this, "", 1);
    }
    
    
	PowerManager powerManager = null;   
	WakeLock wakeLock = null;
    private void InitPowermanager(){
    	if(this.powerManager!=null)
    		return;
        this.powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);  
        this.wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
        												this.getClass().getName());

    }
    
    
	private VideoRecoder vr = null;
	private Handler handler;
    private void InitRecorder(){
        vr = new VideoRecoder(layout,mSurfaceview);
        
        //584x480   853*480
        vr.SetPreviewSize(cfg.PreviewWidth,cfg.PreviewHeight);
        vr.SetSurfaceSize(cfg.SurfaceWidth,cfg.SurfaceHeight);
        

        
        vr.addCallBack(vrcb);
        handler = new Handler(){
        	public void handleMessage(Message msg){
        		switch(msg.what){
        		case 0:
        			//initializing
        			break;
        		case -1:
        			//sdcard_not_ready
        			btStart.setEnabled(false);
        			btStop.setEnabled(false);
        			btStop.setVisibility(View.INVISIBLE);
        			ShowMsg(vr.ErrorMSG);
        			//RelativeLayout errorlayout = (RelativeLayout) View.inflate(MainActivity.this, R.layout.errorview, null);
        			//errorlayout.setLayoutParams(new ViewGroup.LayoutParams(cfg.PreviewWidth, cfg.PreviewHeight));
        			//Mainlayout.addView(errorlayout);
        			break;
        		case 1:
        			//ready
        			btStart.setVisibility(View.VISIBLE);
        			btStop.setVisibility(View.INVISIBLE);
        			btStart.setEnabled(true);
        			btStop.setEnabled(false);
        			//txtstatus.setText("Status:Ready");
        			ShowMsg("Ready to start");
        			break;
        		case 2:
        			//recording
        			StartLED();
        			//txtstatus.setText("Status:Recording");
        			btStart.setEnabled(false);
        			btStop.setEnabled(true);
        			btStop.setVisibility(View.VISIBLE);
        			btStart.setVisibility(View.INVISIBLE);
        			ShowMsg("Recording");
        			break;
        		case 3:
        			StopLED();
        			btStart.setEnabled(false);
        			btStop.setEnabled(false);
        			btStart.setVisibility(View.INVISIBLE);
        			//txtstatus.setText("Status:Stopping");
        			
        			ShowMsg("Stopping");
        			break;
        		case 5:
        			//finished
        			//txtstatus.setText("Status:Stoped");
        			ShowMsg("Done!");
        			vr.StartPreview();
        			btStart.setEnabled(true);
        			btStop.setEnabled(false);
        			btStart.setVisibility(View.VISIBLE);
        			btStop.setVisibility(View.INVISIBLE);
        			break;
        		case 10:
        			vr.switchsize();
        			
        			//handler.sendEmptyMessage(11);
        			break;
        		case 11:
        			while(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
        				Log.e("resize","waiting SCREEN ORIENTATION chuange");
        			}
        			vr.Resize();
        			break;
        			
        		}

                Started = true;
        	}
        };
			
        vr.Initialize();
    }
    
    public void onConfigurationChanged(Configuration cfg){
    	Log.i("cfg",String.valueOf(cfg.orientation));
    	super.onConfigurationChanged(cfg);
    	if(cfg.orientation==2){
    		handler.sendEmptyMessage(11);
    	}else if(cfg.orientation==1){
    		handler.sendEmptyMessage(10);
    	}
    		
    }
    
    
    private void screenoff(){
    	this.wakeLock.release();
        this.wakeLock = this.powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        this.wakeLock.acquire();
    }
    private void screenon(){
    	this.wakeLock.release();
        this.wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getClass().getName());
        this.wakeLock.acquire();
    }
    private void ShowMsg(String str){
    	toast.setText(str);
    	toast.show();
    }
    private VRCallBack vrcb = new VRCallBack(){
	    public void onStatusChanged(int status) {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(status);
		}
	
		public void Framecounter(int Counter) {
			// TODO Auto-generated method stub
			//UT.Set(txtFramecounter,"Frame Captured:".concat(String.valueOf(Counter)));
			Log.i("Encoded Frame",String.valueOf(Counter));
		}
	
		public void CodedFrameCounter(int Counter) {
			// TODO Auto-generated method stub
			//UT.Set(txtcodedcount,"Frame Encoded:".concat(String.valueOf(Counter)));
		}
    };
	private updateTextView UT = new updateTextView();
	class updateTextView implements Runnable{
		private TextView txt=null;
		private String str = null;
		public void Set(TextView v,String s){
			txt = v;
			str = s;
			handler.post(this);
		}
		
		public void run() {
			// TODO Auto-generated method stub
			txt.setText(str);
		}
	}
    class btStartListener implements OnClickListener{
    	
    	public void onClick(View v) {
    		if(!isRecording){
	        	isRecording = true;
		    	vr.StartRecord(cfg.SPF,cfg.FrameRate,cfg.Mode);
		    	v.setEnabled(false);
    		}
    	}
    }
    class btStopListener implements OnClickListener{
    	
    	public void onClick(View v) {
    		if(isRecording){
				isRecording = false;
				vr.StopRecord();
				v.setEnabled(false);
    		}
    	}
    }
    private boolean ending = false;
    private long timemark = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//ShowMsg(String.valueOf(keyCode));
    	switch(keyCode){
    	case 80:
        	if(!Focusing){
        		vr.AutoFocus();
        		Log.w("KeyDown", "Focusing");
        		Focusing = true;
        		return true;
        	}
        	return true;
    	case 24:
    		//vr.zoomin();
    		vr.exposureinc();
    		return true;
    		
    	case 25:
    		//vr.zoomout();
    		vr.exposuredec();
    		return true;
    	case 4:
    		if(System.currentTimeMillis() - timemark > 1000){
    			timemark=System.currentTimeMillis();
    			this.ShowMsg("Click BACK again to close application!");
    			return false;
    		}
    		else{
    			ending = true;
    		}
    		break;
    	case 82:
    		break;
    	case 84:
    		if(cfg.Mode==1&&this.isRecording){
    			vr.CaptureFrame();
    		}
    		ShowMsg("Captured a freme");
    		//vr.ShowParam();
    		break;
    	}
    	return super.onKeyDown(keyCode, event);   	
    }
/**/    
    
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	switch(keyCode){
    	case 24:    		
    		return true;
    	case 25:
    		return true;
    	case 4:
    		
    	case 80:
        	Focusing = false;
    		return true;
    	}
    	return super.onKeyUp(keyCode, event);

    }
    
    
    @Override
    protected void onResume() {
     this.wakeLock.acquire();
     screenon();
     super.onResume();
     if(this.configuring){
    	 this.configuring = false;
    	 this.Initialize();
     }
    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
    	
    }
    protected void onPause() {
    	screenoff();
    	super.onPause();
    }
    
    protected void onDestroy(){
    	if(ending){
	    	if(vr!=null)
	    		vr.onDestroy();
	    	this.wakeLock.release();
	    	if(Started){
	    		super.onDestroy();
	    		System.exit(0);
	    	}
    	}
    	
    }

    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.i("isRecording",String.valueOf(isRecording));
    	if(!isRecording){
	    	menu.add(Menu.NONE,1,1,"Setting");
			return true;
    	}
    	return false;
    	
    }
    private boolean configuring = false;
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case 1:
    		Intent i = new Intent();
    		i.setClass(MainActivity.this,ConfActivity.class);
    		MainActivity.this.vr.onDestroy();
    		this.startActivity(i);
    		MainActivity.this.configuring = true;
    		break;
    	}
		return true;
    	
    }
    PreviewLayout imagePreview = null;
    boolean showed = false;
/*    
    public boolean onTouchEvent(MotionEvent e) {
		//return mGestureDetector.onTouchEvent(e);
    }
/**/
    private void InitTouchEvent(){
    	/*
    	imagePreview = new PreviewLayout(this);
    	imagePreview.setSize(cfg.PreviewWidth, cfg.PreviewHeight);
    	imagePreview.Init();
    	mGestureDetector = new GestureDetector(new SimpleOnGestureListener(){
    	
    		public boolean onDown(MotionEvent e) { 
    			
    		    	if(showed){
    		    		imagePreview.RemoveOut(Mainlayout);
    		    		showed=false;
    		    	}else{
    		    		imagePreview.Showin(Mainlayout);
    		    		showed=true;
    		    	}
    			return true; 
    			  
    		}
    	});
    	/**/
    }
    
    
    
    
}