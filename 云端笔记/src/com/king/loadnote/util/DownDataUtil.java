package com.king.loadnote.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

/**
 * 数据恢复
 * @author Administrator
 *
 */
public class DownDataUtil {

	private Context ctx;
	private static DownDataUtil ddu;
	
	private DownDataUtil(Context ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * 获取单例的DownDataUtil工具类对象
	 * @param ctx
	 * @return
	 */
	public static DownDataUtil getInstant(Context ctx){
		if(ddu == null){
			ddu = new DownDataUtil(ctx);
		}
		return ddu;
	}
	
	/**
	 * 发送数据
	 */
	public void DownLoadFile(final String server_url, final String local_url){
		new Thread(){
			public void run() {
				HttpURLConnection conn = null;
				DataInputStream dis = null;
				FileOutputStream fos = null;
				try{
					URL url = new URL(server_url);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setReadTimeout(8000);
					conn.setConnectTimeout(8000);
					conn.connect();
					if(conn.getResponseCode() == 200){
						InputStream in = conn.getInputStream();
						dis = new DataInputStream(in);
						fos = new FileOutputStream(new File(local_url));
						
						byte[] bytes = new byte[1024];
						int len = -1;
						while((len = dis.read(bytes))>=-1){
							fos.write(bytes);
						}
						fos.flush();
						System.out.println("数据写入完成");
						onFileDownLoad.onSuccess();
					}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						try{
							fos.close();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				
			};
		}.start();
		
	}
	
	private OnFileDownLoad onFileDownLoad;
	public void setOnFileDownLoad(OnFileDownLoad onFileDownLoad) {
		this.onFileDownLoad = onFileDownLoad;
	}
	public interface OnFileDownLoad{
		public void onSuccess();
	}
	
}
