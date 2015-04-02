package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import models.Equipment;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.EquipmentsService;
import service.UsersService;
import utils.Messages;
import views.html.equipments.equipmentsList;

@Security.Authenticated(Secured.class)
public class Equipments extends Controller {
	private static Form<Equipment> equipmentForm = Form.form(Equipment.class);
	
	// List all equipment in warehouse *******************************************************************************************************
	public static Result getEquipmentsList(){
		if(Secured.isSuperUser() || Secured.isWarehouseManager() || Secured.isStudent()){	
			try{
				return ok(equipmentsList.render(UsersService.getUser(session().get("userName")), session().get("role"), EquipmentsService.getEquipmentsList()));
			} catch(Exception exception){
				return badRequest();
			}
		} else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	// Reneder a view for entering new equipment *********************************************************************************************
	public static Result newEquipment(){
		return TODO;
	}
	
	// Delete a equipment by given id ********************************************************************************************************
	public static Result deleteEquipment(String id){
		if(Secured.isSuperUser() || Secured.isWarehouseManager()){	
			EquipmentsService.deleteEquipment(id);
			return ok(equipmentsList.render(UsersService.getUser(session().get("userName")), session().get("role"), EquipmentsService.getEquipmentsList()));
		}  else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	// Edit the equipmet with giveb id *******************************************************************************************************
	public static Result editEquipment(String id){
		return TODO;
	}
	
	// Set the equipment with the give id as borrowd *****************************************************************************************
	public static Result borrowEquipment(){
		return TODO;
	}
	
	// Make a reservation for equipment ******************************************************************************************************
		public static Result makeReservation(String id){
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
