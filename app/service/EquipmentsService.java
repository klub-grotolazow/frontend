package service;

import java.util.ArrayList;
import java.util.List;

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

import models.Course;
import models.Equipment;
import models.EquipmentHire;
import models.User;
import views.html.equipments.equipmentDetails;

/*
 * @Author(name="Lukas Pecak")
 */

public class EquipmentsService {
	
	// Returning the list of equipments in warehouse*********************************************************************
	public static List<Equipment> getEquipmentsList(){
		WSResponse response = null;
		List<Equipment> resultList = new ArrayList<Equipment>();
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_EQUIPMENTS_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		Gson gson = new Gson();
		Equipment[] equipments = gson.fromJson(response.getBody(), Equipment[].class);
		for(int i=0; i<equipments.length; i++){
			resultList.add(equipments[i]);
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_EQUIPMENTS + exception);
		}
		return resultList;
	}
	
	//Delete the equipment with the given id *****************************************************************************************
	public static void deleteEquipment(String id){
		WSResponse response = null;
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.DELETE_EQUIPMENT_URL+id)
										.delete();
		response = result.get(10000);
		if(response.getStatus() == StatusCodes.OK){
			Equipment equipment = new Gson().fromJson(response.getBody(), Equipment.class);
			Controller.flash().put(Messages.SUCCESS, Messages.SUCCESS_DELETED + equipment.name);
		} else{
			Controller.flash().put(Messages.ERROR, Messages.ERROR_DELETE);
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_DELETING_EQUIPMENT + exception);
		}
	}
		
	//Save user ******************************************************************************************************************
	public static Result saveUser(Form<Equipment> boundForm, String id){
		/*if(boundForm.hasErrors()) {
			Controller.flash(Messages.WARNING, Messages.CORRECT_FORM);
			return Controller.badRequest(equipmentDetails.render(UsersService.getUser(Controller.session().get("userName")), 
											Controller.session().get("role"),
											boundForm,
											id));
		}
		Equipment equipment = boundForm.get();
		if(equipment.allowedFor == null) equipment.allowedFor = new ArrayList<Equipment.allowedForEnum>();
		if(equipment.hireHistory == null) equipment.hireHistory = new ArrayList<EquipmentHire>();
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
			return Controller.badRequest(userDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,id));
		}
		if(response.getStatus() == StatusCodes.CREATED){
			Controller.flash(Messages.SUCCESS, Messages.SUCCESS_ADING_USER);
			return Controller.ok(usersList.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), UsersService.getUsersList()));
		} else{
			Controller.flash(Messages.ERROR, Messages.ERROR_ADDING_USER_DETAILS + response.getStatus() +" "+ response.getStatusText());
			return Controller.badRequest(userDetails.render(UsersService.getUser(Controller.session().get("userName")), Controller.session().get("role"), boundForm,id));
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
		}	*/
		return Controller.TODO;
	}
}
