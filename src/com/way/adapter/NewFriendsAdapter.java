package com.way.adapter;

import com.way.db.RosterProvider;
import com.way.db.RosterProvider.RosterConstants;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.SimpleCursorAdapter;

public class NewFriendsAdapter extends SimpleCursorAdapter{
	
	private Context mContext;
	private ContentResolver mContentResolver;
	private LayoutInflater mInflater;// = LayoutInflater.from(context);	
	
	private static final String[] ROSTER_QUERY = new String[] {
		RosterConstants._ID, RosterConstants.JID, RosterConstants.ALIAS,
		RosterConstants.STATUS_MODE, RosterConstants.STATUS_MESSAGE, };	

	
	public NewFriendsAdapter(Context context) {
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
	
	
	public void getNewFriendsCount(){
		
		
		
		
	}
	
}
