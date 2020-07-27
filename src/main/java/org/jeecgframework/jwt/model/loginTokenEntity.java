package org.jeecgframework.jwt.model;

public class loginTokenEntity {

	private String username;
	private String realname;
	private String departcode;
	private String departname;
	private String token;

	public loginTokenEntity() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getDepartcode() {
		return departcode;
	}

	public void setDepartcode(String departcode) {
		this.departcode = departcode;
	}

	public String getDepartname() {
		return departname;
	}

	public void setDepartname(String departname) {
		this.departname = departname;
	}

	@Override
	public String toString() {
		return String.format("loginTokenEntity [username=%s, realname=%s, departcode=%s, departname=%s, token=%s]",
				username, realname, departcode, departname, token);
	}
}
