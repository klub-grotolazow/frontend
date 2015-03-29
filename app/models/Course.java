package models;

import java.util.Date;
import java.util.List;

/*
 * @Author(name="Lukas Pecak")
 */

public class Course {
	public String _id;
	public String name;
	public String description;
	public List<CourseMeeting> meetingHistory;
	
	//public String courseType;
	//public List<String> meetingDates;
	//public List<String> places;
		
	public List<String> members_ids;
	public List<String> graduatedMembers_ids;
	public List<String> instructors_ids;
	public String manager_id;
	
	public Course() {}
	
	@Override
	public String toString() {
		return this.name;
	}
}
