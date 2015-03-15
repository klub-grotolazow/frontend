package service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import play.data.Form;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Messages;
import utils.StatusCodes;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

import models.User;

import views.html.users.userDetails;
import views.html.users.usersList;

/*
 * @Author(name="Lukas Pecak")
 */

public class UsersService {
	
	//Get the list of all users *******************************************************************************************************************************************************
	public static List<User> getUsersList(){
		WSResponse response = null;
		List<User> resultList = new ArrayList<User>();
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_USERS_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		Gson gson = new Gson();
		User[] users = gson.fromJson(response.getBody(), User[].class);
		for(int i=0; i<users.length; i++){
			resultList.add(users[i]);
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_USERS + exception);
		}
		return resultList;
	}
	
	//Delete the user with the given id ***********************************************************************************************************************************************
	public static void deleteUser(String id){
		WSResponse response = null;
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.DELETE_USER_URL+id)
										.delete();
		response = result.get(10000);
		if(response.getStatus() == StatusCodes.OK){
			User user = new Gson().fromJson(response.getBody(), User.class);
			Controller.flash().put(Messages.SUCCESS, Messages.SUCCESS_DELETED + user.firstName + " " + user.lastName);
		} else{
			Controller.flash().put(Messages.ERROR, Messages.ERROR_DELETE);
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_USERS + exception);
		}
	}
	
	//Get user by id ******************************************************************************************************************************************************************
	public static User getUser(String id){
		WSResponse response = null;
		User user = null;
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_ONE_USER+id)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		user = new Gson().fromJson(response.getBody(), User.class);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_USERS + exception);
		}
		return user;
	}
	
	//Save user ***********************************************************************************************************************************************************************
	public static Result saveUser(Form<User> boundForm, String id){
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return Controller.badRequest(userDetails.render(boundForm, id));
		}
		User user = boundForm.get();
		if(user.currentCourses_ids == null) user.currentCourses_ids = new ArrayList<String>();
		if(user.hiredEquipments_ids == null) user.hiredEquipments_ids = new ArrayList<String>();
		if(user.payments_ids == null) user.payments_ids = new ArrayList<String>();
		Gson gson = new Gson();
		String request = gson.toJson(user, User.class);
		WSResponse response = null;
		
		//The case for new user save --------------------------------------------------------------
		if((id == null) || id.length() == 0){

		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.POST_USER_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.post(request);
		response = result.get(10000);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER + exception);
			return Controller.badRequest(userDetails.render(boundForm,id));
		}
		if(response.getStatus() == StatusCodes.CREATED){
			Controller.flash(Messages.SUCCESS, Messages.SUCCESS_ADING_USER);
			return Controller.ok(usersList.render(UsersService.getUsersList()));
		} else{
			Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
			return Controller.badRequest(userDetails.render(boundForm,id));
		}
		} 
		
		//The case for user update -----------------------------------------------------------------
		else{
			user._id = id;
			request = gson.toJson(user, User.class);
			try{
			Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.PUT_USER_URL+id)
											.setContentType(Urls.CONTENT_TYPE_JSON)
											.put(request);
			response = result.get(10000);
			} catch(Exception exception){
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER + exception);
				return Controller.badRequest(userDetails.render(boundForm,id));
			}
			if(response.getStatus() == StatusCodes.OK){
				Controller.flash(Messages.SUCCESS, Messages.SUCCESS_UPDATE_USER);
				return Controller.ok(usersList.render(UsersService.getUsersList()));
			} else{
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
				return Controller.badRequest(userDetails.render(boundForm,id));
			}
		}	
	}
	

}
