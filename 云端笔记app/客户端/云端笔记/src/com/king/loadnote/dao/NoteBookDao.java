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
 * 笔记本的数据库访问层
 * 
 * @author Administrator
 */
public class NoteBookDao {

	private NoteDataBaseHelper helper;

	public NoteBookDao(Context context) {
		helper = new NoteDataBaseHelper(context);
	}

	/**
	 * 添加笔记本
	 * 
	 * @param 是否添加成功
	 */
	public boolean addToNoteBook(NoteBook noteBook) {
		boolean flag = false;
		System.out.println("value:db:" + noteBook.bookName);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("book_name", noteBook.bookName);
		// 默认是有用的
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
	 * 虚拟删除指定id的笔记记录
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
	 * 修改笔记本名
	 * 
	 * @param id 笔记本id
	 * @param bookName 笔记本名称
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
	 * 获取笔记本的信息
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
	 * 分页查询笔记本
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
	 * 获取所有的笔记本
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
	 * 获取笔记本的数量
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
	 * 获取表中的最后一行记录，若为空则返回null
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
	 * 返回第一个noteBook的id
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
