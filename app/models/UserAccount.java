package models;

/*
 * @Author(name="Lukas Pecak")
 */

import play.api.libs.Codecs;

public class UserAccount {
	public String userName;
	public String passwordHash;
	public SystemRoleEnum systemRole;
	public String userId;
	
	public UserAccount() {}
	
	public UserAccount(String userName, String password, SystemRoleEnum systemRole, String userId){
		this.userName = userName;
		this.passwordHash = Codecs.sha1(password);
		this.systemRole = systemRole;
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return this.userName + " - " + this.systemRole;
	}
}
