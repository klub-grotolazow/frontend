package models;

import java.util.Date;

import scala.Option;

/*
 * @Author(name="Lukas Pecak")
 */

public class EquipmentHire {
	public String reservationDate;
	public Option<Date> hireDate;
	public Option<Date> receivingDate;
	public Option<Integer> delayedDays;
	public enum conditionStatusEnum {Ok,Dirty,ToService,Damaged};
	public conditionStatusEnum conditionStatus;
	public User user;
	public User warehouseman;
	
	public EquipmentHire() {}
	
	@Override
	public String toString() {
		return "Reserved at : " + this.reservationDate;
	}
	
}
