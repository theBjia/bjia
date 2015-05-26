package com.llkj.newbjia.customview;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.llkj.newbjia.R;
import com.llkj.newbjia.utils.FinalBitmapUtil;
import com.polites.android.GestureImageView;

public class MyActivity extends Activity {

	private ViewPager guidePages;

	private ArrayList<View> viewList = new ArrayList<View>();

	private ArrayList<String> urls = new ArrayList<String>();

	private FinalBitmapUtil fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my);

		initView();

		initListener();

		initData();
		fillGuanggao(urls);
	}

	private void initView() {
		guidePages = (ViewPager) findViewById(R.id.myguide);

	}

	private void initListener() {

		guidePages.setOnPageChangeListener(new NavigationPageChangeListener());

	}

	private void initData() {

		fb = FinalBitmapUtil.create(this);

		urls = getIntent().getStringArrayListExtra("url");

	}

	/**
	 * 加载商品滑动图片
	 * 
	 * @param arrayList
	 */
	public void fillGuanggao(ArrayList arrayList) {

		for (int i = 0; i < arrayList.size(); i++) {

			GestureImageView iv = new GestureImageView(this);
			iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));

			fb.displayForPicture(iv, arrayList.get(i) + "");

			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

			iv.setTag(i);

			viewList.add(iv);
		}

		guidePages.setAdapter(new MyViewPagerAdapter(viewList));

	}

	class NavigationPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {

		}
	}

	public class MyViewPagerAdapter extends PagerAdapter {

		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {

			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(mListViews.get(position), 0);

			mListViews.get(position).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});

			return mListViews.get(position);
		}

		@Override
		public int getCount() {

			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

	}

}
