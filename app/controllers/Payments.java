package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.EquipmentsService;
import service.PaymentService;
import service.UsersService;
import utils.Messages;
import views.html.payment.paymentsList;
import views.html.payment.paymentOverview;
import views.html.payment.paymentDetails;

@Security.Authenticated(Secured.class)
public class Payments extends Controller {
	
	// Get the list of all payments
	public static Result getPaymentsList(){
		if(Secured.isSuperUser() || Secured.isPaymentManager() || Secured.isCourseManager()){	
			try{
				return ok(paymentsList.render(UsersService.getUser(session().get("userName")), session().get("role"), PaymentService.getPaymentsList(), UsersService.getUsersList()));
			} catch(Exception exception){
				return badRequest();
			}
		} else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	// Create a payment in the system
	public static Result createPayment(){
		return TODO;
	}
	
	// Delete the payment with the given id from the system
	public static Result deletePayment(String id){
		return TODO;
	}
	
	// Edit a payment entry
	public static Result editPayment(String id){
		return TODO;
	}
	
	// Show the payment details in a view
	public static Result showPayment(String id){
		return TODO;
	}
	
	// Save the payment - by sending a REST request to the api backend
		public static Result savePayment(){
			return TODO;
		}
	
}
