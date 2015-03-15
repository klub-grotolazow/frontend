package service;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import utils.Messages;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

import models.Course;

public class CoursesService {
	
	//Get the list of all courses
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
	
	public static Course getCourse(String id){
		WSResponse response = null;
		JSONArray array;
		Course course = new Course();
		if(id != null){
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
		} else{
			course.meetingDates = new ArrayList<String>();
			return course;
		}
	}
}
