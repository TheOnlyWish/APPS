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

			// ĳ���ʼǵ��ļ����ϵĳ�ʼ����
			int index = 0;
			// ĳ���ʼǵ��ļ����ϵĽ�β����
			int latter = 0;

			// ��ѯ�û�id
			int id = userService.findIdByTel(user);

			// �ʼǱ�д�뵽���ݿ�
			for (NoteBook book : noteBooks) {
				// ���û�id��ֵ
				book.out_key_user_id = id;
				noteBookService.addOrUpdateNoteBook(book);
			}

			System.out.println("book:" + books.toString());
			List<Ids> ListIds = new ArrayList<Ids>();
			// �ʼ�д�����ݿ�
			for (int i = 0; i < list.size(); i++) {
				// ��ȡ�ʼ���������������
				int count = list.get(i).attachmentCount;
				// �ʼ�
				NoteJson noteJson = list.get(i);
				// �޸�updated��ֵ
				noteJson.updated = 0;
				// user id ��ֵ��notejson
				noteJson.out_key_user_id = id;
				
				noteService.addOrUpdateUser(noteJson);

				/**
				 * ��ȡ�˼��������еıʼǵ�server_id, client_id д�ظ��ͻ���
				 */
				Ids ids = noteService.queryIds(noteJson);
				ListIds.add(ids);

				String idsJson = gson.toJson(ListIds,
						new TypeToken<List<Ids>>() {
						}.getType());
				System.out.println("ListIds:" + idsJson);
				// ����json
				response.getWriter().write(idsJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			new RuntimeException(e);
		}
	}

}
