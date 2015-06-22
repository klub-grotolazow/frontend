package controllers;

/*
 * @Author(name="Lukas Pecak")
 */


import com.google.gson.Gson;

import models.Auth;
import models.Course;
import models.SystemRoleEnum;
import models.User;
import models.UserAccount;
import play.*;
import play.api.libs.Codecs;
import play.data.Form;
import play.libs.ws.WSResponse;
import play.mvc.*;
import service.RestService;
import service.SecurityService;
import service.UsersService;
import utils.Messages;
import utils.StatusCodes;
import utils.Utils;
import views.html.index;
import views.html.login;
import views.html.signup;
import views.html.userInfo;
import views.html.users.userDetails;


@SuppressWarnings("unused")
public class Application extends Controller {
	private static Form<User> userForm = Form.form(User.class);
	private static Form<Auth> authForm = Form.form(Auth.class);
	
	
	//Register a  new user *******************************************************************************************************************
	public static Result signup(){
		Form<User> boundForm = userForm.bindFromRequest();
		if(boundForm.hasErrors()){
			flash().put(Messages.WARNING, Messages.WARNING_SIGNUP + boundForm.globalErrors());
			ok(signup.render(boundForm));
		}
		User user = null;
		try{
			user = boundForm.get();
		}catch(Exception exception){
			flash().put(Messages.WARNING, Messages.WARNING_SIGNUP + boundForm.globalErrors());
			ok(signup.render(boundForm));
		}
		
		WSResponse response = null;
		try{
			response = RestService.callRESTsignup(user);
		}catch(Exception exception){
			flash().put(Messages.ERROR, Messages.ERROR_SIGNUP + exception);
			return badRequest(signup.render(userForm));
		}
		if(StatusCodes.CREATED == response.getStatus()){
			flash().put(Messages.SUCCESS, Messages.SUCCESS_SIGNUP + user.firstName + " " + user.lastName);
			return index();
		}else{
			/*if(StatusCodes.UNAUTHORIZED == response.getStatus()){
				flash().put(Messages.WARNING, Messages.WARNING_SIGNUP);
				return ok(signup.render(boundForm));
			}*/
			flash().put(Messages.ERROR, Messages.ERROR_SIGNUP + response.getStatus() + " " + response.getStatusText());
			return badRequest("HTTP > " + response.getStatus() + " " + response.getStatusText() + new Gson().toJson(user));
		}
	}
	
	//Register a  new user *******************************************************************************************************************
	public static Result showSignup(){
		return ok(signup.render(userForm));
	}
	
	
	// Show the info page at startup ***********************************************************************************************************
	public static Result index() {
    	response().setContentType("text/html; charset=UTF-8");
        return ok(index.render(UsersService.getUser(session().get("userName")), 
        						session().get("role")));
    }
	
	public static Result index2(String id) {
		Form<Course> courseForm = Form.form(Course.class).bindFromRequest();
    	response().setContentType("text/html; charset=UTF-8");
        return ok(index.render(UsersService.getUser(session().get("userName")), 
        						session().get("role") + id + courseForm.get().name));
    }
    
    // Show the user info page *****************************************************************************************************************
	public static Result account() {
    	response().setContentType("text/html; charset=UTF-8");
        return ok(userDetails.render(UsersService.getUser(session().get("userName")),
        								session().get("role"), 
        								Form.form(User.class).fill(UsersService.getUser(session().get(Utils.CURRENT_USER_ID))),
        								session().get(Utils.CURRENT_USER_ID)));
    }

    // Show the login form ********************************************************************************************************************
    public static Result login(){
    	Form<UserAccount> loginForm = Form.form(UserAccount.class);
    	return ok(login.render(UsersService.getUser(session().get("userName")), 
    							session().get("role"),authForm));
    }
    
    // Authenticate the user *******************************************************************************************************************
    public static Result authenticate() {
        //Form<UserAccount> loginForm = Form.form(UserAccount.class).bindFromRequest();
    	Form<Auth> boundForm = authForm.bindFromRequest();
        if (boundForm.hasErrors()) {
        	flash().put(Messages.WARNING, Messages.CORRECT_FORM);
            return badRequest(login.render(UsersService.getUser(session().get("userName")), 
            					session().get("role"), boundForm));
        }
        //UserAccount validatedUser = validate(loginForm.get().userName, Codecs.md5(loginForm.get().passwordHash.getBytes()));
        Auth auth = boundForm.get();
        auth.passwordHash = Codecs.md5(auth.passwordHash.getBytes());
        WSResponse response = RestService.callRESTlogin(auth);
        //return ok(response.getStatus() + " " + response.getStatusText() + " > " + response.getBody());
        if(response == null){
        	flash().put(Messages.ERROR, Messages.ERROR_LOGIN);
        	return redirect(routes.Application.login());
        }
        if(StatusCodes.OK == response.getStatus()){ 
        	User user = new Gson().fromJson(response.getBody(), User.class);
        	SecurityService.setupSessionAuthData(user, response.getHeader(RestService.AUTHORIZATION));
        	flash(Messages.SUCCESS, Messages.SUCCESS_LOGIN + user.firstName + " !"); 
        	return redirect(routes.Application.index());
        }
        else {
        	flash().put(Messages.ERROR, Messages.ERROR_LOGIN);
        	return redirect(routes.Application.login());
        }
    }
    
   /* // Validate the users data ******************************************************************************************************************
    public static UserAccount validate(String email, String password) {
    	UserAccount authenticatedUser = UsersService.authenticate(email, password);
        if (authenticatedUser == null) {	// is not authenticated
          return null;
        }
        return authenticatedUser;												// is authenticated
    }*/
    
    // Logout user ******************************************************************************************************************************
    public static Result logout() {
        session().clear();
        flash(Messages.SUCCESS, Messages.LOGOUT_SUCCESS);
        return redirect(routes.Application.login());
    }
}
