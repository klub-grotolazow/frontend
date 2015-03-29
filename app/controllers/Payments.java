package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.payment.paymentList;
import views.html.payment.paymentOverview;
import views.html.payment.paymentDetails;

@Security.Authenticated(Secured.class)
public class Payments extends Controller {
	
	// Get the list of all payments
	public static Result getPaymentsList(){
		return TODO;
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
