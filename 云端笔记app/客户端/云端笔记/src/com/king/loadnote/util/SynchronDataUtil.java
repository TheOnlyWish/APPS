package com.king.loadnote.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.loadnote.dao.NoteBookDao;
import com.king.loadnote.dao.NoteDao;
import com.king.loadnote.domain.Ids;
import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.NoteBook;
import com.king.loadnote.domain.NoteJson;
import com.king.loadnote.domain.UploadFile;
import com.king.loadnote.globle.Globle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * ͬ�����ݵ��ƶ�
 * 
 * @author Administrator
 * 
 */
public class SynchronDataUtil {

	private HttpUtils utils;
	private static SynchronDataUtil util;
	private static Activity mActivity;

	private SynchronDataUtil(Activity mActivity) {
		this.mActivity = mActivity;
	}

	/**
	 * Ψһ���ⴴ��
	 * 
	 * @param ctx
	 * @return
	 */
	public static SynchronDataUtil getInstant(Activity mActivity) {
		if (util == null) {
			util = new SynchronDataUtil(mActivity);
		}
		return util;
	}

	public void updateData() {
		new Thread() {
			public void run() {
				// ��Ҫ�ϴ��ĸ��������и���
				UploadFile uf = new UploadFile();
				// ��Ҫ�ϴ��ģ�ת����json��note
				List<NoteJson> noteJsons = new ArrayList<NoteJson>();
				NoteDao dao = new NoteDao(mActivity);
				// ���µ�note����
				final List<Note> listNote = dao.getUpdatedNotes();
				// �ʼǱ�dao
				NoteBookDao bookDao = new NoteBookDao(mActivity);
				// ���бʼǱ�����
				List<NoteBook> noteBooks = bookDao.getAllNoteBooks();
				if (listNote.size() > 0) {
					System.out.println("listNoteSize:" + listNote.size());
					// �ͻ������
					// Ϊnote.attachment���ϸ�ֵ
					for (int i = 0; i < listNote.size(); i++) {
						Note n = listNote.get(i);
						// ����Ҫת����json�����ݴ�ŵ�noteJsons
						NoteJson jn = initNoteJson(n, uf);
						// ������и�����uf��
						noteJsons.add(jn);
					}
					utils = new HttpUtils();
					// �ϴ�������
					RequestParams params = new RequestParams();

					// ��ʼ���ı�����
					Gson gson = new Gson();
					// �ʼǱ��ı�����json����
					String noteBooksValue = gson.toJson(noteBooks,
							new TypeToken<List<NoteBook>>() {
							}.getType());

					// �ʼ��ı�����json����
					String noteValue = gson.toJson(noteJsons,
							new TypeToken<List<NoteJson>>() {
							}.getType());
					String fileNames = "";
					String fileTypes = "";
					if (uf.fileNames.size() > 0) {
						// ��listת����String
						fileNames = gson.toJson(uf.fileNames);
						fileTypes = gson.toJson(uf.fileTypes);
					}

					System.out.println("upnotes" + noteValue);

					System.out.println("json:filenames��" + fileNames);
					System.out.println("json:fileTypes��" + fileTypes);

					// ����û���
					SharedPUtil sp = SharedPUtil.getInstant(mActivity);
					params.addBodyParameter("user", sp.getString("telNumber"));
					String body = null;
					try {
						body = URLEncoder.encode(noteValue, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					params.addBodyParameter("upnotes", body);
					params.addBodyParameter("books", noteBooksValue);
					System.out.println("upnotes" + body);

					String url = Globle.UPLOAD_URL;

					if (TextUtils.isEmpty(fileNames.trim())) {
						// ͬ���ľ�Ϊ�ı�
						url = Globle.UPLOAD_TEXT_URL;

					} else {
						// ͬ�������ı����ļ�
						params.addBodyParameter("names", fileNames);
						params.addBodyParameter("filetypes", fileTypes);

						for (int i = 0; i < uf.files.size(); i++) {
							params.addBodyParameter("file" + i, uf.files.get(i));
						}
					}

					System.out.println("json:uf.files.size():"
							+ uf.files.size());

					sendPost(params, url, listNote);
				} else {
					// û�и��µļ���
					setOnUpdateListener.onNoteDataToUpdate();
				}
			}

		}.start();
	}

	private void sendPost(RequestParams params, String url,
			final List<Note> listNote) {
		// send
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException e, String errorText) {
				if(setOnUpdateListener != null){
					setOnUpdateListener.onFailure(e, errorText);
				}
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if(setOnUpdateListener != null){
					setOnUpdateListener.onLoading(total, current, isUploading);
				}
			}

			@Override
			public void onStart() {
				if(setOnUpdateListener != null){
					setOnUpdateListener.onStart();
				}
			}

			@Override
			public void onCancelled() {
				if(setOnUpdateListener != null){
					setOnUpdateListener.onCancelled();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> result) {
				
				setOnUpdateListener.onSuccess(result, listNote);
			}
		});
	};

	
	private SetOnUpdateListener setOnUpdateListener;
	public void setOnUpdateListener(SetOnUpdateListener setOnUpdateListener) {
		this.setOnUpdateListener = setOnUpdateListener;
	}
	/**
	 * �ͻ��˵��õĽӿ�
	 * @author Administrator
	 *
	 */
	public interface SetOnUpdateListener {
		public void onFailure(HttpException e, String errorText);

		public void onLoading(long total, long current, boolean isUploading);

		public void onStart();

		public void onCancelled();

		public void onSuccess(ResponseInfo<String> result, List<Note> listNote);
		
		public void onNoteDataToUpdate();
	}

	/**
	 * ��ʼ��NoteJson
	 * 
	 * @return
	 */
	private NoteJson initNoteJson(Note note, UploadFile uf) {
		NoteJson json = new NoteJson();
		json._id = note._id;
		json.body = note.body;
		json.is_usable = note.is_usable;
		json.isPaint = note.isPaint;
		json.outKeyNoteBook = note.outKeyNoteBook;
		json.title = note.title;
		json.updated = note.updated;
		json.writeTime = note.writeTime;
		int size = note.attachment_uri.size();
		System.out.println("note.attachment_uri.size():"
				+ note.attachment_uri.size());
		if (size > 0) {
			json.attachmentCount = size;
			List<String> uris = note.attachment_uri;
			List<String> types = note.attachment_type;
			List<String> names = note.attachment_name;

			StringBuilder sb_uri = new StringBuilder();
			StringBuilder sb_type = new StringBuilder();
			StringBuilder sb_name = new StringBuilder();
			for (int i = 0; i < uris.size(); i++) {
				uf.files.add(new File(uris.get(i)));
				try {
					uf.fileNames.add(URLEncoder.encode(names.get(i), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				uf.fileTypes.add(types.get(i));

				if (i == 0) {
					sb_uri.append(uris.get(i));
					sb_type.append(types.get(i));
					sb_name.append(names.get(i));
				} else {
					sb_uri.append("," + uris.get(i));
					sb_type.append("," + types.get(i));
					sb_name.append("," + names.get(i));
				}
			}
			json.attachment_name_str = sb_name.toString();
			json.attachment_uri_str = sb_uri.toString();
			json.attachment_type_str = sb_type.toString();
		}
		return json;
	}

}
