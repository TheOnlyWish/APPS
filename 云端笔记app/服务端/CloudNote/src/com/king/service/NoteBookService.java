package com.king.service;

import java.util.List;

import com.king.dao.NoteBookDao;
import com.king.domain.Ids;
import com.king.domain.NoteBook;

public class NoteBookService {

	NoteBookDao dao = new NoteBookDao();
	
	/**
	 * ��ӻ��޸ıʼǱ�
	 * @param book
	 */
	public void addOrUpdateNoteBook(NoteBook book){
		dao.addOrUpdateUser(book);
	}
	
}
