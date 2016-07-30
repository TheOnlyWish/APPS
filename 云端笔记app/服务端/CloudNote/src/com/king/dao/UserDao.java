package com.king.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.king.utils.JdbcUtils;

/**
 * �û���¼ע��dao��
 * @author Administrator
 *
 * user table
 * 	id int primary key auto_increment
 *  tel varchar
 *  pass varchar
 *
 */
public class UserDao {
	

	/**
	 * ������ݵ����ݿ�
	 * @param telNumber
	 * @param password
	 */
	public void addUser(String telNumber, String password) {
		
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = JdbcUtils.getConnection();
			// ��������
			String sql = "insert into user(tel,pass) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, telNumber);
			pstmt.setString(2, password);
			int f = pstmt.executeUpdate();
			System.out.println("***********"+f);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(conn != null){
					conn.close();
					conn = null;
				}
				if(pstmt != null){
					pstmt.close();
					pstmt = null;
				}
				
			}catch(Exception e){
			}
		}
		
		
	}

	/**
	 * �����ݿ��в�ѯ�û��Ƿ����
	 * @param telNumber
	 * @param password
	 * @return true / false
	 */
	public boolean queryUse(String telNumber, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			conn = JdbcUtils.getConnection();
			// ��������
			String sql = "SELECT * FROM USER WHERE tel=? AND pass=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, telNumber);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			// �ж��Ƿ����
			flag = rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(conn != null){
					conn.close();
					conn = null;
				}
				if(pstmt != null){
					pstmt.close();
					pstmt = null;
				}
				
			}catch(Exception e){
			}
		}
		return flag;
	}
	
	/**
	 * ͨ���绰�����Ѱid
	 * @param telNumber
	 * @return
	 */
	public int findIdByTel(String telNumber) {
		int id = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			conn = JdbcUtils.getConnection();
			// ��������
			String sql = "SELECT  id FROM USER WHERE tel=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, telNumber);
			rs = pstmt.executeQuery();
			// �ж��Ƿ����
			if(rs.next()){
				id = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(conn != null){
					conn.close();
					conn = null;
				}
				if(pstmt != null){
					pstmt.close();
					pstmt = null;
				}
				
			}catch(Exception e){
			}
		}
		return id;
	}

}
