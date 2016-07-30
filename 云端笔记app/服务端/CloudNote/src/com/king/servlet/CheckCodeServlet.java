package com.king.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.king.domain.CodeMessage;
import com.king.domain.DataBean;
import com.king.domain.JsonData;

public class CheckCodeServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		super.init();
		// �����Ƿ����overtime��keyValue
		listenerMap();
	}

	/**
	 * sid String ��ѡ ���˻�id //
	 * appId String ��ѡ Ӧ��Id //
	 * sign String ��ѡ ��֤��Ϣ��ʹ��MD5���ܣ��˻�id+ʱ���+�˻���Ȩ���ƣ�����32λ��Сд�� //
	 * time String ��ѡ ʱ���yyyyMMddHHmmssSSS����Чʱ��Ϊ30���� //
	 * templateId String ��ѡ ģ��Id //
	 * to String ��ѡ ���Ž��ն��ֻ����루���ڶ��Ų�Ҫ��ǰ׺�����ʶ��ź���ǰ�����Ӧ�Ĺ������ţ����ձ���0081��//
	 * param String ��ѡ �������ݣ������滻ģ����{����}�����ж���滻���ݣ���Ӣ�Ķ��Ÿ�������//
	 */
	public static Map<String, DataBean> map = new HashMap<String, DataBean>();
	public static final String REQUEST_CODE_SUCCESS = "200";
	public static final String REQUEST_CODE_FAIL = "400";
	

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// ��ȡ�绰����
		String telNumber = (String) request.getParameter("telNumber");
		// ��ȡʱ���ַ���
		String nowTime = (String) request.getParameter("nowTime");
		// ��ȡʱ�����ֵ
		long clickTime = Long.parseLong(request.getParameter("clickTime"));
		// ����CodeMessage����
		CodeMessage cm = new CodeMessage();
		cm.setTo(telNumber);
		cm.setTime(nowTime+"000");
		System.out.println("**test:CheckCodeServlet:"+cm);
		
		// ���ö��Žӿ�
		boolean flag = sendGet(cm,clickTime);
		// ���Դ���
//		boolean flag = true;
		PrintWriter pw = response.getWriter();
		if(flag){
			// ����һ����ʶ����ʾ������뷢�ͳɹ�
			pw.write(REQUEST_CODE_SUCCESS);
		}else{
			// ����һ����ʶ����ʾ������뷢��ʧ��
			pw.write(REQUEST_CODE_FAIL);
		}
		pw.close();
		
		// http://www.ucpaas.com/maap/sms/code?sid=58d0ef07ce2fafe13bbc8f841f90622a&appId=ad8c48fb47ae4c4f98f62ce6d6be7f46&time=20160531083810000&sign=bd95fd2d04cfd5ff223dbb75380c9a35&to=15170066526&templateId=24745&param=15170066526,1
		
	}

	/**
	 * �����Ƿ����overtime��keyValue
	 */
	private void listenerMap() {
		new Thread(){
			public void run() {
				while(true){
					if(map != null && map.size() > 0){
						removeTimeOverKeyValue();
					}
					
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("����map���ϵļ���:"+map.size());
				}
				
			};
		}.start();
	}
	
	/**
	 * �������ϲ��ҳ�ʱkeyValue���Ƴ�
	 */
	private void removeTimeOverKeyValue() {
		Set<String> keys = map.keySet();
		long currentTime = new Date().getTime();
		for(String key : keys){
			DataBean data = map.get(key);
			System.out.println("currentTime:"+currentTime+",data.clcikTime:"+data.clickTime);
			if((currentTime - data.clickTime) > (data.clickTime + 60*1000*2)){
				map.remove(key);
			}
		}
	}

	/**
	 * ���ö��Žӿ�
	 * @param cm
	 */
	private boolean sendGet(CodeMessage cm, long clickTime) {
		Random r = new Random();
		int code = r.nextInt(9000)+1000;
		// ������֤��Ϊ{1}������{2}��������ȷ������֤��
		cm.setParam(code+","+1);
		String spec = cm.getRootURL()+"?sid="+cm.getSid()+"&appId="+cm.getAppId()+"&time="+cm.getTime()+"&sign="+cm.getSign()+"&to="+cm.getTo()
		+"&templateId="+cm.getTemplateId()+"&param="+cm.getParam();
		System.out.println("cm:spec:"+spec);
		try {
			URL url = new URL(spec);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// ����ͨ�õ���������
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			conn.connect();
			InputStream in = conn.getInputStream();
			StringBuffer sb = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String value = "";
			while((value = reader.readLine())!= null){
				sb.append(value);
			}
			String json = sb.toString();
			
			System.out.println("json:"+json);
			
			Gson gson = new Gson();
			JsonData jsonData = gson.fromJson(json, JsonData.class);
			
			// ����json
			System.out.println("Test:jsonData:jsonData:"+jsonData.resp.respCode+","+jsonData.resp.failure);
			// ����ӿڵ����ж�
			if(jsonData.resp.respCode.equals("000000")){ // 0000000
				// �洢��map����
				saveToMap(cm.getTo(), cm.getTime(), clickTime, code);
				System.out.println("�浽map������");
				return true;
			}else{
				// fail
				System.out.println("δ�浽map����");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * ���浽Map
	 * @param telNumber
	 * @param nowTime
	 */
	 public void saveToMap(String telNumber, String nowTime, long clickTime, int code) {
		DataBean dataBean = new DataBean();
		dataBean.code = code;
		dataBean.clickTime = clickTime;
		dataBean.time = nowTime;
		map.put(telNumber, dataBean);
		
	}

}
