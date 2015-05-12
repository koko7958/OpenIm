package com.way.activity;

import com.way.service.XXService;

import android.app.Activity;

public class MyBaseActivity extends Activity{

	protected XXService mXxService;
	
	public XXService getXXService(){
		return mXxService;
	};
	
}
