package com.sumslack.test.request;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class User {
	@NotEmpty(message="用户名不能为空")
	private String username;
	@NotEmpty(message="密码不能为空")
	@Length(min=6,max=18,message="密码长度是6~18位")
	private String password;
	
	private String nick;
	
	@Range(min=1,max=150,message="年龄必须在1~150岁之间")
	private int age;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
