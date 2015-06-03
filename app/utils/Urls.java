package utils;

/*
 * @Author(name="Lukas Pecak")
 */

public class Urls {
	//Content types
	public static final String CONTENT_TYPE_JSON = "application/json";
	
	//Users urls
	public static final String POST_USER_URL = 				"/users";
	public static final String GET_USERS_URL = 				"/users";
	public static final String DELETE_USER_URL =			"/users/";
	public static final String GET_ONE_USER = 				"/users/";
	public static final String PUT_USER_URL	=				"/users/";
	
	//Course urls
	public static final String DELETE_COURSE_URL =			"/courses/";
	public static final String PUT_COURSE_URL = 			"/courses/";
	public static final String GET_COURSES_URL = 			"/courses";
	public static final String POST_COURSE_URL = 			"/courses";
	public static final String GET_COURSE_URL =				"/courses/";

	//Equipment urls
	public static final String GET_EQUIPMENTS_URL =			"/equipments";
	public static final String GET_EQUIPMENT_URL =			"/equipments/";
	public static final String DELETE_EQUIPMENT_URL =		"/equipments/";
	public static final String SAVE_EQUIPMENTS_URL =		"/equipments";
	public static final String PUT_EQUIPMENTS_URL =			"/equipments/";
	
	//Payments urls
	public static final String GET_PAYMENTS_URL =			"/payments";
	
	//CourseMeeting urls
	public static final String POST_COURSE_MEETING_URL =	"/courses/notfound/meetings";
	
	//CourseMeeting post url
	public static String getCourseMeetingPostUrl(String courseId){
		if(courseId != null){
			//System.out.println("the cours id is ->"+courseId+"<-");
			StringBuilder builder = new StringBuilder();
			builder.append("/courses/")
					.append(courseId)
					.append("/meetings");
			return builder.toString();
		} else{
			return "/courses/notfound/meetings";
		}
	}
	
	//Get CourseMeetings list url
	public static String getCourseMeetingsGetUrl(String courseId){
		if(courseId != null){
			//System.out.println("the cours id is ->"+courseId+"<-");
			StringBuilder builder = new StringBuilder();
			builder.append("/courses/")
					.append(courseId)
					.append("/meetings");
			return builder.toString();
		} else{
			return "/courses/notfound/meetings";
		}
	}
	
	//Get course meeting url
	public static String getCourseMeetingGetUrl(String courseId, String meetingId){
		if((courseId != null) && (meetingId != null)){
			//System.out.println("the meeting id is ->"+courseId+"<-");
			StringBuilder builder = new StringBuilder();
			builder.append("/courses/")
					.append(courseId)
					.append("/meetings/")
					.append(meetingId);
			return builder.toString();
		} else{
			return "/courses/notfound/meetings";
		}
	}
	
	//Put course meeting url
	public static String getCourseMeetingPutUrl(String courseId, String meetingId){
		if((courseId != null) && (meetingId != null)){
			//System.out.println("the meeting id is ->"+courseId+"<-");
			StringBuilder builder = new StringBuilder();
			builder.append("/courses/")
					.append(courseId)
					.append("/meetings/")
					.append(meetingId);
			return builder.toString();
		} else{
			return "/courses/notfound/meetings";
		}
	}
	
	//Put course url
	public static String getCoursePutUrl(String courseId){
		if(courseId != null){
			//System.out.println("the meeting id is ->"+courseId+"<-");
			StringBuilder builder = new StringBuilder();
			builder.append("/courses/")
					.append(courseId);
			return builder.toString();
		} else{
			return "/courses/notfound";
		}
	}
	

	
}
