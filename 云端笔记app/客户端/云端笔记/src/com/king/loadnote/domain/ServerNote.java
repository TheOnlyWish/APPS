package com.king.loadnote.domain;

import java.util.ArrayList;
import java.util.List;

public class ServerNote {

	// �ʼ�id
	public int _id;
	// Ĭ�ϵ����û������ıʼǱ����е����ϲ�ıʼǱ�id
	public int outKeyNoteBook;
	// ��Ӧ�û�id
	public int out_key_user_id;
	// �ͻ��˵ļ�¼id
	public int client_id;
	// �ʼǱ���
	public String title;
	// �ʼ�����
	public String body;
	// �ʼ�д���¼�
	public long writeTime;
	// �ʼǵĸ�����uri·��
	public ArrayList<String> attachment_uri = new ArrayList<String>();
	public String attachment_uri_str;
	// �ʼǵĸ���������.jpg,.png...
	public ArrayList<String> attachment_type = new ArrayList<String>();
	public String attachment_type_str;
	// �ʼǵĸ�������
	public ArrayList<String> attachment_name = new ArrayList<String>();
	public String attachment_name_str;
	// �ʼ��Ƿ��޸Ĺ��������һ������֮ǰ
	public int updated;
	// �ʼ��Ƿ����Ĭ�ϵ���1
	public int is_usable = 1;
	// Ĭ�ϱ�ʾ ���Զ���滭
	public int isPaint = 0;
	// ����˴洢��uri
	public String server_attachment_url;

	public ServerNote() {
	}

	public ServerNote(int _id, int outKeyNoteBook, int out_key_user_id,
			int client_id, String title, String body, long writeTime,
			ArrayList<String> attachment_uri, String attachment_uri_str,
			ArrayList<String> attachment_type, String attachment_type_str,
			ArrayList<String> attachment_name, String attachment_name_str,
			int updated, int is_usable, int isPaint,
			String server_attachment_url) {
		super();
		this._id = _id;
		this.outKeyNoteBook = outKeyNoteBook;
		this.out_key_user_id = out_key_user_id;
		this.client_id = client_id;
		this.title = title;
		this.body = body;
		this.writeTime = writeTime;
		this.attachment_uri = attachment_uri;
		this.attachment_uri_str = attachment_uri_str;
		this.attachment_type = attachment_type;
		this.attachment_type_str = attachment_type_str;
		this.attachment_name = attachment_name;
		this.attachment_name_str = attachment_name_str;
		this.updated = updated;
		this.is_usable = is_usable;
		this.isPaint = isPaint;
		this.server_attachment_url = server_attachment_url;
	}

	public String getServer_attachment_url() {
		return server_attachment_url;
	}

	public void setServer_attachment_url(String server_attachment_url) {
		this.server_attachment_url = server_attachment_url;
	}

	public int getOut_key_user_id() {
		return out_key_user_id;
	}

	public void setOut_key_user_id(int out_key_user_id) {
		this.out_key_user_id = out_key_user_id;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
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

	public ArrayList<String> getAttachment_uri() {
		return attachment_uri;
	}

	public void setAttachment_uri(ArrayList<String> attachment_uri) {
		this.attachment_uri = attachment_uri;
	}

	public ArrayList<String> getAttachment_type() {
		return attachment_type;
	}

	public void setAttachment_type(ArrayList<String> attachment_type) {
		this.attachment_type = attachment_type;
	}

	public ArrayList<String> getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(ArrayList<String> attachment_name) {
		this.attachment_name = attachment_name;
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

	@Override
	public String toString() {
		return "Note [_id=" + _id + ", outKeyNoteBook=" + outKeyNoteBook
				+ ", out_key_user_id=" + out_key_user_id + ", client_id="
				+ client_id + ", title=" + title + ", body=" + body
				+ ", writeTime=" + writeTime + ", attachment_uri="
				+ attachment_uri + ", attachment_uri_str=" + attachment_uri_str
				+ ", attachment_type=" + attachment_type
				+ ", attachment_type_str=" + attachment_type_str
				+ ", attachment_name=" + attachment_name
				+ ", attachment_name_str=" + attachment_name_str + ", updated="
				+ updated + ", is_usable=" + is_usable + ", isPaint=" + isPaint
				+ ", server_attachment_url=" + server_attachment_url + "]";
	}

	public String jointList(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				sb.append(list.get(i));
			} else {
				sb.append("," + list.get(i));
			}
		}
		return sb.toString();
	}

	// �Ƚ�
	public int compareTo(Note o) {
		return (int) (o.writeTime - this.writeTime);
	}

}
