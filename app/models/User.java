 package models;

/*
 *@Author(name="Lukas Pecak")
 */

 import java.util.List;
 
public class User{
	public String _id;
	public Auth auth;
	public String firstName;
	public String lastName;
	public String email;
	public String peselNr; 								
	public String idCardNr; 							
	public int hoursPoints;
	public Address address;								
	public int age;										
	public String phoneNr;								
	public String ICEphoneNr;
	public String indexNr;								
	public String instructorCard;										
	public List<String> currentCourses_ids;
	public List<String> hiredEquipments_ids;
	public List<String> payments_ids;
	
	public User() {}
	
	public User(String firstName, String lastName, String email, String peselNr, String idCardNr, String feeStatus, int hoursPoints) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return this.firstName + " " + this.lastName;
	}
}


