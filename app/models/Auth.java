package models;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.List;

public class Auth {
	 public String userName;
	 public String passwordHash;
	 public String authToken;
	 public String tokenExpiredDate;
	 public List<String> roles;
     public String feeStatus ;  
     
     public Auth() {}
     
     @Override
     public String toString() {
      	return this.userName + " | " + this.passwordHash;
     }
}
