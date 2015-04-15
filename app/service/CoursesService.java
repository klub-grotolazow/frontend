package service;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import play.data.Form;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Messages;
import utils.StatusCodes;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

import models.Course;
import models.CourseMeeting;
import models.User;
import views.html.courses.coursesList;
import views.html.courses.courseDetails;
import views.html.courses.courseOverview;
import views.html.courses.courseMeetingDetails;

public class CoursesService {
	private static Form<Course> courseForm = Form.form(Course.class);
	
	//Get the list of all courses *************************************************************************************************************
	public static List<Course> getCoursesList(){
		WSResponse response = null;
		JSONArray array;
		List<Course> resultList = new ArrayList<Course>();
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_COURSES_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		Gson gson = new Gson();
		Course[] courses = gson.fromJson(response.getBody(), Course[].class);
		for(int i=0; i<courses.length; i++){
			resultList.add(courses[i]);
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_LOADING_COURSES + exception);
		}
		return resultList;
	}
	
	//Get one course by id *******************************************************************************************************************
	public static Course getCourse(String id){
		WSResponse response = null;
		Course course = new Course();
		
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_COURSE_URL + id)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		Gson gson = new Gson();
		course = gson.fromJson(response.getBody(), Course.class);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_LOADING_COURSES);
		}
		return course;	
	}
	
	//Get one course meeting be course and meeting id
	public static CourseMeeting getCourseMeeting(String courseId, String meetingId){
		WSResponse response = null;
		CourseMeeting meeting = new CourseMeeting();
		
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.getCourseMeetingGetUrl(courseId, meetingId))
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		Gson gson = new Gson();
		meeting = gson.fromJson(response.getBody(), CourseMeeting.class);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_LOADING_MEETINGS + exception);
		}
		return meeting;	
	}
	
	//Get the list of all courses meetings *****************************************************************************************************
	public static List<CourseMeeting> getCoursesMeetingsList(String courseId){
		WSResponse response = null;
		List<CourseMeeting> resultList = new ArrayList<CourseMeeting>();
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.getCourseMeetingsGetUrl(courseId))
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		Gson gson = new Gson();
		CourseMeeting[] meetings = gson.fromJson(response.getBody(), CourseMeeting[].class);
		for(int i=0; i<meetings.length; i++){
			resultList.add(meetings[i]);
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_LOADING_MEETINGS + exception);
		}
		return resultList;
	}
	
	// Save Course or update course ***********************************************************************************************************
	public static Result saveCourse(Form<Course> boundForm, String id){
		Course course = boundForm.get();
		System.out.println("Manager_id : "+ course.manager_id);
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm, course, UsersService.getUsersList(), id));
		}
				
		Gson gson = new Gson();
		String request = gson.toJson(course, Course.class);
		System.out.println(request);
		WSResponse response = null;
		
		//The case for new course save --------------------------------------------------------------
		if((id == null) || id.length() == 0){

			if(course.members_ids == null) course.members_ids = new ArrayList<String>();
			if(course.graduatedMembers_ids == null) course.graduatedMembers_ids = new ArrayList<String>();
			if(course.meetingHistory == null) course.meetingHistory = new ArrayList<CourseMeeting>();
			if(course.instructors_ids == null) course.instructors_ids = new ArrayList<String>();
			//if(course.manager_id == null) course.manager_id = "";
			
			request = gson.toJson(course, Course.class);
			
			try{
				Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.POST_COURSE_URL)
												.setContentType(Urls.CONTENT_TYPE_JSON)
												.post(request);
				response = result.get(10000);
			} catch(Exception exception){
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + exception);
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList(), id));
			}
			if(response.getStatus() == StatusCodes.CREATED){
				Controller.flash(Messages.SUCCESS, Messages.SUCCESS_ADDING_COURSE);
				return Controller.ok(coursesList.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), CoursesService.getCoursesList(), UsersService.getUsersList()));
			} else{
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + response.getStatus() +" "+ response.getStatusText());
				return Controller.ok(request + " | "+response.getBody());//return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList(), id));
			}
		}
		
		//The case for course update -----------------------------------------------------------------
		else{
			course = CoursesService.getCourse(id);
			//System.out.println("Description in boundform i save service course class > " +boundForm.get().description + "and in course is  " + course.description);
			
			//For all moified field add the if condition here ____________________________________
			if(course != null){
			if(course.name != boundForm.get().name) course.name = new String(boundForm.get().name);
			if(course.description != boundForm.get().description) course.description = new String(boundForm.get().description);
			if((course.manager_id != null) && (boundForm.get().manager_id != null)){
				if(course.manager_id != boundForm.get().manager_id) course.manager_id = new String(boundForm.get().manager_id);
			}
			course.instructors_ids = new ArrayList<String>();
			for(CourseMeeting meeting : course.meetingHistory){
				if(!course.instructors_ids.contains(meeting.instructor_id)){
					course.instructors_ids.add(meeting.instructor_id);
				}
			}
			
			request = gson.toJson(course, Course.class);
			
			
			try{
				Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.PUT_COURSE_URL+id)
												.setContentType(Urls.CONTENT_TYPE_JSON)
												.put(request);
				response = result.get(10000);
			} catch(Exception exception){
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + exception);
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList(), id));
			}
			} else{
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + Messages.COURSE_NOT_FOUND);
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList(), id));
			}
			
			if(response.getStatus() == StatusCodes.OK){
				Controller.flash(Messages.SUCCESS, Messages.SUCCESS_UPDATE_COURSE);
				return Controller.ok(coursesList.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), CoursesService.getCoursesList(), UsersService.getUsersList()));
			} else{
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList(), id));
			}
		}
	}
	
	//Saving course draft *********************************************************************************************************************
	public static String saveCourseDraft(Form<Course> boundForm, String courseId){
		Course course = boundForm.get();
		System.out.println("bound form in service class is -> " +boundForm);
		
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return null;
		}
		
		if((courseId == null) || (courseId == "")){
			course.graduatedMembers_ids = new ArrayList<String>();
			course.instructors_ids = new ArrayList<String>();
			course.members_ids = new ArrayList<String>();
			course.meetingHistory = new ArrayList<CourseMeeting>();
			
			System.out.println("In save course draft!");
			Gson gson = new Gson();
			String request = gson.toJson(course, Course.class);
			WSResponse response = null;
			
			try{
				Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.POST_COURSE_URL)
												.setContentType(Urls.CONTENT_TYPE_JSON)
												.post(request);
				response = result.get(10000);
			} catch(Exception exception){
				return null;
			}
			System.out.println(request +"|"+response.getBody());
			if(response.getStatus() == StatusCodes.CREATED){		
				return gson.fromJson(response.getBody(), Course.class)._id; 
			} else{
				return null;
			}
		} else return courseId;
	}
	
	// Deleting course with the given id *********************************************************************************************************************************************
	public static void deleteCourse(String id){
		Course course = CoursesService.getCourse(id);
		WSResponse response = null;
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.DELETE_COURSE_URL+id)
										.delete();
		response = result.get(10000);
		if(response.getStatus() == StatusCodes.OK){
			User user = new Gson().fromJson(response.getBody(), User.class);
			Controller.flash().put(Messages.SUCCESS, Messages.SUCCESS_DELETED_COURSE + course.name);
		} else{
			Controller.ok(response.getBody()); //Controller.flash().put(Messages.ERROR, Messages.ERROR_DELETE_COURSE + response.getStatus() + " " + response.getStatusText());
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_DELETE_COURSE + exception);
		}
	}
	
	// Save CourseMeeting or update course ***********************************************************************************************************
		public static Result saveCourseMeeting(Form<CourseMeeting> boundForm, String courseId, String meetingId){
			//System.out.println("In save course meeting  courseid is : "+courseId/*Controller.session().get(Utils.DRAFT_COURSE*/);
			CourseMeeting meeting = null;
			try {
				meeting = boundForm.get();
			}
			catch(Exception exception){
				Controller.flash().put(Messages.WARNING, Messages.WARNING_CORRECT_MEETING_FORM_DATA + exception);
				return Controller.badRequest(courseMeetingDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm, meeting, UsersService.getUsersList(), Controller.session().get(Utils.DRAFT_COURSE), ""));
			}
			
			if(boundForm.hasErrors()) {
				Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
				return Controller.badRequest(courseMeetingDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm, meeting, UsersService.getUsersList(), Controller.session().get(Utils.DRAFT_COURSE), ""));
			}

			meeting.presentMembers_ids = Utils.toStringList(meeting.presentMembers_ids.get(0));
					
			Gson gson = new Gson();
			String request;
			WSResponse response = null;
			
			//The case for new course save --------------------------------------------------------------
			if((meetingId == null) || meetingId == ""){

				if(meeting.presentMembers_ids == null) meeting.presentMembers_ids = new ArrayList<String>();

				request = gson.toJson(meeting, CourseMeeting.class);
				
				try{
					System.out.println("In the saveCourseMeeting Draftcourse in session is : "+Controller.session().get(Utils.DRAFT_COURSE));
					Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.getCourseMeetingPostUrl(Controller.session().get(Utils.DRAFT_COURSE)))
													.setContentType(Urls.CONTENT_TYPE_JSON)
													.post(request);
					response = result.get(10000);
				} catch(Exception exception){
					Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + exception);
					return Controller.badRequest(courseMeetingDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm, meeting, UsersService.getUsersList(), Controller.session().get(Utils.DRAFT_COURSE), ""));
				}
				
				if(response.getStatus() == StatusCodes.OK){
					Controller.flash(Messages.SUCCESS, Messages.SUCCESS_ADDING_COURSE);
					Course course = gson.fromJson(response.getBody(), Course.class);
					course.instructors_ids.clear();
					for(CourseMeeting courseMeeting : course.meetingHistory){
						if(!course.instructors_ids.contains(courseMeeting)) course.instructors_ids.add(courseMeeting.instructor_id);			
					}
					CoursesService.saveCourse(courseForm.fill(course), course._id);
					return Controller.ok(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), courseForm.fill(CoursesService.getCourse(Controller.session().get(Utils.DRAFT_COURSE))), CoursesService.getCourse(Controller.session().get(Utils.DRAFT_COURSE)), UsersService.getUsersList(), courseId));
				} else{
					Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE_MEETING + response.getStatus() +" "+ response.getStatusText());
					return Controller.badRequest(courseMeetingDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm, meeting, UsersService.getUsersList(), Controller.session().get(Utils.DRAFT_COURSE), ""));
				}
			}
			
			//The case for course update -----------------------------------------------------------------
			else{
				meeting = CoursesService.getCourseMeeting(courseId, meetingId);
				//System.out.println("Description in boundform i save service course class > " +boundForm.get().description + "and in course is  " + course.description);
				
				//For all moified field add the if condition here ____________________________________
				if(meeting != null){
				if(meeting.group != boundForm.get().group) meeting.group = new Integer(boundForm.get().group);
				if(meeting.place != boundForm.get().place) meeting.place = new String(boundForm.get().place);
				if(meeting.date != boundForm.get().date) meeting.date = new String(boundForm.get().date);
				if(meeting.subject != boundForm.get().subject) meeting.subject = new String(boundForm.get().subject);
				if(meeting.description != boundForm.get().description) meeting.description = new String(boundForm.get().description);
				if((meeting.instructor_id != null) && (boundForm.get().instructor_id != null)){
					if(meeting.instructor_id != boundForm.get().instructor_id) meeting.instructor_id = new String(boundForm.get().instructor_id);
				}
				if((meeting.presentMembers_ids == null) || meeting.presentMembers_ids.size() == 0){
					meeting.presentMembers_ids = new ArrayList<String>();
					meeting.presentMembers_ids = Utils.toStringList(boundForm.get().presentMembers_ids.get(0));
				} else{
					List<String> temp = Utils.toStringList(boundForm.get().presentMembers_ids.get(0));
					for(String element : temp){
						if(!meeting.presentMembers_ids.contains(element)){
							meeting.presentMembers_ids.add(element);
						}
					}
				}
				
				request = gson.toJson(meeting, Course.class);
				
				try{
					Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.getCourseMeetingPutUrl(courseId, meetingId))
													.setContentType(Urls.CONTENT_TYPE_JSON)
													.put(request);
					response = result.get(10000);
				} catch(Exception exception){
					Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + exception);
					return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), courseForm.fill(CoursesService.getCourse(courseId)), CoursesService.getCourse(courseId), UsersService.getUsersList(), courseId));
				}
				} else{ // if meeting is null - so we propablu could not get the template with the requested id from the server
					Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + Messages.COURSE_NOT_FOUND);
					return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), courseForm.fill(CoursesService.getCourse(courseId)), CoursesService.getCourse(courseId), UsersService.getUsersList(), courseId));
				}
				
				if(response.getStatus() == StatusCodes.OK){
					Controller.flash(Messages.SUCCESS, Messages.SUCCESS_UPDATE_COURSE);
					System.out.println("The save meeting body is -> "+response.getBody());
					return Controller.ok(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), courseForm.fill(CoursesService.getCourse(courseId)), CoursesService.getCourse(courseId), UsersService.getUsersList(), courseId));
				} else{
					Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE_MEETING + response.getStatus() +" "+ response.getStatusText());
					return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), courseForm.fill(CoursesService.getCourse(courseId)), CoursesService.getCourse(courseId), UsersService.getUsersList(), courseId));
				}
				//return Controller.ok(response.getStatus() + "  "+response.getStatusText());
			}
		}
	
}
