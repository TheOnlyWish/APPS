package com.king.service;

import java.util.List;

import com.king.dao.NoteDao;
import com.king.domain.Ids;
import com.king.domain.Note;
import com.king.domain.NoteJson;

public class NoteService {
	NoteDao dao = new NoteDao();
	
	public void addOrUpdateUser(NoteJson note){
		dao.addOrUpdateUser(note);
	}

	public Ids queryIds(NoteJson nj) {
		return dao.queryIds(nj);
	}

	public List<Ids> getServerIdsByTel(int userId) {
		return dao.getServerIdsByTel(userId);
	}

	public Note getNote(int server_id) {
		return dao.getNote(server_id);
	}
	
}
