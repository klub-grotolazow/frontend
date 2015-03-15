package models;

import scala.Option;

/*
 * @Author(name="Lukas Pecak")
 */

public class Address {
	public String voivodeship;//Option<String> voivodeship;
	public String town;
	public String street;
	public int buildingNr;
	public int apartmentNr;//Option<Integer> apartmentNr;
	public String zipCode;//Option<String> zipCode;
	
	public Address() {}
	
	@Override
	public String toString() {
		return this.town + ", " + this.street + " " + this.buildingNr;
	}
}
