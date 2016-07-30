package com.king.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.king.domain.Ids;
import com.king.domain.NoteBook;
import com.king.utils.JdbcUtils;

/**
 * 笔记本的dao层
 * 
 * @author Administrator
 */
public class NoteBookDao {

	/**
	 * 添加或修改笔记本
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
			 * 查询是否存在该笔记本 存在就修改 不存在就添加
			 */
			sql = "select _id from notebooks where client_id=? and out_key_user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book._id); // 服务端id
			pstmt.setInt(2, book.out_key_user_id); // 服务端id
			rs = pstmt.executeQuery();
			int _id = -1;
			if (rs.next()) {
				_id = rs.getInt("_id");
			}
			System.out.println("__________________id"+_id);

			pstmt.clearParameters();
			if (_id != -1) {
				// 修改
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
				// 添加
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
