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
import service.UsersService;
import utils.Messages;
import models.Course;
import views.html.courses.coursesList;

public class Courses extends Controller {
	private static Form<Course> courseForm = Form.form(Course.class);
	private static List<Course> cl = new ArrayList<Course>();
	//Displaying the list of courses
	public static Result getCourses() throws JSONException {
		try{
			
			return ok(coursesList.render(cl));
		} catch(Exception exception){
			return badRequest();
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
			//return ok(userDetails.render(userForm));
			return ok(coursesList.render(cl));
		}
	
}
