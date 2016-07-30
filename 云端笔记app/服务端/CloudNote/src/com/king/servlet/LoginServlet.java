package com.king.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.service.UserService;

public class LoginServlet extends HttpServlet {

	private UserService service = new UserService();
	
	private final String LOGIN_SUCCESS = "200";
	private final String LOGIN_FAIL = "400";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// ��ȡ����
		String telNumber = request.getParameter("telNumber");
		String password = request.getParameter("password");
		System.out.println("telNumber"+telNumber+",password"+password);
		// ��ѯ���ݿ�
		boolean isOk = service.userLogin(telNumber, password);
		System.out.println("**test:LoginServlet:userLogin:"+isOk);
		PrintWriter pw = response.getWriter();
		if(isOk){
			// ����һ����ʶ����ʾ��¼�ɹ�
			pw.write(LOGIN_SUCCESS);
		}else{
			// ����һ����ʶ����ʾ��¼ʧ�ܣ���֤�����
			pw.write(LOGIN_FAIL);
		}
		pw.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
