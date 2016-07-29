package com.king.loadnote.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5±àÂë¹¤¾ß
 * @author Administrator
 */
public class Md5Util {

	/**
	 * md5±àÂë
	 * @param value
	 * @return
	 */
	public static String encoder(String value){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md5.update(value.getBytes());
		byte[] bytes = md5.digest();
		return getString(bytes);
	}
	
	 private static String getString(byte[] b){  
	        StringBuffer sb = new StringBuffer();  
	         for(int i = 0; i < b.length; i ++){  
	        	 sb.append(b[i]);  
	         }  
	         return sb.toString();  
	}  
	
}
