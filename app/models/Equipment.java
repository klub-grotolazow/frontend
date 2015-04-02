package models;

import java.util.List;

import scala.Option;

/*
 * @Author(name="Lukas Pecak")
 */

public class Equipment {
	public String _id;
	public String serialNumber;
	public String name;
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
	//public enum conditionEnum {Ok,Dirty,ToService,Damaged};
	//public Option<conditionEnum> condition;
	public String description;
	//public Option<RopeParameters> ropeParameters;
	//public enum carabinerTypeEnum {Zakręcany,Niezakręcany};
	//public Option<carabinerTypeEnum> carabinerType; 
	public List<EquipmentHire> hireHistory;
	
	public Equipment() {}
	
	@Override
	public String toString() {
		return this.name + " >> " + this.serialNumber;
	}
	
}
