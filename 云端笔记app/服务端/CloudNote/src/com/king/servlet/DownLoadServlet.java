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
		 * 1. 查询该用户下的所有server_id集合 2. 获取客户端传过来的json， 解析成List<Integer> 3.
		 * 在server_id集合中移除List<Integer> 4. 判断移除后的集合大小是否大于0 5. 大于0，访问数据库的去记录集合 6.
		 * 生成json，发送到客户端 7. 文件将下载路径包含到json中
		 */
		// 从客户端获取用户电话号码
		String tel = request.getParameter("user");

		// 查询该用户下的所有server_id集合
		int userId = userService.findIdByTel(tel);
		// 数据库中查询
		List<Ids> ids = noteService.getServerIdsByTel(userId);

		System.out.println("服务端：" + ids);

		// 获取客户端传过来的json(server_id)， 解析成List<Integer>
		String idsJson = request.getParameter("ids");
		Gson gson = new Gson();

		List<Integer> client_ids = gson.fromJson(idsJson,
				new TypeToken<List<Integer>>() {
				}.getType());
		System.out.println("客户端:" + client_ids);
		System.out.println("server_id集合中移除List之前：" + ids.size());
		// 在server_id集合中移除List<Integer>
		for (int i = 0; i < client_ids.size(); i++) {
			int id = client_ids.get(i);
			// 判断是否存在于listIds
			for(int j = 0; j < ids.size(); j ++){
				int _id = ids.get(j).server_id;
				System.out.println("客户端：" + id);
				System.out.println("服务端：" + _id);
				
				if(_id == id){
					ids.get(j).server_id = -1;
					break;
				}
			}
		}
		System.out.println("server_id集合中移除List之后：" + ids.size());
		// 判断移除后的集合大小是否大于0
		if (ids.size() > 0) {
			// 需要传输数据
			List<Note> notes = new ArrayList<Note>();
			List<NoteBook> books = new ArrayList<NoteBook>();
			// 访问数据库的记录集合
			for (int i = 0; i < ids.size(); i++) {
				Note note = noteService.getNote(ids.get(i).server_id);
				if(ids.get(i).server_id != -1){
					notes.add(note);
				}
			}

			System.out.println("访问数据库的记录集合 :" + notes.toString());
			String json = gson.toJson(notes, new TypeToken<List<Note>>() {
			}.getType());

			response.getWriter().write(URLEncoder.encode(json, "utf-8"));

			System.out.println("json:" + json);

		} else {
			// 不需要传输数据
			response.getWriter().write("noData");
		}
	}
}
