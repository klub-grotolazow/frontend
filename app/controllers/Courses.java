package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.ArrayList;
import java.util.List;

import models.User;
import models.UserAccount;

import org.json.JSONException;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import service.CoursesService;
import service.UsersService;
import utils.Messages;
import models.Course;
import views.html.index;
import views.html.courses.coursesList;
import views.html.courses.courseDetails;
import views.html.courses.courseOverview;

public class Courses extends Controller {
	private static Form<Course> courseForm = Form.form(Course.class);
	private static List<Course> cl = new ArrayList<Course>();
	
	//Displaying the list of courses
	public static Result getCourses() throws JSONException {
		try{
			return ok(coursesList.render(CoursesService.getCoursesList()));
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.ERROR_LOADING_COURSES);
			return badRequest(index.render());
		}
	}
	
	//Show the course overview
	public static Result showCourse(String _id){
		try{
			return ok(courseOverview.render());
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.ERROR_LOADING_COURSE_VIEW);
			return badRequest(index.render());
		}
		
	}
	
	//Edit course
	public static Result editCourse(String id){
		/*
		User user = UsersService.getUser(id);
		if(user == null){
			Controller.flash().put(Messages.ERROR, Messages.USER_NOT_FOUND);
			return ok(usersList.render(UsersService.getUsersList()));
		} else{
			session().put(Messages.EDIT_USER_KEY, id.toString());
			Form<User> form = userForm.fill(user);
			return ok(userDetails.render(form));
		}*/
		return ok(coursesList.render(cl));
	}
		
	//Deleting course
	public static Result deleteCourse(String id){
		/*
		UsersService.deleteUser(id);	
		return ok(usersList.render(UsersService.getUsersList()));
		*/
		return ok(coursesList.render(cl));
	}
	
	//Adding new course
	public static Result newCourse(){
		return ok(courseDetails.render(courseForm));
	}
	
	//Adding new course
	public static Result saveCourse(){
		//return ok(userDetails.render(userForm));
		return ok(coursesList.render(cl));
	}
}
