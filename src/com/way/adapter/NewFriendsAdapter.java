package com.way.adapter;

import com.way.activity.MyBaseActivity;
import com.way.activity.NewFriendsActivity;
import com.way.adapter.ContactAdapter.Roster;
import com.way.adapter.ContactAdapter.ViewHolder;
import com.way.db.RosterProvider;
import com.way.db.RosterProvider.RosterConstants;
import com.way.util.L;
import com.way.xx.R;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class NewFriendsAdapter extends SimpleCursorAdapter{
	
	private static final String TAG = "NewFriendsAdapter";
	private MyBaseActivity mContext;
	private ContentResolver mContentResolver;
	private LayoutInflater mInflater;// = LayoutInflater.from(context);	
	
	private static final String[] ROSTER_QUERY = new String[] {
		RosterConstants._ID, RosterConstants.JID, RosterConstants.ALIAS,
		RosterConstants.STATUS_MODE, RosterConstants.STATUS_MESSAGE, RosterConstants.SUBSCRIBE};	

	
	public NewFriendsAdapter(MyBaseActivity context) {
		// TODO Auto-generated constructor stub
		super(context, 0, null, ROSTER_QUERY, null);
		mContext = context;
		mContentResolver = context.getContentResolver();
		mInflater = LayoutInflater.from(context);		
	}	
	
	
	public void requery(){
		Cursor cursor = mContentResolver.query(RosterProvider.CONTENT_URI,
				ROSTER_QUERY, RosterProvider.RosterConstants.DIRECTION + "=?", 
				new String[]{RosterProvider.DIRECTION_TO}, RosterConstants.ALIAS);
		
		changeCursor(cursor);		
	}
	
	
	public int getNewFriendsCount(){
		L.i(TAG, "lzctest->getNewFriendsCount");
		
		int count = 0;
		
		Cursor cursor = this.getCursor();
		
		
		
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			if(RosterProvider.SUBSCRIBE_PENDING.equals(cursor.getString(cursor.getColumnIndexOrThrow(RosterConstants.SUBSCRIBE)))){
				count++;
			}
			
			cursor.moveToNext();
			
		}
		
		
		L.i(TAG, "lzctest->getNewFriendsCount->count:"+count);
		return count;
		
		
		
	}
	
	static class ViewHolder {
		ImageView headView;
		TextView idView;
		TextView nickView;
		TextView subscribleView;

	}	

	public class Roster {
		private String jid;
		private String alias;
		private String statusMode;
		private String statusMessage;
		private String subscribeState;

		public String getJid() {
			return jid;
		}

		public void setJid(String jid) {
			this.jid = jid;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public String getStatusMode() {
			return statusMode;
		}

		public void setStatusMode(String statusMode) {
			this.statusMode = statusMode;
		}

		public String getStatusMessage() {
			return statusMessage;
		}

		public void setStatus_message(String statusMessage) {
			this.statusMessage = statusMessage;
		}

		public void setSubscribeState(String state){
			this.subscribeState = state;
		}
		
		public String getSubscribeState(){
			return subscribeState;
		}
		
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Cursor cursor = this.getCursor();
		cursor.moveToPosition(position);
		

		Roster roster = new Roster();
		roster.setJid(cursor.getString(cursor
				.getColumnIndexOrThrow(RosterConstants.JID)));
		
		String alias[] = cursor.getString(cursor
				.getColumnIndexOrThrow(RosterConstants.ALIAS)).split("@");
		
		roster.setAlias(alias[0]);		
		
//		roster.setAlias(cursor.getString(cursor
//				.getColumnIndexOrThrow(RosterConstants.ALIAS)));
		roster.setSubscribeState(cursor.getString(cursor
				.getColumnIndexOrThrow(RosterConstants.SUBSCRIBE)));
		
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			L.i("liweiping", "new  child ");

			convertView = mInflater.inflate(
					R.layout.new_friends_list_item_for_buddy, parent, false);	
		}
		
		
		holder.headView = (ImageView) convertView.findViewById(R.id.icon);
		holder.idView = (TextView) convertView
				.findViewById(R.id.contact_list_item_id);
		holder.nickView = (TextView) convertView
				.findViewById(R.id.contact_list_item_name);
		holder.subscribleView = (TextView) convertView
				.findViewById(R.id.subscrible_state);
		
		
		holder.idView.setText(roster.getJid());
		holder.nickView.setText(roster.getAlias());
		holder.headView.setImageResource(R.drawable.login_default_avatar);
		if(RosterProvider.SUBSCRIBE_PENDING.equals(roster.getSubscribeState())){
			holder.subscribleView.setText(R.string.subscribe_accept);
			holder.subscribleView.setBackgroundColor(mContext.getResources().getColor(R.color.skin_blue));//R.color.skin_blue);
			holder.subscribleView.setClickable(true);
			MyOnClickListener mClickListener = new MyOnClickListener();
			mClickListener.setId(roster.getJid());
			holder.subscribleView.setOnClickListener(mClickListener);
			
		}else{
			holder.subscribleView.setText(R.string.subscribe_added);
			holder.subscribleView.setBackgroundColor(mContext.getResources().getColor(R.color.gray));//R.color.gray);
			holder.subscribleView.setClickable(false);
		}
		

		return convertView;
	}
	
	public class MyOnClickListener implements View.OnClickListener{
		String id;
		
		public void setId(String jid){
			this.id = jid;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			mContext.getXXService().acceptNewFried(id);
		}
		
		
	}
	
	
}
