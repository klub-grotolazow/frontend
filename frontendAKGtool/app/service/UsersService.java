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
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

import models.User;

/*
 * @Author(name="Lukas Pecak")
 */

public class UsersService {
	
	public static List<User> getUsersList(){
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
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_USERS);
		}
		return resultList;
	}
	
}
