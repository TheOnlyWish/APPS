package com.king.loadnote.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import android.util.Base64;

/**
 * ���ڽ��ı����ļ���IO��ת����ΪBase64�Ĺ����� ע��:�ļ����ı�����̫�� suggest:
 * ��post�����ϴ����ݣ���Ϊget���󴫵ݵ������ı�������
 * 
 * @author Administrator
 * 
 */
public class ConvertUtil {

	/**
	 * ������String�ı�ת��base64
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
	 * ��base64�ַ���ת����String�ַ���
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
	 * ��������ת����base64 ����:�����Զ��ر�
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
	 * ��base64���룬����outputStream--�ļ�
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
