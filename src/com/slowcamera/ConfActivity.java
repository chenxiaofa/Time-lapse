package com.slowcamera;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfActivity extends ListActivity {
	private class size{
		public int width=0;
		public int height=0;
		public size(int w,int h){
			width = w;
			height = h;
		}
	}
	
	private mConfiguration config = null;
	public final String[] SPF = {
    		"1000","1500","2000","2500","3000","3500","4000","4500","5000","6000"
    		,"7000","8000","9000","10000","15000","20000","30000","40000","50000","60000"
    };
    public final long[] lSPF = {
    		1000,1500,2000,2500,3000,3500,4000,4500,5000,6000
    		,7000,8000,9000,10000,15000,20000,30000,40000,50000,60000
    };
    
    
    public final  String[] Sizes={"176x144","320x240","352x288","480x480","640x480","704x576","720x408","720x480","720x576","768x432","800x448","960x720","1280x720","1360x720","1920x1080"
    };
    
    /*
    		//"176x144","320x240","352x288","640x480","720x480",
    		//"720x576",
    		"528x432",
    		"848x480",
    		"1280x720"
    		
    };*/
    public final  size[] iSize={new size(176,144), new size(320,240), new size(352,288), new size(480,480), new size(640,480), new size(704,576), new size(720,408), new size(720,480), new size(720,576), new size(768,432), new size(800,448), new size(960,720), new size(1280,720), new size(1360,720), new size(1920,1080)
};
    /*
		//new size(176,144),new size(320,240),new size(352,288),
		//new size(640,480),new size(720,480),new size(720,576),
		//new size(800,448),
    	new size(528,432),
		new size(848,480)
		,new size(1280,720)
    };/**/
    public final String[] FrameRate = {
    		"10","15","20","25","30"
    };
    
    public final int[] iFrameRate = {
    		10,15,20,25,30
    };
    
    public final String[] Modes={
    		"Auto","Manual"
    };
    public final int[] iModes={
    		0,1
    };
    public final String[] ifshowparam={
    		"Yes","No"
    };
    public final boolean[] bifshowparam={
    		true,false
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.confactivity);
        setTitle("Setting");
        config = InitConf();
        //spinnerFrameRate = (Spinner)this.findViewById(R.id.spinner1);
        

        CreateConfigurationView();

    }
    private List<List<Object>> items =new ArrayList<List<Object>>();
	private void CreateConfigurationView(){
    	
    	List<Object> row = new ArrayList<Object>();
    	TextView txtview = null;
    	Spinner spinner = null;

    	
    	row = new ArrayList<Object>();
    	
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText("Speed Of Capture");
    	txtview.setTextSize(20);
    	txtview.setPadding(20, 20,0, 25);
    	TextPaint paint = txtview.getPaint();
    	paint.setFakeBoldText(true); 
    	row.add(txtview);
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText(String.valueOf(config.SPF).concat(" MS"));
    	txtview.setPadding(20, 65, 0, 5);
    	row.add(txtview);
    	
    	spinner = new Spinner(ConfActivity.this);
    	
    	ArrayAdapter<String> adt = 	new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, SPF);
    	adt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    	spinner.setAdapter(adt);
    	spinner.setVisibility(View.INVISIBLE);
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				config.SPF = lSPF[arg2];
				TextView txt = (TextView)items.get(0).get(1);
				txt.setText(String.valueOf(config.SPF).concat(" MS"));
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
    	for(int i=0;i<lSPF.length;i++)
    		if(lSPF[i]==config.SPF)
    			spinner.setSelection(i);
    	row.add(spinner);
    	items.add(row);
    	
    	/////////////////////////////////////////////////////////////////////////////////
    	
    	row = new ArrayList<Object>();
    	
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText("Size");
    	txtview.setTextSize(20);
    	txtview.setPadding(20, 20,0, 25);
    	paint = txtview.getPaint();
    	paint.setFakeBoldText(true); 
    	row.add(txtview);
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText(String.valueOf(config.PreviewWidth).concat("x").concat(String.valueOf(config.PreviewHeight)));
    	txtview.setPadding(20, 65, 0, 5);
    	row.add(txtview);
    	
    	
    	spinner = new Spinner(ConfActivity.this);
    	
    	adt = 	new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, Sizes);
    	adt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    	spinner.setAdapter(adt);
    	spinner.setVisibility(View.INVISIBLE);
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				config.PreviewHeight = iSize[arg2].height;
				config.PreviewWidth = iSize[arg2].width;
				config.setDisplaySize();
				//config.SurfaceHeight = iSize[arg2].height;
				//config.SurfaceWidth = iSize[arg2].width;
				TextView txt = (TextView)items.get(1).get(1);
				txt.setText(String.valueOf(config.PreviewWidth).concat("x").concat(String.valueOf(config.PreviewHeight)));

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
    	for(int i=0;i<iSize.length;i++){
    		if(config.PreviewHeight==iSize[i].height&&config.PreviewWidth==iSize[i].width)
    			spinner.setSelection(i);
    	}
    	row.add(spinner);
    	items.add(row);
    	
    	////////////////////////////////////////////////////////////////////
    	
    	row = new ArrayList<Object>();
    	
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText("Video FrameRate");
    	txtview.setTextSize(20);
    	txtview.setPadding(20, 20,0, 25);
    	paint = txtview.getPaint();
    	paint.setFakeBoldText(true); 
    	row.add(txtview);
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText(String.valueOf(config.FrameRate).concat(" FPS"));
    	txtview.setPadding(20, 65, 0, 5);
    	row.add(txtview);
    	
    	spinner = new Spinner(ConfActivity.this);
    	
    	adt = 	new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, FrameRate);
    	adt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    	spinner.setAdapter(adt);
    	spinner.setVisibility(View.INVISIBLE);
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				config.FrameRate = iFrameRate[arg2];
				TextView txt = (TextView)items.get(2).get(1);
				txt.setText(String.valueOf(config.FrameRate).concat(" FPS"));
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
    	for(int i=0;i<iFrameRate.length;i++)
    		if(iFrameRate[i]==config.FrameRate)
    			spinner.setSelection(i);
    	row.add(spinner);
    	items.add(row);
    	//////////////////////////////////////////////////////////////////////////////////
    	////////////////////////////////////////////////////////////////////
    	
    	row = new ArrayList<Object>();
    	
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText("Capture Mode");
    	txtview.setTextSize(20);
    	txtview.setPadding(20, 20,0, 25);
    	paint = txtview.getPaint();
    	paint.setFakeBoldText(true); 
    	row.add(txtview);
    	txtview = new TextView(ConfActivity.this);
    	txtview.setPadding(20, 65, 0, 5);
    	row.add(txtview);
    	
    	spinner = new Spinner(ConfActivity.this);
    	
    	adt = 	new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, Modes);
    	adt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    	spinner.setAdapter(adt);
    	spinner.setVisibility(View.INVISIBLE);
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				config.Mode = iModes[arg2];
				TextView txt = (TextView)items.get(3).get(1);
				txt.setText(Modes[arg2]);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
    	for(int i=0;i<Modes.length;i++)
    		if(iModes[i]==config.Mode){
    			spinner.setSelection(i);
    			txtview.setText(Modes[i]);
    		}
    	row.add(spinner);
    	items.add(row);
    	/**/
    	/*
    	//////////////////////////////////////////////////////////////////////////////////
    	row = new ArrayList<Object>();
    	
    	txtview = new TextView(ConfActivity.this);
    	txtview.setText("Show Parameters");
    	txtview.setTextSize(20);
    	txtview.setPadding(20, 20,0, 25);
    	paint = txtview.getPaint();
    	paint.setFakeBoldText(true); 
    	row.add(txtview);
    	txtview = new TextView(ConfActivity.this);
    	txtview.setPadding(20, 65, 0, 5);
    	row.add(txtview);
    	
    	spinner = new Spinner(ConfActivity.this);
    	
    	adt = 	new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, ifshowparam);
    	adt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    	spinner.setAdapter(adt);
    	spinner.setVisibility(View.INVISIBLE);
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				config.showparam  = bifshowparam[arg2];
				TextView txt = (TextView)items.get(4).get(1);
				txt.setText(ifshowparam[arg2]);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
    	for(int i=0;i<bifshowparam.length;i++)
    		if(bifshowparam[i]==config.showparam){
    			spinner.setSelection(i);
    			txtview.setText(ifshowparam[i]);
    		}
    	row.add(spinner);
    	items.add(row);
    	/**/
    	//////////////////////////////////////////////////////////////////////////////////
    	
    	this.setListAdapter (new MyAdapter(this));
    	
    }
	public void onListItemClick(ListView l, View v, int position, long id) { 

		Spinner sp = (Spinner) (((List<Object>)items.get(position)).get(2));
		//sp.setVisibility(View.VISIBLE);
		sp.performClick();
		Log.i(String.valueOf(position),"aaaa");
		
	}
	public class MyAdapter extends BaseAdapter{

		private boolean[] isloaded = null;
		private LayoutInflater mInflater;
		
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
			isloaded = new boolean[items.size()];
		}
		public int getCount() {
			return items.size();
		}


		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}


		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}


		public View getView(int position, View convertView, ViewGroup parent) {			
			if(convertView==null){
					convertView = mInflater.inflate(R.layout.confview,null);
					RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.rlayout);
					List<Object> row = (List<Object>) items.get(position);
					if(!isloaded[position]){
						for(int i = row.size()-1; i >= 0;i--){
							View v = (View)row.get(i);
							layout.addView(v);
						}
					}
					convertView.setTag(layout);
			}
			if(!isloaded[position])
				isloaded[position]= true;
			
			return convertView;
		}

	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private mConfiguration InitConf(){

        mConfiguration cfg = null;
    	Context cont = this.getApplicationContext();
    	File configFile = new File(cont.getFilesDir().getPath()+"config.cfg");
    	
    	if(!configFile.exists()){
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
	        } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}else{
    		try {
				ObjectInputStream objinput = new ObjectInputStream(new FileInputStream(configFile));
				try {
					cfg = (mConfiguration)objinput.readObject();
					objinput.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		} catch (Exception e) {
				// TODO Auto-generated catch block
    			Log.e("Error",e.getMessage());
				e.printStackTrace();
			} 
    	}
    	return cfg;
    }
    private void SaveCFG(mConfiguration cfg){
    	Log.i("Saving ","CFG");
    	Context cont = this.getApplicationContext();
    	File configFile = new File(cont.getFilesDir().getPath()+"config.cfg");
    	if(configFile.exists())
    		configFile.delete();
    	
    	try {
			configFile.createNewFile();
	    	ObjectOutputStream  objoutput = new ObjectOutputStream (new FileOutputStream(configFile));
	    	objoutput.writeObject(cfg);
	    	objoutput.flush();
	    	objoutput.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    public void onPause(){
    	SaveCFG(this.config);
    	super.onPause();
    }
    public void onDestroy(){
    	
    	//SaveCFG(this.config);
    	super.onDestroy();
    }
}
