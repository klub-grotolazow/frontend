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
	//private static List<Course> cl = new ArrayList<Course>();

	//Displaying the list of courses ********************************************************************************************************** 
	public static Result getCourses() throws JSONException {
		try{
			return ok(coursesList.render(UsersService.getUser(session().get("userName")), 
											session().get("role"), 
											CoursesService.getCoursesList(), 
											UsersService.getUsersList()));
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
	public static Result editCourse(String courseId){
		Course course = CoursesService.getCourse(courseId);
		if(course == null){
			Controller.flash().put(Messages.ERROR, Messages.COURSE_NOT_FOUND);
			return ok(coursesList.render(UsersService.getUser(session().get("userName")), 
											session().get("role"), 
											CoursesService.getCoursesList(), 
											UsersService.getUsersList()));
		} else{
			CoursesService.saveCourseDraft(course, courseId);
			Form<Course> form = courseForm.fill(course);
			return ok(courseDetails.render(UsersService.getUser(session().get("userName")), 
											session().get("role"), 
											form, 
											course, 
											UsersService.getUsersList(), 
											courseId));
		}
	}
		
	//Deleting course **************************************************************************************************************************
	public static Result deleteCourse(String id){
		CoursesService.deleteCourse(id);	
		return ok(coursesList.render(UsersService.getUser(session().get("userName")), 
										session().get("role"), 
										CoursesService.getCoursesList(), 
										UsersService.getUsersList()));
	}
	
	//Adding new course ************************************************************************************************************************
	public static Result newCourse(){
		if(Secured.isSuperUser() || Secured.isCourseManager()){
			Course course = new Course();
			if(session().containsKey(Utils.DRAFT_COURSE)) {
				Gson gson = new Gson();
				course = gson.fromJson(session().get(Utils.DRAFT_COURSE), Course.class);
			}
			if(course._id != "new") session().remove(Utils.DRAFT_COURSE);
			return ok(courseDetails.render(UsersService.getUser(session().get("userName")), 
											session().get("role"), 
											courseForm, 
											new Course(), 
											UsersService.getUsersList(), 
											Utils.NEW));
		} else {
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.index());
		}
	}
	
	// Saving course in database with api backend **********************************************************************************************
	public static Result saveCourse(String courseId){
		if(Secured.isSuperUser() || Secured.isCourseManager()){
		Form<Course> boundForm = courseForm.bindFromRequest();
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return Controller.badRequest(courseDetails.render(UsersService.getUser(Controller.session().get("userName")), 
																Controller.session().get("role"), 
																boundForm, 
																boundForm.get(), 
																UsersService.getUsersList(), 
																courseId));
		}
		return CoursesService.saveCourse(boundForm.get(), courseId);
		} else {
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.index());
		}
	}

	
	// Load course draft ************************************************************************************************************************
		public static Result loadCourseDraft(){
			Gson gson = new Gson();
			Course course;
			if(session().containsKey(Utils.DRAFT_COURSE)){
				course = gson.fromJson(session().get(Utils.DRAFT_COURSE), Course.class);
				return ok(courseDetails.render(UsersService.getUser(session().get("userName")), 
												session().get("role"), 
												courseForm.fill(course), 
												course, 
												UsersService.getUsersList(), 
												course._id));
			} else {
				Course emptyCourse = new Course();
				return ok(courseDetails.render(UsersService.getUser(session().get("userName")), 
												session().get("role"), 
												courseForm.fill(emptyCourse), 
												emptyCourse, 
												UsersService.getUsersList(), 
												emptyCourse._id));
			}
		}
	
	// Load the course meeting details view *****************************************************************************************************
	public static Result newCourseMeeting(String courseId){
		if(Secured.isSuperUser() || Secured.isCourseManager()){
			Form<Course> boundForm = courseForm.bindFromRequest();
			Course course = boundForm.get();
			if(boundForm.hasErrors()) {
				Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
				return badRequest(courseDetails.render(UsersService.getUser(session().get("userName")), 
														session().get("role"), 
														boundForm, 
														CoursesService.getCourse(courseId), 
														UsersService.getUsersList(), 
														courseId));
			}
			Course draft = CoursesService.loadCourseDraft();
			if(draft == null) draft = new Course();
 			if(draft.name != course.name) draft.name = course.name;
			if(draft.description != course.description) draft.description = course.description;
			if(draft.manager_id != course.manager_id) draft.manager_id = course.manager_id;
			CoursesService.saveCourseDraft(draft, courseId);
			if(!(Controller.session().containsKey(Utils.DRAFT_COURSE) || (Controller.session().get(Utils.DRAFT_COURSE) == ""))){
				flash().put(Messages.ERROR, Messages.ERROR_SAVING_COURSE_DRAFT);
				return badRequest(courseDetails.render(UsersService.getUser(session().get("userName")), 
														session().get("role"), 
														boundForm, 
														CoursesService.getCourse(courseId), 
														UsersService.getUsersList(), 
														courseId));
			} else{
				return ok(courseMeetingDetails.render(UsersService.getUser(session().get("userName")), 
														session().get("role"), 
														meetingForm, 
														new CourseMeeting(),
														UsersService.getUsersList(),
														courseId, ""));
			}
		} else {
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.index());
		}
	}	
		
	// Load the course meeting details view *****************************************************************************************************
	public static Result editCourseMeeting(String courseId, String meetingId){
		CourseMeeting meeting = null;
		Course course = CoursesService.loadCourseDraftForFormFilling();
		for(CourseMeeting courseMeeting : course.meetingHistory){
			if(courseMeeting._id.equals(meetingId)){
				meeting = courseMeeting;
				break;
			}
		}
		if(meeting == null) {
			meeting = new CourseMeeting();
			meeting.presentMembers_ids = new ArrayList<String>();
		}
		return ok(courseMeetingDetails.render(UsersService.getUser(session().get("userName")), 
												session().get("role"), 
												meetingForm.fill(meeting), 
												meeting, 
												UsersService.getUsersList(), 
												courseId,
												meetingId));
	}
	
	// Save course meeting **********************************************************************************************************************
	public static Result saveCourseMeeting(String courseId, String meetingId){
		Form<CourseMeeting> boundForm = meetingForm.bindFromRequest();
		return CoursesService.saveCourseMeeting(boundForm, courseId, meetingId);
	}
	
	// Delete course meeting ********************************************************************************************************************
	public static Result deleteCourseMeeting(String courseId, String meetingId){
		return CoursesService.deleteMeeting(courseId, meetingId);
	}
	
	
}
