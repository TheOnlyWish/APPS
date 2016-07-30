package com.king.service;

import com.king.dao.UserDao;

/**
 * 注册登录的业务层
 * @author Administrator
 *
 */
public class UserService {

	private UserDao dao = new UserDao();
	
	/**
	 * 用户注册
	 * @param telNumber
	 * @param password
	 */
	public void userRegist(String telNumber, String password){
		dao.addUser(telNumber, password);
	}
	
	/**
	 * 用户登录
	 * @param telNumber
	 * @param password
	 * @return
	 */
	public boolean userLogin(String telNumber, String password){
		return dao.queryUse(telNumber, password);
	}

	/**
	 * 查询用户是否存在与数据库
	 * @param telNumber
	 * @param password
	 * @return
	 */
	public boolean userQuery(String telNumber, String password) {
		return dao.queryUse(telNumber, password);
	}
	
	/**
	 * 通过电话号码查询到用户id
	 * @param tel
	 * @return
	 */
	public int findIdByTel(String tel){
		return dao.findIdByTel(tel);
	}
	
}
