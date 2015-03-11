package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import org.json.JSONException;

import com.fasterxml.jackson.databind.JsonNode;


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
import views.html.users.usersList;
import views.html.users.userDetails;
import views.html.users.signIn;

public class Users extends Controller {
	private static Form<User> userForm = Form.form(User.class);
	private static final Form<UserAccount> userAccountForm = Form.form(UserAccount.class);
	
	//Edit user
	public static Result editUser(String id){
		User user = UsersService.getUser(id);
		if(user == null){
			Controller.flash().put(Messages.ERROR, Messages.USER_NOT_FOUND);
			return ok(usersList.render(UsersService.getUsersList()));
		} else{
			session().put(Messages.EDIT_USER_KEY, id.toString());
			Form<User> form = userForm.fill(user);
			return ok(userDetails.render(form));
		}
	}
	
	//Deleting user
	public static Result deleteUser(String id){
		UsersService.deleteUser(id);	
		return ok(usersList.render(UsersService.getUsersList()));
	}
	
	//Displaying the users list
	public static Result getUsersList() throws JSONException {
		try{
			return ok(usersList.render(UsersService.getUsersList()));
		} catch(Exception exception){
			return badRequest();
		}
    }
	
	//Saving user in database 
	public static Result saveUser(){
		Form<User> boundForm = userForm.bindFromRequest();
		if(boundForm.hasErrors()) {
			flash(Messages.WARNING, Messages.CORRECT_FORM);
			return badRequest(userDetails.render(boundForm));
		}
		User user = boundForm.get();
		JsonNode json = Json.toJson(user);
		WSResponse response = null;
		
		//The case for new user save
		if(!session().containsKey(Messages.EDIT_USER_KEY)){
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.POST_USER_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.post(json);
		response = result.get(10000);
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.ERROR_ADDING_USER);
			return badRequest(userDetails.render(boundForm));
		}
		if(response.getStatus() == StatusCodes.CREATED){
			flash(Messages.SUCCESS, Messages.SUCCESS_ADING_USER);
			return ok(usersList.render(UsersService.getUsersList()));
		} else{
			flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
			return badRequest(userDetails.render(boundForm));
		}
		} 
		
		//The case for user update
		else{
			user._id = session().get(Messages.EDIT_USER_KEY);
			json = Json.toJson(user);
			try{
			Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.PUT_USER_URL+session().get(Messages.EDIT_USER_KEY))
											.setContentType(Urls.CONTENT_TYPE_JSON)
											.put(json);
			response = result.get(10000);
			session().remove(Messages.EDIT_USER_KEY);
			} catch(Exception exception){
				flash(Messages.ERROR, Messages.ERROR_ADDING_USER);
				return badRequest(userDetails.render(boundForm));
			}
			if(response.getStatus() == StatusCodes.OK){
				flash(Messages.SUCCESS, Messages.SUCCESS_UPDATE_USER);
				return ok(usersList.render(UsersService.getUsersList()));
			} else{
				flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS);
				return badRequest(userDetails.render(boundForm));
			}
		}	
	}
	
	//Adding new user
	public static Result newUser(){
		return ok(userDetails.render(userForm));
	}
	
	//Sign in user 
	public static Result signIn(){
		return ok(signIn.render(userAccountForm));
	}
	
	//Verify user
	public static Result verifyUser(){
		return TODO;
	}
	
	
	
	
}
