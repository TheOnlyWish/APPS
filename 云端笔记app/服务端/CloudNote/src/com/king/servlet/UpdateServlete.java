package com.king.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import com.king.domain.Ids;
import com.king.domain.NoteJson;
import com.king.globle.Globle;
import com.king.service.NoteService;
import com.king.service.UserService;

public class UpdateServlete extends HttpServlet {

	NoteService noteService = new NoteService();
	UserService userService = new UserService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html,charset=UTF-8");

		String save_path = "D:/apache-tomcat-6.0.35/webapps/cloudnote/files/";
		SmartUpload smartUpload = new SmartUpload();

		try {
			smartUpload.initialize(this.getServletConfig(), request, response);
			smartUpload.upload();
			Files listFile = smartUpload.getFiles();

			String note = URLDecoder.decode(smartUpload.getRequest()
					.getParameter("upnotes"), "utf-8");
			System.out.println("note:" + note);
			String fileTypes = smartUpload.getRequest().getParameter(
					"filetypes");
			String fileNames = URLDecoder.decode(smartUpload.getRequest()
					.getParameter("names"), "utf-8");
			String user = smartUpload.getRequest().getParameter("user");
			String books = smartUpload.getRequest().getParameter("books");

			System.out.println("books:" + books);

			List<String> types = null;
			List<String> names = null;

			System.out.println("fileTypes" + fileTypes);
			System.out.println("fileNames" + fileNames);
			System.out.println("user" + user);

			System.out.println("user:" + user);
			save_path += user;
			System.out.println("save_path" + save_path);
			File f = new File(save_path);
			System.out.println("isExist:" + f.exists());
			System.out.println("付文件架被创建");
			if(!f.exists()){
				f.mkdirs();
			}

			System.out.println("用户名文件架被创建");

			/**
			 * 1. 解析json 成List<NoteJson> 2. 生成本地文件，并将路径存放到NoteJson中 3.
			 * 将List<NoteJson>存储到数据库
			 */

			Gson gson = new Gson();

			types = gson.fromJson(fileTypes, new TypeToken<List<String>>() {
			}.getType());
			names = gson.fromJson(fileNames, new TypeToken<List<String>>() {
			}.getType());

			List<NoteJson> list = gson.fromJson(note,
					new TypeToken<List<NoteJson>>() {
					}.getType());

			System.out.println("list集合：" + list);

			// 某个笔记的文件集合的初始索引
			int index = 0;
			// 某个笔记的文件集合的结尾索引
			int latter = 0;

			// 查询用户id
			int id = userService.findIdByTel(user);

			System.out.println("list集合大小:" + list.size());

			// 每份笔记
			for (int i = 0; i < list.size(); i++) {
				// 获取笔记所带附件的数量
				int count = list.get(i).attachmentCount;
				// 笔记
				NoteJson noteJson = list.get(i);
				// user id 赋值给notejson
				noteJson.out_key_user_id = id;
				// 有附件
				if (count > 0) {
					// 服务器内部存的文件地址
					StringBuilder server_uri = new StringBuilder();
					// 客户端储存路径
					// StringBuilder client_uri = new StringBuilder();

					// 客户端存储类型
					StringBuilder client_type = new StringBuilder();

					// 客户端存储名
					StringBuilder client_name = new StringBuilder();

					// 附件名称
					String[] fNames = noteJson.attachment_name_str.split(",");
					// 更改结尾索引
					latter = index + count;
					for (int k = index; k < latter; k++) {
						String filePath = save_path + "/" + fNames[k - index];
						// 保存文件
						com.jspsmart.upload.File file = listFile.getFile(k);

						server_uri.append( Globle.APPURL +"/files/" + user + "/"
								+ fNames[k - index] + ",");
						System.out.println("储存名:" + "/files/" + user + "/"
								+ fNames[k - index]);

						file.saveAs("/files/" + user + "/" + fNames[k - index],
								SmartUpload.SAVE_VIRTUAL);
					}

					// 服务器储存地址
					noteJson.server_attachment_uri = server_uri.deleteCharAt(
							server_uri.length() - 1).toString();
					// 更改起始索引
					index = latter;
				}
			}

			List<Ids> ListIds = new ArrayList<Ids>();
			// 写入数据库
			for (NoteJson nj : list) {
				System.out.println("nj:" + nj.toString());
				// 修改updated的值
				nj.updated = 0;
				noteService.addOrUpdateUser(nj);
				/**
				 * 获取此集合中所有的笔记的server_id, client_id 写回给客户端
				 */
				Ids ids = noteService.queryIds(nj);
				ListIds.add(ids);
			}
			String idsJson = gson.toJson(ListIds, new TypeToken<List<Ids>>() {
			}.getType());
			System.out.println("idsJson:" + idsJson);
			// 返回json
			response.getWriter().write(idsJson);

		} catch (Exception e) {
			response.getWriter().print(e);
		}
	}

}
