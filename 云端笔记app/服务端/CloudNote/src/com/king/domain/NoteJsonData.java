package com.king.domain;

import java.util.List;

public class NoteJsonData {

	public List<NoteJ> notes;
	
	
	class NoteJ{
		
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
		
		
	}
	
	
}
