package com.H264;

public interface EncoderCallBack {
	public void OnEncodeCompleted(int Counter);
	public void OnFinished();
	public void OnError(int errid,String errmsg);
}