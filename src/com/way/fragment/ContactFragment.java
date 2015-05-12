package com.way.fragment;

import com.way.activity.ChatActivity;
import com.way.activity.HomeActivity;
import com.way.activity.MainActivity;
import com.way.activity.NewFriendsActivity;
import com.way.adapter.ContactAdapter;
import com.way.adapter.TopContactAdapter;
import com.way.swipelistview.SwipeListView;
import com.way.xx.R;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

public class ContactFragment extends BaseFragment{
	private ListView topList;
	private ListView rosterList;
	private RelativeLayout newFriends;
	private TextView unreadFriends;
	
	private ContactAdapter contactAdapter;
	private TopContactAdapter topAdapter;
	
	private HomeActivity hActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		contactAdapter = new ContactAdapter(ContactFragment.this.getActivity());
		topAdapter = new TopContactAdapter(ContactFragment.this.getActivity());

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
		newFriends = (RelativeLayout) view.findViewById(R.id.new_friend);
		unreadFriends = (TextView) view.findViewById(R.id.unreadFriends);
		
		newFriends.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ContactFragment.this.getActivity(), NewFriendsActivity.class);
				
				startActivity(intent);
			}
		});
		
		topList.setAdapter(topAdapter);
		topList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				String uId = topAdapter.getRoster(position).getJid();
				String uName = topAdapter.getRoster(position).getAlias();
				
				startChatActivity(uId, uName);
			}
			
		});
		
		
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
		topAdapter.requery();
		hActivity.requeryNewFriends();
		hActivity.updateFriendsBadge();
		
		
	}
	
	private void startChatActivity(String userJid, String userName) {
		Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
		Uri userNameUri = Uri.parse(userJid);
		chatIntent.setData(userNameUri);
		chatIntent.putExtra(ChatActivity.INTENT_EXTRA_USERNAME, userName);
		startActivity(chatIntent);
	}

	@Override
	public void onUpdateNewFriendsCount(int count) {
		// TODO Auto-generated method stub
		if(count > 0){
//			unreadFriends。setVisibility(View.VISIBLE);
			unreadFriends.setVisibility(View.VISIBLE);
			unreadFriends.setText(Integer.valueOf(count).toString());
		}else{
			unreadFriends.setVisibility(View.GONE);
		}
	}		
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		hActivity = (HomeActivity) activity;
		
	}
}
