package models;

import java.util.Date;

import scala.Option;

/*
 * @Author(name="Lukas Pecak")
 */

public class EquipmentHire {
	public String _id;
	public String reservationDate;
	public String hireDate;
	public String returnDate;
	//public Option<Integer> delayedDays;
	public enum conditionStatusEnum {Ok,Dirty,ToService,Damaged};
	public conditionStatusEnum conditionStatus;
	public String user_id;
	public String warehouseman_id;
	
	public EquipmentHire() {}
	
	@Override
	public String toString() {
		return "Reserved at : " + this.reservationDate;
	}
	
}
