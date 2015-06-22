package models;

import java.util.ArrayList;
import java.util.List;

/*
 * @Author(name="Lukas Pecak")
 */

public class Equipment {
	public String _id;	
	public String name;
	public String description;																																								
	public boolean isAvailable;
	public boolean isServicing;
	public boolean isReserved;
	public boolean isHired;	
	public enum allowedForEnum {CandidateMember,CommonMember,HonourableMember,SupportingMember};
	public List<allowedForEnum> allowedFor;																																											
	public String purchaseDate;
	public String nextInspectionDate;
	public Integer price;
	public String serialNumber;																																								
	public List<EquipmentHire> hireHistory;
	
	public Equipment() {
		this.allowedFor = new ArrayList<Equipment.allowedForEnum>();
		this.hireHistory = new ArrayList<EquipmentHire>();
	}
	
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
