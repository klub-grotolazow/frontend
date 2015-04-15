package models;

import java.util.List;

/*
 * @Author(name="Lukas Pecak")
 */

public class Course {
	public String _id;
	public String name;
	public String description;
	public String manager_id;
	public List<CourseMeeting> meetingHistory;	
	public List<String> members_ids;
	public List<String> graduatedMembers_ids;
	public List<String> instructors_ids;
	
	
	public Course() {}
	
	@Override
	public String toString() {
		return this.name;
	}
}
