package com.king.domain;

import java.util.List;

public class NoteJsonData {

	public List<NoteJ> notes;
	
	
	class NoteJ{
		
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
		
		
	}
	
	
}
