package com.llkj.newbjia.friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.sortlistview.ClearEditText;
import com.example.sortlistview.GroupMemberBean;
import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.ContactsAdapter;
import com.llkj.newbjia.adpater.ContactsAdapter.MyClicker;
import com.llkj.newbjia.bean.KeyBean;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.bean.UserInfoBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.sortlistview.CharacterParser;
import com.llkj.newbjia.sortlistview.PinyinComparator;
import com.llkj.newbjia.sortlistview.SideBar;
import com.llkj.newbjia.sortlistview.SortModel;
import com.llkj.newbjia.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class FriendFragment extends BaseFragment implements OnClickListener,
		MyClicker, SectionIndexer {

	private ListView lv_content;
	private ArrayList arrayList;
	private ContactsAdapter adapter;
	private LinearLayout ll_title_back;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private List<SortModel> SourceDateList;
	private SideBar sideBar;
	private RelativeLayout rl_newfriend;
	private int mRequestId, mDelFriendId;
	private String uid;
	private SortModel deleSortModel;

	private TextView dialog;
	private ClearEditText mClearEditText;
	private LinearLayout titleLayout;
	private TextView title;
	private TextView tvNofriends;
	private ImageView ivAddFriend;
	private int lastFirstVisibleItem = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.layout_friend, null);
			initView();
			initListener();
			intData();
			uid = UserInfoBean.getUserInfo(getActivity()).getUid();

			if (StringUtil.isNetworkConnected(getActivity())) {
				if (!StringUtil.isEmpty(uid)) {
					isLoaded = true;
					mRequestId = mRequestManager.friendsList(uid, true);
				} else {
					ToastUtil.makeShortText(getActivity(), R.string.xiandenglu);
				}
			} else {
				ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
			}

		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isLoaded) {
			reFreshData();
		}
	}

	public void reFreshData() {
		if (StringUtil.isNetworkConnected(getActivity())) {
			if (!StringUtil.isEmpty(uid)) {

				mRequestId = mRequestManager.friendsList(uid, true);
			} else {
				ToastUtil.makeShortText(getActivity(), R.string.xiandenglu);
			}
		} else {
			ToastUtil.makeShortText(getActivity(), R.string.no_wangluo);
		}
	}

	private void intData() {
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
	}

	private void initListener() {
		ll_title_back.setOnClickListener(this);
		rl_newfriend.setOnClickListener(this);
		ivAddFriend.setOnClickListener(this);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				if (adapter == null)
					return;
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					lv_content.setSelection(position);
				}

			}
		});
		sideBar.setTextView(dialog);
	}

	private void initView() {
		dialog = (TextView) rootView.findViewById(R.id.dialog);

		titleLayout = (LinearLayout) rootView.findViewById(R.id.title_layout);
		title = (TextView) rootView.findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) rootView
				.findViewById(R.id.title_layout_no_friends);
		sideBar = (SideBar) rootView.findViewById(R.id.sidrbar);
		lv_content = (ListView) rootView.findViewById(R.id.lv_content);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
		rl_newfriend = (RelativeLayout) rootView
				.findViewById(R.id.rl_newfriend);
		mClearEditText = (ClearEditText) rootView
				.findViewById(R.id.filter_edit);

		ivAddFriend = (ImageView) rootView.findViewById(R.id.iv_add_right);

		titleLayout.setVisibility(View.GONE);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 这个时候不需要挤压效果 就把他隐藏掉
				titleLayout.setVisibility(View.GONE);
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
			}
			break;
		case R.id.rl_newfriend:
			intent = new Intent(getActivity(), NewFriendActivity.class);
			// TODO delete it .
			startActivityForResult(intent, 100);
			break;
		case R.id.iv_add_right:
			intent = new Intent(getActivity(), AddFriendActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 100:
				reFreshData();
				break;
			case 200:
				break;
			default:
				break;
			}
		}
	}

	// 把arrayList 封装成sortmodel对象
	private List<SortModel> filledData(ArrayList arraylist) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < arraylist.size(); i++) {
			if (((HashMap) arraylist.get(i)).containsKey("user_name")) {
				SortModel sortModel = new SortModel();
				HashMap map = (HashMap) arraylist.get(i);
				sortModel.setName(map.get("user_name").toString());
				sortModel.setPic(map.get("logo").toString());
				sortModel.setUid(map.get("uid").toString());
				// 汉字转换成拼音
				String pinyin = characterParser.getSelling(map.get("user_name")
						.toString());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}

				mSortList.add(sortModel);
			}
		}
		Collections.sort(mSortList, pinyinComparator);
		return mSortList;

	}

	@Override
	public void myCLick(View v, int position) {
		// TODO
		switch (position) {
		case 1:
			SortModel mContent = (SortModel) v.getTag();
			ToastUtil.makeLongText(getActivity(), mContent.getUid() + "");
			Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
			intent.putExtra(KeyBean.KEY_FID, mContent.getUid());
			getActivity().startActivity(intent);
			break;
		case 2:
			deleSortModel = (SortModel) v.getTag();
			mDelFriendId = mRequestManager.friendDel(uid,
					deleSortModel.getUid(), true);

			break;
		default:
			break;
		}

	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					isLoaded = true;
					SourceDateList = filledData(arrayList);
					adapter = new ContactsAdapter(getActivity(),
							SourceDateList, this);
					lv_content.setAdapter(adapter);

				} else {
					isLoaded = false;
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}

			} else if (mDelFriendId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					SourceDateList.remove(deleSortModel);
					adapter.notifyDataSetChanged();
				}

			}

		}
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
			tvNofriends.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		if (filterDateList != null && filterDateList.size() > 0) {
			Collections.sort(filterDateList, pinyinComparator);
			adapter.updateListView(filterDateList);
		}

		if (filterDateList == null || filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}

}
