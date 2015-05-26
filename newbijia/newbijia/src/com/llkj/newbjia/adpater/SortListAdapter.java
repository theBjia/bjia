package com.llkj.newbjia.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.llkj.newbjia.R;
import com.llkj.newbjia.adpater.FenLeiAdapter.ViewHolder;
import com.llkj.newbjia.bean.ResponseBean;
import com.llkj.newbjia.http.PoCRequestManager;
import com.llkj.newbjia.http.PoCService;
import com.llkj.newbjia.http.PoCRequestManager.OnRequestFinishedListener;
import com.llkj.newbjia.main.GoodsActivity;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SortListAdapter extends BaseAdapter implements
		OnRequestFinishedListener {

	private LayoutInflater inflater;
	protected Map<Integer, View> viewMap = new HashMap<Integer, View>();
	private Map<Integer, ViewHolder> viewHolderMap = new HashMap<Integer, SortListAdapter.ViewHolder>();
	private Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
	private ArrayList arrayList;
	// TODO
	private SimpleAdapter item_menu_second;

	@SuppressWarnings("rawtypes")
	private ArrayList list;
	private Context context;
	private FinalBitmapUtil fb;

	protected PoCRequestManager mRequestManager;

	private int mRequestId;

	@SuppressWarnings("rawtypes")
	public SortListAdapter(Context context, ArrayList list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		fb = FinalBitmapUtil.create(context);
		mRequestManager = PoCRequestManager.from(context);
		mRequestManager.addOnRequestFinishedListener(this);
	}

	@SuppressWarnings("rawtypes")
	public void notifyDataSetChanged(ArrayList arrayList) {
		viewMap.clear();
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		final ViewHolder holder;
		View pv;
		if (viewMap.get(position) == null) {
			pv = inflater.inflate(R.layout.item_fenlei2, null);
			holder = new ViewHolder();
			holder.tv_content = (TextView) pv.findViewById(R.id.tv_content);
			holder.iv_content = (ImageView) pv.findViewById(R.id.iv_content);
			holder.iv_menu_folded = (ImageView) pv
					.findViewById(R.id.iv_menu_folded);
			holder.ll_content_sort = (LinearLayout) pv
					.findViewById(R.id.ll_content_sort);

			holder.gv_content_second = (GridView) pv
					.findViewById(R.id.gv_content_second);

			final HashMap map = (HashMap) list.get(position);
			final int position_tmp = position;
			holder.ll_content_sort.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// this is mainly used to put the data into the second class
					// menu.
					if (holder.isShow) {
						holder.gv_content_second.setVisibility(View.GONE);
						holder.isShow = false;
						// 设置旋转角度
						Matrix matrix = new Matrix();
						matrix.setRotate(0);
						Bitmap bitmap = ((BitmapDrawable) context
								.getResources().getDrawable(
										R.drawable.arrow_right_white))
								.getBitmap();
						// 重新绘制Bitmap
						bitmap = Bitmap.createBitmap(bitmap, 0, 0,
								bitmap.getWidth(), bitmap.getHeight(), matrix,
								true);
						holder.iv_menu_folded.setImageBitmap(bitmap);

					} else {
				
						// 设置旋转角度
						Matrix matrix = new Matrix();
						matrix.setRotate(90);
						Bitmap bitmap = ((BitmapDrawable) context
								.getResources().getDrawable(
										R.drawable.arrow_right_white))
								.getBitmap();
						// 重新绘制Bitmap
						bitmap = Bitmap.createBitmap(bitmap, 0, 0,
								bitmap.getWidth(), bitmap.getHeight(), matrix,
								true);
						
						holder.iv_menu_folded.setImageBitmap(bitmap);
						
						if (holder.isFirst ) {
							if (map.containsKey("cat_id")) {
								String cat_id = map.get("cat_id") + "";
								mRequestId = mRequestManager.classList(cat_id,
										true);
								positionMap.put(mRequestId, position_tmp);
								//holder.isFirst = false;
							}
						} else {
							holder.gv_content_second
									.setVisibility(View.VISIBLE);
							holder.isShow = true;
						}

					}

				}
			});

			holder.gv_content_second
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							Adapter adapter = parent.getAdapter();
							Map<String, String> map = (Map<String, String>) adapter
									.getItem(position);
							if (map.containsKey("cat_id")) {
								String cat_id = map.get("cat_id") + "";
								Intent intent = new Intent(context,
										GoodsActivity.class);
								intent.putExtra("cat_id", cat_id);
								context.startActivity(intent);

							}
						}
					});
			viewHolderMap.put(position, holder);

			pv.setTag(holder);
			viewMap.put(position, pv);

		} else {
			pv = viewMap.get(position);
			holder = (ViewHolder) pv.getTag();
		}
		HashMap map = (HashMap) list.get(position);
		if (map.containsKey("cat_name")) {
			holder.tv_content.setText(map.get("cat_name") + "");
		}
		if (map.containsKey("cat_logo")) {
			fb.displayForPicture(holder.iv_content, map.get("cat_logo") + "");
		}

		return pv;
	}

	private class ViewHolder {
		TextView tv_content;
		ImageView iv_content;
		ImageView iv_menu_folded;
		LinearLayout ll_content_sort;
		GridView gv_content_second;
		Boolean isShow = false;
		Boolean isFirst = true;

	}

	@Override
	public void onRequestFinished(int requestId, int resultCode, Bundle payload) {
		// TODO
		if (resultCode == PoCService.SUCCESS_CODE) {
			if (mRequestId == requestId) {
				int result = payload.getInt(ResponseBean.RESPONSE_STATE);
				if (result == 1) {
					arrayList = payload
							.getParcelableArrayList(ResponseBean.RESPONSE_LIST);

					item_menu_second = new SimpleAdapter(context, arrayList,
							R.layout.item_fenlei_grid,
							new String[] { "cat_name" }, 
							new int[] { R.id.tv_sort_second });

					ViewHolder mHolder = viewHolderMap.get(positionMap
							.get(mRequestId));
					mHolder.gv_content_second.setAdapter(item_menu_second);
					mHolder.gv_content_second.setVisibility(View.VISIBLE);
					mHolder.isShow = true;
					mHolder.isFirst = false;
					positionMap.clear();
				} else {
					String msg = payload
							.getString(ResponseBean.RESPONSE_MESSAGE);
					ToastUtil.makeShortText(context, msg);
				}

			}

		}
	}

	@Override
	public void onRequestPrepareListener() {
		// TODO

	}

}
