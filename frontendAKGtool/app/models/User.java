package models;
/*
@Author(name="Lukas Pecak")

fields from json backend model
 {
"firstName":"",
"lastName":"",
"email":"",
"peselNr":"",
"idCardNr":"",
"feeStatus":"",
"hoursPoints":
}
 */

public class User {
	public String firstName;
	public String lastName;
	public String email;
	public String peselNr;
	public String idCardNr;
	public String feeStatus;
	public int houersPoints;
	
	public User() {}
	
	public User(String firstName, String lastName, String email, String peselNr, String idCardNr, String feeStatus, int houersPoints) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.peselNr = peselNr;
		this.idCardNr = idCardNr;
		this.feeStatus = feeStatus;
		this.houersPoints = houersPoints;
	}
	
	@Override
	public String toString() {
		return this.firstName + " " + this.lastName + " e-mail : " + this.email;
	}
}


