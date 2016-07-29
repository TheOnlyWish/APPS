package com.king.loadnote.globle;

/**
 * 全局变量
 * @author Administrator
 *
 */
public class Globle {

	// 主机+项目名
	public static String SERVLER_URL = "http://192.168.155.1:8080/cloudnote";
	// 注册地址
	public static String REGIST_URL = SERVLER_URL +"/RegistServlet";
	// 请求验证码
	public static String REQUEST_CHECKCODE = SERVLER_URL +"/CheckCodeServlet";
	// 登录地址
	public static String LOGIN_URL = SERVLER_URL +"/LoginServlet";
	// 上传文件+文本地址
	public static String UPLOAD_URL = SERVLER_URL + "/UpdateServlete";
	// 上传文本
	public static String UPLOAD_TEXT_URL = SERVLER_URL + "/UpdateTextServlet";
	// 下载数据
	public static String DOWNLOAD_URL = SERVLER_URL + "/DownLoadServlet";
	
	
	
}
