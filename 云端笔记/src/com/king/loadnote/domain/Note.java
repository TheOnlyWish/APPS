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
public class Note implements Comparable<Note>, Parcelable {
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
	// �ʼǵĸ��� *******
	public ArrayList<File> attachements = new ArrayList<File>();
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
	// �ͻ��˵ļ�¼id
	public int server_id;
	// ����˴洢��uri
	public String server_attachment_url;
	
	public Note() {
	}

	public Note(int _id, int outKeyNoteBook, String title, String body,
			long writeTime, ArrayList<String> attachements,
			ArrayList<String> attachment_uri, String attachment_uri_str,
			ArrayList<String> attachment_type, String attachment_type_str,
			ArrayList<String> attachment_name, String attachment_name_str,
			int updated, int is_usable, int isPaint) {
		super();
		this._id = _id;
		this.outKeyNoteBook = outKeyNoteBook;
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
	}

	public ArrayList<File> getAttachements() {
		return attachements;
	}

	public void setAttachements(ArrayList<File> attachements) {
		this.attachements = attachements;
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

	// �ȽϷ���
	@Override
	public int compareTo(Note another) {
		return (int) (another.writeTime - this.writeTime);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	protected Note(Parcel source) {
		_id = source.readInt();
		outKeyNoteBook = source.readInt();
		title = source.readString();
		body = source.readString();
		writeTime = source.readLong();
		updated = source.readInt();
		is_usable = source.readInt();
		attachment_uri_str = source.readString();
		attachment_type_str = source.readString();
		attachment_name_str = source.readString();
		isPaint = source.readInt();

	}

	// 2��ʵ��Parcelable�ӿڵ�public void writeToParcel(Parcel dest, int flags)����
	// ͨ��������д
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// ������д��Parcel
		dest.writeInt(_id);
		dest.writeInt(outKeyNoteBook);
		dest.writeString(title);
		dest.writeString(body);
		dest.writeLong(writeTime);
		dest.writeInt(updated);
		dest.writeInt(is_usable);
		attachment_uri_str = jointList(attachment_uri);
		attachment_type_str = jointList(attachment_type);
		attachment_name_str = jointList(attachment_name);
		dest.writeString(attachment_uri_str);
		dest.writeString(attachment_type_str);
		dest.writeString(attachment_name_str);
		dest.writeInt(isPaint);

	}

	// 3���Զ��������б��뺬��һ������ΪCREATOR�ľ�̬��Ա���ó�Ա����Ҫ��ʵ��Parcelable.Creator�ӿڼ��䷽��
	public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
		@Override
		public Note createFromParcel(Parcel source) {
			// ��Parcel�ж�ȡ����
			// �˴�read˳������write˳��
			return new Note(source);
		}

		@Override
		public Note[] newArray(int size) {
			return new Note[size];
		}

	};

	@Override
	public String toString() {
		return "Note [_id=" + _id + ", outKeyNoteBook=" + outKeyNoteBook
				+ ", title=" + title + ", body=" + body + ", writeTime="
				+ writeTime + ", attachements=" + attachements
				+ ", attachment_uri=" + attachment_uri
				+ ", attachment_uri_str=" + attachment_uri_str
				+ ", attachment_type=" + attachment_type
				+ ", attachment_type_str=" + attachment_type_str
				+ ", attachment_name=" + attachment_name
				+ ", attachment_name_str=" + attachment_name_str + ", updated="
				+ updated + ", is_usable=" + is_usable + ", isPaint=" + isPaint
				+ "]";
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

}
