package models;

import java.util.Date;
import java.util.List;

import scala.Option;
import utils.Utils;

/*
 * @Author(name="Lukas Pecak")
 */

public class CourseMeeting {
	public String _id;
	public int group;
	public String place;
	public String date;
	public String subject;
	public String description;
	public enum meetingStatusEnum {Planned,Done,Cancelled};
	public meetingStatusEnum meetingStatus;
	public String instructor_id;
	public List<String> presentMembers_ids;
	
	public CourseMeeting() {}
	
	@Override
	public String toString() {
		return this.place + " at : " + this.date;
	}
}
