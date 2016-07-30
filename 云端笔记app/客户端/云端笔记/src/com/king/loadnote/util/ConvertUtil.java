package com.king.loadnote.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import android.util.Base64;

/**
 * 用于将文本，文件，IO流转换成为Base64的工具类 注意:文件或文本不能太大 suggest:
 * 用post请求上传数据，因为get请求传递的数据文本有限制
 * 
 * @author Administrator
 * 
 */
public class ConvertUtil {

	/**
	 * 将长串String文本转化base64
	 * 
	 * @param str
	 * @return
	 */
	public static String StrToBase64(String str) {
		try {
			return new String(Base64.encode(str.getBytes("utf-8"),
					Base64.DEFAULT), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将base64字符串转化成String字符串
	 * 
	 * @param base64
	 * @return
	 */
	public static String base64ToStr(String base64) {
		try {
			return new String(Base64.decode(base64, Base64.DEFAULT), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将输入流转换成base64 附加:流会自动关闭
	 * 
	 * @return
	 */
	public static String encodeBase64File(String path) {
		File file = new File(path);
		FileInputStream inputFile;
		try {
			inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将base64解码，生成outputStream--文件
	 * 
	 * for example: 316KB -- base64 -- 78KB
	 */
	public static void decoderBase64File(String base64Code, String savePath)
			throws Exception {
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		FileOutputStream out = new FileOutputStream(savePath);
		out.write(buffer);
		out.close();
	}

}
