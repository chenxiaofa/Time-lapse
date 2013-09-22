package com.slowcamera;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PreviewLayout extends LinearLayout{
	Animation animIn=null,animOut=null;
	int status = 1;
	Context context =null;
	ArrayList<ImageView> images = null;
	LinearLayout newlayout =null;
	public PreviewLayout(Context context) {
		super(context);
		this.context = context;
		newlayout = (LinearLayout) View.inflate(context, R.layout.previewlayout, null);
		images = new ArrayList<ImageView>();
		
    	ImageView image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	/*
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	image = new ImageView(context);
    	image.setImageResource(R.drawable.ic_launcher);
    	images.add(image);
    	/**/
    	mGallery = (Gallery) newlayout.getChildAt(0);
    	
    	

	}
	Gallery mGallery = null;
    public void setSize(int w,int h){
    	newlayout.setLayoutParams(new ViewGroup.LayoutParams(w, h));
    }
    
    public void Init(){
    	mGallery.setAdapter(new ImageAdapter());
    	this.addView(newlayout);
    }
	public boolean showing = false;
	public void onAnimationEnd(){
		showing = false;
		super.onAnimationEnd();
	}
	public void onAnimationStart(){
		showing = true;
		super.onAnimationStart();
	}
	public void Showin(RelativeLayout l){
		animIn =AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
		this.setAnimation(animIn);
		l.addView(this);
		this.status = 1;
	}
	public void RemoveOut(RelativeLayout l){
		animOut =AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
		this.setAnimation(animOut);
		l.removeView(this);
		this.status = 2;
	}
	private class ImageAdapter extends BaseAdapter{
		
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			images.get(position).setLayoutParams( new Gallery.LayoutParams(136, 88));
			return images.get(position);
		}

	}
	
	
}
