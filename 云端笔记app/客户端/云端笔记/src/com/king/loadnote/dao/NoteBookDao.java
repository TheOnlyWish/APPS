package com.king.loadnote.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.loadnote.db.NoteDataBaseHelper;
import com.king.loadnote.domain.NoteBook;

/**
 * �ʼǱ������ݿ���ʲ�
 * 
 * @author Administrator
 */
public class NoteBookDao {

	private NoteDataBaseHelper helper;

	public NoteBookDao(Context context) {
		helper = new NoteDataBaseHelper(context);
	}

	/**
	 * ��ӱʼǱ�
	 * 
	 * @param �Ƿ���ӳɹ�
	 */
	public boolean addToNoteBook(NoteBook noteBook) {
		boolean flag = false;
		System.out.println("value:db:" + noteBook.bookName);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("book_name", noteBook.bookName);
		// Ĭ�������õ�
		values.put("is_usable", noteBook.is_usable);
		long columns = db.insert("notebooks", null, values);
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteFromNoteBookByIdRealy(String id) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		int columns = db.delete("notebooks", "_id=?", new String[]{id});
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}
	
	/**
	 * ����ɾ��ָ��id�ıʼǼ�¼
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteFromNoteBookByIdVirtual(String id) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("is_usable", 0);
		int columns = db.update("notebooks", values, "_id == ?", new String[]{id});
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * �޸ıʼǱ���
	 * 
	 * @param id �ʼǱ�id
	 * @param bookName �ʼǱ�����
	 * @return
	 */
	public boolean updateNoteBook(String id, String bookName) {
		boolean flag = false;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("book_name", bookName);
		int columns = db.update("notebooks", values, "_id=?",
				new String[] { id });
		if (columns > 0) {
			flag = true;
		}
		db.close();
		return flag;
	}

	/**
	 * ��ȡ�ʼǱ�����Ϣ
	 * 
	 * @param id
	 * @return
	 */
	public NoteBook getNoteBook(String id) {
		NoteBook book = null;
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = new String[] { "_id", "book_name" ,"is_usable"};
		Cursor cursor = db.query("notebooks", columns, "_id=?",
				new String[] { id }, null, null, null);
		if (cursor.moveToNext()) {
			book = new NoteBook();
			book._id = cursor.getInt(cursor.getColumnIndex(columns[0]));
			book.bookName = cursor.getString(cursor.getColumnIndex(columns[1]));
			book.is_usable = cursor.getInt(cursor.getColumnIndex(columns[2]));
			book = new NoteBook();
		}
		db.close();
		return book;
	}

	/**
	 * ��ҳ��ѯ�ʼǱ�
	 * 
	 * @param first
	 * @param count
	 * @return
	 */
	public List<NoteBook> getnoteBooks(int first, int count) {
		List<NoteBook> list = new ArrayList<NoteBook>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id, book_name, is_usable from notebooks limit ? offset ?",
				new String[] { String.valueOf(count), String.valueOf(first) });
		while(cursor.moveToNext()){
			NoteBook book = new NoteBook();
			book._id = cursor.getInt(0);
			book.bookName = cursor.getString(1);
			book.is_usable = cursor.getInt(2);
			list.add(book);
		}
		db.close();
		return list;
	}
	
	/**
	 * ��ȡ���еıʼǱ�
	 * @param first
	 * @param count
	 * @return
	 */
	public List<NoteBook> getAllNoteBooks() {
		List<NoteBook> list = new ArrayList<NoteBook>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id, book_name, is_usable from notebooks",
				null);
		while(cursor.moveToNext()){
			NoteBook book = new NoteBook();
			book._id = cursor.getInt(0);
			book.bookName = cursor.getString(1);
			book.is_usable = cursor.getInt(2);
			list.add(book);
		}
		db.close();
		return list;
	}

	/**
	 * ��ȡ�ʼǱ�������
	 * 
	 * @return
	 */
	public int getCount() {
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id from notebooks", null);
		if(cursor.moveToNext()){
			count = cursor.getInt(0);
		}
		db.close();
		return count;
	}
	
	/**
	 * ��ȡ���е����һ�м�¼����Ϊ���򷵻�null
	 * @return
	 */
	public NoteBook getLastNoteBook(){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id, book_name, is_usable from notebooks", null);
		if(cursor.moveToNext()){
			cursor.moveToLast();
			NoteBook book = new NoteBook();
			book._id = cursor.getInt(0);
			book.bookName = cursor.getString(1);
			book.is_usable = cursor.getInt(2);
			return book;
		}
		return null;
		
	}

	/**
	 * ���ص�һ��noteBook��id
	 * @return
	 */
	public int getFirstBookId() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id from notebooks", null);
		if(cursor.moveToNext()){
			return cursor.getInt(0);
		}
		return -1;
	}
	

}
