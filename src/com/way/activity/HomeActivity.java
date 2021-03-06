package com.way.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.way.util.FileUtils;
import com.way.adapter.NewFriendsAdapter;
import com.way.adapter.RecentChatAdapter;
import com.way.adapter.RosterAdapter;
import com.way.app.XXBroadcastReceiver.EventHandler;
import com.way.db.ChatProvider;
import com.way.db.RosterProvider;
import com.way.db.RosterProvider.RosterConstants;
import com.way.fragment.BaseFragment;
import com.way.fragment.ContactFragment;
import com.way.fragment.RecentChatFragment;
import com.way.fragment.SettingsFragment;
import com.way.quickaction.ActionItem;
import com.way.quickaction.QuickAction;
import com.way.quickaction.QuickAction.OnActionItemClickListener;
import com.way.service.IConnectionStatusCallback;
import com.way.service.XXService;
import com.way.slidingmenu.SlidingMenu;
import com.way.util.L;
import com.way.util.NetUtil;
import com.way.util.PreferenceConstants;
import com.way.util.PreferenceUtils;
import com.way.util.StatusMode;
import com.way.util.T;
import com.way.util.XMPPHelper;
import com.way.view.AddRosterItemDialog;
import com.way.xx.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class HomeActivity extends MyBaseActivity	implements
OnClickListener, IConnectionStatusCallback, EventHandler,
FragmentCallBack {

	private static final String TAG = "HomeActivity";
	
	private static final int MAX_FRAGMENT = 3;
	
	private static final int FRAGMENT_0 = 0;
	private static final int FRAGMENT_1 = FRAGMENT_0+1;	
	private static final int FRAGMENT_2 = FRAGMENT_0+2;		
	
	private BaseFragment[] fragSet= new BaseFragment[MAX_FRAGMENT];
	
	private FragmentManager fragmentManager = null;
	private BaseFragment currentFragment = null;

//	private ImageView Btn_navi_0;	
//	private ImageView Btn_navi_1;
//	private ImageView Btn_navi_2;
//	
//	private Bitmap btn_navi_0_u; 
//	private Bitmap btn_navi_0_s;
//	private Bitmap btn_navi_1_u; 
//	private Bitmap btn_navi_1_s;
//	private Bitmap btn_navi_2_u; 
//	private Bitmap btn_navi_2_s;	

	
	private Bitmap[] btnU = new Bitmap[MAX_FRAGMENT];
	private Bitmap[] btnS = new Bitmap[MAX_FRAGMENT];	
	
	private ImageView[] btn = new ImageView[MAX_FRAGMENT];
	
	private TextView msgBadge;
	private TextView friendsBadge;
	
	private RecentChatAdapter msgBadgeAdapter;
	private NewFriendsAdapter friendsBadgeAdapter;
	public Handler mHandler = new Handler();
	
	private ContentResolver mContentResolver;
	private ContentObserver msgObserver;
	private ContentObserver friendsObserver;
	private static final int ID_CHAT = 0;
	private static final int ID_AVAILABLE = 1;
	private static final int ID_AWAY = 2;
	private static final int ID_XA = 3;
	private static final int ID_DND = 4;
	public static HashMap<String, Integer> mStatusMap;
	static {
		mStatusMap = new HashMap<String, Integer>();
		mStatusMap.put(PreferenceConstants.OFFLINE, -1);
		mStatusMap.put(PreferenceConstants.DND, R.drawable.status_shield);
		mStatusMap.put(PreferenceConstants.XA, R.drawable.status_invisible);
		mStatusMap.put(PreferenceConstants.AWAY, R.drawable.status_leave);
		mStatusMap.put(PreferenceConstants.AVAILABLE, R.drawable.status_online);
		mStatusMap.put(PreferenceConstants.CHAT, R.drawable.status_qme);
	}
//	private Handler mainHandler = new Handler();
//	private XXService mXxService;
	private SlidingMenu mSlidingMenu;
	private View mNetErrorView;
	private TextView mTitleNameView;
	private ImageView mTitleStatusView;
	private ProgressBar mTitleProgressBar;
	private ImageView mTitleAddView;

	private static final String[] GROUPS_QUERY = new String[] {
		RosterConstants._ID, RosterConstants.GROUP, };
	private static final String[] ROSTER_QUERY = new String[] {
		RosterConstants._ID, RosterConstants.JID, RosterConstants.ALIAS,
		RosterConstants.STATUS_MODE, RosterConstants.STATUS_MESSAGE, };

	public List<String> getRosterGroups() {
		// we want all, online and offline
		List<String> list = new ArrayList<String>();
		Cursor cursor = getContentResolver().query(RosterProvider.GROUPS_URI,
				GROUPS_QUERY, null, null, RosterConstants.GROUP);
		int idx = cursor.getColumnIndex(RosterConstants.GROUP);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			list.add(cursor.getString(idx));
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}	

	
	public List<String[]> getRosterContacts() {
		// we want all, online and offline
		List<String[]> list = new ArrayList<String[]>();
		Cursor cursor = getContentResolver().query(RosterProvider.CONTENT_URI,
				ROSTER_QUERY, null, null, RosterConstants.ALIAS);
		int JIDIdx = cursor.getColumnIndex(RosterConstants.JID);
		int aliasIdx = cursor.getColumnIndex(RosterConstants.ALIAS);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String jid = cursor.getString(JIDIdx);
			String alias = cursor.getString(aliasIdx);
			if ((alias == null) || (alias.length() == 0))
				alias = jid;
			list.add(new String[] { jid, alias });
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	protected void setViewImage(ImageView v, String value) {
		int presenceMode = Integer.parseInt(value);
		int statusDrawable = getIconForPresenceMode(presenceMode);
		v.setImageResource(statusDrawable);
		if (statusDrawable == R.drawable.status_busy)
			v.setVisibility(View.INVISIBLE);

	}

	private int getIconForPresenceMode(int presenceMode) {
		return StatusMode.values()[presenceMode].getDrawableId();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_left_fragment_btn:
			mSlidingMenu.showMenu(true);
			break;
		case R.id.show_right_fragment_btn:
			mSlidingMenu.showSecondaryMenu(true);
			break;
		case R.id.ivTitleName:
			if (isConnected())
				showStatusQuickAction(v);
			break;
			
		case R.id.ivTitleBtnRightImage:
			XXService xxService = getService();
			if (xxService == null || !xxService.isAuthenticated()) {
				return;
			}
			new AddRosterItemDialog(getHomeActivity(),
					xxService).show();// 添加联系人
			break;
		default:
			break;
		}
	}

	private void showStatusQuickAction(View v) {
		QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
		quickAction.addActionItem(new ActionItem(ID_CHAT,
				getString(R.string.status_chat), getResources().getDrawable(
						R.drawable.status_qme)));
		quickAction.addActionItem(new ActionItem(ID_AVAILABLE,
				getString(R.string.status_available), getResources()
						.getDrawable(R.drawable.status_online)));
		quickAction.addActionItem(new ActionItem(ID_AWAY,
				getString(R.string.status_away), getResources().getDrawable(
						R.drawable.status_leave)));
		quickAction.addActionItem(new ActionItem(ID_XA,
				getString(R.string.status_xa), getResources().getDrawable(
						R.drawable.status_invisible)));
		quickAction.addActionItem(new ActionItem(ID_DND,
				getString(R.string.status_dnd), getResources().getDrawable(
						R.drawable.status_shield)));
		quickAction
				.setOnActionItemClickListener(new OnActionItemClickListener() {

					@Override
					public void onItemClick(QuickAction source, int pos,
							int actionId) {
						// TODO Auto-generated method stub
						if (!isConnected()) {
							T.showShort(HomeActivity.this,
									R.string.conversation_net_error_label);
							return;
						}
						switch (actionId) {
						case ID_CHAT:
							mTitleStatusView
									.setImageResource(R.drawable.status_qme);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MODE,
									PreferenceConstants.CHAT);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MESSAGE,
									getString(R.string.status_chat));
							break;
						case ID_AVAILABLE:
							mTitleStatusView
									.setImageResource(R.drawable.status_online);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MODE,
									PreferenceConstants.AVAILABLE);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MESSAGE,
									getString(R.string.status_available));
							break;
						case ID_AWAY:
							mTitleStatusView
									.setImageResource(R.drawable.status_leave);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MODE,
									PreferenceConstants.AWAY);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MESSAGE,
									getString(R.string.status_away));
							break;
						case ID_XA:
							mTitleStatusView
									.setImageResource(R.drawable.status_invisible);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MODE,
									PreferenceConstants.XA);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MESSAGE,
									getString(R.string.status_xa));
							break;
						case ID_DND:
							mTitleStatusView
									.setImageResource(R.drawable.status_shield);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MODE,
									PreferenceConstants.DND);
							PreferenceUtils.setPrefString(HomeActivity.this,
									PreferenceConstants.STATUS_MESSAGE,
									getString(R.string.status_dnd));
							break;
						default:
							break;
						}
						mXxService.setStatusFromConfig();
//						SettingsFragment fragment = (SettingsFragment) getSupportFragmentManager()
//								.findFragmentById(R.id.main_right_fragment);
//						fragment.readData();
					}
				});
		quickAction.show(v);
	}

	public abstract class EditOk {
		abstract public void ok(String result);
	}
	

	ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mXxService = ((XXService.XXBinder) service).getService();
			mXxService.registerConnectionStatusCallback(HomeActivity.this);
			// 开始连接xmpp服务器
			if (!mXxService.isAuthenticated()) {
				String usr = PreferenceUtils.getPrefString(HomeActivity.this,
						PreferenceConstants.ACCOUNT, "");
				String password = PreferenceUtils.getPrefString(
						HomeActivity.this, PreferenceConstants.PASSWORD, "");
				mXxService.Login(usr, password);
				// mTitleNameView.setText(R.string.login_prompt_msg);
				// setStatusImage(false);
				// mTitleProgressBar.setVisibility(View.VISIBLE);
			} else {
				mTitleNameView.setText(XMPPHelper
						.splitJidAndServer(PreferenceUtils.getPrefString(
								HomeActivity.this, PreferenceConstants.ACCOUNT,
								"")));
				setStatusImage(true);
				mTitleProgressBar.setVisibility(View.GONE);
			}
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
		bindService(new Intent(HomeActivity.this, XXService.class),
				mServiceConnection, Context.BIND_AUTO_CREATE
						+ Context.BIND_DEBUG_UNBIND);
	}	
	
//	private void startChatActivity(String userJid, String userName) {
//		Intent chatIntent = new Intent(HomeActivity.this, ChatActivity.class);
//		Uri userNameUri = Uri.parse(userJid);
//		chatIntent.setData(userNameUri);
//		chatIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME, userName);
//		startActivity(chatIntent);
//	}

	private boolean isConnected() {
		return mXxService != null && mXxService.isAuthenticated();
	}	
	
	@Override
	public void onNetChange() {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
			T.showShort(this, R.string.net_error_tip);
			mNetErrorView.setVisibility(View.VISIBLE);
		} else {
			mNetErrorView.setVisibility(View.GONE);
		}
	}

	private void setStatusImage(boolean isConnected) {
		if (!isConnected) {
			mTitleStatusView.setVisibility(View.GONE);
			return;
		}
		String statusMode = PreferenceUtils.getPrefString(this,
				PreferenceConstants.STATUS_MODE, PreferenceConstants.AVAILABLE);
		int statusId = mStatusMap.get(statusMode);
		if (statusId == -1) {
			mTitleStatusView.setVisibility(View.GONE);
		} else {
			mTitleStatusView.setVisibility(View.VISIBLE);
			mTitleStatusView.setImageResource(statusId);
		}
	}

	@Override
	public void connectionStatusChanged(int connectedState, String reason) {
		switch (connectedState) {
		case XXService.CONNECTED:
			mTitleNameView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils
					.getPrefString(HomeActivity.this,
							PreferenceConstants.ACCOUNT, "")));
			mTitleProgressBar.setVisibility(View.GONE);
			// mTitleStatusView.setVisibility(View.GONE);
			setStatusImage(true);
			break;
		case XXService.CONNECTING:
			mTitleNameView.setText(R.string.login_prompt_msg);
			mTitleProgressBar.setVisibility(View.VISIBLE);
			mTitleStatusView.setVisibility(View.GONE);
			break;
		case XXService.DISCONNECTED:
			mTitleNameView.setText(R.string.login_prompt_no);
			mTitleProgressBar.setVisibility(View.GONE);
			mTitleStatusView.setVisibility(View.GONE);
			T.showLong(this, reason);
			break;

		default:
			break;
		}
	}

	@Override
	public XXService getService() {
		return mXxService;
	}

	@Override
	public MainActivity getMainActivity() {
		return null;
	}

	@Override
	public HomeActivity getHomeActivity() {
		// TODO Auto-generated method stub
		return this;
	}	

	
    private void initBtn() {
		// TODO Auto-generated method stub
    	
    	
    	btnU[FRAGMENT_0] = FileUtils.readBitMap(getApplicationContext(), R.drawable.button_navi_0_us);
    	btnS[FRAGMENT_0] = FileUtils.readBitMap(getApplicationContext(), R.drawable.button_navi_0);    	
    	
    	btnU[FRAGMENT_1] = FileUtils.readBitMap(getApplicationContext(), R.drawable.button_navi_1_us);
    	btnS[FRAGMENT_1] = FileUtils.readBitMap(getApplicationContext(), R.drawable.button_navi_1);    	
    	
    	btnU[FRAGMENT_2] = FileUtils.readBitMap(getApplicationContext(), R.drawable.button_navi_2_us);
    	btnS[FRAGMENT_2] = FileUtils.readBitMap(getApplicationContext(), R.drawable.button_navi_2);    	    	
		
	}
	
	private void clearBtn(){
		for(int i=0; i< btn.length; i++){
			btn[i].setImageBitmap(btnU[i]);
		}
	}
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
		startService(new Intent(HomeActivity.this, XXService.class));        
        
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
        
        setContentView(R.layout.activity_home);
        
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.titlebar);
        
//        TextView txx = (TextView) findViewById(R.id.titlebar_title);
        
//        txx.setText("敢美美容");
        
        mContentResolver = getContentResolver();
        
        initFragment();
        initBtn();
		
		btn[FRAGMENT_0] = (ImageView) findViewById(R.id.button_navi_0_img);
		findViewById(R.id.button_navi_0).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeFragment(fragSet[FRAGMENT_0]);		
				clearBtn();
				btn[FRAGMENT_0].setImageBitmap(btnS[FRAGMENT_0]);				
			}
		});
		
		btn[FRAGMENT_1] = (ImageView) findViewById(R.id.button_navi_1_img);
		findViewById(R.id.button_navi_1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeFragment(fragSet[FRAGMENT_1]);	
				clearBtn();
				btn[FRAGMENT_1].setImageBitmap(btnS[FRAGMENT_1]);				
			}
		});
		
		
		btn[FRAGMENT_2] = (ImageView) findViewById(R.id.button_navi_2_img);
		findViewById(R.id.button_navi_2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeFragment(fragSet[FRAGMENT_2]);
				clearBtn();
				btn[FRAGMENT_2].setImageBitmap(btnS[FRAGMENT_2]);
				
				// TODO Auto-generated method stub

			}
		});
        
        
//        View Btn_account = (View) findViewById(R.id.button_navi_3);
//        Btn_account.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {				
//				changeFragment(fragSet[FRAGMENT_3]);
//			}
//			
//		});	        

		mTitleAddView = (ImageView) findViewById(R.id.ivTitleBtnRightImage);
		mTitleAddView.setImageResource(R.drawable.setting_add_account_white);
		mTitleAddView.setVisibility(View.VISIBLE);
		mTitleAddView.setOnClickListener(this);        

		
		mTitleNameView = (TextView) findViewById(R.id.ivTitleName);
        
		mTitleProgressBar = (ProgressBar) findViewById(R.id.ivTitleProgress);
		mTitleStatusView = (ImageView) findViewById(R.id.ivTitleStatus);
		mTitleNameView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils
				.getPrefString(this, PreferenceConstants.ACCOUNT, "")));
		mTitleNameView.setOnClickListener(this);		
		
		msgBadge = (TextView) findViewById(R.id.badge_0);
		friendsBadge = (TextView) findViewById(R.id.badge_1);		
		
		msgBadge.setVisibility(View.GONE);
		friendsBadge.setVisibility(View.GONE);
		
		msgBadgeAdapter = new RecentChatAdapter(this);
		
		friendsBadgeAdapter = new NewFriendsAdapter(HomeActivity.this);
		
		msgObserver = new ContentObserver(mHandler) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				
				msgBadgeAdapter.requery();
				updateMsgBadge();
			}
			
		};
		
		friendsObserver = new ContentObserver(mHandler) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				
				friendsBadgeAdapter.requery();
				updateFriendsBadge();
			}
			
		};
		
		friendsBadgeAdapter = new NewFriendsAdapter(HomeActivity.this);
		

//		friendsBadgeAdapter = new RosterAdapter(this);		
		
        fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.add(R.id.fragment_container, fragSet[FRAGMENT_0]);
		fragmentTransaction.commit();
		
		clearBtn();
		btn[FRAGMENT_0].setImageBitmap(btnS[FRAGMENT_0]);	
		
		
    }

	protected void updateMsgBadge() {
		// TODO Auto-generated method stub
		if(msgBadgeAdapter.getUnreadMsgCount()>0){
			msgBadge.setVisibility(View.VISIBLE);
		}else{
			msgBadge.setVisibility(View.GONE);
		}

		msgBadge.setText(new String().valueOf(msgBadgeAdapter.getUnreadMsgCount()));
	}


	
	public void updateFriendsBadge() {
		// TODO Auto-generated method stub
		if(friendsBadgeAdapter.getNewFriendsCount()>0){
			friendsBadge.setVisibility(View.VISIBLE);
		}else{
			friendsBadge.setVisibility(View.GONE);
		}

		friendsBadge.setText(new String().valueOf(friendsBadgeAdapter.getNewFriendsCount()));
		
		if (fragSet[FRAGMENT_1] == currentFragment){
			currentFragment.onUpdateNewFriendsCount(friendsBadgeAdapter.getNewFriendsCount());
		}
	}
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		bindXMPPService();
		mContentResolver.registerContentObserver(ChatProvider.CONTENT_URI, true, msgObserver);
		mContentResolver.registerContentObserver(RosterProvider.CONTENT_URI, true, friendsObserver);		
	}	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		msgBadgeAdapter.requery();		
		updateMsgBadge();
		friendsBadgeAdapter.requery();
		updateFriendsBadge();
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unbindXMPPService();
		mContentResolver.unregisterContentObserver(msgObserver);
		mContentResolver.unregisterContentObserver(friendsObserver);
	}
	
    private void initFragment() {
		// TODO Auto-generated method stub
    	
    	fragSet[FRAGMENT_0] = new RecentChatFragment();
    	fragSet[FRAGMENT_1] = new ContactFragment();
    	fragSet[FRAGMENT_2] = new SettingsFragment();    	
    	
	}

	private void changeFragment(BaseFragment fragment){
		
		if(currentFragment != fragment){
			currentFragment = fragment;
		}else{
			Log.d(TAG, "current fragment is this one, no need to change");
		}
		
		if(!currentFragment.isAdded()){
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_container, currentFragment);
			fragmentTransaction.commit();    	
		}
    }
 
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for(int i=0; i<btn.length; i++){
			btnU[i].recycle();
			btnS[i].recycle();
		}
		
	}
	
	
	public interface UpdateNewFriendsCallback{
		public void onUpdateNewFriendsCount(int count);
	}


	public void requeryNewFriends(){
		friendsBadgeAdapter.requery();
	}
	
	
}
