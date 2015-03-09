package models;

import java.util.Date;
import java.util.List;

/*
 * @Author(name="Lukas Pecak")
 */

public class Course {
	public String _id;
	public String name;
	public String courseType;
	public List<Date> meetingDates;
	public List<String> places;
	public String description;
	public List<CourseMeeting> meetingHistory;
	public List<User> members;
	public List<User> graduatedMembers;
	public User instructor;
	public User manager;
	
	public Course() {}
	
	@Override
	public String toString() {
		return this.name + " | " + this.courseType;
	}
}
