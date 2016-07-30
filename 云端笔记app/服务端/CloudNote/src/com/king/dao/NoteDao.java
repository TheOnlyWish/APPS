package com.king.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.king.domain.Ids;
import com.king.domain.Note;
import com.king.domain.NoteJson;
import com.king.utils.JdbcUtils;

public class NoteDao {

	/**
	 * 批量添加笔记
	 * 
	 * @param telNumber
	 * @param password
	 */
	public void addOrUpdateUser(NoteJson note) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = null;
			/**
			 * 查询是否存在该笔记 存在就修改 不存在就添加
			 */
			sql = "select _id from notes where client_id=? and out_key_user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, note._id);
			pstmt.setInt(2, note.out_key_user_id);
			rs = pstmt.executeQuery();
			int _id = -1;
			if (rs.next()) {
				_id = rs.getInt("_id");
			}
			pstmt.clearParameters();
			if (_id != -1) {
				// 修改
				sql = "update notes set server_attachment_uri = ?, client_id = ?,title = ?,out_key_notebook = ?,write_time = ?,"
						+ "body = ?,attachment_uri = ?, attachment_type = ?,attachment_name = ?,updated = ?,is_usable = ?,"
						+ "isPaint = ?,out_key_user_id = ? where client_id = ? and out_key_user_id=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, note.server_attachment_uri);
				pstmt.setInt(2, note._id);
				pstmt.setString(3, note.title);
				pstmt.setInt(4, note.outKeyNoteBook);
				pstmt.setLong(5, note.writeTime);
				pstmt.setString(6, note.body);
				pstmt.setString(7, note.attachment_uri_str);
				pstmt.setString(8, note.attachment_type_str);
				pstmt.setString(9, note.attachment_name_str);
				pstmt.setInt(10, note.updated);
				pstmt.setInt(11, note.is_usable);
				pstmt.setInt(12, note.isPaint);
				pstmt.setInt(13, note.out_key_user_id);
				pstmt.setInt(14, note._id);
				pstmt.setInt(15, note.out_key_user_id);
				pstmt.executeUpdate();
				
				System.out.println("修改方法");

			} else {
				// 添加
				System.out.println("添加方法");
				sql = "insert into notes(server_attachment_uri,client_id,title,out_key_notebook,write_time,"
						+ "body,attachment_uri,attachment_type,attachment_name,updated,is_usable,"
						+ "isPaint,out_key_user_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, note.server_attachment_uri);
				pstmt.setInt(2, note._id);
				pstmt.setString(3, note.title);
				pstmt.setInt(4, note.outKeyNoteBook);
				pstmt.setLong(5, note.writeTime);
				pstmt.setString(6, note.body);
				pstmt.setString(7, note.attachment_uri_str);
				pstmt.setString(8, note.attachment_type_str);
				pstmt.setString(9, note.attachment_name_str);
				pstmt.setInt(10, note.updated);
				pstmt.setInt(11, note.is_usable);
				pstmt.setInt(12, note.isPaint);
				pstmt.setInt(13, note.out_key_user_id);
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

	/**
	 * 通过用户id来查询笔记记录数
	 * 
	 * @param userId
	 * @return
	 */
	public int getNoteCount(int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select server_attachment_uri,client_id,title,out_key_notebook,write_time,"
					+ "body,attachment_uri,attachment_type,attachment_name,updated,is_usable,"
					+ "isPaint,out_key_user_id from notes where out_key_user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	/**
	 * 通过userId获取所属笔记
	 * 
	 * @param userId
	 * @return
	 */
	public List<NoteJson> getNoteByUserId(int userId) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<NoteJson> list = new ArrayList<NoteJson>();
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select count(_id) from notes where out_key_user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {

				NoteJson note = new NoteJson();

				note.server_attachment_uri = rs.getString("server_attachment_uri");
				note._id = rs.getInt("client_id");
				note.title = rs.getString("title");
				note.outKeyNoteBook = rs.getInt("out_key_notebook");
				note.writeTime = rs.getLong("write_time");
				note.body = rs.getString("body");
				note.attachment_uri_str = rs.getString("attachment_uri");
				note.attachment_type_str = rs.getString("attachment_type");
				note.attachment_name_str = rs.getString("attachment_name");
				note.updated = rs.getInt("updated");
				note.is_usable = rs.getInt("is_usable");
				note.isPaint = rs.getInt("isPaint");
				note.out_key_user_id = rs.getInt("out_key_user_id");
				
				list.add(note);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 获取服务端和客户端id对象
	 * @param nj
	 * @return
	 */
	public Ids queryIds(NoteJson nj) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Ids id = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select _id from notes where out_key_user_id=? and client_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nj.out_key_user_id);
			pstmt.setInt(2, nj._id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				id = new Ids();
				id.server_id = rs.getInt(1);  // 服务端id
				id.client_id = nj._id;  // 客户端id
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return id;
	}

	/**
	 * 获取该用户下的所有的ids：server_id, client_id 集合
	 * @param tel
	 * @return
	 */
	public List<Ids> getServerIdsByTel(int userId) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Ids> list = new ArrayList<Ids>();
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select _id, client_id from notes where out_key_user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Ids id = new Ids();
				id.server_id = rs.getInt(1);
				id.client_id = rs.getInt(2);
				list.add(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public Note getNote(int server_id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Note note = null;
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select server_attachment_uri,client_id,title,out_key_notebook,write_time,"
				+ "body,attachment_uri,attachment_type,attachment_name,updated,is_usable,"
				+ "isPaint,out_key_user_id from notes where _id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, server_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				note = new Note();
				note.server_attachment_url = rs.getString("server_attachment_uri");
				note._id = server_id; //服务端id
				note.client_id = rs.getInt("client_id"); //客户端id
				note.title = rs.getString("title");
				note.outKeyNoteBook = rs.getInt("out_key_notebook");
				note.writeTime = rs.getLong("write_time");
				note.body = rs.getString("body");
				note.attachment_uri_str = rs.getString("attachment_uri");
				note.attachment_type_str = rs.getString("attachment_type");
				note.attachment_name_str = rs.getString("attachment_name");
				note.updated = rs.getInt("updated");
				note.is_usable = rs.getInt("is_usable");
				note.isPaint = rs.getInt("isPaint");
				note.out_key_user_id = rs.getInt("out_key_user_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return note;
	}



}
