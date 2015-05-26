package com.llkj.newbjia.main;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.llkj.newbjia.BaseActivity;
import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.GoodsAdapter;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.utils.StringUtil;
import com.llkj.newbjia.utils.ToastUtil;

public class GoodsActivity extends BaseActivity {
	private PullToRefreshGridView mPullRefreshGridView;

	private GridView mGridView;
	private ArrayList arrayList;
	private GoodsAdapter adapter;

	private int mRequestId, page, mLoadMoreId;
	private String id, key;
	private Intent bigIntent;

	public static GoodsActivity newInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goods);
		setTitle(R.string.goods, true, R.string.kongzifuchuan, false,
				R.string.kongzifuchuan);

		initView();
		initListener();
		initData();

		if (StringUtil.isNetworkConnected(this)) {
			page = 1;
			if (!StringUtil.isEmpty(id)) {
				mRequestId = mRequestManager.goodList(id, page + "", true);
			} else if (!StringUtil.isEmpty(key)) {
				mRequestId = mRequestManager.searchGoods(key, page + "", true);
			}

		} else {
			ToastUtil.makeShortText(this, R.string.no_wangluo);
		}
		newInstance = this;
	}

	private void initView() {

		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		mGridView.setVerticalScrollBarEnabled(false);
	}

	private void initData() {
		bigIntent = getIntent();

		if (bigIntent.hasExtra("cat_id")) {
			id = bigIntent.getStringExtra("cat_id");
		}

		if (bigIntent.hasExtra("key")) {
			key = bigIntent.getStringExtra("key");
		}

		arrayList = new ArrayList();
		adapter = new GoodsAdapter(this, arrayList);
		mGridView.setAdapter(adapter);
	}

	private void initListener() {
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page = 1;
						if (!StringUtil.isEmpty(id)) {
							mRequestId = mRequestManager.goodList(id,
									page + "", true);
						} else if (!StringUtil.isEmpty(key)) {
							mRequestId = mRequestManager.searchGoods(key, page
									+ "", true);
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {

						page++;
						if (!StringUtil.isEmpty(id)) {
							mLoadMoreId = mRequestManager.goodList(id, page
									+ "", true);
						} else if (!StringUtil.isEmpty(key)) {
							mLoadMoreId = mRequestManager.searchGoods(key, page
									+ "", true);
						}
					}
				});

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap map = (HashMap) arrayList.get(position);
				String goods_id = map.get("goods_id") + "";
				Log.i("tuangou", goods_id);
				if (!StringUtil.isEmpty(goods_id)) {
					Intent intent = new Intent(GoodsActivity.this,
							GoodDetailTwoActivity.class);
					intent.putExtra("id", goods_id);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		super.onRequestFinished(requestId, resultCode, payload);
		if (resultCode == PoCService.SUCCESS_CODE) {
			mPullRefreshGridView.onRefreshComplete();
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList.clear();
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (newList != null && newList.size() > 0) {
						arrayList.addAll(newList);
						adapter.notifyDataSetChanged();
					} else {
						ToastUtil.makeShortText(this, R.string.no_data);
					}

				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}
			if (mLoadMoreId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					ArrayList newList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);
					if (newList != null && newList.size() > 0) {
						arrayList.addAll(newList);
						adapter.notifyDataSetChanged();
					} else {
						ToastUtil.makeShortText(this, R.string.no_data);
					}
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(this, msg);
				}

			}

		}
	}

}
