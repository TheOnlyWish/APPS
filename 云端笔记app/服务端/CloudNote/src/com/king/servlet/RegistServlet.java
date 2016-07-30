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
	 * ע�⣺�ͻ����Զ�����ʱ���Ƿ񳬹�60�� �жϵ������»�ȡ��֤��
	 */

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// ��ȡ�û��ύ�Ĳ���
		// ��ȡ�绰����
		String telNumber = (String) request.getParameter("telNumber");
		// ��ȡ�û������ı�base64����������
		String password = (String) request.getParameter("password");
		// ��ȡ�û��������֤��
		String code = (String) request.getParameter("code");

		System.out.println("**test:RegistServlet:" + code);
		// ?telNumber=15170066526&password=123456&code=2020
		// ��������д���
		PrintWriter pw = response.getWriter();
		// �ж����ݿ����Ƿ����
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
//			System.out.println("registServlet�е�map�ļ�ֵkey:"+key);
//			System.out.println("registServlet�е�map�ļ�ֵvalue:"+map.get(key).code);
//			if (key.equals(telNumber)) {
//				DataBean data = map.get(key);
//				if (data.code == code_) {
//					System.out.println("������map��");
//				}
//			}
//		}
		System.out.println("mapSize:"+map.size());
		///////////////////
//		boolean matchOk = isExistInMap(telNumber, Integer.parseInt(code));
		boolean matchOk = true;
		System.out.println("matchOk:" + matchOk);
		if (matchOk) {
			// ��ŵ����ݿ�
			service.userRegist(telNumber, password);
			// ����һ����ʶ����ʾע��ɹ�
			pw.write(REGIST_SUCCESS);
		} else {
			// ����һ����ʶ����ʾע��ʧ�ܣ���֤�����
			pw.write(REGIST_FAIL);

		}
		pw.close();
	}

	/**
	 * �ж��Ƿ������map���ϣ���ƥ����ȷ
	 */
	private boolean isExistInMap(String telNumber, int code) {
		Set<String> keys = map.keySet();
		for (String key : keys) {
			System.out.println("registServlet�е�map�ļ�ֵkey:"+key);
			System.out.println("registServlet�е�map�ļ�ֵvalue:"+map.get(key).code);
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
	 * �Ƴ����ݴ�map
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
