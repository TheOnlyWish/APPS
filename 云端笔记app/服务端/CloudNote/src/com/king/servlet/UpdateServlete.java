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
			System.out.println("���ļ��ܱ�����");
			if(!f.exists()){
				f.mkdirs();
			}

			System.out.println("�û����ļ��ܱ�����");

			/**
			 * 1. ����json ��List<NoteJson> 2. ���ɱ����ļ�������·����ŵ�NoteJson�� 3.
			 * ��List<NoteJson>�洢�����ݿ�
			 */

			Gson gson = new Gson();

			types = gson.fromJson(fileTypes, new TypeToken<List<String>>() {
			}.getType());
			names = gson.fromJson(fileNames, new TypeToken<List<String>>() {
			}.getType());

			List<NoteJson> list = gson.fromJson(note,
					new TypeToken<List<NoteJson>>() {
					}.getType());

			System.out.println("list���ϣ�" + list);

			// ĳ���ʼǵ��ļ����ϵĳ�ʼ����
			int index = 0;
			// ĳ���ʼǵ��ļ����ϵĽ�β����
			int latter = 0;

			// ��ѯ�û�id
			int id = userService.findIdByTel(user);

			System.out.println("list���ϴ�С:" + list.size());

			// ÿ�ݱʼ�
			for (int i = 0; i < list.size(); i++) {
				// ��ȡ�ʼ���������������
				int count = list.get(i).attachmentCount;
				// �ʼ�
				NoteJson noteJson = list.get(i);
				// user id ��ֵ��notejson
				noteJson.out_key_user_id = id;
				// �и���
				if (count > 0) {
					// �������ڲ�����ļ���ַ
					StringBuilder server_uri = new StringBuilder();
					// �ͻ��˴���·��
					// StringBuilder client_uri = new StringBuilder();

					// �ͻ��˴洢����
					StringBuilder client_type = new StringBuilder();

					// �ͻ��˴洢��
					StringBuilder client_name = new StringBuilder();

					// ��������
					String[] fNames = noteJson.attachment_name_str.split(",");
					// ���Ľ�β����
					latter = index + count;
					for (int k = index; k < latter; k++) {
						String filePath = save_path + "/" + fNames[k - index];
						// �����ļ�
						com.jspsmart.upload.File file = listFile.getFile(k);

						server_uri.append( Globle.APPURL +"/files/" + user + "/"
								+ fNames[k - index] + ",");
						System.out.println("������:" + "/files/" + user + "/"
								+ fNames[k - index]);

						file.saveAs("/files/" + user + "/" + fNames[k - index],
								SmartUpload.SAVE_VIRTUAL);
					}

					// �����������ַ
					noteJson.server_attachment_uri = server_uri.deleteCharAt(
							server_uri.length() - 1).toString();
					// ������ʼ����
					index = latter;
				}
			}

			List<Ids> ListIds = new ArrayList<Ids>();
			// д�����ݿ�
			for (NoteJson nj : list) {
				System.out.println("nj:" + nj.toString());
				// �޸�updated��ֵ
				nj.updated = 0;
				noteService.addOrUpdateUser(nj);
				/**
				 * ��ȡ�˼��������еıʼǵ�server_id, client_id д�ظ��ͻ���
				 */
				Ids ids = noteService.queryIds(nj);
				ListIds.add(ids);
			}
			String idsJson = gson.toJson(ListIds, new TypeToken<List<Ids>>() {
			}.getType());
			System.out.println("idsJson:" + idsJson);
			// ����json
			response.getWriter().write(idsJson);

		} catch (Exception e) {
			response.getWriter().print(e);
		}
	}

}
