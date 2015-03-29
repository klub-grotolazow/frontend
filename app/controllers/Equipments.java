package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class Equipments extends Controller {
	
	// List all equipment in warehouse *******************************************************************************************************
	public static Result getEquipmentsList(){
		return TODO;
	}
	
	// Reneder a view for entering new equipment *********************************************************************************************
	public static Result newEquipment(){
		return TODO;
	}
	
	// Delete a equipment by given id ********************************************************************************************************
	public static Result deleteEquipment(String id){
		return TODO;
	}
	
	// Edit the equipmet with giveb id *******************************************************************************************************
	public static Result editEquipment(String id){
		return TODO;
	}
	
	// Set the equipment with the give id as borrowd *****************************************************************************************
	public static Result borrowEquipment(){
		return TODO;
	}
	
	// Set the equipmet as returned **********************************************************************************************************
	public static Result returnEquipment(){
		return TODO;
	}
	
	// Show the view with the equipment overview *********************************************************************************************
	public static Result showEquipment(String id){
		return TODO;
	}
	
	// Save or update the equipment with the given id ****************************************************************************************
	public static Result saveEquipment(){
		return TODO;
	}
	
	// Book the equipment with the given id **************************************************************************************************
		public static Result bookEquipment(){
			return TODO;
		}
	
}
