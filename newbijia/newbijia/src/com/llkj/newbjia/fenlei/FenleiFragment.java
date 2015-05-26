package com.llkj.newbjia.fenlei;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.llkj.newbjia.BaseFragment;
import com.llkj.newbjia.MainActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.FenLeiAdapter;
import com.llkj.newbjia.adpater.SortListAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.main.GoodsActivity;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

/**
 * 分类
 * 
 * @author
 * 
 */
public class FenleiFragment extends BaseFragment implements OnClickListener {
	private ImageView bt_search;
	private EditText et_content;

	private GridView gr_content;

	private ListView lv_content;
	private ArrayList arrayList;
	// private FenLeiAdapter adapter;
	private SortListAdapter adapter;

	private LinearLayout ll_title_back;

	private int mRequestId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.layout_fenlei, null);

			initView();
			initListener();

			if (StringUtil.isNetworkConnected(getActivity())) {
				mRequestId = mRequestManager.classList("", true);
				isLoaded = true;
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

	private void initListener() {
		ll_title_back.setOnClickListener(this);
		bt_search.setOnClickListener(this);

		// gr_content is not used anymore ,so these codes can be deleted.
		gr_content.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap map = (HashMap) arrayList.get(arg2);
				// if (map.containsKey("cat_next")) {
				// String cat_next = map.get("cat_next") + "";
				if (map.containsKey("cat_id")) {
					String cat_id = map.get("cat_id") + "";
					// if ("0".equals(cat_next)) {
					Intent intent = new Intent(getActivity(),
							GoodsActivity.class);
					intent.putExtra("cat_id", cat_id);
					startActivity(intent);
					// } else {
					// Intent intent = new Intent(getActivity(),
					// FenleiActivity.class);
					// intent.putExtra("cat_id", cat_id);
					// startActivity(intent);
					// }
				}

				// }

			}
		});
	}

	private void initView() {
		bt_search = (ImageView) rootView.findViewById(R.id.bt_search);
		gr_content = (GridView) rootView.findViewById(R.id.gr_content);

		lv_content = (ListView) rootView.findViewById(R.id.lv_content);
		ll_title_back = (LinearLayout) rootView
				.findViewById(R.id.ll_title_back);
		et_content = (EditText) rootView.findViewById(R.id.et_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_back:
			if ((MainActivity) getActivity() != null) {
				((MainActivity) getActivity()).switchContent(0);
			}
			break;
		case R.id.bt_search:
			String content = et_content.getText() + "";
			if (!StringUtil.isEmpty(content)) {
				Intent intent = new Intent(getActivity(), GoodsActivity.class);
				intent.putExtra("key", content);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(getActivity(),
						R.string.contentnotisnull);
			}
			break;
		default:
			break;
		}

	}

	/**
	 * Make sure that when there is no data ,then refresh in its life cycle.
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isLoaded){
			if (StringUtil.isNetworkConnected(getActivity())) {
				mRequestId = mRequestManager.classList("", true);
			} 

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
					adapter = new SortListAdapter(getActivity(), arrayList);
					// TODO set the listView adapter.
					isLoaded = true;
					lv_content.setAdapter(adapter);
				} else {
					isLoaded = false;
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(getActivity(), msg);
				}

			}

		}
	}

}
