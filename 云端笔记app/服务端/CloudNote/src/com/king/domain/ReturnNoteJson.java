package com.king.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * �ʼ�bean
 * 
 * @author Administrator
 */
public class ReturnNoteJson {
	// �ʼ�id
	public int _id;
	// Ĭ�ϵ����û������ıʼǱ����е����ϲ�ıʼǱ�id
	public int outKeyNoteBook;
	// �ʼǱ�����
	public String bookName;
	// user id
	public int out_key_user_id;
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
	// �������洢������·��
	public String server_attachment_uri;
	@Override
	public String toString() {
		return "NoteJson [_id=" + _id + ", outKeyNoteBook=" + outKeyNoteBook
				+ ", title=" + title + ", body=" + body + ", writeTime="
				+ writeTime + ", attachment_uri_str=" + attachment_uri_str
				+ ", attachment_type_str=" + attachment_type_str
				+ ", attachment_name_str=" + attachment_name_str + ", updated="
				+ updated + ", is_usable=" + is_usable + ", isPaint=" + isPaint
				+ ", attachmentCount=" + attachmentCount
				+ ", server_attachment_uri=" + server_attachment_uri + "]";
	}
}
