package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.ArrayList;

import models.CourseMeeting;

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
			course = Utils.prepareForHtml(course);
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
			course = new Course();
			course = Utils.prepareForHtml(course);
			return ok(courseDetails.render(UsersService.getUser(session().get("userName")), 
											session().get("role"), 
											courseForm, 
											course, 
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
	public static Result loadCourseDraft(String courseId){
		Gson gson = new Gson();
		Course course;
		if(session().containsKey(Utils.DRAFT_COURSE)){
			course = gson.fromJson(session().get(Utils.DRAFT_COURSE), Course.class);
			course = Utils.prepareForHtml(course);
			return ok(courseDetails.render(UsersService.getUser(session().get("userName")), 
											session().get("role"), 
											courseForm.fill(course), 
											course, 
											UsersService.getUsersList(), 
											courseId));
		} else {
			Course emptyCourse = new Course();
			emptyCourse = Utils.prepareForHtml(emptyCourse);
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
				Course gettedCourse = CoursesService.getCourse(courseId);
				gettedCourse = Utils.prepareForHtml(gettedCourse);
				return badRequest(courseDetails.render(UsersService.getUser(session().get("userName")), 
														session().get("role"), 
														boundForm, 
														gettedCourse, 
														UsersService.getUsersList(), 
														courseId));
			}
			Course draft = CoursesService.loadCourseDraft();
			if(draft == null) draft = new Course();
 			if(draft.name != course.name) draft.name = course.name;
			if(draft.description != course.description) draft.description = course.description;
			if(draft.manager_id != course.manager_id) draft.manager_id = course.manager_id;
			if(draft.members_ids == null) draft.members_ids = new ArrayList<String>();
			draft.members_ids = Utils.toStringList(course.members_ids.get(0));
			CoursesService.saveCourseDraft(draft, courseId);
			if(!(Controller.session().containsKey(Utils.DRAFT_COURSE) || (Controller.session().get(Utils.DRAFT_COURSE) == ""))){
				flash().put(Messages.ERROR, Messages.ERROR_SAVING_COURSE_DRAFT);
				Course gettedCourse = CoursesService.getCourse(courseId);
				gettedCourse = Utils.prepareForHtml(gettedCourse);
				return badRequest(courseDetails.render(UsersService.getUser(session().get("userName")), 
														session().get("role"), 
														boundForm, 
														gettedCourse, 
														UsersService.getUsersList(), 
														courseId));
			} else{
				return ok(courseMeetingDetails.render(UsersService.getUser(session().get("userName")), 
														session().get("role"), 
														meetingForm, 
														new CourseMeeting(),
														UsersService.getUsersList(),
														UsersService.getCourseMembers(draft),
														draft.members_ids,
														courseId,
														""));
			}
		} else {
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.index());
		}
	}	
		
	// Load the course meeting details view *****************************************************************************************************
	public static Result editCourseMeeting(String courseId, String meetingId){
		Form<Course> boundForm = courseForm.bindFromRequest();
		Course course = boundForm.get();
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return badRequest(courseDetails.render(UsersService.getUser(session().get("userName")), 
													session().get("role"), 
													boundForm, 
													boundForm.get(), 
													UsersService.getUsersList(), 
													courseId));
		}
		Course draft = CoursesService.loadCourseDraft();
		if(draft == null) draft = new Course();
			if(draft.name != course.name) draft.name = course.name;
		if(draft.description != course.description) draft.description = course.description;
		if(draft.manager_id != course.manager_id) draft.manager_id = course.manager_id;
		if(draft.members_ids != null){
			draft.members_ids.clear();
			draft.members_ids = Utils.toStringList(course.members_ids.get(0));
		} else{
			draft.members_ids = new ArrayList<String>();
		}
		CoursesService.saveCourseDraft(draft, courseId);
		
		CourseMeeting meeting = null;
		course = draft;
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
		String presentMenbersString = Utils.toCSS(meeting.presentMembers_ids);
		meeting.presentMembers_ids.clear();
		meeting.presentMembers_ids.add(presentMenbersString);
		if(meeting.presentMembers_ids.size() == 0) meeting.presentMembers_ids.add("");
		meetingForm.fill(meeting);
		return ok(courseMeetingDetails.render(UsersService.getUser(session().get("userName")), 
												session().get("role"), 
												meetingForm.fill(meeting), 
												meeting, 
												UsersService.getUsersList(),
												UsersService.getCourseMembers(course),
												course.members_ids, 
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
		Form<Course> boundForm = courseForm.bindFromRequest();
		return CoursesService.deleteMeeting(boundForm, courseId, meetingId);
	}
	
	
}
