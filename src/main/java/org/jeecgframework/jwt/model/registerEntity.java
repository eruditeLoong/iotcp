package org.jeecgframework.jwt.model;

public class registerEntity {

	private String username;
	private String password;
	private String repassword;
	private String verCode;
	private String merchantName;
	private String contact;

	public registerEntity() {
		super();
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

	public String getVerCode() {
		return verCode;
	}

	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}

	@Override
	public String toString() {
		return String.format("repasswordEntity [username=%s, password=%s, repassword=%s, verCode=%s]", username,
				password, repassword, verCode);
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
