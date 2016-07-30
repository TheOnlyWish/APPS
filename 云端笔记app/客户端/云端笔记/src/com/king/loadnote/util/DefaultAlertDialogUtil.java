package com.king.loadnote.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DefaultAlertDialogUtil {

	private static DefaultAlertDialogUtil util;
	private static Context context;
	
	
	private DefaultAlertDialogUtil(Context context) {
		this.context = context;
	}

	public static DefaultAlertDialogUtil getInstant(Context context){
		return util;
	}
	
	
	
	public void show(String title, String content, String yes, String no){
		AlertDialog.Builder builder = new Builder(context); 
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(yes, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				myDialogInterface.clickPositiveButton();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(no, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				myDialogInterface.clickNegativeButton();
				dialog.dismiss();
			}
		});
		AlertDialog ad = builder.create();
		ad.show();
	}
	
	private MyDialogInterface myDialogInterface;
	public void setMyDialogInterface(MyDialogInterface myDialogInterface) {
		this.myDialogInterface = myDialogInterface;
	}
	public interface MyDialogInterface{
		
		void clickPositiveButton();
		void clickNegativeButton();
		
	}
	
}
