package controllers;

/*
 * @Author(name="Lukas Pecak")
 */


import models.Payment;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.CoursesService;
import service.PaymentsService;
import service.UsersService;
import utils.Messages;
import utils.StatusCodes;
import utils.Utils;
import views.html.payment.paymentsList;
import views.html.payment.paymentDetails;

@Security.Authenticated(Secured.class)
public class Payments extends Controller {
	private static Form<Payment> paymentForm = Form.form(Payment.class);
	
	// Get the list of all payments
	public static Result getPaymentsList(){
		if(Secured.isSuperUser() || Secured.isPaymentManager() || Secured.isCourseManager()){	
			try{
				return ok(paymentsList.render(UsersService.getUser(session().get("userName")), 
												session().get("role"), 
												PaymentsService.getPaymentsList(), 
												UsersService.getUsersList()));
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
		Payment payment = new Payment();
		return ok(paymentDetails.render(Utils.getCurrentUser(),
										Utils.getRoles(),
										paymentForm.fill(payment),
										payment,
										UsersService.getUsersList(),
										CoursesService.getCoursesList(),
										null));
	}
	
	// Delete the payment with the given id from the system
	public static Result deletePayment(String id){
		if(Secured.isSuperUser() || Secured.isPaymentManager()){	
			PaymentsService.deletePayment(id);
			return ok(paymentsList.render(UsersService.getUser(session().get("userName")), 
													session().get("role"), 
													PaymentsService.getPaymentsList(), 
													UsersService.getUsersList()));
		}  else {
			Controller.session().clear();
			Controller.flash().put(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
			return redirect(routes.Application.login());
		}
	}
	
	// Edit a payment entry
	public static Result editPayment(String id){
		Payment payment = PaymentsService.getPayment(id);
		Form<Payment> form = paymentForm.fill(payment);
		return ok(paymentDetails.render(Utils.getCurrentUser(),
										Utils.getRoles(),
										form,
										payment,
										UsersService.getUsersList(),
										CoursesService.getCoursesList(),
										id));
	}
	
	// Show the payment details in a view
	public static Result showPayment(String id){
		return TODO;
	}
	
	// Save the payment - by sending a REST request to the api backend
	public static Result savePayment(String id){
		Payment payment = paymentForm.bindFromRequest().get();
		int status = 0;
		try{
		//Save new
		if((id == null) || (id == "")){
			status = PaymentsService.savePayment(payment); 
		//Update existing
		}else{
			status = PaymentsService.updatePayment(payment, id);
		}
		} catch(Exception exception){
			flash().put(Messages.ERROR, Messages.ERROR_SAVING_PAYMENT + exception);
			return badRequest(paymentDetails.render(Utils.getCurrentUser(),
													Utils.getRoles(),
													paymentForm.fill(payment),
													payment,
													UsersService.getUsersList(),
													CoursesService.getCoursesList(),
													id));
		}
		if((status == StatusCodes.CREATED) || (status == StatusCodes.OK)){
		flash().put(Messages.SUCCESS, Messages.SUCCESS_SAVING_PAYMENT);
		return ok(paymentsList.render(UsersService.getUser(session().get("userName")), 
										session().get("role"), 
										PaymentsService.getPaymentsList(), 
										UsersService.getUsersList()));
		} else{
			flash().put(Messages.ERROR, Messages.ERROR_SAVING_PAYMENT + "Status "+status);
			return badRequest(paymentDetails.render(Utils.getCurrentUser(),
													Utils.getRoles(),
													paymentForm.fill(payment),
													payment,
													UsersService.getUsersList(),
													CoursesService.getCoursesList(),
													id));
		}
	}
	
}
