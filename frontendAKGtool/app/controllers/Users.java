package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import java.io.FileInputStream;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.F.Option;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import static play.libs.F.Function;
import static play.libs.F.Promise;
import models.Setting;
import models.User;
import models.UserAccount;
import play.data.Form;
import play.mvc.Controller;
import play.libs.ws.*;
import utils.SampleData;
import views.html.users.usersList;
import views.html.users.userDetails;
import views.html.users.signIn;

public class Users extends Controller {
	private static final String POST_USER_URL = "/users";
	private static final Form<User> userForm = Form.form(User.class);
	private static final Form<UserAccount> userAccountForm = Form.form(UserAccount.class);
	
	public static Result getUsersList() {
        return ok(usersList.render(SampleData.getSampleUsers(10)));
    }
	
	
	public static Result saveUser(){
		Form<User> boundForm = userForm.bindFromRequest();
		if(boundForm.hasErrors()) {
			flash("warning", "Please correct the form below.");
			return badRequest(userDetails.render(boundForm));
		}
		User user = boundForm.get();
		JsonNode json = Json.toJson(user);
		WSResponse response = null;
		String apiUrl;
		try{
			FileInputStream input = new FileInputStream("config.properties");
			java.util.Properties properties = new java.util.Properties();
			properties.load(input);
			Setting setting = new Setting();
			setting.apiUrl = properties.getProperty("apiUrl");
			apiUrl = setting.apiUrl;
		}catch(Exception exception){
			flash("error", "Can't load properties file !");
			apiUrl = "";
		}
		
		try{
		Promise<WSResponse> result = WS.url(apiUrl+POST_USER_URL).post(json);
		response = result.get(10000);
		} catch(Exception exception){
			flash("error","Error! Problem with adding user! Details : Cann not connect to server. Check if the server is running or the server url is crorrect.");
			return badRequest(userDetails.render(boundForm));
		}
		
		if(response.getStatus() == 201){
			flash("success", "Success! User added to list!");
			return ok(usersList.render(SampleData.getSampleUsers(10)));
		} else{
			flash("error","Error! Problem with adding user! Details :"+response.getStatus() +" "+ response.getStatusText());
			return badRequest(userDetails.render(boundForm));
		}
		
	}
	
	public static Result newUser(){
		return ok(userDetails.render(userForm));
	}
	
	
	public static Result signIn(){
		return ok(signIn.render(userAccountForm));
	}
	
	public static Result verifyUser(){
		return TODO;
	}
	
	
	
	
}
