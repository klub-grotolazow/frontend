package controllers;

/*
 * @Author(name="Lukas Pecak")
 */


import models.SystemRoleEnum;
import models.User;
import models.UserAccount;
import play.*;
import play.api.libs.Codecs;
import play.data.Form;
import play.mvc.*;
import service.UsersService;
import utils.Messages;
import views.html.index;
import views.html.login;
import views.html.userInfo;

@SuppressWarnings("unused")
public class Application extends Controller {
	
	// Show the info page at sturtup ***********************************************************************************************************
	public static Result index() {
    	response().setContentType("text/html; charset=UTF-8");
        return ok(index.render(UsersService.getUser(session().get("userName")), session().get("role")));
    }
    
    // Show the user info page *****************************************************************************************************************
	public static Result account() {
    	response().setContentType("text/html; charset=UTF-8");
        return ok(userInfo.render(UsersService.getUser(session().get("userName")), session().get("role")));
    }

    // Show the loging form ********************************************************************************************************************
    public static Result login(){
    	Form<UserAccount> loginForm = Form.form(UserAccount.class);
    	return ok(login.render(UsersService.getUser(session().get("userName")), session().get("role"),loginForm));
    }
    
    // Authenticate the user *******************************************************************************************************************
    public static Result authenticate() {
        Form<UserAccount> loginForm = Form.form(UserAccount.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(UsersService.getUser(session().get("userName")), session().get("role"), loginForm));
        }
        UserAccount validatedUser = validate(loginForm.get().userName, Codecs.md5(loginForm.get().passwordHash.getBytes()));
        if(validatedUser == null){  	
        	flash(Messages.ERROR, Messages.ERROR_USER_NAME_PASSWORD); 
        	return forbidden(login.render(UsersService.getUser(session().get("userName")), session().get("role"), loginForm));
        }
        else {
            session().clear();
            session("userName", validatedUser.userId);
            session("role", validatedUser.systemRole.toString());
            User currentUser = UsersService.getUser(session().get("userName"));
            flash(Messages.SUCCESS, Messages.getLogginSuccess(currentUser.firstName + " " + currentUser.lastName, session().get("role")));//Messages.getLogginSuccess(loginForm.get().userName, SystemRoleEnum.Instructor.toString()));
            return redirect(routes.Application.index());
        }
    }
    
    // Validate the users data ******************************************************************************************************************
    public static UserAccount validate(String email, String password) {
    	UserAccount authenticatedUser = UsersService.authenticate(email, password);
        if (authenticatedUser == null) {	// is not authenticated
          return null;
        }
        return authenticatedUser;												// is authenticated
    }
    
    // Logout user ******************************************************************************************************************************
    public static Result logout() {
        session().clear();
        flash(Messages.SUCCESS, Messages.LOGOUT_SUCCESS);
        return redirect(routes.Application.login());
    }
}
