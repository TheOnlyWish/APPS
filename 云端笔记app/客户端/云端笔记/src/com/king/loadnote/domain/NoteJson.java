package com.king.loadnote.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * �ʼ�bean
 * 
 * @author Administrator
 */
public class NoteJson {
	// �ʼ�id
	public int _id;
	// Ĭ�ϵ����û������ıʼǱ����е����ϲ�ıʼǱ�id
	public int outKeyNoteBook;
	// �ʼǱ���
	public String title;
	// �ʼ�����
	public String body;
	// �ʼ�д���¼�
	public long writeTime;
	// �ʼǵĸ���   *******
	// �ʼǵĸ�����uri·��
	public String attachment_uri_str;
	// �ʼǵĸ���������.jpg,.png...
	public String attachment_type_str;
	// �ʼǵĸ�������
	public String attachment_name_str;
	// �ʼ��Ƿ��޸Ĺ��������һ������֮ǰ
	public int updated;
	// �ʼ��Ƿ����Ĭ�ϵ���1
	public int is_usable = 1;
	// Ĭ�ϱ�ʾ ���Զ���滭
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
