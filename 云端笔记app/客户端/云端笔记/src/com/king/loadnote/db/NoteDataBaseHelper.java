package com.king.loadnote.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDataBaseHelper extends SQLiteOpenHelper {

	public NoteDataBaseHelper(Context context) {
		super(context, "notedb", null, 6, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建笔记本表
		db.execSQL("create table notebooks(_id integer primary key autoincrement, book_name varchar(100) not null, is_usable integer)");
		// 创建笔记表
		db.execSQL("create table notes(_id integer primary key autoincrement, out_key_notebook integer not null, title varchar(20) not null, write_time integer, body text, attachment_uri text, attachment_type varchar(10), attachment_name varchar(64), updated integer, is_usable integer, isPaint integer, server_note_id int(40))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	

}
