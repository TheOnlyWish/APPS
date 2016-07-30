package com.king.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.domain.DataBean;
import com.king.service.UserService;

public class RegistServlet extends HttpServlet {

	private Map<String, DataBean> map = CheckCodeServlet.map;

	private UserService service = new UserService();

	private final String REGIST_SUCCESS = "200";
	private final String NOT_REGIST_AGAIN = "300";
	private final String REGIST_FAIL = "400";

	/**
	 * 注意：客户端自动检验时间是否超过60秒 判断弹框：重新获取验证码
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// 获取用户提交的参数
		// 获取电话号码
		String telNumber = (String) request.getParameter("telNumber");
		// 获取用户传来的被base64编码后的密码
		String password = (String) request.getParameter("password");
		// 获取用户传入的验证码
		String code = (String) request.getParameter("code");

		System.out.println("**test:RegistServlet:" + code);
		// ?telNumber=15170066526&password=123456&code=2020
		// 如果集合中存在
		PrintWriter pw = response.getWriter();
		// 判断数据库中是否存在
		boolean isExit = service.userQuery(telNumber, password);
		if (isExit) {
			pw.write(NOT_REGIST_AGAIN);
			pw.close();
			return;
		}

		///////////////////
//		int code_ = Integer.parseInt(code);
//		Set<String> keys = map.keySet();
//		for (String key : keys) {
//			System.out.println("registServlet中的map的键值key:"+key);
//			System.out.println("registServlet中的map的键值value:"+map.get(key).code);
//			if (key.equals(telNumber)) {
//				DataBean data = map.get(key);
//				if (data.code == code_) {
//					System.out.println("存在与map中");
//				}
//			}
//		}
		System.out.println("mapSize:"+map.size());
		///////////////////
//		boolean matchOk = isExistInMap(telNumber, Integer.parseInt(code));
		boolean matchOk = true;
		System.out.println("matchOk:" + matchOk);
		if (matchOk) {
			// 存放到数据库
			service.userRegist(telNumber, password);
			// 返回一个标识，表示注册成功
			pw.write(REGIST_SUCCESS);
		} else {
			// 返回一个标识，表示注册失败，验证码错误
			pw.write(REGIST_FAIL);

		}
		pw.close();
	}

	/**
	 * 判断是否存在与map集合，并匹配正确
	 */
	private boolean isExistInMap(String telNumber, int code) {
		Set<String> keys = map.keySet();
		for (String key : keys) {
			System.out.println("registServlet中的map的键值key:"+key);
			System.out.println("registServlet中的map的键值value:"+map.get(key).code);
			if (key.equals(telNumber)) {
				DataBean data = map.get(key);
				if (data.code == code) {
					removeFromMap(telNumber);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 移除数据从map
	 * 
	 * @param telNumber
	 */
	synchronized private void removeFromMap(String telNumber) {
		map.remove(telNumber);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
