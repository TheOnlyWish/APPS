package com.king.loadnote.domain;

/**
 * �ʼǱ�bean
 * @author Administrator
 */
public class NoteBook {
	
	// �ʼǱ�id
	public int _id;
	public String bookName;
	public int is_usable = 1;
	
	public int noteCount;

	@Override
	public String toString() {
		return "NoteBook [_id=" + _id + ", bookName=" + bookName
				+ ", is_usable=" + is_usable + ", noteCount=" + noteCount + "]";
	}
	
	
}
