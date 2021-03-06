package com.king.zhbj.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.king.zhbj.MainActivity;
import com.king.zhbj.base.BaseMenuDetailPager;
import com.king.zhbj.base.BasePager;
import com.king.zhbj.base.menudetail.InteractMenuDetailPager;
import com.king.zhbj.base.menudetail.NewsMenuDetailPager;
import com.king.zhbj.base.menudetail.PhotoMenuDetailPager;
import com.king.zhbj.base.menudetail.TopicMenuDetailPager;
import com.king.zhbj.domain.NewsData;
import com.king.zhbj.domain.NewsData.NewsMenuData;
import com.king.zhbj.fragment.LeftMenuFragment;
import com.king.zhbj.global.GlobalContants;
import com.king.zhbj.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 新闻中心
 * 
 * @author Kevin
 * 
 */
public class NewsCenterPager extends BasePager {

	private ArrayList<BaseMenuDetailPager> mPagers;// 4个菜单详情页的集合
	private NewsData mNewsData;

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("初始化新闻中心数据....");

		tvTitle.setText("新闻");
		setSlidingMenuEnable(true);// 打开侧边栏

		String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL,
				mActivity);

		if (!TextUtils.isEmpty(cache)) {// 如果缓存存在,直接解析数据, 无需访问网路
			parseData(cache);
		}

		getDataFromServer();// 不管有没有缓存, 都获取最新数据, 保证数据最新
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();

		// 使用xutils发送请求
		utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
				new RequestCallBack<String>() {

					// 访问成功, 在主线程运行
					@Override
					public void onSuccess(ResponseInfo responseInfo) {
						String result = (String) responseInfo.result;
						System.out.println("返回结果:" + result);

						parseData(result);

						// 设置缓存
						CacheUtils.setCache(GlobalContants.CATEGORIES_URL,
								result, mActivity);
					}

					// 访问失败, 在主线程运行
					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
								.show();
						error.printStackTrace();
					}

				});
	}

	/**
	 * 解析网络数据
	 * 
	 * @param result
	 */
	protected void parseData(String result) {
		Gson gson = new Gson();
		mNewsData = gson.fromJson(result, NewsData.class);
		System.out.println("解析结果:" + mNewsData);

		// 刷新测边栏的数据
		MainActivity mainUi = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
		leftMenuFragment.setMenuData(mNewsData);

		// 准备4个菜单详情页
		mPagers = new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,
				mNewsData.data.get(0).children));
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));

		setCurrentMenuDetailPager(0);// 设置菜单详情页-新闻为默认当前页
	}

	/**
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPager(int position) {
		BaseMenuDetailPager pager = mPagers.get(position);// 获取当前要显示的菜单详情页
		flContent.removeAllViews();// 清除之前的布局
		flContent.addView(pager.mRootView);// 将菜单详情页的布局设置给帧布局

		// 设置当前页的标题
		NewsMenuData menuData = mNewsData.data.get(position);
		tvTitle.setText(menuData.title);

		pager.initData();// 初始化当前页面的数据

		if (pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);
		} else {
			btnPhoto.setVisibility(View.GONE);
		}
	}

}
