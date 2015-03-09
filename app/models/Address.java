package models;

import scala.Option;

/*
 * @Author(name="Lukas Pecak")
 */

public class Address {
	public Option<String> voivodeship;
	public String town;
	public String street;
	public String buildingNr;
	public Option<String> apartmentNr;
	public Option<String> zipCode;
	
	public Address() {}
	
	@Override
	public String toString() {
		return this.town + ", " + this.street + " " + this.buildingNr;
	}
}
