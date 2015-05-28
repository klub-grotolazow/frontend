package service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import play.api.libs.Codecs;
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



import models.Course;
import models.SystemRoleEnum;
import models.User;
import models.UserAccount;
import views.html.users.userDetails;
import views.html.users.usersList;

/*
 * @Author(name="Lukas Pecak")
 */

public class UsersService {
	
	// Authenticat the user ****************************************************************************************************
	public static UserAccount authenticate(String userName, String passwordHash) {
        //fake kode
		UserAccount account = new UserAccount();
		account.userId = "5517c70be4b0cb9971c1719c";
		account.userName = "root";
		account.systemRole = SystemRoleEnum.SuperUser;
		account.passwordHash = Codecs.md5(new String("root").getBytes());
		if(account.userName.equals(userName) && account.passwordHash.equals(passwordHash)) return account;
		
		account.userId = "552568ce397a10473066f356";
		account.userName = "lukaspecak";
		account.systemRole = SystemRoleEnum.SuperUser;
		account.passwordHash = "ca513602238bd1cda290fa1eb2a74ab1";
		if(account.userName.equals(userName) && account.passwordHash.equals(passwordHash)) return account;
		
		account.userId = "5517d4db07b4c25ec8c1ca07";
		account.userName = "instruktor";
		account.systemRole = SystemRoleEnum.Instructor;
		account.passwordHash = Codecs.md5(new String("instruktor").getBytes());
		if(account.userName.equals(userName) && account.passwordHash.equals(passwordHash)) return account;
		
		account.userId = "5517d55d07b4c25ec8c1ca09";
		account.userName = "kierownik";
		account.systemRole = SystemRoleEnum.CourseManager;
		account.passwordHash = Codecs.md5(new String("kierownik").getBytes());
		if(account.userName.equals(userName) && account.passwordHash.equals(passwordHash)) return account;
		
		account.userId = "5517d50307b4c25ec8c1ca08";
		account.userName = "student";
		account.systemRole = SystemRoleEnum.Student;
		account.passwordHash = Codecs.md5(new String("student").getBytes());
		if(account.userName.equals(userName) && account.passwordHash.equals(passwordHash)) return account;
		
		account.userId = "5517d5fd07b4c25ec8c1ca0a";
		account.userName = "magazyn";
		account.systemRole = SystemRoleEnum.WarehouseManager;
		account.passwordHash = Codecs.md5(new String("magazyn").getBytes());
		if(account.userName.equals(userName) && account.passwordHash.equals(passwordHash)) return account;
		
		return null;
    }
	
	//Get the list of all users ************************************************************************************************
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
	
	//Delete the user with the given id *****************************************************************************************
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
	
	//Get user by id ************************************************************************************************************
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
	
	//Save user ******************************************************************************************************************
	public static Result saveUser(Form<User> boundForm, String id){
		if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return Controller.badRequest(userDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm, id));
		}
		User user = boundForm.get();
		System.out.println("The user have value > " + user);
		if(user.currentCourses_ids == null) user.currentCourses_ids = new ArrayList<String>();
		if(user.hiredEquipments_ids == null) user.hiredEquipments_ids = new ArrayList<String>();
		if(user.payments_ids == null) user.payments_ids = new ArrayList<String>();
		//if(user.feeStatus == null) user.feeStatus = User.feeStatusEnum.OnTime;
		Gson gson = new Gson();
		System.out.println("After gson" + user);
		//String request = "{\"firstName\": \"Lukas\",\"lastName\": \"Pęcak\",\"email\": \"michal.kijania@gmail.com\",\"feeStatus\": \"OnTime\",\"hoursPoints\": 0,\"address\": {\"voivodeship\": \"Lesser Poland\",\"town\": \"Krakow\",\"street\": \"Bracka\",\"buildingNr\": 12,\"apartmentNr\": 2},\"currentCourses_ids\": [],\"hiredEquipments_ids\": [],\"payments_ids\": []}";   
		String request = gson.toJson(user, User.class);
		System.out.println("Request value > " + request);
		
		WSResponse response = null;
		System.out.println(request);
		
		//The case for new user save --------------------------------------------------------------
		if((id == null) || id.length() == 0){

		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.POST_USER_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.post(request);
		response = result.get(10000);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER + exception);
			return Controller.badRequest(userDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,id));
		}
		if(response.getStatus() == StatusCodes.CREATED){
			Controller.flash(Messages.SUCCESS, Messages.SUCCESS_ADING_USER);
			return Controller.ok(usersList.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), UsersService.getUsersList()));
		} else{
			Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
			return Controller.ok(request +" | "+ response.getBody());//return Controller.badRequest(userDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,id));
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
				return Controller.badRequest(userDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,id));
			}
			if(response.getStatus() == StatusCodes.OK){
				Controller.flash(Messages.SUCCESS, Messages.SUCCESS_UPDATE_USER);
				return Controller.ok(usersList.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), UsersService.getUsersList()));
			} else{
				Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
				return Controller.ok(request +" | "+ response.getBody());//return Controller.badRequest(userDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,id));
			}
		}	
	}
	
	//Get course members list
	public static List<User> getCourseMembers(Course course){
		List<User> result = new ArrayList<User>();
		List<User> usersList = getUsersList();
		if((usersList != null) && (course != null) && (course.members_ids != null)){
			for(User user : usersList){
				if(course.members_ids.contains(user._id)){
					result.add(user);
				}
			}
		}
		return result;
	}

}
