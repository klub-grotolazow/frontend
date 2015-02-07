package models;

/*
 * @Author(name="Lukas Pecak")
 */

public class UserAccount {
	public String userName;
	public String passwordHash;
	public int privilageLevel;
	
	public UserAccount() {}
	
	@Override
	public String toString() {
		return this.userName;
	}
}
