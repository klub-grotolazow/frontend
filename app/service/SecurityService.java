package service;

import java.util.List;

import models.Auth;
import models.SystemRoleEnum;
import models.User;
import models.UserAccount;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import utils.Messages;
import utils.StatusCodes;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

/*
 * @Author(name="Lukas Pecak")
 */

public class SecurityService {	
	public static final String 	USER_ID = 			"userId";
	public static final String	USER_NAME = 		"userName";
	public static final String 	TOKEN = 			"token";
	public static final String 	ROLES = 			"roles";
	public static final int 	USER_NAME_INDEX = 	0;
	public static final int 	TOKEN_INDEX = 		1;
	public static final int 	ROLES_INDEX = 		2;
	public static final String 	EMPTY = 			"";
	
	
	// Check if has role
	public static boolean checkRole(SystemRoleEnum role){
		List<String> roles = Utils.getRolesList();
		if(roles.contains(role.toString())){
			return true;
		}else{
			return false;
		}
	}
	
	// Generate authorization header
	public static String getAuthHeader(){
		String header = Utils.getCurrentUserName() + "&" + Utils.getCurrentUserToken();
		return header;
	}	
	
	// Generate the Sign up header
	public static String getSignupHeader(User user){
		String header = user.auth.userName + "&" + user.auth.passwordHash;
		return header;
	}
	
	// Generate the logging header
	public static String getLoggingHeader(Auth auth){
		String header = auth.userName + "&" + auth.passwordHash;
		return header;
	}
	
	public static void setupSessionAuthData(User user, String authHeader){
		List<String> authList = null;
		if((authHeader != null) && (authHeader.length() > ROLES_INDEX)){
			authList = Utils.toAuthList(authHeader);
		}
		Controller.session().clear();
		Controller.session().put(USER_ID, user._id);
		if(authList.size() > USER_NAME_INDEX) Controller.session().put(USER_NAME, authList.get(USER_NAME_INDEX));
		if(authList.size() > TOKEN_INDEX) Controller.session().put(TOKEN, authList.get(TOKEN_INDEX));
		if(authList.size() > ROLES_INDEX){
			Controller.session().put(ROLES, authList.get(ROLES_INDEX));
		}else{
			Controller.session().put(ROLES, EMPTY);
		}
	}
	
	/*// Get the authentication data from server **************************************************************************
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
	}*/
	
	public static void isForbidden(WSResponse response){
		if(response.getStatus() == StatusCodes.FORBIDDEN){
			Controller.flash().put(Messages.FORBIDDEN, Messages.FORBIDDEN_REMOTE_ACCESS);
		}
	}
}
