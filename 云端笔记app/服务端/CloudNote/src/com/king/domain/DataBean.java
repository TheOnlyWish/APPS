package com.king.domain;

/**
 * ���ڴ洢��Map���ϵ�����
 * �����¼�--time
 * ������--code
 * @author Administrator
 *
 */
public class DataBean {
	// ����ʱ��
	public String time;
	// ��֤��
	public int code;
	// �����¼�����ֵ
	public long clickTime;
	@Override
	public String toString() {
		return "DataBean [time=" + time + ", code=" + code + ", clickTime="
				+ clickTime + "]";
	}
}
