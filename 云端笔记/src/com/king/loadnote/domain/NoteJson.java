package com.king.loadnote.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 笔记bean
 * 
 * @author Administrator
 */
public class NoteJson {
	// 笔记id
	public int _id;
	// 默认等于用户创建的笔记本表中的最上层的笔记本id
	public int outKeyNoteBook;
	// 笔记标题
	public String title;
	// 笔记内容
	public String body;
	// 笔记写入事件
	public long writeTime;
	// 笔记的附件   *******
	// 笔记的附件的uri路径
	public String attachment_uri_str;
	// 笔记的附件的类型.jpg,.png...
	public String attachment_type_str;
	// 笔记的附件名称
	public String attachment_name_str;
	// 笔记是否被修改过，在最后一个更新之前
	public int updated;
	// 笔记是否可用默认等于1
	public int is_usable = 1;
	// 默认表示 非自定义绘画
	public int isPaint = 0;
	public int attachmentCount = 0;
	

	public NoteJson() {
	}

	public NoteJson(int _id, int outKeyNoteBook, String title, String body,
			long writeTime, String attachment_uri_str,
			String attachment_type_str, String attachment_name_str,
			int updated, int is_usable, int isPaint, int attachmentCount) {
		super();
		this._id = _id;
		this.outKeyNoteBook = outKeyNoteBook;
		this.title = title;
		this.body = body;
		this.writeTime = writeTime;
		this.attachment_uri_str = attachment_uri_str;
		this.attachment_type_str = attachment_type_str;
		this.attachment_name_str = attachment_name_str;
		this.updated = updated;
		this.is_usable = is_usable;
		this.isPaint = isPaint;
		this.attachmentCount = attachmentCount;
	}




	public int getAttachmentCount() {
		return attachmentCount;
	}



	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}



	public int getIsPaint() {
		return isPaint;
	}

	public void setIsPaint(int isPaint) {
		this.isPaint = isPaint;
	}

	public String getAttachment_uri_str() {
		return attachment_uri_str;
	}

	public void setAttachment_uri_str(String attachment_uri_str) {
		this.attachment_uri_str = attachment_uri_str;
	}

	public String getAttachment_type_str() {
		return attachment_type_str;
	}

	public void setAttachment_type_str(String attachment_type_str) {
		this.attachment_type_str = attachment_type_str;
	}

	public String getAttachment_name_str() {
		return attachment_name_str;
	}

	public void setAttachment_name_str(String attachment_name_str) {
		this.attachment_name_str = attachment_name_str;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getOutKeyNoteBook() {
		return outKeyNoteBook;
	}

	public void setOutKeyNoteBook(int outKeyNoteBook) {
		this.outKeyNoteBook = outKeyNoteBook;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(long writeTime) {
		this.writeTime = writeTime;
	}


	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public int getIs_usable() {
		return is_usable;
	}

	public void setIs_usable(int is_usable) {
		this.is_usable = is_usable;
	}



}
