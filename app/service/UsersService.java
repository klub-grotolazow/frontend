package service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import utils.Messages;
import utils.StatusCodes;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

import models.User;

/*
 * @Author(name="Lukas Pecak")
 */

public class UsersService {
	
	//Get the list of all users
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
	
	//Delete the user with the given id
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
	
	//Get user by id
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
}
