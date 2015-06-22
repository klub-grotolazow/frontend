package controllers;

/*
 * @Author(name="Lukas Pecak")
 */


import org.json.JSONException;

import play.mvc.Result;
import play.mvc.Security;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import service.UsersService;
import utils.Messages;
import views.html.index;
import views.html.users.usersList;
import views.html.users.userDetails;
import views.html.users.userOverview;

@Security.Authenticated(Secured.class)
public class Users extends Controller {
	private static Form<User> userForm = Form.form(User.class);
	
	
	//Show user overview **********************************************************************************************************************
	public static Result showUser(String id){
		try{
			Controller.flash().put(Messages.ERROR, Messages.USER_NOT_FOUND);
			return ok(userOverview.render());
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.ERROR_ADDING_USER + exception);
			return badRequest(index.render(UsersService.getUser(session().get("userName")), session().get("role")));
		}
	}
	
	//Edit user *******************************************************************************************************************************
	public static Result editUser(String id){
		if(Secured.isSuperUser()){	
			User user = UsersService.getUser(id);
			if(user == null){
				Controller.flash().put(Messages.ERROR, Messages.USER_NOT_FOUND);
				return ok(usersList.render(UsersService.getUser(session().get("userName")), session().get("role"), UsersService.getUsersList()));
			} else{
				Form<User> form = userForm.fill(user);
				return 
						ok(userDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), form, id));
			}
		} else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	//Deleting user ***************************************************************************************************************************
	public static Result deleteUser(String id){
		if(Secured.isSuperUser()){	
			UsersService.deleteUser(id);	
			return ok(usersList.render(UsersService.getUser(session().get("userName")), session().get("role"), UsersService.getUsersList()));
		}  else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	//Displaying the users list ***************************************************************************************************************
	public static Result getUsersList() throws JSONException {
		if(Secured.isSuperUser()){	
			try{
				return ok(usersList.render(UsersService.getUser(session().get("userName")), session().get("role"), UsersService.getUsersList()));
			} catch(Exception exception){
				return badRequest();
			}
		} else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
    }
	
	//Saving user in beckend api ************************************************************************************************************** 
	public static Result saveUser(String id){
		if(Secured.isSuperUser()){
			Form<User> boundForm = userForm.bindFromRequest();
			return UsersService.saveUser(boundForm, id);
		} else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	//Adding new user *************************************************************************************************************************
	public static Result newUser(){
		if(Secured.isSuperUser()){
			return ok(userDetails.render(UsersService.getUser(session().get("userName")), session().get("role"), userForm,null));
		} else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	

	
	
	
}
