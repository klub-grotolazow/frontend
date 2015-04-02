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

public class CoursesService {
	
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
			Controller.flash(Messages.ERROR, Messages.ERROR_LOADING_COURSES);
		}
		return resultList;
	}
	
	//Get one course by id *******************************************************************************************************************
	public static Course getCourse(String id){
		WSResponse response = null;
		JSONArray array;
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
	
	// Save Course or update course ***********************************************************************************************************
	public static Result saveCourse(Form<Course> boundForm, String id){
		Course course = boundForm.get();
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm, course, UsersService.getUsersList(), id));
		}
		
	
		
		Gson gson = new Gson();
		String request = gson.toJson(course, Course.class);
		WSResponse response = null;
		
		//The case for new user save --------------------------------------------------------------
		if((id == null) || id.length() == 0){

			if(course.members_ids == null) course.members_ids = new ArrayList<String>();
			if(course.graduatedMembers_ids == null) course.graduatedMembers_ids = new ArrayList<String>();
			if(course.meetingHistory == null) course.meetingHistory = new ArrayList<CourseMeeting>();
			if(course.instructors_ids == null) course.instructors_ids = new ArrayList<String>();
			if(course.manager_id == null) course.manager_id = "";
			
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
				return Controller.ok(request + " | " +response.getBody()); //Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList()));
			}
			}
		
		//The case for course update -----------------------------------------------------------------
		else{
			course = CoursesService.getCourse(id);
			System.out.println("Description in boundform i save service course class > " +boundForm.get().description + "and in course is  " + course.description);
			if(course.name != boundForm.get().name) course.name = new String(boundForm.get().name);
			if(course.description != boundForm.get().description) course.description = new String(boundForm.get().description);
			request = gson.toJson(course, Course.class);
			System.out.println(request);
			
			try{
				Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.PUT_COURSE_URL+id)
												.setContentType(Urls.CONTENT_TYPE_JSON)
												.put(request);
				response = result.get(10000);
			} catch(Exception exception){
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_COURSE + exception);
				return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList(), id));
			}
			if(response.getStatus() == StatusCodes.OK){
				Controller.flash(Messages.SUCCESS, Messages.SUCCESS_UPDATE_COURSE);
				return Controller.ok(coursesList.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), CoursesService.getCoursesList(), UsersService.getUsersList()));
			} else{
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
				return Controller.ok(request + " | " +response.getBody()); //return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,course, UsersService.getUsersList()));
			}
		}
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
}
