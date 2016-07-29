package com.king.loadnote.activity;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.king.loadnote.R;
import com.king.loadnote.fragment.content.impl.ContentFragment;
import com.king.loadnote.fragment.content.impl.MenuFragment;
import com.king.loadnote.pager.content.AllNotesContentPager;
import com.king.loadnote.pager.content.UselessNoteContentPager;
import com.king.loadnote.pager.impl.AllNotesPager;
import com.king.loadnote.pager.impl.NoteBooksPager;
import com.king.loadnote.pager.impl.UselessNotesPager;

public class MainActivity extends FragmentActivity {

	private FrameLayout menu;
	private FrameLayout content;
	private DrawerLayout drawerLayout;
	private ContentFragment contentFragment;
	private MenuFragment menuFragment;
	private MyBroadCaseReceiver receiver;
	private NoteBookBroadCaseReceiver noteBookReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initListener();
		initData();

	}

	@Override
	public void onBackPressed() {
		// 判断allNotesContentPager多选栏是否出现
		AllNotesPager ancp = (AllNotesPager) contentFragment.getPagers().get(0);
		AllNotesContentPager pager = ancp.getAllNotesContentPager();
		// 判断uselessNoteContent多选栏是否出现
		UselessNotesPager uncp = (UselessNotesPager) contentFragment
				.getPagers().get(2);
		if (uncp.getUselessNoteContentPager() != null) {
			UselessNoteContentPager uselessPager = uncp
					.getUselessNoteContentPager();
			if (uselessPager.isShowCheckBox) {
				uselessPager.clearCheckedMap();
				return;
			} 
		}
		if (pager.isShowCheckBox) {
			pager.clearCheckedMap();
		} else{
			super.onBackPressed();
		}

	}

	private void initView() {
		setContentView(R.layout.activity_main);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		content = (FrameLayout) findViewById(R.id.content);
		menu = (FrameLayout) findViewById(R.id.menu);
	}

	private void initListener() {

		drawerLayout.setDrawerListener(new DrawerListener() {

			/**
			 * 当抽屉开启或关闭的过程中调用 滑动幅度0-1
			 */
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

			}

			/**
			 * 当抽屉完全开启时调用
			 */
			@Override
			public void onDrawerOpened(View drawerView) {

			}

			/**
			 * 当抽屉完全关闭时调用
			 */
			@Override
			public void onDrawerClosed(View drawerView) {

			}

			/**
			 * 移动当抽屉状态改变是时调用
			 */
			@Override
			public void onDrawerStateChanged(int newState) {

			}

		});

	}

	private void initData() {
		drawerLayout.setDrawerShadow(R.drawable.shape_rect_drawer,
				GravityCompat.START);

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fm.beginTransaction();

		contentFragment = new ContentFragment();
		beginTransaction.replace(R.id.content, contentFragment);

		menuFragment = new MenuFragment();
		beginTransaction.replace(R.id.menu, menuFragment).commit();

		receiver = new MyBroadCaseReceiver();
		IntentFilter filter = new IntentFilter("REFLASH_NOTE_LIST");
		registerReceiver(receiver, filter);
		
		noteBookReceiver = new NoteBookBroadCaseReceiver();
		IntentFilter noteBookFilter = new IntentFilter("REFLASH_NOTE_BOOK_LIST");
		registerReceiver(noteBookReceiver, noteBookFilter);
		

	}

	public void openLeftDrawer() {
		drawerLayout.openDrawer(Gravity.LEFT);
	}

	public void closeLeftDrawer() {
		drawerLayout.closeDrawer(Gravity.LEFT);
	}

	public ContentFragment getContentFragment() {
		return contentFragment;
	}

	public MenuFragment getMenuFragment() {
		return menuFragment;
	}

	// public void setDrawerEnable(boolean enable){
	// drawerLayout.setEnabled(enable);
	// }

	public void setDrawerEnable() {
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	}

	public void setDrawerDisable() {
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	}

	class MyBroadCaseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			// 由于添加页面AddNoteTextActivity不能调用其他自定义Pager对象的刷新UI的方法，所以在mainActivity中发广播，去刷新
			AllNotesPager allNotesPager = (AllNotesPager) contentFragment
					.getPagers().get(0);
			allNotesPager.getAllNotesContentPager().initData();
		}

	}
	
	
	class NoteBookBroadCaseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			// 由于添加页面NoteBookActivity不能调用其他自定义Pager对象的刷新UI的方法，所以在mainActivity中发广播，去刷新
			NoteBooksPager noteBooksPager = (NoteBooksPager) contentFragment
					.getPagers().get(1);
			noteBooksPager.getNoteBookContentPager().initData();
		}

	}
	
	

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		unregisterReceiver(noteBookReceiver);
		super.onDestroy();
	}
}
