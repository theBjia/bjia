package com.llkj.newbjia.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llkj.db.DBHelper;
import com.llkj.db.PrivateChatMessagesEntity;
import com.llkj.db.RecentlyContacts;
import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.MyApplication;
import com.llkj.newbjia.MyClicker;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.RecentlyChatAdpter;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.utils.TimeUtils;

/**
 * 收藏夹页
 * 
 * @author John
 * 
 */
public class RecentlyChatFragment extends BaseFragment implements
		OnClickListener, MyClicker {

	private ListView lv_content;
	private ArrayList<RecentlyContacts> arrayList;
	private RecentlyChatAdpter adapter;
	private LinearLayout ll_title_back;
	private List<RecentlyContacts> rcList;
	private ArrayList<PrivateChatMessagesEntity> pEntitys;
	private DBHelper db;
	//TODO  attention!!! null pointer.
	private OnChatMessageListener messageListener;

	public OnChatMessageListener getMessageListener() {
		return messageListener;
	}
    // don't forget it .
	public void setMessageListener(OnChatMessageListener messageListener) {
		this.messageListener = messageListener;
	}

	private int number;
	public boolean isOpened;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.layout_conversation, null);
			initView();
			initListener();
			initData();
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	private void initView() {
		isOpened = true;
		lv_content = (ListView) rootView.findViewById(R.id.lv_content);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
	}

	private void initData() {
		if (db == null) {
			db = DBHelper.getInstance(getActivity());
		}

		if (arrayList == null) {
			arrayList = new ArrayList<RecentlyContacts>();
		}
		adapter = new RecentlyChatAdpter(getActivity(), arrayList, this);
		lv_content.setAdapter(adapter);
		ArrayList<RecentlyContacts> rcList = db.queryRecentlyRecord();
		Collections.sort(arrayList, new TimeComparator());
		arrayList.addAll(rcList);
		adapter.notifyDataSetChanged();

	}

	public PrivateChatMessagesEntity getKefuList() {
		pEntitys = db.queryOtherChatRecord("0", "1");
		if (pEntitys.size() == 0) {
			return null;
		}
		return pEntitys.get(0);
	}

	private void initListener() {
		ll_title_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
			}
			break;
		case R.id.rl_background:
			Intent intent = new Intent(getActivity(), ChatPersonActivity.class);
			intent.putExtra(Constants.TARGETID, MyApplication.esq_id);
			intent.putExtra(Constants.TAGETNAME, MyApplication.esq_name);
			intent.putExtra("isKefu", true);
			startActivityForResult(intent, 200);
			break;
		default:
			break;
		}
	}

	public void updateList() {
		if (arrayList == null) {
			arrayList = new ArrayList<RecentlyContacts>();
		}
		arrayList.clear();
		if (db == null) {
			db = DBHelper.getInstance(getActivity());
		}
		ArrayList<RecentlyContacts> rcList = db.queryRecentlyRecord();	
		Collections.sort(rcList, new TimeComparator());
		arrayList.addAll(rcList);
		// TODO 判断是否需要通知外界是否有新的消息。
		if(haveUnReadMessage(arrayList)){
			messageListener.onNewMessageComing();
		}else{
			
		}
		this.adapter.notifyDataSetChanged();

	}

	@Override
	public void myClick(View v, int i) {
		// TODO Auto-generated method stub
		RecentlyContacts rentity;
		switch (i) {
		case 0:
			rentity = (RecentlyContacts) v.getTag();
			String reId = rentity.getReceiveId();
			boolean istrue = db.deleteRecentlyRecord(rentity.getReceiveId());
			if (rentity.getMessageChatType().equals(Constants.ONREPLY)) {
				db.deleteOtherChat();
			} else {
				db.deletePrivateChat(UserInfoBean.getUserInfo(getActivity())
						.getUid(), rentity.getReceiveId());
			}
			updateList();
			break;
		case 1:
			rentity = (RecentlyContacts) v.getTag();
			if (Constants.ONTRANSMIT.equals(rentity.getMessageChatType())) {
				Intent intent = new Intent(getActivity(),
						ChatPersonActivity.class);
				intent.putExtra(Constants.TARGETID, rentity.getReceiveId());
				intent.putExtra(Constants.TAGETNAME, rentity.getReceiveName());
				intent.putExtra(Constants.TAGETPHOTO, rentity.getReceiveLogo());
				startActivity(intent);
			} else {
				Intent intent = new Intent(getActivity(),
						ChatPersonActivity.class);
				intent.putExtra(Constants.TARGETID, MyApplication.esq_id);
				intent.putExtra(Constants.TAGETNAME, MyApplication.esq_name);
				intent.putExtra("isKefu", true);
				startActivityForResult(intent, 200);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void myLongClick(View view, int type) {
		// TODO Auto-generated method stub

	}

	private Boolean haveUnReadMessage(ArrayList<RecentlyContacts> rcList) {
		//TODO once meet the unread message ,then stop and return.
		for (RecentlyContacts recentlyContacts : rcList) {
			String messagenum = recentlyContacts.getUnReadNumber();
			if (Integer.parseInt(messagenum) > 0) {
				return true;
			} 
		}
		
		return false;
	}
	
	public interface OnChatMessageListener{
		public void  onNewMessageComing();
	}
}
