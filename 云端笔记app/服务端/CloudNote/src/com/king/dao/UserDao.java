package com.king.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.king.utils.JdbcUtils;

/**
 * 用户登录注册dao层
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
	 * 添加数据到数据库
	 * @param telNumber
	 * @param password
	 */
	public void addUser(String telNumber, String password) {
		
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = JdbcUtils.getConnection();
			// 插入数据
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
	 * 在数据库中查询用户是否存在
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
			// 插入数据
			String sql = "SELECT * FROM USER WHERE tel=? AND pass=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, telNumber);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			// 判断是否存在
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
	 * 通过电话号码查寻id
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
			// 插入数据
			String sql = "SELECT  id FROM USER WHERE tel=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, telNumber);
			rs = pstmt.executeQuery();
			// 判断是否存在
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
