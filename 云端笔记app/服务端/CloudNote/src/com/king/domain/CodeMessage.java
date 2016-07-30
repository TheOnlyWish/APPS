package com.king.domain;

import com.king.utils.EncryptUtil;

public class CodeMessage {
	// 短信接口地址
	private String rootURL = "http://www.ucpaas.com/maap/sms/code";
	// 账户id
	private String sid = "58d0ef07ce2fafe13bbc8f841f90622a";
	// 应用id
	private String appId = "ad8c48fb47ae4c4f98f62ce6d6be7f46";
	// 签名
	private String sign;
	// 时间戳
	private String time;
	// 短信模版id
	private String templateId = "24745";
	// 发送短信的number
	private String to;
	// 参数
	private String param;
	// 用户授权令牌
	private String authToken = "3e95e29d7843ef39275232064a9a26e3";
	// code
	private String code;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CodeMessage() {
		super();
	}

	public CodeMessage(String time, String to, String param) {
		super();
		this.time = time;
		this.to = to;
		this.param = param;
	}

	public void setRootURL(String rootURL) {
		this.rootURL = rootURL;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getSid() {
		return sid;
	}

	public String getAppId() {
		return appId;
	}

	/**
	 * 整合
	 * 
	 * @return
	 */
	public String getSign() {
		EncryptUtil encodeUtil = new EncryptUtil();
		String src = getSid() + getTime() + getAuthToken();
		System.out.println("src:"+src);
		try {
			return encodeUtil.md5Digest(src).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getRootURL() {
		return rootURL;
	}

	@Override
	public String toString() {
		return "CodeMessage [rootURL=" + rootURL + ", sid=" + sid + ", appId="
				+ appId + ", sign=" + sign + ", time=" + time + ", templateId="
				+ templateId + ", to=" + to + ", param=" + param
				+ ", authToken=" + authToken + "]";
	}
	
}
