package com.king.servlet;

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
import com.king.domain.Ids;
import com.king.domain.NoteBook;
import com.king.domain.NoteJson;
import com.king.service.NoteBookService;
import com.king.service.NoteService;
import com.king.service.UserService;

public class UpdateTextServlet extends HttpServlet {

	NoteService noteService = new NoteService();
	UserService userService = new UserService();
	NoteBookService noteBookService = new NoteBookService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html,charset=UTF-8");

		try {

			String note = URLDecoder.decode(request.getParameter("upnotes"),
					"utf-8");
			String user = request.getParameter("user");

			String books = request.getParameter("books");

			Gson gson = new Gson();

			List<NoteJson> list = gson.fromJson(note,
					new TypeToken<List<NoteJson>>() {
					}.getType());

			List<NoteBook> noteBooks = gson.fromJson(books,
					new TypeToken<List<NoteBook>>() {
					}.getType());

			// 某个笔记的文件集合的初始索引
			int index = 0;
			// 某个笔记的文件集合的结尾索引
			int latter = 0;

			// 查询用户id
			int id = userService.findIdByTel(user);

			// 笔记本写入到数据库
			for (NoteBook book : noteBooks) {
				// 将用户id赋值
				book.out_key_user_id = id;
				noteBookService.addOrUpdateNoteBook(book);
			}

			System.out.println("book:" + books.toString());
			List<Ids> ListIds = new ArrayList<Ids>();
			// 笔记写入数据库
			for (int i = 0; i < list.size(); i++) {
				// 获取笔记所带附件的数量
				int count = list.get(i).attachmentCount;
				// 笔记
				NoteJson noteJson = list.get(i);
				// 修改updated的值
				noteJson.updated = 0;
				// user id 赋值给notejson
				noteJson.out_key_user_id = id;
				
				noteService.addOrUpdateUser(noteJson);

				/**
				 * 获取此集合中所有的笔记的server_id, client_id 写回给客户端
				 */
				Ids ids = noteService.queryIds(noteJson);
				ListIds.add(ids);

				String idsJson = gson.toJson(ListIds,
						new TypeToken<List<Ids>>() {
						}.getType());
				System.out.println("ListIds:" + idsJson);
				// 返回json
				response.getWriter().write(idsJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException(e);
		}
	}

}
