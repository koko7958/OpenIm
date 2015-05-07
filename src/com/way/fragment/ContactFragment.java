package com.way.fragment;

import com.way.activity.ChatActivity;
import com.way.activity.HomeActivity;
import com.way.activity.MainActivity;
import com.way.adapter.ContactAdapter;
import com.way.swipelistview.SwipeListView;
import com.way.xx.R;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.ExpandableListView.OnChildClickListener;

public class ContactFragment extends Fragment{
	private ListView topList;
	private ListView rosterList;
	
	private ContactAdapter contactAdapter;
	private SimpleCursorAdapter topAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		contactAdapter = new ContactAdapter(ContactFragment.this.getActivity());

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_contact, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	
	private void initView(View view) {
		
		topList = (ListView) view.findViewById(R.id.topList);
		rosterList = (ListView) view.findViewById(R.id.rosterList);
		
		rosterList.setAdapter(contactAdapter);

		rosterList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				String uId = ((ContactAdapter) contactAdapter).getRoster(position).getJid();
				String uName = contactAdapter.getRoster(position).getAlias();
				
				startChatActivity(uId, uName);
			}
			
		});
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		((ContactAdapter) contactAdapter).requery();	
		
	}
	
	private void startChatActivity(String userJid, String userName) {
		Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
		Uri userNameUri = Uri.parse(userJid);
		chatIntent.setData(userNameUri);
		chatIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME, userName);
		startActivity(chatIntent);
	}		
	
}
