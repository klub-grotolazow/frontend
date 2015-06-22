package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import utils.Messages;
import utils.StatusCodes;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

import controllers.Equipments;
import models.Equipment;
import models.EquipmentHire;

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
	
	//Get available equipment
	public static List<Equipment> getAvailableEquipment(){
		List<Equipment> resultList = new ArrayList<Equipment>();
		List<Equipment> allEquipment = getEquipmentsList();
		for(Equipment equipment : allEquipment){
			if(equipment.isAvailable) resultList.add(equipment);
		}
		return resultList;
	}
	
	//Get reserved equipment
	public static List<Equipment> getReservedEquipment(){
		List<Equipment> resultList = new ArrayList<Equipment>();
		List<Equipment> allEquipment = getEquipmentsList();
		for(Equipment equipment : allEquipment){
			if(equipment.isReserved) resultList.add(equipment);
		}
		return resultList;
	}
		
	//Get hired equipment
	public static List<Equipment> getHiredEquipment(){
		List<Equipment> resultList = new ArrayList<Equipment>();
		List<Equipment> allEquipment = getEquipmentsList();
		for(Equipment equipment : allEquipment){
			if(equipment.isHired) resultList.add(equipment);
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
		if(stateChanged(equipment)){
			if(equipment.isHired){                                               //Todo user id-s
				EquipmentHire hire = new EquipmentHire();
				hire.warehouseman_id = Utils.getCurrentUser()._id;
				hire.user_id = Utils.getCurrentUser()._id;
				hire.hireDate = new Date().toString();
				equipment.hireHistory.add(hire);
			}
			if(equipment.isReserved){
				EquipmentHire hire = new EquipmentHire();
				hire.warehouseman_id = Utils.getCurrentUser()._id;
				hire.user_id = Utils.getCurrentUser()._id;
				hire.reservationDate = new Date().toString();
				equipment.hireHistory.add(hire);
			}
		}
		WSResponse response = null;
		Gson gson = new Gson();
		String json = gson.toJson(equipment);
		response = RestService.callREST(Urls.SAVE_EQUIPMENTS_URL,json , Equipment.class, true, RestService.restServiceEnum.POST);
		return response.getStatus();

	}
	
	//Update equipment ******************************************************************************************************************
	public static int updateEquipment(Equipment equipment, String id) throws Exception{
		WSResponse response = null;
		equipment._id = id;
		if(equipment.hireHistory.size() == 0){
			equipment.hireHistory = EquipmentsService.getEquipment(id).hireHistory;
		}
		if(stateChanged(equipment)){											//Todo user id-s and equipment condition
			if(equipment.isHired){
				EquipmentHire hire = new EquipmentHire();
				hire.warehouseman_id = Utils.getCurrentUser()._id;
				hire.user_id = Utils.getCurrentUser()._id;
				hire.hireDate = new Date().toString();
				equipment.hireHistory.add(hire);
			}
			if(equipment.isReserved){
				if(wasHired()){
					EquipmentHire hire = new EquipmentHire();
					hire.warehouseman_id = Utils.getCurrentUser()._id;
					hire.user_id = Utils.getCurrentUser()._id;
					hire.returnDate = new Date().toString();
					hire.conditionStatus = EquipmentHire.conditionStatusEnum.Ok;
					equipment.hireHistory.add(hire);
				}else{
					EquipmentHire hire = new EquipmentHire();
					hire.warehouseman_id = Utils.getCurrentUser()._id;
					hire.user_id = Utils.getCurrentUser()._id;
					hire.reservationDate = new Date().toString();
					equipment.hireHistory.add(hire);
				}
			}
			if(equipment.isAvailable){
				if(wasHired()){
					EquipmentHire hire = new EquipmentHire();
					hire.warehouseman_id = Utils.getCurrentUser()._id;
					hire.user_id = Utils.getCurrentUser()._id;
					hire.returnDate = new Date().toString();
					hire.conditionStatus = EquipmentHire.conditionStatusEnum.Ok;
					equipment.hireHistory.add(hire);
				}
			}
			if(equipment.isServicing){
				if(wasHired()){
					EquipmentHire hire = new EquipmentHire();
					hire.warehouseman_id = Utils.getCurrentUser()._id;
					hire.user_id = Utils.getCurrentUser()._id;
					hire.returnDate = new Date().toString();
					hire.conditionStatus = EquipmentHire.conditionStatusEnum.Ok;
					equipment.hireHistory.add(hire);
				}
			}
		}
		response = RestService.callREST(Urls.PUT_EQUIPMENTS_URL+id, new Gson().toJson(equipment), Equipment.class, true, RestService.restServiceEnum.PUT);
		return response.getStatus();
	}
	
	//Saves the equipment state hired/reserved/available/service in session
	public static void saveState(Equipment equipment){
		if(equipment.isAvailable == true){
			if(Controller.session().containsKey(Equipments.EQUIPMENT_STATE)) Controller.session().remove(Equipments.EQUIPMENT_STATE);
			Controller.session().put(Equipments.EQUIPMENT_STATE, Equipments.stateEnum.Available.toString());
		}
		if(equipment.isServicing == true){
			if(Controller.session().containsKey(Equipments.EQUIPMENT_STATE)) Controller.session().remove(Equipments.EQUIPMENT_STATE);
			Controller.session().put(Equipments.EQUIPMENT_STATE, Equipments.stateEnum.Service.toString());
		}
		if(equipment.isHired == true){
			if(Controller.session().containsKey(Equipments.EQUIPMENT_STATE)) Controller.session().remove(Equipments.EQUIPMENT_STATE);
			Controller.session().put(Equipments.EQUIPMENT_STATE, Equipments.stateEnum.Hired.toString());
		}
		if(equipment.isReserved == true){
			if(Controller.session().containsKey(Equipments.EQUIPMENT_STATE)) Controller.session().remove(Equipments.EQUIPMENT_STATE);
			Controller.session().put(Equipments.EQUIPMENT_STATE, Equipments.stateEnum.Reserved.toString());
		}
	}
	
	//Check the equipment state hired/reserved/available/service in session
	public static boolean stateChanged(Equipment equipment){
		String state = null;
		if(Controller.session().containsKey(Equipments.EQUIPMENT_STATE)) state = Controller.session().get(Equipments.EQUIPMENT_STATE);
		if(equipment.isAvailable == true){
			if(Equipments.stateEnum.Available.toString().equals(state)){
				Controller.session().remove(Equipments.EQUIPMENT_STATE);
				return false;
			}
		}
		if(equipment.isServicing == true){
			if(Equipments.stateEnum.Service.toString().equals(state)){
				Controller.session().remove(Equipments.EQUIPMENT_STATE);
				return false;
			}
		}
		if(equipment.isHired == true){
			if(Equipments.stateEnum.Hired.toString().equals(state)){
				Controller.session().remove(Equipments.EQUIPMENT_STATE);
				return false;
			}
		}
		if(equipment.isReserved == true){
			if(Equipments.stateEnum.Reserved.toString().equals(state)){
				Controller.session().remove(Equipments.EQUIPMENT_STATE);
				return false;
			}
		}
		return true;
	}
	
	//Check if was hired
	public static boolean wasHired(){
		if(Equipments.stateEnum.Hired.toString().equals(Controller.session().get(Equipments.EQUIPMENT_STATE))) return true;
		else return false;
	}
}
