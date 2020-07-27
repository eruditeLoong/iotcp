package org.jeecgframework.jwt.model;

public class repasswordEntity {

	private String username;
	private String password;
	private String repassword;
	private String yancode;

	public repasswordEntity() {
		super();
	}

	public repasswordEntity(String username, String password, String repassword, String yancode) {
		super();
		this.username = username;
		this.password = password;
		this.repassword = repassword;
		this.yancode = yancode;
	}

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

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getYancode() {
		return yancode;
	}

	public void setYancode(String yancode) {
		this.yancode = yancode;
	}

	@Override
	public String toString() {
		return String.format("repasswordEntity [username=%s, password=%s, repassword=%s, yancode=%s]", username,
				password, repassword, yancode);
	}

}
