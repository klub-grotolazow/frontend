 package models;

import java.util.List;

import scala.Option;
/*
 *@Author(name="Lukas Pecak")
 */

public class User {
	public String _id;
	public String firstName;
	public String lastName;
	public enum membershipTypeTagEnum {CandidateMember,CommonMember,HonourableMember,SupportingMember};
	public membershipTypeTagEnum membershipTypeTag;
	public enum courseRoleEnum {NonCourseMember,CourseMember,CourseManager,Instructor};
	public courseRoleEnum courseRole;
	public enum paymentRoleEnum {NonAccounter,Accounter};
	public paymentRoleEnum paymentRole;
	public enum warehouseRoleEnum {WarehouseUser,WarehouseMan};
	public warehouseRoleEnum warehouseRole;
	public String email;
	public String peselNr; 								// \\d{11}
	public String idCardNr; 							// [A-Z]{3}\\d{6}
	public enum feeStatusEnum {OnTime,  Overpayed,OnePaymentMissing,Blocked};
	public feeStatusEnum feeStatus;
	public int hoursPoints;
	public Address address;								//Option<Address> address;
	public enum genderEnum {Woman,Man};
	public genderEnum gender;							//Option<genderEnum> gender;
	public int age;										//Option<String> age;
	public String phoneNr;								//Option<String> phoneNr;
	public String indexNr;								//Option<String> indexNr; 			//\\d{6}
	public String instructorCard;						//Option<String> instructorCard;
	public String instructorSpecialization;				//Option<String> instructorSpecialization;
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


