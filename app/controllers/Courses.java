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
import play.mvc.Security;
import service.CoursesService;
import service.UsersService;
import utils.Messages;
import models.Course;
import views.html.index;
import views.html.courses.coursesList;
import views.html.courses.courseDetails;
import views.html.courses.courseOverview;

@Security.Authenticated(Secured.class)
public class Courses extends Controller {
	private static Form<Course> courseForm = Form.form(Course.class);
	private static List<Course> cl = new ArrayList<Course>();
	
	//Displaying the list of courses ********************************************************************************************************** 
	public static Result getCourses() throws JSONException {
		try{
			return ok(coursesList.render(UsersService.getUser(session().get("userName")), session().get("role"), CoursesService.getCoursesList()));
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.ERROR_LOADING_COURSES);
			return badRequest(index.render(UsersService.getUser(session().get("userName")), session().get("role")));
		}
	}
	
	//Show the course overview ****************************************************************************************************************
	public static Result showCourse(String _id){
		try{
			return ok(courseOverview.render());
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.ERROR_LOADING_COURSE_VIEW);
			return badRequest(index.render(UsersService.getUser(session().get("userName")), session().get("role")));
		}
		
	}
	
	//Edit course *****************************************************************************************************************************
	public static Result editCourse(String id){
		Course course = CoursesService.getCourse(id);
		if(course == null){
			Controller.flash().put(Messages.ERROR, Messages.COURSE_NOT_FOUND);
			return ok(coursesList.render(UsersService.getUser(session().get("userName")), session().get("role"), CoursesService.getCoursesList()));
		} else{
			Form<Course> form = courseForm.fill(course);
			return ok(courseDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), form, course, UsersService.getUsersList(), id));
		}
	}
		
	//Deleting course **************************************************************************************************************************
	public static Result deleteCourse(String id){
		CoursesService.deleteCourse(id);	
		return ok(coursesList.render(UsersService.getUser(session().get("userName")), session().get("role"), CoursesService.getCoursesList()));
	}
	
	//Adding new course ************************************************************************************************************************
	public static Result newCourse(){
		return ok(courseDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), courseForm, CoursesService.getCourse(null), UsersService.getUsersList(), null));
	}
	
	// Saving course in database with api backend **********************************************************************************************
	public static Result saveCourse(String id){
		Form<Course> boundForm = courseForm.bindFromRequest();
		System.out.println("The boundform description > " + boundForm.get().description);
		return CoursesService.saveCourse(boundForm, id);
	}
}
