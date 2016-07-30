package com.king.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * ���ڽ��ı����ļ���IO��ת����ΪBase64�Ĺ����� ע��:�ļ����ı�����̫��
 * suggest:
 * 		��post�����ϴ����ݣ���Ϊget���󴫵ݵ������ı�������
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
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			return encoder.encode(str.getBytes("UTF-8"));
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
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return new String(decoder.decodeBuffer(base64), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��������ת����base64 ����:�����Զ��ر�
	 * 
	 * @param in
	 * @return
	 */
	public static String InputStreamToBase64(InputStream in) {
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			return encoder.encode(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * ��base64���룬����outputStream--�ļ�
	 * 
	 * for example:
	 * 		316KB -- base64 -- 78KB
	 * 
	 * @param base64
	 * @param parent
	 *            ��·��
	 * @param fileName
	 *            �ļ���(�ļ���.��׺)
	 */
	public static void base64ToFile(String base64, String parent,
			String fileName) {
		BASE64Decoder decoder = new BASE64Decoder();
		ByteArrayInputStream bais = null;
		FileOutputStream fos = null;
		try {
			byte[] bytes = decoder.decodeBuffer(base64);
			bais = new ByteArrayInputStream(bytes);
			File file = new File(parent, fileName);
			fos = new FileOutputStream(file);
			byte[] bys = new byte[1024];
			int len = -1;
			while ((bais.read(bys)) != -1) {
				fos.write(bys);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			try {
				if (bais != null) {
					bais.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

}
