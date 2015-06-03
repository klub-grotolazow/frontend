package models;

import java.util.ArrayList;
import java.util.List;

import scala.Option;

/*
 * @Author(name="Lukas Pecak")
 */

public class Equipment {
	public String _id;
	
	public String name;
	public String description;
																																								/*public enum equipmentTypeEnum {Carabiner,Rope};
																																								public equipmentTypeEnum equipmentType;*/
	public boolean isAvailable;
	public boolean isServicing;
	public boolean isReserved;
	public boolean isHired;
	
	public enum allowedForEnum {CandidateMember,CommonMember,HonourableMember,SupportingMember};
	public List<allowedForEnum> allowedFor;
																																												//public Option<String> producer;
	public String purchaseDate;
	public String nextInspectionDate;
	public Integer price;
	public String serialNumber;
																																									//public enum conditionEnum {Ok,Dirty,ToService,Damaged};
																																									//public Option<conditionEnum> condition;
	
																																								//public Option<RopeParameters> ropeParameters;
																																								//public enum carabinerTypeEnum {Zakręcany,Niezakręcany};
																																								//public Option<carabinerTypeEnum> carabinerType; 
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
