package models;

import java.util.Date;

/*
 * @Author(name="Lukas Pecak")
 */

public class Payment {
	public String _id;
	public enum paymentTypeEnum {MonthlyFee,PenaltyForEquipmentDamage,CourseCost};
	public paymentTypeEnum paymentType;
	public int amount;
	public enum paymentStatusEnum {WaitingForPayment,NotPayed,Payed};
	public paymentStatusEnum paymentStatus;
	public Date dueDate;
	public User user; 
	public Course coures;
	public User Accounter;
	
	public Payment() {}
	
	@Override
	public String toString() {
		return "Payment : " + this.amount;
	}
}
