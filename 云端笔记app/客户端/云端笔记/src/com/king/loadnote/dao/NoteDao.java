package com.king.loadnote.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.king.loadnote.db.NoteDataBaseHelper;
import com.king.loadnote.domain.Note;

/**
 * �ʼ���Ϣ���ݿ⽻����
 * 
 * @author Administrator
 */
public class NoteDao {

	private NoteDataBaseHelper helper;

	public NoteDao(Context context) {
		helper = new NoteDataBaseHelper(context);
	}

	/**
	 * ��ӱʼǵ��ʼǱ� ע�⣺out_key_notebook Ĭ��1����һ���ʼǱ�
	 * 
	 * @param note
	 * @return
	 */
	public boolean addToNotes(Note note) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("out_key_notebook", note.outKeyNoteBook);
		values.put("title", note.title);
		values.put("write_time", note.writeTime);
		values.put("body", note.body);
		values.put("isPaint", note.isPaint);
		String attachment_uri = "";
		String attachment_type = "";
		String attachment_name = "";
		if (note.attachment_uri.size() > 0) {
			List<String> uris = note.attachment_uri;
			List<String> types = note.attachment_type;
			List<String> names = note.attachment_name;

			for (int i = 0; i < uris.size(); i++) {
				if (i != 0) {
					attachment_uri += "," + uris.get(i);
					attachment_type += "," + types.get(i);
					attachment_name += "," + names.get(i);
				} else {
					attachment_uri = uris.get(i);
					attachment_type = types.get(i);
					attachment_name = names.get(i);
				}

			}
		} else {
			if (TextUtils.isEmpty(note.attachment_uri_str)) {

			}
			attachment_uri = note.attachment_uri_str;
			attachment_type = note.attachment_type_str;
			attachment_name = note.attachment_name_str;
		}
		values.put("attachment_uri", attachment_uri);
		values.put("attachment_type", attachment_type);
		values.put("attachment_name", attachment_name);
		values.put("updated", note.updated);
		values.put("is_usable", note.is_usable);
		values.put("server_note_id", note.server_id);
		long columns = db.insert("notes", null, values);
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * ��ʵɾ���ʼǼ�¼
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteFromNotesRealy(String id) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		int columns = db.delete("notes", "_id==?", new String[] { id });
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * ���������
	 * 
	 * @return
	 */
	public void clearRubbishBox() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from notes where is_usable == 0");
		db.close();
	}

	/**
	 * ����ɾ���ʼǼ�¼
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteFromNotesVirtual(String id) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// ����ɾ��
		values.put("is_usable", 0);
		int columns = db.update("notes", values, "_id==?", new String[] { id });
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * �޸ıʼǼ�¼
	 * 
	 * @param note
	 * @return
	 */
	public boolean updateNotes(Note note) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		if (note.outKeyNoteBook != -1) {
			values.put("out_key_notebook", note.outKeyNoteBook);
		}
		if (note.title != null) {
			values.put("title", note.title);
		}
		if (note.writeTime != -1) {
			values.put("write_time", note.writeTime);
		}
		if (note.body != null) {
			values.put("body", note.body);
		}
		if (note.is_usable != -1) {
			values.put("is_usable", note.is_usable);
		}
		String attachment_uri = "";
		String attachment_type = "";
		String attachment_name = "";
		List<String> uris = note.attachment_uri;
		List<String> types = note.attachment_type;
		List<String> names = note.attachment_name;
		for (int i = 0; i < uris.size(); i++) {
			if (i != 0) {
				attachment_uri += "," + uris.get(i);
				attachment_type += "," + types.get(i);
				attachment_name += "," + names.get(i);
			} else {
				attachment_uri = uris.get(i);
				attachment_type = types.get(i);
				attachment_name = names.get(i);
			}
		}
		values.put("attachment_uri", attachment_uri);
		values.put("attachment_type", attachment_type);
		values.put("attachment_name", attachment_name);
		values.put("updated", note.updated);
		values.put("isPaint", note.isPaint);

		int columns = db.update("notes", values, "_id==?",
				new String[] { String.valueOf(note._id) });
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * �޸ļ�¼
	 * 
	 * @param id
	 * @param is_usable
	 */
	public void updateNotes(int id, int is_usable, int outkeybook) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update notes set is_usable = ?, out_key_notebook=? where _id = ?",
				new String[] { String.valueOf(is_usable),
						String.valueOf(outkeybook), String.valueOf(id) });
		db.close();
	}

	/**
	 * �޸ļ�¼��server_id
	 * 
	 * @param id
	 * @param is_usable
	 */
	public void updateServerId(int server_note_id, int client_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update notes set server_note_id = ? where _id = ?",
				new String[] { String.valueOf(server_note_id),
						String.valueOf(client_id) });
		db.close();
	}

	/**
	 * �޸��Ƿ��Ѹ��µ�ֵ
	 * 
	 * @param id
	 * @param updated
	 */
	public void updateUpdateValue(int id, int updated) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update notes set updated = ? where _id = ?", new String[] {
				String.valueOf(updated), String.valueOf(id) });
		db.close();
	}

	/**
	 * �޸ıʼǵ�д��ʱ��
	 * 
	 * @param time
	 * @param id
	 * @return
	 */
	public boolean updateNoteWriteTime(long time, String id) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("write_time", time);
		int columns = db.update("notes", values, "_id==?", new String[] { id });
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * ��ȡ��Χ�ڵĵ�note����
	 * 
	 * @param first
	 *            �ӵڼ�������
	 * @param count
	 *            ��ҳ��ѯ������
	 * @return
	 */
	public List<Note> getAllNotes(String first, String count) {
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"select _id, out_key_notebook, title, write_time, body, attachment_uri, attachment_type, attachment_name, updated, is_usable, isPaint from notes where is_usable == 1 limit ? offset ?",
						new String[] { String.valueOf(count),
								String.valueOf(first) });

		List<Note> list = new ArrayList<Note>();
		while (cursor.moveToNext()) {
			Note note = new Note();
			note._id = cursor.getInt(0);
			note.outKeyNoteBook = cursor.getInt(1);
			note.title = cursor.getString(2);
			note.writeTime = cursor.getLong(3);
			note.body = cursor.getString(4);
			String attachment_uri = "";
			String attachment_type = "";
			String attachment_name = "";

			note.attachment_uri_str = cursor.getString(5);
			note.attachment_type_str = cursor.getString(6);
			note.attachment_name_str = cursor.getString(7);
			note.attachment_uri = new ArrayList<String>();
			note.attachment_type = new ArrayList<String>();
			note.attachment_name = new ArrayList<String>();
			if (!TextUtils.isEmpty(note.attachment_uri_str.trim())) {
				String[] uris = note.attachment_uri_str.split(",");
				String[] types = note.attachment_type_str.split(",");
				String[] names = note.attachment_name_str.split(",");

				for (int i = 0; i < uris.length; i++) {
					note.attachment_uri.add(uris[i]);
					note.attachment_type.add(types[i]);
					note.attachment_name.add(names[i]);
				}
			}
			note.updated = cursor.getInt(8);
			note.is_usable = cursor.getInt(9);
			note.isPaint = cursor.getInt(10);
			list.add(note);
		}
		db.close();
		return list;
	}

	/**
	 * ��ȡ��Χ�ڵĵ�note����
	 * 
	 * @param count
	 *            ��ҳ��ѯ������
	 * @return
	 */
	public List<Note> getReorderNotes(int count) {
		int first = getCount() - count;

		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"select _id, out_key_notebook, title, write_time, body, attachment_uri, attachment_type, attachment_name, updated, is_usable, isPaint from notes where is_usable == 1 limit ? offset ?",
						new String[] { String.valueOf(count),
								String.valueOf(first) });

		List<Note> list = new ArrayList<Note>();
		while (cursor.moveToNext()) {
			Note note = new Note();
			note._id = cursor.getInt(0);
			note.outKeyNoteBook = cursor.getInt(1);
			note.title = cursor.getString(2);
			note.writeTime = cursor.getLong(3);
			note.body = cursor.getString(4);
			String attachment_uri = "";
			String attachment_type = "";
			String attachment_name = "";

			System.out.println("ǰ:" + note.attachment_uri_str);
			note.attachment_uri_str = cursor.getString(5);
			System.out.println("��:" + note.attachment_uri_str);
			note.attachment_type_str = cursor.getString(6);
			note.attachment_name_str = cursor.getString(7);
			note.attachment_uri = new ArrayList<String>();
			note.attachment_type = new ArrayList<String>();
			note.attachment_name = new ArrayList<String>();
			// System.out.println("note.attachment_uri_str:"+note.attachment_uri_str);
			if (!TextUtils.isEmpty(note.attachment_uri_str)) {
				System.out.println();
				String[] uris = note.attachment_uri_str.split(",");
				String[] types = note.attachment_type_str.split(",");
				String[] names = note.attachment_name_str.split(",");

				for (int i = 0; i < uris.length; i++) {
					note.attachment_uri.add(uris[i]);
					note.attachment_type.add(types[i]);
					note.attachment_name.add(names[i]);
				}
			}
			note.updated = cursor.getInt(8);
			note.is_usable = cursor.getInt(9);
			note.isPaint = cursor.getInt(10);
			list.add(note);
		}
		db.close();
		return list;
	}

	/**
	 * ��ȡ���еı����ĵ�����
	 * 
	 * @return
	 */
	public List<Note> getUpdatedNotes() {

		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"select _id, out_key_notebook, title, write_time, body, attachment_uri, attachment_type, attachment_name, updated, is_usable, isPaint from notes where updated==?",
						new String[] { "1" });

		List<Note> list = new ArrayList<Note>();
		while (cursor.moveToNext()) {
			Note note = new Note();
			note._id = cursor.getInt(0);
			note.outKeyNoteBook = cursor.getInt(1);
			note.title = cursor.getString(2);
			note.writeTime = cursor.getLong(3);
			note.body = cursor.getString(4);
			String attachment_uri = "";
			String attachment_type = "";
			String attachment_name = "";

			note.attachment_uri_str = cursor.getString(5);
			note.attachment_type_str = cursor.getString(6);
			note.attachment_name_str = cursor.getString(7);
			note.attachment_uri = new ArrayList<String>();
			note.attachment_type = new ArrayList<String>();
			note.attachment_name = new ArrayList<String>();
			if (!TextUtils.isEmpty(note.attachment_uri_str)) {
				String[] uris = note.attachment_uri_str.split(",");
				String[] types = note.attachment_type_str.split(",");
				String[] names = note.attachment_name_str.split(",");

				for (int i = 0; i < uris.length; i++) {
					note.attachment_uri.add(uris[i]);
					note.attachment_type.add(types[i]);
					note.attachment_name.add(names[i]);
				}
			}
			note.updated = cursor.getInt(8);
			note.is_usable = cursor.getInt(9);
			note.isPaint = cursor.getInt(10);
			list.add(note);
		}
		db.close();
		return list;
	}

	/**
	 * ��ȡ��Χ�ڵĵ� �����õ� note����
	 * 
	 * @param count
	 *            ��ҳ��ѯ������
	 * @return
	 */
	public List<Note> getUselessNotes(int count) {
		int first = getCount() - count;

		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"select _id, out_key_notebook, title, write_time, body, attachment_uri, attachment_type, attachment_name, updated, is_usable, isPaint from notes where is_usable == 0 limit ? offset ?",
						new String[] { String.valueOf(count),
								String.valueOf(first) });

		List<Note> list = new ArrayList<Note>();
		while (cursor.moveToNext()) {
			Note note = new Note();
			note._id = cursor.getInt(0);
			note.outKeyNoteBook = cursor.getInt(1);
			note.title = cursor.getString(2);
			note.writeTime = cursor.getLong(3);
			note.body = cursor.getString(4);
			String attachment_uri = "";
			String attachment_type = "";
			String attachment_name = "";

			note.attachment_uri_str = cursor.getString(5);
			note.attachment_type_str = cursor.getString(6);
			note.attachment_name_str = cursor.getString(7);
			note.attachment_uri = new ArrayList<String>();
			note.attachment_type = new ArrayList<String>();
			note.attachment_name = new ArrayList<String>();
			if (!TextUtils.isEmpty(note.attachment_uri_str)) {
				String[] uris = note.attachment_uri_str.split(",");
				String[] types = note.attachment_type_str.split(",");
				String[] names = note.attachment_name_str.split(",");

				for (int i = 0; i < uris.length; i++) {
					note.attachment_uri.add(uris[i]);
					note.attachment_type.add(types[i]);
					note.attachment_name.add(names[i]);
				}
			}
			note.updated = cursor.getInt(8);
			note.is_usable = cursor.getInt(9);
			note.isPaint = cursor.getInt(10);
			list.add(note);
		}
		db.close();
		return list;
	}

	/**
	 * ��ѯ�ܼ�¼��
	 */
	public int getCount() {
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db.rawQuery(
				"select count(_id) from notes where is_usable == ?",
				new String[] { String.valueOf(1) });
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		db.close();
		return count;
	}

	/**
	 * ��ѯĳ����¼
	 */
	public Note getNote(String id) {
		System.out.println("test:" + id);
		Note note = null;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"select _id, out_key_notebook, title, write_time, body, attachment_uri, attachment_type, attachment_name, updated, is_usable, isPaint from notes where _id==?",
						new String[] { id });

		if (cursor.moveToNext()) {
			System.out.println("test:cursor");
			note = new Note();
			note._id = cursor.getInt(0);
			note.outKeyNoteBook = cursor.getInt(1);
			note.title = cursor.getString(2);
			note.writeTime = cursor.getLong(3);
			note.body = cursor.getString(4);
			String[] uris = cursor.getString(5).split(",");
			String[] types = cursor.getString(6).split(",");
			String[] names = cursor.getString(7).split(",");
			for (int i = 0; i < uris.length; i++) {
				note.attachment_uri.add(uris[i]);
				note.attachment_type.add(types[i]);
				note.attachment_name.add(names[i]);
			}
			note.updated = cursor.getInt(8);
			note.is_usable = cursor.getInt(9);
			note.isPaint = cursor.getInt(10);
		}
		db.close();
		return note;
	}

	/**
	 * ��ѯĳ���ʼǱ��ıʼ�����
	 * 
	 * @param id
	 *            �ʼǱ���id
	 */
	public int getNotesCount(int id) {
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor cursor = db
				.rawQuery(
						"select count(_id) from notes where is_usable == 1 and out_key_notebook = ? and is_usable=1",
						new String[] { String.valueOf(id) });
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		db.close();
		return count;
	}

	/**
	 * ��ѯserver_note_id��Ϊnull��id����
	 * 
	 * @param id
	 *            �ʼǱ���id
	 */
	public List<Integer> getServerNoteId() {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<Integer> list = new ArrayList<Integer>();
		Cursor cursor = db
				.rawQuery(
						"select server_note_id from notes where (server_note_id != null or server_note_id<>'')",
						null);
		while (cursor.moveToNext()) {
			list.add(cursor.getInt(0));
		}
		db.close();
		return list;
	}

	/**
	 * ͨ���ʼǱ������ѯ���ʼǵ�id����
	 * 
	 * @param oldId
	 * @param newId
	 */
	public List<Integer> getNotesByOutKeyBookId(int id) {

		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"select _id from notes where out_key_notebook=?",
				new String[] { String.valueOf(id) });
		List<Integer> ids = new ArrayList<Integer>();
		while (cursor.moveToNext()) {
			ids.add(cursor.getInt(0));
		}

		db.close();
		return ids;
	}

	/**
	 * ͨ���ʼǱ������ѯ���ʼǵļ���
	 * 
	 * @param oldId
	 * @param newId
	 */
	public List<Note> getNotesByBookId(int id) {

		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select _id, out_key_notebook, title, write_time, body, attachment_uri, attachment_type, attachment_name, updated, is_usable, isPaint from notes where out_key_notebook=? and is_usable=1",
						new String[] { String.valueOf(id) });
		List<Note> list = new ArrayList<Note>();
		while (cursor.moveToNext()) {
			Note note = new Note();
			note._id = cursor.getInt(0);
			note.outKeyNoteBook = cursor.getInt(1);
			note.title = cursor.getString(2);
			note.writeTime = cursor.getLong(3);
			note.body = cursor.getString(4);
			if(cursor.getString(5) != null){
				String[] uris = cursor.getString(5).split(",");
				String[] types = cursor.getString(6).split(",");
				String[] names = cursor.getString(7).split(",");
				for (int i = 0; i < uris.length; i++) {
					note.attachment_uri.add(uris[i]);
					note.attachment_type.add(types[i]);
					note.attachment_name.add(names[i]);
				}
			}
			note.updated = cursor.getInt(8);
			note.is_usable = cursor.getInt(9);
			note.isPaint = cursor.getInt(10);
			list.add(note);
		}

		db.close();
		return list;
	}

	/**
	 * ͨ���ʼ�id���ҵ��ʼǲ��ı�ʼǱ���� ���ú�״̬Ϊ�ѱ༭
	 * 
	 * @param noteId
	 * @param outKeyBookId
	 */
	public void updateNoteOutkeyBookIdById(int noteId, int outKeyBookId) {

		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update notes set out_key_notebook=?, updated=1 where _id=?",
				new String[] { String.valueOf(outKeyBookId),
						String.valueOf(noteId) });
		db.close();

	}

}
