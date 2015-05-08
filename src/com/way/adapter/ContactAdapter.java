package com.way.adapter;

import java.util.ArrayList;
import java.util.List;

import com.way.adapter.RosterAdapter.Roster;
import com.way.db.RosterProvider;
import com.way.db.RosterProvider.RosterConstants;

import com.way.util.L;

import com.way.util.StatusMode;
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

public class ContactAdapter extends SimpleCursorAdapter{

	private Context mContext;
	private ContentResolver mContentResolver;
	private LayoutInflater mInflater;// = LayoutInflater.from(context);	
	
	private static final String[] ROSTER_QUERY = new String[] {
		RosterConstants._ID, RosterConstants.JID, RosterConstants.ALIAS,
		RosterConstants.STATUS_MODE, RosterConstants.STATUS_MESSAGE, };	

	
	public ContactAdapter(Context context) {
		// TODO Auto-generated constructor stub
		super(context, 0, null, ROSTER_QUERY, null);
		mContext = context;
		mContentResolver = context.getContentResolver();
		mInflater = LayoutInflater.from(context);		
	}
	
	
	public void requery() {
		// we want all, online and offline
		List<String[]> list = new ArrayList<String[]>();
		Cursor cursor = mContentResolver.query(RosterProvider.CONTENT_URI,
				ROSTER_QUERY, RosterProvider.RosterConstants.TOP + "=?", new String[]{RosterProvider.TOP_NO}, RosterConstants.ALIAS);
		
		Cursor oldCursor = getCursor();		
		
		changeCursor(cursor);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Cursor cursor = this.getCursor();
		cursor.moveToPosition(position);

		Roster roster = new Roster();
		roster.setJid(cursor.getString(cursor
				.getColumnIndexOrThrow(RosterConstants.JID)));
		roster.setAlias(cursor.getString(cursor
				.getColumnIndexOrThrow(RosterConstants.ALIAS)));
		roster.setStatus_message(cursor.getString(cursor
				.getColumnIndexOrThrow(RosterConstants.STATUS_MESSAGE)));
		roster.setStatusMode(cursor.getString(cursor
				.getColumnIndexOrThrow(RosterConstants.STATUS_MODE)));	
		
		int presenceMode = Integer.parseInt(roster.getStatusMode());
		ViewHolder holder;
		if (convertView == null
				|| convertView.getTag(R.drawable.ic_launcher + presenceMode) == null) {
			L.i("liweiping", "new  child ");
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.contact_list_item_for_buddy, parent, false);
			holder.headView = (ImageView) convertView.findViewById(R.id.icon);
			holder.statusView = (ImageView) convertView
					.findViewById(R.id.stateicon);
			holder.nickView = (TextView) convertView
					.findViewById(R.id.contact_list_item_name);
			holder.onlineModeView = (ImageView) convertView
					.findViewById(R.id.online_mode);
			holder.statusMsgView = (TextView) convertView
					.findViewById(R.id.contact_list_item_state);
			convertView.setTag(R.drawable.ic_launcher + presenceMode, holder);
			convertView.setTag(R.string.app_name, R.drawable.ic_launcher
					+ presenceMode);
		} else {
			L.i("liweiping", "get child form case");
			holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
					+ presenceMode);
		}
		holder.nickView.setText(roster.getAlias());

		holder.statusMsgView.setText(TextUtils.isEmpty(roster
				.getStatusMessage()) ? mContext.getString(R.string.status_offline) : roster.getStatusMessage());
		setViewImage(holder.onlineModeView, holder.headView, holder.statusView,
				roster.getStatusMode());

		convertView.setTag(R.id.xxx01, position);
//		convertView.setTag(R.id.xxx02, childPosition);
//		cursor.close();
		return convertView;
		
		
	}
	
	protected void setViewImage(ImageView online, ImageView head, ImageView v,
			String value) {
		int presenceMode = Integer.parseInt(value);
		int statusDrawable = getIconForPresenceMode(presenceMode);
		if (statusDrawable == -1) {
			v.setVisibility(View.INVISIBLE);
			head.setImageResource(R.drawable.login_default_avatar_offline);
			online.setImageDrawable(null);
			return;
		}
		head.setImageResource(R.drawable.login_default_avatar);
		online.setImageResource(R.drawable.terminal_icon_ios_online);
		v.setImageResource(statusDrawable);

	}

	private int getIconForPresenceMode(int presenceMode) {
		return StatusMode.values()[presenceMode].getDrawableId();
	}

	
	
	
	static class ViewHolder {
		ImageView headView;
		TextView nickView;
		ImageView statusView;
		ImageView onlineModeView;
		TextView statusMsgView;

	}	

	public class Roster {
		private String jid;
		private String alias;
		private String statusMode;
		private String statusMessage;

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

	}
	
	
	
	public Roster getRoster(int position) {
		// Given the group, we return a cursor for all the children within
		// that group
		
		Cursor cursor = getCursor();
		
		cursor.moveToPosition(position);
				
		Roster roster = new Roster();
		if (!cursor.isAfterLast()) {

			roster.setJid(cursor.getString(cursor
					.getColumnIndexOrThrow(RosterConstants.JID)));
			roster.setAlias(cursor.getString(cursor
					.getColumnIndexOrThrow(RosterConstants.ALIAS)));
			roster.setStatus_message(cursor.getString(cursor
					.getColumnIndexOrThrow(RosterConstants.STATUS_MESSAGE)));
			roster.setStatusMode(cursor.getString(cursor
					.getColumnIndexOrThrow(RosterConstants.STATUS_MODE)));
		}
		
		
//		cursor.close();
		return roster;
	}
	
	
}
