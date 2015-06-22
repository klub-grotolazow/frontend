package models;

/*
 * @Author(name="Lukas Pecak")
 */

public class Address {
	public String voivodeship;
	public String town;
	public String street;
	public int buildingNr;
	public int apartmentNr;
	public String zipCode;
	
	public Address() {}
	
	@Override
	public String toString() {
		return this.town + ", " + this.street + " " + this.buildingNr;
	}
}
