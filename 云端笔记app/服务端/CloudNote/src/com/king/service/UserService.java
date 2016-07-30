package com.king.service;

import com.king.dao.UserDao;

/**
 * ע���¼��ҵ���
 * @author Administrator
 *
 */
public class UserService {

	private UserDao dao = new UserDao();
	
	/**
	 * �û�ע��
	 * @param telNumber
	 * @param password
	 */
	public void userRegist(String telNumber, String password){
		dao.addUser(telNumber, password);
	}
	
	/**
	 * �û���¼
	 * @param telNumber
	 * @param password
	 * @return
	 */
	public boolean userLogin(String telNumber, String password){
		return dao.queryUse(telNumber, password);
	}

	/**
	 * ��ѯ�û��Ƿ���������ݿ�
	 * @param telNumber
	 * @param password
	 * @return
	 */
	public boolean userQuery(String telNumber, String password) {
		return dao.queryUse(telNumber, password);
	}
	
	/**
	 * ͨ���绰�����ѯ���û�id
	 * @param tel
	 * @return
	 */
	public int findIdByTel(String tel){
		return dao.findIdByTel(tel);
	}
	
}
