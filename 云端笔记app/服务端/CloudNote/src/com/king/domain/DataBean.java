package com.king.domain;

/**
 * 用于存储在Map集合的数据
 * 请求事件--time
 * 请求码--code
 * @author Administrator
 *
 */
public class DataBean {
	// 请求时间
	public String time;
	// 验证码
	public int code;
	// 请求事件毫秒值
	public long clickTime;
	@Override
	public String toString() {
		return "DataBean [time=" + time + ", code=" + code + ", clickTime="
				+ clickTime + "]";
	}
}
