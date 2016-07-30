package com.king.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.king.domain.Ids;
import com.king.domain.NoteBook;
import com.king.utils.JdbcUtils;

/**
 * �ʼǱ���dao��
 * 
 * @author Administrator
 */
public class NoteBookDao {

	/**
	 * ��ӻ��޸ıʼǱ�
	 * 
	 * @param telNumber
	 * @param password
	 */
	public void addOrUpdateUser(NoteBook book) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = null;
			/**
			 * ��ѯ�Ƿ���ڸñʼǱ� ���ھ��޸� �����ھ����
			 */
			sql = "select _id from notebooks where client_id=? and out_key_user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book._id); // �����id
			pstmt.setInt(2, book.out_key_user_id); // �����id
			rs = pstmt.executeQuery();
			int _id = -1;
			if (rs.next()) {
				_id = rs.getInt("_id");
			}
			System.out.println("__________________id"+_id);

			pstmt.clearParameters();
			if (_id != -1) {
				// �޸�
				sql = "update notebooks set book_name=?, client_id=?, out_key_user_id=?, is_usable=? "
						+ "where client_id = ? and out_key_user_id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, book.bookName);
				pstmt.setInt(2, book._id);
				pstmt.setInt(3, book.out_key_user_id);
				pstmt.setInt(4, book.is_usable);
				pstmt.setInt(5, book._id);
				pstmt.setInt(6, book.out_key_user_id);
				pstmt.executeUpdate();

			} else {
				// ���
				sql = "insert into notebooks(book_name, client_id, out_key_user_id, is_usable) values(?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, book.bookName);
				pstmt.setInt(2, book._id);
				pstmt.setInt(3, book.out_key_user_id);
				pstmt.setInt(4, book.is_usable);
				pstmt.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}

			} catch (Exception e) {
			}
		}

	}

}
