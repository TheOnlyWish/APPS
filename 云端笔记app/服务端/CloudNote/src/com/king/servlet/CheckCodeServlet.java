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
		// 监听是否存在overtime的keyValue
		listenerMap();
	}

	/**
	 * sid String 必选 主账户id //
	 * appId String 必选 应用Id //
	 * sign String 必选 验证信息，使用MD5加密（账户id+时间戳+账户授权令牌），共32位（小写） //
	 * time String 必选 时间戳yyyyMMddHHmmssSSS，有效时间为30分钟 //
	 * templateId String 必选 模板Id //
	 * to String 必选 短信接收端手机号码（国内短信不要加前缀，国际短信号码前须带相应的国家区号，如日本：0081）//
	 * param String 必选 内容数据，用于替换模板中{数字}，若有多个替换内容，用英文逗号隔开即可//
	 */
	public static Map<String, DataBean> map = new HashMap<String, DataBean>();
	public static final String REQUEST_CODE_SUCCESS = "200";
	public static final String REQUEST_CODE_FAIL = "400";
	

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// 获取电话号码
		String telNumber = (String) request.getParameter("telNumber");
		// 获取时间字符串
		String nowTime = (String) request.getParameter("nowTime");
		// 获取时间毫秒值
		long clickTime = Long.parseLong(request.getParameter("clickTime"));
		// 创建CodeMessage对象
		CodeMessage cm = new CodeMessage();
		cm.setTo(telNumber);
		cm.setTime(nowTime+"000");
		System.out.println("**test:CheckCodeServlet:"+cm);
		
		// 调用短信接口
		boolean flag = sendGet(cm,clickTime);
		// 测试代码
//		boolean flag = true;
		PrintWriter pw = response.getWriter();
		if(flag){
			// 返回一个标识，表示服务端请发送成功
			pw.write(REQUEST_CODE_SUCCESS);
		}else{
			// 返回一个标识，表示服务端请发送失败
			pw.write(REQUEST_CODE_FAIL);
		}
		pw.close();
		
		// http://www.ucpaas.com/maap/sms/code?sid=58d0ef07ce2fafe13bbc8f841f90622a&appId=ad8c48fb47ae4c4f98f62ce6d6be7f46&time=20160531083810000&sign=bd95fd2d04cfd5ff223dbb75380c9a35&to=15170066526&templateId=24745&param=15170066526,1
		
	}

	/**
	 * 监听是否存在overtime的keyValue
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
					
					System.out.println("测试map集合的监听:"+map.size());
				}
				
			};
		}.start();
	}
	
	/**
	 * 遍历集合查找超时keyValue并移除
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
	 * 调用短信接口
	 * @param cm
	 */
	private boolean sendGet(CodeMessage cm, long clickTime) {
		Random r = new Random();
		int code = r.nextInt(9000)+1000;
		// 您的验证码为{1}，请于{2}分钟内正确输入验证码
		cm.setParam(code+","+1);
		String spec = cm.getRootURL()+"?sid="+cm.getSid()+"&appId="+cm.getAppId()+"&time="+cm.getTime()+"&sign="+cm.getSign()+"&to="+cm.getTo()
		+"&templateId="+cm.getTemplateId()+"&param="+cm.getParam();
		System.out.println("cm:spec:"+spec);
		try {
			URL url = new URL(spec);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置通用的请求属性
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent",
//					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
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
			
			// 解析json
			System.out.println("Test:jsonData:jsonData:"+jsonData.resp.respCode+","+jsonData.resp.failure);
			// 如果接口调用判断
			if(jsonData.resp.respCode.equals("000000")){ // 0000000
				// 存储到map集合
				saveToMap(cm.getTo(), cm.getTime(), clickTime, code);
				System.out.println("存到map集合了");
				return true;
			}else{
				// fail
				System.out.println("未存到map集合");
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
	 * 保存到Map
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
