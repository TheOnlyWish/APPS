package com.king.domain;

/**
 * json�Ľ�������
 * @author Administrator
 *
 */
public class JsonData {

	public Resp resp;
	
	public class Resp{
		public String respCode;
		public String failure;
		public TemplateSMS templateSMS;
		
	}
	
	public class TemplateSMS{
		public String createDate;
		public String smsId;
	}
	
}
