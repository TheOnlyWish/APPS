package com.king.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.utils.ConvertUtil;

public class SynchronizationServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		/**
		 * ×Ô¶¨Òåbase64Util
		 */
		File file = new File("D:\\cloudTree.png");
		FileInputStream fis = new FileInputStream(file);
		String base64 = ConvertUtil.InputStreamToBase64(fis);
		System.out.println("base64:"+base64);
		ConvertUtil.base64ToFile(base64, "D:", "1.png");
		System.out.println("finish!");
		
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
