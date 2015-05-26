package com.llkj.newbjia.adpater;

import com.llkj.newbjia.utils.FinalBitmapUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImgAdapter extends BaseAdapter {

	private Context _context;
	private FinalBitmapUtil fb;

	public ImgAdapter(Context context, FinalBitmapUtil fb) {
		_context = context;
		this.fb = fb;
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(_context);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String[] uri = new String[] { "http://www.bjia.co/APP/images/b1.jpg",
				"http://www.bjia.co/APP/images/b2.jpg",
				"http://www.bjia.co/APP/images/b3.jpg",
				"http://www.bjia.co/APP/images/b4.jpg",
				"http://www.bjia.co/APP/images/b5.jpg", };
		fb.displayForPicture(viewHolder.imageView, uri[position % 5]);

		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}
}
