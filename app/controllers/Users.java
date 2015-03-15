package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.ArrayList;

import org.json.JSONException;

import com.google.gson.Gson;

import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import static play.libs.F.Promise;
import models.User;
import models.UserAccount;
import play.data.Form;
import play.mvc.Controller;
import service.UsersService;
import utils.Messages;
import utils.StatusCodes;
import utils.Urls;
import utils.Utils;
import views.html.index;
import views.html.users.usersList;
import views.html.users.userDetails;
import views.html.users.signIn;
import views.html.users.userOverview;

public class Users extends Controller {
	private static Form<User> userForm = Form.form(User.class);
	private static final Form<UserAccount> userAccountForm = Form.form(UserAccount.class);
	
	//Show user overview **********************************************************************************************************************
	public static Result showUser(String id){
		try{
			Controller.flash().put(Messages.ERROR, Messages.USER_NOT_FOUND);
			return ok(userOverview.render());
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.ERROR_ADDING_USER + exception);
			return badRequest(index.render());
		}
	}
	
	//Edit user *******************************************************************************************************************************
	public static Result editUser(String id){
		User user = UsersService.getUser(id);
		if(user == null){
			Controller.flash().put(Messages.ERROR, Messages.USER_NOT_FOUND);
			return ok(usersList.render(UsersService.getUsersList()));
		} else{
			Form<User> form = userForm.fill(user);
			return ok(userDetails.render(form, id));
		}
	}
	
	//Deleting user ***************************************************************************************************************************
	public static Result deleteUser(String id){
		UsersService.deleteUser(id);	
		return ok(usersList.render(UsersService.getUsersList()));
	}
	
	//Displaying the users list ***************************************************************************************************************
	public static Result getUsersList() throws JSONException {
		try{
			return ok(usersList.render(UsersService.getUsersList()));
		} catch(Exception exception){
			return badRequest();
		}
    }
	
	//Saving user in beckend api ************************************************************************************************************** 
	public static Result saveUser(String id){
		Form<User> boundForm = userForm.bindFromRequest();
		return UsersService.saveUser(boundForm, id);
	}
	
	//Adding new user *************************************************************************************************************************
	public static Result newUser(){
		return ok(userDetails.render(userForm,null));
	}
	
	//Sign in user ****************************************************************************************************************************
	public static Result signIn(){
		return ok(signIn.render(userAccountForm));
	}
	
	//Verify user *****************************************************************************************************************************
	public static Result verifyUser(){
		return TODO;
	}
	
	
	
	
}
