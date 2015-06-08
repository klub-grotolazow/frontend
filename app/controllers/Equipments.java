package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import com.google.gson.Gson;

import models.Equipment;
import models.EquipmentHire;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.EquipmentsService;
import service.UsersService;
import utils.Messages;
import utils.StatusCodes;
import utils.Utils;
import views.html.equipments.equipmentsList;
import views.html.equipments.equipmentDetails;

@Security.Authenticated(Secured.class)
public class Equipments extends Controller {
	public static final String EQUIPMENT_STATE = "equipmentState";
	public static enum stateEnum {Available, Reserved, Hired, Service};
	private static Form<Equipment> equipmentForm = Form.form(Equipment.class);

	
	// List all equipment in warehouse *******************************************************************************************************
	public static Result getEquipmentsList(){
		if(Secured.isSuperUser() || Secured.isWarehouseManager() || Secured.isStudent()){	
			try{
				return ok(equipmentsList.render(UsersService.getUser(session().get("userName")), 
													session().get("role"), 
													EquipmentsService.getEquipmentsList()));
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
		//@(currentUser: User, role: String, equipmentForm: Form[Equipment], id: String)
		Equipment newEquipment = new Equipment();
		newEquipment.isAvailable = true;
		EquipmentsService.saveState(newEquipment);
		newEquipment = new Equipment();
		return ok(equipmentDetails.render(UsersService.getUser(session().get("userName")), 
											session().get("role"),
											equipmentForm,
											newEquipment,
											UsersService.getUsersList(),
											null));
	}
	
	// Delete a equipment by given id ********************************************************************************************************
	public static Result deleteEquipment(String id){
		if(Secured.isSuperUser() || Secured.isWarehouseManager()){	
			EquipmentsService.deleteEquipment(id);
			return ok(equipmentsList.render(UsersService.getUser(session().get("userName")), 
												session().get("role"), 
												EquipmentsService.getEquipmentsList()));
		}  else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	// Edit the equipment with given id *******************************************************************************************************
	public static Result editEquipment(String id){
		Equipment equipment = EquipmentsService.getEquipment(id);
		EquipmentsService.saveState(equipment);
		Form<Equipment> form = equipmentForm.fill(equipment);
		return ok(equipmentDetails.render(Utils.getCurrentUser(), 
											Utils.getRoles(),
											form,
											equipment,
											UsersService.getUsersList(),
											id));
	}
	
	// Set the equipment with the give id as borrowd *****************************************************************************************
	public static Result hireEquipment(){
		return ok(equipmentsList.render(Utils.getCurrentUser(), 
										Utils.getRoles(), 
										EquipmentsService.getReservedEquipment()));
	}
	
	// Make a reservation for equipment ******************************************************************************************************
	public static Result makeReservation(){
		return ok(equipmentsList.render(Utils.getCurrentUser(), 
										Utils.getRoles(), 
										EquipmentsService.getAvailableEquipment()));
	}
	
	// Set the equipmet as returned **********************************************************************************************************
	public static Result returnEquipment(){
		return ok(equipmentsList.render(Utils.getCurrentUser(), 
										Utils.getRoles(), 
										EquipmentsService.getHiredEquipment()));
	}
	
	// Show the view with the equipment overview *********************************************************************************************
	public static Result showEquipment(String id){
		return TODO;
	}
	
	// Save or update the equipment with the given id ****************************************************************************************
	public static Result saveEquipment(String id){
		Equipment equipment = equipmentForm.bindFromRequest().get();
		int status = 0;
		try{
		//Save new
		if((id == null) || (id == "")){
			status = EquipmentsService.saveEquipment(equipment); 
		//Update existing
		}else{
			status = EquipmentsService.updateEquipment(equipment, id);
		}
		} catch(Exception exception){
			flash().put(Messages.ERROR, Messages.ERROR_SAVING_EQUIPMENT + exception);
			return badRequest(equipmentDetails.render(Utils.getCurrentUser(), 
														Utils.getRoles(),
														equipmentForm,
														equipment,
														UsersService.getUsersList(),
														id));
		}
		if((status == StatusCodes.CREATED) || (status == StatusCodes.OK)){
		flash().put(Messages.SUCCESS, Messages.SUCCESS_SAVING_EQUIPMENT + equipment.name);
		return ok(equipmentsList.render(Utils.getCurrentUser(), 
											Utils.getRoles(), 
											EquipmentsService.getEquipmentsList()));
		} else{
			flash().put(Messages.ERROR, Messages.ERROR_SAVING_EQUIPMENT + "Status "+status);
			return badRequest(equipmentDetails.render(Utils.getCurrentUser(), 
														Utils.getRoles(),
														equipmentForm,
														equipment,
														UsersService.getUsersList(),
														id));
		}
	}
	
	// Book the equipment with the given id **************************************************************************************************
		public static Result bookEquipment(String id){
			Equipment equipment = EquipmentsService.getEquipment(id);
			if(Utils.getCurrentUser().feeStatus.equals(User.feeStatusEnum.Blocked)){
				flash().put(Messages.ERROR, Messages.ERROR_BOOKING_EQUIPMENT);
				return forbidden(equipmentsList.render(Utils.getCurrentUser(), 
														Utils.getRoles(), 
														EquipmentsService.getEquipmentsList()));
			}
			if(equipment.isAvailable){
				equipment.isReserved = true;
				equipment.isAvailable = false;
				EquipmentHire hire = new EquipmentHire();
				hire.reservationDate = Utils.getDate();
				hire.user_id = Utils.getCurrentUser()._id;
				hire.warehouseman_id = "brak";
				equipment.hireHistory.add(hire);
				flash().put(Messages.SUCCESS, Messages.SUCCESS_BOOKIN_EQUIPMENT);
				return ok(equipmentsList.render(Utils.getCurrentUser(), 
														Utils.getRoles(), 
														EquipmentsService.getEquipmentsList()));
			}else{
				flash().put(Messages.ERROR, Messages.ERROR_BOOKING_EQUIPMENT_ALRADY_BOOKED);
				return forbidden(equipmentsList.render(Utils.getCurrentUser(), 
														Utils.getRoles(), 
														EquipmentsService.getEquipmentsList()));
			}
		}
	
}
