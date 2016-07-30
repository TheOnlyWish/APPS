package com.king.loadnote.activity;

import com.king.loadnote.R;
import com.king.loadnote.R.id;
import com.king.loadnote.R.layout;
import com.king.loadnote.fragment.splash.impl.SplashBottomFragment;
import com.king.loadnote.util.SharedPUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * 用户的登录/注册界面
 * @author Administrator
 *
 */
public class SplashActivity extends FragmentActivity {

	private FrameLayout fl_bottom;
	private FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 判断是否登入过
		checkIsLogined();
		
		initView();
		initListener();
		initData();
		
		
	}

	/**
	 * 判断是否登入过
	 */
	private void checkIsLogined() {
		
		SharedPUtil util = SharedPUtil.getInstant(this);
		String telNumber = util.getString("telNumber");
		if(telNumber != null){
			// 跳转主页
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}

	private void initView() {
		
		setContentView(R.layout.activity_splash);
		fl_bottom = (FrameLayout) findViewById(R.id.splash_bottom);
		
	}

	private void initListener() {
		
	}

	private void initData() {
		
		SplashBottomFragment splashBottomFragment = new SplashBottomFragment();
		// 将splashBottomFragment放入FrameLayout
		fm = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fm.beginTransaction();
		beginTransaction.replace(R.id.splash_bottom, splashBottomFragment);
		beginTransaction.commit();
		
	}
	
}
