package com.llkj.newbjia.adpater;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.llkj.newbjia.R;
import com.llkj.newbjia.sortlistview.SortModel;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.llkj.newbjia.utils.StringUtil;

@SuppressLint("NewApi")
public class ContactsAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;

	private FinalBitmapUtil fb;
	private MyClicker myCLicker;

	public ContactsAdapter(Context mContext, List<SortModel> list,
			MyClicker myCLicker) {
		this.mContext = mContext;
		this.list = list;
		fb = FinalBitmapUtil.create(mContext);
		this.myCLicker = myCLicker;

	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		final ViewHolder viewHolder;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_friend,
					null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
			viewHolder.myphoto = (ImageView) view.findViewById(R.id.myphoto);
			viewHolder.rl_one = (RelativeLayout) view.findViewById(R.id.rl_one);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			//viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setVisibility(View.GONE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
			// viewHolder.tvLetter.setTextColor(Color.WHITE);
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tv_delete.setVisibility(View.INVISIBLE);
		viewHolder.rl_one.setTag(mContent);
		viewHolder.tv_delete.setTag(mContent);
		viewHolder.rl_one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myCLicker.myCLick(v, 1);
				viewHolder.tv_delete.setVisibility(View.INVISIBLE);
			}
		});
		viewHolder.tv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myCLicker.myCLick(v, 2);
			}
		});
		viewHolder.rl_one.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				viewHolder.tv_delete.setVisibility(View.VISIBLE);
				return true;
			}
		});

		viewHolder.tvTitle.setText(this.list.get(position).getName());

		String logo = this.list.get(position).getPic();
		if (!StringUtil.isEmpty(logo)) {
			fb.displayForHeader(viewHolder.myphoto, logo);
		} else {
			viewHolder.myphoto.setImageResource(R.drawable.icon_defalut);
		}

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter, tv_delete;// 首字母
		TextView tvTitle;// 标题
		ImageView myphoto;// 头像
		RelativeLayout rl_one;

	}

	public interface MyClicker {
		void myCLick(View v, int position);
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}