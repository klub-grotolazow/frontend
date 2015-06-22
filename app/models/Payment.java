package models;

/*
 * @Author(name="Lukas Pecak")
 */

public class Payment {
	public String _id;
	public enum paymentTypeEnum {MonthlyFee,PenaltyForEquipmentDamage,CourseCost, Salary, OtherPayment};
	public paymentTypeEnum paymentType;
	public int amount;
	public enum paymentStatusEnum {WaitingForPayment,NotPayed,Payed};
	public paymentStatusEnum paymentStatus;
	public String dueDate;
	public String user_id; 
	public String course_id;
	public String accounter_id;

	public Payment() {}
	
	@Override
	public String toString() {
		return "Payment : " + this.amount;
	}
}
