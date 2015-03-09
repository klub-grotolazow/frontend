package models;

/*
 * @Author(name="Lukas Pecak")
 */

public class RopeParameters {
	public int ropeLength;
	public int ropeDiameter;
	public enum ropeTypeEnum {Elastic,Static};
	public ropeTypeEnum ropeType;
	
	public RopeParameters(){}
	
	@Override
	public String toString() {
		return this.ropeType.toString();
	}
}
