package service;

import models.User;
import models.UserAccount;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import utils.Messages;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

/*
 * @Author(name="Lukas Pecak")
 */

public class SecurityService {
	
	public static String getSignupHeader(User user){
		String header = user.auth.userName + "&" + user.auth.passwordHash;
		return header;
	}
	
	// Get the authentication data from server **************************************************************************
	public static User authenticateUser(String userName, String passwordHash){	//user name can be users email
		UserAccount userAccount = new UserAccount();
		userAccount.userName = userName;
		userAccount.passwordHash = passwordHash;
		WSResponse response = null;
		User user = null;
		String json = new Gson().toJson(userAccount);
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_ONE_USER) // change the url to ask the server for user account
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.post(json);
		response = result.get(10000);
		user = new Gson().fromJson(response.getBody(), User.class);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_AUTHENTICATING_USER + exception);
		}
		return user;
	}
}
