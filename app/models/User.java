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
	public Option<Address> address;
	public enum genderEnum {Woman,Man};
	public Option<genderEnum> gender;
	public Option<Integer> age;
	public Option<String> phoneNr;
	public Option<String> indexNr; 						//\\d{6}
	public Option<String> instructorCard;
	public Option<String> instructorSpecialization;
	public List<String> currentCourses_ids;
	public List<String> hiredEquipments_ids;
	
	public User() {}
	
	public User(String firstName, String lastName, String email, String peselNr, String idCardNr, String feeStatus, int hoursPoints) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		
		/*
		this.peselNr = peselNr;
		this.idCardNr = idCardNr;
		this.feeStatus = feeStatus;
		this.hoursPoints = hoursPoints;
		*/
	}
	
	@Override
	public String toString() {
		return this.firstName + " " + this.lastName + " e-mail : " + this.email;
	}
}


