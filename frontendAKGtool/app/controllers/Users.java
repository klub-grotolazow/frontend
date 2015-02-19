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
	private static final Form<User> userForm = Form.form(User.class);
	private static final Form<UserAccount> userAccountForm = Form.form(UserAccount.class);
	
	//Displaying the users list
	public static Result getUsersList() throws JSONException {
		/*
		WSResponse response = null;
		JSONArray array;
		List<User> resultList = new ArrayList<User>();
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_USERS_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		JSONObject object = new JSONObject(Utils.wrapJsonTable(response.getBody()));
		array = object.getJSONArray(Utils.USERS_JSON_TABLE);
		for(int i=0;i<array.length();i++){
			User user = new Gson().fromJson(array.get(i).toString(), User.class);
			resultList.add(user);
		}
		} catch(Exception exception){
			flash(Messages.ERROR, Messages.CANT_LOAD_USERS);
			return badRequest(index.render());
		}
		*/
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
