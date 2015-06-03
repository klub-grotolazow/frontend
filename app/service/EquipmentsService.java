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
	
	// Return a equipment with specialized id
	public static Equipment getEquipment(String id){
		WSResponse response = null;
		Equipment equipment = null;
		try{
			response = RestService.callREST(Urls.GET_EQUIPMENT_URL+id, null, null, true, RestService.restServiceEnum.GET);
			equipment = new Gson().fromJson(response.getBody(), Equipment.class);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_EQUIPMENTS + exception);
		}
		return equipment;
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
			Controller.flash().put(Messages.SUCCESS, Messages.SUCCESS_DELETED_EQUIPMENT + equipment.name);
		} else{
			Controller.flash().put(Messages.ERROR, Messages.ERROR_DELETING_EQUIPMENT);
		}
		} catch(Exception exception){	
			Controller.flash(Messages.ERROR, Messages.ERROR_DELETING_EQUIPMENT + exception);
		}
	}
		
	//Save equipment ******************************************************************************************************************
	public static int saveEquipment(Equipment equipment) throws Exception{
		if(equipment.allowedFor == null) equipment.allowedFor = new ArrayList<Equipment.allowedForEnum>();
		if(equipment.hireHistory == null) equipment.hireHistory = new ArrayList<EquipmentHire>();
		WSResponse response = null;
		Gson gson = new Gson();
		String json = gson.toJson(equipment);
		System.out.println(json);
		response = RestService.callREST(Urls.SAVE_EQUIPMENTS_URL,json , Equipment.class, true, RestService.restServiceEnum.POST);
		return response.getStatus();

	}
	
	//Update equipment ******************************************************************************************************************
	public static int updateEquipment(Equipment equipment, String id){
		WSResponse response = null;
		equipment._id = id;
		if(equipment.allowedFor == null) equipment.allowedFor = new ArrayList<Equipment.allowedForEnum>();
		if(equipment.hireHistory == null) equipment.hireHistory = new ArrayList<EquipmentHire>();
		System.out.println(new Gson().toJson(equipment));
		response = RestService.callREST(Urls.PUT_EQUIPMENTS_URL+id, new Gson().toJson(equipment), Equipment.class, true, RestService.restServiceEnum.PUT);
		return response.getStatus();
	}
}
