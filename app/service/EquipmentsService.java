package service;

import java.util.ArrayList;
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

import models.Course;
import models.Equipment;
import models.User;

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
}
