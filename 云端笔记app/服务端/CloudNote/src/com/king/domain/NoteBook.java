package com.king.domain;

/**
 * 笔记本bean
 * 
 * @author Administrator
 */
public class NoteBook {
	// 客户端笔记本id
	public int _id;
	// 服务器端笔记本id
	public int server_id;
	// 笔记本名称
	public String bookName;
	// 对应用户id
	public int out_key_user_id;

	public int is_usable;

	public int getIs_usable() {
		return is_usable;
	}

	public void setIs_usable(int is_usable) {
		this.is_usable = is_usable;
	}

	public NoteBook() {
	}

	public int getServer_id() {
		return server_id;
	}

	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

	public NoteBook(int _id, int server_id, String bookName,
			int out_key_user_id, int is_usable) {
		super();
		this._id = _id;
		this.server_id = server_id;
		this.bookName = bookName;
		this.out_key_user_id = out_key_user_id;
		this.is_usable = is_usable;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public int getOut_key_user_id() {
		return out_key_user_id;
	}

	public void setOut_key_user_id(int out_key_user_id) {
		this.out_key_user_id = out_key_user_id;
	}

	@Override
	public String toString() {
		return "NoteBook [_id=" + _id + ", server_id=" + server_id
				+ ", bookName=" + bookName + ", out_key_user_id="
				+ out_key_user_id + ", is_usable=" + is_usable + "]";
	}
}
