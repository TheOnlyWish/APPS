package com.king.servlet;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.domain.Ids;
import com.king.domain.Note;
import com.king.domain.NoteBook;
import com.king.service.NoteBookService;
import com.king.service.NoteService;
import com.king.service.UserService;

public class DownLoadServlet extends HttpServlet {

	UserService userService = new UserService();
	NoteService noteService = new NoteService();
	NoteBookService noteBookService = new NoteBookService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html,charset=UTF-8");
		/**
		 * 1. ��ѯ���û��µ�����server_id���� 2. ��ȡ�ͻ��˴�������json�� ������List<Integer> 3.
		 * ��server_id�������Ƴ�List<Integer> 4. �ж��Ƴ���ļ��ϴ�С�Ƿ����0 5. ����0���������ݿ��ȥ��¼���� 6.
		 * ����json�����͵��ͻ��� 7. �ļ�������·��������json��
		 */
		// �ӿͻ��˻�ȡ�û��绰����
		String tel = request.getParameter("user");

		// ��ѯ���û��µ�����server_id����
		int userId = userService.findIdByTel(tel);
		// ���ݿ��в�ѯ
		List<Ids> ids = noteService.getServerIdsByTel(userId);

		System.out.println("����ˣ�" + ids);

		// ��ȡ�ͻ��˴�������json(server_id)�� ������List<Integer>
		String idsJson = request.getParameter("ids");
		Gson gson = new Gson();

		List<Integer> client_ids = gson.fromJson(idsJson,
				new TypeToken<List<Integer>>() {
				}.getType());
		System.out.println("�ͻ���:" + client_ids);
		System.out.println("server_id�������Ƴ�List֮ǰ��" + ids.size());
		// ��server_id�������Ƴ�List<Integer>
		for (int i = 0; i < client_ids.size(); i++) {
			int id = client_ids.get(i);
			// �ж��Ƿ������listIds
			for(int j = 0; j < ids.size(); j ++){
				int _id = ids.get(j).server_id;
				System.out.println("�ͻ��ˣ�" + id);
				System.out.println("����ˣ�" + _id);
				
				if(_id == id){
					ids.get(j).server_id = -1;
					break;
				}
			}
		}
		System.out.println("server_id�������Ƴ�List֮��" + ids.size());
		// �ж��Ƴ���ļ��ϴ�С�Ƿ����0
		if (ids.size() > 0) {
			// ��Ҫ��������
			List<Note> notes = new ArrayList<Note>();
			List<NoteBook> books = new ArrayList<NoteBook>();
			// �������ݿ�ļ�¼����
			for (int i = 0; i < ids.size(); i++) {
				Note note = noteService.getNote(ids.get(i).server_id);
				if(ids.get(i).server_id != -1){
					notes.add(note);
				}
			}

			System.out.println("�������ݿ�ļ�¼���� :" + notes.toString());
			String json = gson.toJson(notes, new TypeToken<List<Note>>() {
			}.getType());

			response.getWriter().write(URLEncoder.encode(json, "utf-8"));

			System.out.println("json:" + json);

		} else {
			// ����Ҫ��������
			response.getWriter().write("noData");
		}
	}
}
