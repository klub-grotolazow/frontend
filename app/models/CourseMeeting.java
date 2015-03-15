package models;

import java.util.Date;
import java.util.List;

import scala.Option;

/*
 * @Author(name="Lukas Pecak")
 */

public class CourseMeeting {
	public String place;
	public String date;
	public String subject;
	public Option<String> description;
	public Option<String> meetingType;
	public enum meetingStatusEnum {Planned,Done,Cancelled};
	public meetingStatusEnum meetingStatus;
	public List<String> presentMembers_ids;
	
	public CourseMeeting() {}
	
	@Override
	public String toString() {
		return this.place + " at : " + this.date;
	}
}
