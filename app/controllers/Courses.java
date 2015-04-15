package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.ArrayList;
import java.util.List;

import models.CourseMeeting;
import models.User;
import models.UserAccount;

import org.json.JSONException;

import com.google.gson.Gson;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.CoursesService;
import service.UsersService;
import utils.Messages;
import utils.Utils;
import models.Course;
import views.html.index;
import views.html.courses.coursesList;
import views.html.courses.courseDetails;
import views.html.courses.courseOverview;
import views.html.courses.courseMeetingDetails;

@Security.Authenticated(Secured.class)
public class Courses extends Controller {
	private static Form<Course> courseForm = Form.form(Course.class);
	private static Form<CourseMeeting> meetingForm = Form.form(CourseMeeting.class);
	private static List<Course> cl = new ArrayList<Course>();
	
	/*public static Result courseAddManager(id: String, managerId: String){
		
		return todo;
	}*/
	
	//Displaying the list of courses ********************************************************************************************************** 
	public static Result getCourses() throws JSONException {
		try{
			return ok(coursesList.render(UsersService.getUser(session().get("userName")), session().get("role"), CoursesService.getCoursesList(), UsersService.getUsersList()));
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
			return ok(coursesList.render(UsersService.getUser(session().get("userName")), session().get("role"), CoursesService.getCoursesList(), UsersService.getUsersList()));
		} else{
			Form<Course> form = courseForm.fill(course);
			return ok(courseDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), form, course, UsersService.getUsersList(), id));
		}
	}
		
	//Deleting course **************************************************************************************************************************
	public static Result deleteCourse(String id){
		CoursesService.deleteCourse(id);	
		return ok(coursesList.render(UsersService.getUser(session().get("userName")), session().get("role"), CoursesService.getCoursesList(), UsersService.getUsersList()));
	}
	
	//Adding new course ************************************************************************************************************************
	public static Result newCourse(){
		if(Secured.isSuperUser() || Secured.isCourseManager()){
		return ok(courseDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), courseForm, new Course(), UsersService.getUsersList(), ""));
		} else {
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.index());
		}
	}
	
	// Saving course in database with api backend **********************************************************************************************
	public static Result saveCourse(String id){
		if(Secured.isSuperUser() || Secured.isCourseManager()){
		Form<Course> boundForm = courseForm.bindFromRequest();
		//System.out.println("Manager_id : "+ boundForm.get().manager_id);
		return CoursesService.saveCourse(boundForm, id);
		} else {
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.index());
		}
	}
	
	// mayby only in courses service class !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// Save course draft ************************************************************************************************************************
	public static Result saveCourseDraft(){
		return TODO;
	}
	
	// Load course draft ************************************************************************************************************************
		public static Result loadCourseDraft(){
			return TODO;
		}
	
	// Load the course meeting details view *****************************************************************************************************
	public static Result newCourseMeeting(String courseId){
		if(Secured.isSuperUser() || Secured.isCourseManager()){
			System.out.println("The id in newCourseMeeting is -> "+courseId);
			Form<Course> boundForm = courseForm.bindFromRequest();
			//System.out.println("bound form is -> " +boundForm);
			//System.out.println("The session draft param is : " + session().get(Utils.DRAFT_COURSE));
			if(Controller.session().containsKey(Utils.DRAFT_COURSE)){ 
				Controller.session().remove(Utils.DRAFT_COURSE);
			} 
			String draftId = CoursesService.saveCourseDraft(boundForm, courseId);
			System.out.println("The id in newCourseMeeting is -> "+draftId);
			Controller.session().put(Utils.DRAFT_COURSE, draftId);
			
			if(!(Controller.session().containsKey(Utils.DRAFT_COURSE) || (Controller.session().get(Utils.DRAFT_COURSE) == ""))){
				flash().put(Messages.ERROR, Messages.ERROR_SAVING_COURSE_DRAFT);
				System.out.println("Redirecting from newCourseMeeting to course details");
				return badRequest(courseDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), boundForm, CoursesService.getCourse(courseId), UsersService.getUsersList(), courseId));
			} else{
				/*if(session().containsKey(Utils.DRAFT_COURSE)){
					System.out.println("Contains a draft object! ->" + id);
					session().remove(Utils.DRAFT_COURSE);
				}
				session().put(Utils.DRAFT_COURSE, id);
				System.out.println("Session param for draft is -> "+session().get(Utils.DRAFT_COURSE));*/
				return ok(courseMeetingDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), meetingForm, new CourseMeeting(), UsersService.getUsersList(), courseId, ""));
			}
			
		} else {
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.index());
		}
	}	
		
	// Load the course meeting details view *****************************************************************************************************
	public static Result loadCourseMeeting(String courseId, String meetingId){
		return TODO;
	}
	
	// Save course meeting **********************************************************************************************************************
	public static Result saveCourseMeeting(String courseId, String meetingId){
		Form<CourseMeeting> boundForm = meetingForm.bindFromRequest();
		return CoursesService.saveCourseMeeting(boundForm, courseId, meetingId);
	}
	
	// Delete course meeting ********************************************************************************************************************
	public static Result deleteCourseMeeting(String courseId, String meetingId){
		return TODO;
	}
	
	
}
