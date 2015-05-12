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
import service.RestService.restServiceEnum;
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
		List<Course> resultList = new ArrayList<Course>();
		try{
			WSResponse response = RestService.callREST(Urls.GET_COURSES_URL, null, null, true, RestService.restServiceEnum.GET);
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
			response = RestService.callREST(Urls.GET_COURSE_URL+id, null, null, true, RestService.restServiceEnum.GET);	
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
		response = RestService.callREST(Urls.getCourseMeetingGetUrl(courseId, meetingId), null, null, true, RestService.restServiceEnum.GET);	
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
			response = RestService.callREST(Urls.getCourseMeetingsGetUrl(courseId), null, null, true, RestService.restServiceEnum.GET);	
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
	public static Result saveCourse(Course course, String courseId){
		Gson gson = new Gson();
		Course draft;
		if(Controller.session().containsKey(Utils.DRAFT_COURSE)) 
			draft = gson.fromJson(Controller.session().get(Utils.DRAFT_COURSE), Course.class);
		else
			draft = course;
		if(draft.name != course.name) draft.name = course.name;
		if(draft.description != course.description) draft.description = course.description;
		if(draft.manager_id != course.manager_id) draft.manager_id = course.manager_id;
		course = draft;
		String request = gson.toJson(course, Course.class);
		WSResponse response = null;
		if(course.members_ids == null) course.members_ids = new ArrayList<String>();
		if(course.graduatedMembers_ids == null) course.graduatedMembers_ids = new ArrayList<String>();
		if(course.meetingHistory == null) course.meetingHistory = new ArrayList<CourseMeeting>();
		if(course.instructors_ids == null) course.instructors_ids = new ArrayList<String>();
		if(course._id == "new") {
			course._id = null;
		}
		for(CourseMeeting meeting : course.meetingHistory){
			meeting._id = null;
		}
		course.instructors_ids.clear();
		for(CourseMeeting meeting : course.meetingHistory){
			if(!course.instructors_ids.contains(meeting.instructor_id)){
				course.instructors_ids.add(meeting.instructor_id);
			}
		}
		
		//The case for new course save --------------------------------------------------------------
		if((courseId == null) || (courseId.length() == 0) || (courseId == "new")){
			request = gson.toJson(course, Course.class);	
			try{
				response = RestService.callREST(Urls.POST_COURSE_URL, request, Course.class, true, RestService.restServiceEnum.POST);
				Controller.session().remove(Utils.DRAFT_COURSE);
			} catch(Exception exception){
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + exception);
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), 
																	Controller.session().get("role"), 
																	courseForm.fill(course),
																	course, 
																	UsersService.getUsersList(), 
																	courseId));
			}
			if(response.getStatus() == StatusCodes.CREATED){
				Controller.flash(Messages.SUCCESS, Messages.SUCCESS_ADDING_COURSE);
				return Controller.ok(coursesList.render(UsersService.getUser(Controller.session().get("userName")), 
														Controller.session().get("role"), 
														CoursesService.getCoursesList(), 
														UsersService.getUsersList()));
			} else{
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + response.getStatus() +" "+ response.getStatusText());
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), 
																	Controller.session().get("role"), 
																	courseForm.fill(course),
																	course, 
																	UsersService.getUsersList(), 
																	courseId));
			}
		}
		
		//The case for course update -----------------------------------------------------------------
		else{
			request = gson.toJson(course, Course.class);
			try{
				response = RestService.callREST(Urls.getCoursePutUrl(courseId), request, Course.class, true, RestService.restServiceEnum.PUT);
				Controller.session().remove(Utils.DRAFT_COURSE);
			} catch(Exception exception){
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + exception);
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), 
																	Controller.session().get("role"), 
																	courseForm.fill(course),
																	course, 
																	UsersService.getUsersList(), 
																	courseId));
			}
		}
		if((response.getStatus() == StatusCodes.CREATED) || (response.getStatus() == StatusCodes.OK)){
			Controller.flash(Messages.SUCCESS, Messages.SUCCESS_UPDATE_COURSE);
			return Controller.ok(coursesList.render(UsersService.getUser(Controller.session().get("userName")), 
													Controller.session().get("role"), 
													CoursesService.getCoursesList(), 
													UsersService.getUsersList()));
		} else{
			Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
			return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), 
																Controller.session().get("role"), 
																courseForm.fill(course),
																course, 
																UsersService.getUsersList(), 
																courseId));
		}
	}
	
	//Saving course draft *********************************************************************************************************************
	public static String saveCourseDraft(Course course, String courseId){
		Gson gson = new Gson();
		if(course.members_ids == null) course.members_ids = new ArrayList<String>();
		if(course.graduatedMembers_ids == null) course.graduatedMembers_ids = new ArrayList<String>();
		if(course.meetingHistory == null) course.meetingHistory = new ArrayList<CourseMeeting>();
		if(course.instructors_ids == null) course.instructors_ids = new ArrayList<String>();
		course.instructors_ids.clear();
		for(CourseMeeting meeting : course.meetingHistory){
			if(!course.instructors_ids.contains(meeting.instructor_id)){
				course.instructors_ids.add(meeting.instructor_id);
			}
		}
		String request = gson.toJson(course, Course.class);
		if(Controller.session().containsKey(Utils.DRAFT_COURSE)) Controller.session().remove(Utils.DRAFT_COURSE);
		Controller.session().put(Utils.DRAFT_COURSE, request);
		return courseId;
	}
	
	// Load the course draft details view *****************************************************************************************************
	public static Course loadCourseDraft(){
		if(Controller.session().containsKey(Utils.DRAFT_COURSE)){
			Gson gson = new Gson();
			return gson.fromJson(Controller.session().get(Utils.DRAFT_COURSE), Course.class);
		} 
		return null;
	}
		
	// Load the course draft details view *****************************************************************************************************
	public static Course loadCourseDraftForFormFilling(){
		if(Controller.session().containsKey(Utils.DRAFT_COURSE)){
			Gson gson = new Gson();
			Course course = gson.fromJson(Controller.session().get(Utils.DRAFT_COURSE), Course.class);
			if(course.meetingHistory != null){
				for(CourseMeeting meeting : course.meetingHistory){
					String presentMembers = Utils.toCSS(meeting.presentMembers_ids);
					System.out.println(presentMembers);
					meeting.presentMembers_ids.clear();
					meeting.presentMembers_ids.add(presentMembers);
				}
			}		
			return course;
		} 
		return null;
	}
	
	// Deleting course with the given id *********************************************************************************************************************************************
	public static void deleteCourse(String id){
		Course course = CoursesService.getCourse(id);
		WSResponse response = null;
		try{
			response = RestService.callREST(Urls.DELETE_COURSE_URL+id, null, null, false, RestService.restServiceEnum.DELETE);	
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
	
	// Deleting meeting with the given id *********************************************************************************************************************************************
	public static Result deleteMeeting(String courseId, String meetingId){
		Course course = loadCourseDraft();
		if(course != null){
			if(course.meetingHistory != null){
				for(CourseMeeting meeting : course.meetingHistory){					
					if(meeting._id != null){
						if(meeting._id.equals(meetingId)) {
							course.meetingHistory.remove(meeting);
							saveCourseDraft(course, courseId);
							return Controller.ok(courseDetails.render(UsersService.getUser(Controller.session().get("userName")),
																		Controller.session().get("role"), 
																		courseForm.fill(course),course, 
																		UsersService.getUsersList(), 
																		courseId));
						}
					}	
				}
			}
		} 
		Controller.flash().put(Messages.ERROR, Messages.ERROR_DELETING_MEETING);
		return Controller.ok(coursesList.render(UsersService.getUser(Controller.session().get("userName")), 
												Controller.session().get("role"), 
												CoursesService.getCoursesList(), 
												UsersService.getUsersList()));	
	}
	
	// Save CourseMeeting or update course ***********************************************************************************************************
	public static Result saveCourseMeeting(Form<CourseMeeting> boundForm, String courseId, String meetingId){
		CourseMeeting meeting = null;
		try {
			meeting = boundForm.get();
			meeting._id = meetingId;
		}
		catch(Exception exception){
			Controller.flash().put(Messages.WARNING, Messages.WARNING_CORRECT_MEETING_FORM_DATA + exception);
			return Controller.badRequest(courseMeetingDetails.render(UsersService.getUser(Controller.session().get("userName")), 
																		Controller.session().get("role"), 
																		boundForm, 
																		meeting, 
																		UsersService.getUsersList(), 
																		courseId, 
																		meetingId));
		}
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return Controller.badRequest(courseMeetingDetails.render(UsersService.getUser(Controller.session().get("userName")), 
																		Controller.session().get("role"), 
																		boundForm, 
																		meeting, 
																		UsersService.getUsersList(), 
																		courseId, 
																		meetingId));
		}
		meeting.presentMembers_ids = Utils.toStringList(meeting.presentMembers_ids.get(0));	
		Gson gson = new Gson();
		String request;
		WSResponse response = null;
		if(meeting.presentMembers_ids == null) meeting.presentMembers_ids = new ArrayList<String>();
		Course draft = CoursesService.loadCourseDraft();
		if(draft == null) {
			draft = new Course();
		}
		if(draft.meetingHistory == null) draft.meetingHistory = new ArrayList<CourseMeeting>();
		if(draft.graduatedMembers_ids == null) draft.graduatedMembers_ids = new ArrayList<String>();
		if(draft.members_ids == null) draft.members_ids = new ArrayList<String>();
		if(draft.instructors_ids == null) draft.instructors_ids = new ArrayList<String>();
			
		//The case for new course meeting save --------------------------------------------------------------			
		/*if("new".equals(courseId)){
			meeting._id = "1";
			for(CourseMeeting courseMeeting : draft.meetingHistory){
				if(courseMeeting._id.length() <= meeting._id.length()) meeting._id = courseMeeting._id + "1";
			}
		}*/
		if(draft.meetingHistory == null) draft.meetingHistory = new ArrayList<CourseMeeting>(); 	
		if((meetingId == null) || (meetingId == "")){
			meeting._id = Utils.generateKey();;
			/*for(CourseMeeting courseMeeting : draft.meetingHistory){
				
				if(courseMeeting._id.length() <= meeting._id.length()) meeting._id = courseMeeting._id + "1";
			}*/
			draft.meetingHistory.add(meeting);
			} else{
				for(CourseMeeting courseMeeting : draft.meetingHistory){
					if(courseMeeting._id.equals(meetingId)){
						meeting._id = meetingId;
						draft.meetingHistory.remove(courseMeeting);
						draft.meetingHistory.add(meeting);
						break;
					}
				}
			}
			if(!draft.instructors_ids.contains(meeting.instructor_id)) draft.instructors_ids.add(meeting.instructor_id);
			CoursesService.saveCourseDraft(draft, draft._id);
			System.out.println("After save meeting in session is : "+Controller.session().get(Utils.DRAFT_COURSE));
			return Controller.ok(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), 
														Controller.session().get("role"), 
														courseForm.fill(draft), 
														draft, 
														UsersService.getUsersList(), 
														courseId));
	}

	
}
