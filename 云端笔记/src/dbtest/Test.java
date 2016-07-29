package dbtest;

import java.util.Date;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.king.loadnote.dao.NoteBookDao;
import com.king.loadnote.dao.NoteDao;
import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.NoteBook;
import com.king.loadnote.util.DateTextUtil;

public class Test extends AndroidTestCase {

	public void add() {
		NoteBookDao dao = new NoteBookDao(getContext());

		for (int i = 1; i < 20; i++) {
			NoteBook noteBook = new NoteBook();
			noteBook.bookName = "第" + i + "本笔记本";
			noteBook.is_usable = 1;
			dao.addToNoteBook(noteBook);
		}

		// 查询
		List<NoteBook> books = dao.getnoteBooks(0, 20);
		int j = 0;
		for (NoteBook book : books) {
			++j;
			System.out.println("test:****" + book.bookName + ":"
					+ book.is_usable + ":" + book.is_usable + ":" + j);
		}

	}

	public void delete() {
		// 置为无用
		NoteBookDao dao = new NoteBookDao(getContext());

		boolean f = dao.deleteFromNoteBookByIdVirtual("1");

		List<NoteBook> books = dao.getnoteBooks(0, 19);
		int j = 0;
		for (NoteBook book : books) {
			++j;
			System.out.println("test:****" + book.bookName + ":"
					+ book.is_usable + ":" + book.is_usable + ":" + j);
		}

		boolean ff = dao.deleteFromNoteBookByIdRealy(2 + "");

		books = dao.getnoteBooks(0, 19);
		j = 0;
		for (NoteBook book : books) {
			++j;
			System.out.println("test:****" + book.bookName + ":"
					+ book.is_usable + ":" + book.is_usable + ":" + j);
		}

	}

	public void note() {

		NoteDao dao = new NoteDao(getContext());

		// add
		// for (int i = 0; i < 20; i++) {
		// Note note = new Note();
		// note.title = "第" + i + 1 + "笔记";
		// note.outKeyNoteBook = i % 3;
		// note.body = "哈哈哈哈哈哈哈哈" + i;
		// note.is_usable = 1;
		// note.updated = 0;
		// note.writeTime = new Date().getTime() + 1000 * i;
		// dao.addToNotes(note);
		// }
		//
		// // query all
		List<Note> list = dao
				.getAllNotes(String.valueOf(1), String.valueOf(10));
		int j = 0;
		// for (Note n : list) {
		//
		// System.out.println("test:****" + n._id + ":" + n.body + ":"
		// + n.title + ":" + j);
		// j++;
		//
		// }

		// // delete
		// dao.deleteFromNotesVirtual("1");
		// Note note = dao.getNote("1");
		// dao.deleteFromNotesRealy("1");
		System.out.println("****************");
		for (Note n : list) {
			System.out.println("test:****" + n._id + ":*" + n.attachment_name
					+ "*:" + j);
			j++;
		}

	}

	public void query() {
		NoteDao dao = new NoteDao(getContext());

		Note note = dao.getNote(String.valueOf(5));

		System.out.println("test:" + note.attachment_name);

		List<Note> list = dao
				.getAllNotes(String.valueOf(1), String.valueOf(10));
		System.out.println("****************");
		for (Note n : list) {
			System.out.println("test:****" + n._id + ":*" + n.attachment_name
					+ "*:");
		}
	}

	public void testUpdate() {
		NoteDao dao = new NoteDao(getContext());
		List<Note> list = dao
				.getAllNotes(String.valueOf(1), String.valueOf(60));
		for (Note n : list) {
			Log.i("test",
					"test:****" + n._id + ":*"
							+ DateTextUtil.getDateString(n.writeTime) + "*:");
		}
		// ///////////////////////////

		for (int i = 3; i < 6; i++) {
			dao.updateNoteWriteTime(
					(long) (new Date().getTime() - 1000 * 60 * 60 * 24), i + "");
		}
		list = null;
		list = dao.getAllNotes(String.valueOf(1), String.valueOf(60));
		for (Note n : list) {
			System.out.println("test:****" + n._id + ":" + n._id + ":*"
					+ DateTextUtil.getDateString(n.writeTime) + "*:");
		}

	}

	public void addNotes() {
		NoteDao dao = new NoteDao(getContext());
//		for (int i = 10; i < 20; i++) {
//			Note note = new Note();
//			note.title = "第" + i + "笔记";
//			note.outKeyNoteBook = i % 2;
//			note.body = "哈哈哈哈哈哈哈哈" + i;
//			note.is_usable = 1;
//			note.updated = 0;
//			note.writeTime = new Date().getTime() - 1000 * 60 * 60 * 24 * i;
//			System.out.println("test:"+DateTextUtil.getDateString(note.writeTime));
//			dao.addToNotes(note);
//		}

		List<Note> list = dao
				.getAllNotes(String.valueOf(0), String.valueOf(20));
		for (Note n : list) {
			System.out.println("test:" + n._id + ":" + n.title + ":" + n.body
					+ ":" + DateTextUtil.getDateString(n.writeTime));
		}

	}
	
	public void update(){
		
		
		NoteDao dao = new NoteDao(getContext());
		
		dao.updateNoteWriteTime(new Date().getTime(), 3+"");
		
		Note note = dao.getNote("3");
		System.out.println("test:"+DateTextUtil.getDateString(note.writeTime));
	}
	
	
	public void addNoteBooks(){
		NoteBookDao dao = new NoteBookDao(getContext());
		
		NoteBook book = new NoteBook();
		book.bookName = "第一本笔记本";
		book.is_usable = 1;
		dao.addToNoteBook(book);
		NoteBook book1 = new NoteBook();
		book1.bookName = "第二本笔记本";
		book1.is_usable = 1;
		dao.addToNoteBook(book1);
		
		
		
	}

}
