package com.way.activity;


import com.way.adapter.NewFriendsAdapter;
import com.way.db.RosterProvider;
import com.way.service.XXService;
import com.way.util.L;
import com.way.util.PreferenceConstants;
import com.way.util.PreferenceUtils;
import com.way.util.XMPPHelper;
import com.way.xx.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class NewFriendsActivity extends MyBaseActivity implements FragmentCallBack{

	private ListView newFriendsList;
	private TextView mTitleNameView;
	private NewFriendsAdapter newFriendsAdapter;
	private ContentResolver mContentResolver;
	private ContentObserver newFriendsObserver;
	private Handler mHandler = new Handler();
//	private XXService mXxService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_new_friends);
		
		mTitleNameView = (TextView) findViewById(R.id.ivTitleName);
		mTitleNameView.setText(R.string.new_friends_title);
		newFriendsList = (ListView)findViewById(R.id.new_friends_list);
		
		newFriendsAdapter = new NewFriendsAdapter(NewFriendsActivity.this);
		
		newFriendsList.setAdapter(newFriendsAdapter);

		mContentResolver = getContentResolver();
		
		newFriendsObserver = new ContentObserver(mHandler) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				newFriendsAdapter.requery();
				
			}
			
		};
	
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mContentResolver.registerContentObserver(RosterProvider.CONTENT_URI, true, newFriendsObserver);	
		bindXMPPService();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		newFriendsAdapter.requery();
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mContentResolver.unregisterContentObserver(newFriendsObserver);
		unbindXMPPService();
	}

	
	ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mXxService = ((XXService.XXBinder) service).getService();
//			mXxService.registerConnectionStatusCallback(NewFriendsActivity.this);
//			// 开始连接xmpp服务器
//			if (!mXxService.isAuthenticated()) {
//				String usr = PreferenceUtils.getPrefString(NewFriendsActivity.this,
//						PreferenceConstants.ACCOUNT, "");
//				String password = PreferenceUtils.getPrefString(
//						NewFriendsActivity.this, PreferenceConstants.PASSWORD, "");
//				mXxService.Login(usr, password);
//				// mTitleNameView.setText(R.string.login_prompt_msg);
//				// setStatusImage(false);
//				// mTitleProgressBar.setVisibility(View.VISIBLE);
//			} else {
//				mTitleNameView.setText(XMPPHelper
//						.splitJidAndServer(PreferenceUtils.getPrefString(
//								NewFriendsActivity.this, PreferenceConstants.ACCOUNT,
//								"")));
//				setStatusImage(true);
//				mTitleProgressBar.setVisibility(View.GONE);
//			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mXxService.unRegisterConnectionStatusCallback();
			mXxService = null;
		}

	};
	

	private void unbindXMPPService() {
		try {
			unbindService(mServiceConnection);
			L.i(LoginActivity.class, "[SERVICE] Unbind");
		} catch (IllegalArgumentException e) {
			L.e(LoginActivity.class, "Service wasn't bound!");
		}
	}

	private void bindXMPPService() {
		L.i(LoginActivity.class, "[SERVICE] Unbind");
		bindService(new Intent(NewFriendsActivity.this, XXService.class),
				mServiceConnection, Context.BIND_AUTO_CREATE
						+ Context.BIND_DEBUG_UNBIND);
	}	
		
	
	public XXService getService(){
		return mXxService;
	}

	@Override
	public MainActivity getMainActivity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeActivity getHomeActivity() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
