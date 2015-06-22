package service;

import java.util.ArrayList;
import java.util.List;

import models.Payment;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import utils.Messages;
import utils.StatusCodes;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

/*
 * @Author(name="Lukas Pecak")
 */

public class PaymentsService {
	
	// Returning the list of payments ****************************************************************************************
	public static List<Payment> getPaymentsList(){
		WSResponse response = null;
		List<Payment> resultList = new ArrayList<Payment>();
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.GET_PAYMENTS_URL)
										.setContentType(Urls.CONTENT_TYPE_JSON)
										.get();
		response = result.get(10000);
		Gson gson = new Gson();
		Payment[] payments = gson.fromJson(response.getBody(), Payment[].class);
		for(int i=0; i<payments.length; i++){
			resultList.add(payments[i]);
		}
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_PAYMENTS + exception);
		}
		return resultList;
	}
	
	// Return a equipment with specialized id
	public static Payment getPayment(String id){
		WSResponse response = null;
		Payment payment = null;
		try{
			response = RestService.callREST(Urls.GET_PAYMENT_URL+id, null, null, true, RestService.restServiceEnum.GET);
			payment = new Gson().fromJson(response.getBody(), Payment.class);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_PAYMENTS + exception);
		}
		return payment;
	}
	
	//Delete payment service
	public static void deletePayment(String id){
		WSResponse response = null;
		try{
		Promise<WSResponse> result = WS.url(Utils.getApiUrl()+Urls.DELETE_PAYMENT_URL+id)
										.delete();
		response = result.get(10000);
		if(response.getStatus() == StatusCodes.OK){
			Controller.flash().put(Messages.SUCCESS, Messages.SUCCESS_DELETED_PAYMENT);
		} else{
			Controller.flash().put(Messages.ERROR, Messages.ERROR_DELETING_PAYMENT +"HTTP -> "+ response.getStatus());
		}
		} catch(Exception exception){	
			Controller.flash(Messages.ERROR, Messages.ERROR_DELETING_PAYMENT + exception);
		}
	}
	
	//Save payment
	public static int savePayment(Payment payment) throws Exception{
		WSResponse response = null;
		Gson gson = new Gson();
		String json = gson.toJson(payment);
		response = RestService.callREST(Urls.SAVE_PAYMENTS_URL, json, Payment.class, true, RestService.restServiceEnum.POST);
		return response.getStatus();
	}
	
	//Update payment
	public static int updatePayment(Payment payment, String id) throws Exception{
		WSResponse response = null;
		payment._id = id;
		response = RestService.callREST(Urls.PUT_PAYMENTS_URL+id, new Gson().toJson(payment), Payment.class, true, RestService.restServiceEnum.PUT);
		return response.getStatus();	
	}
	
	
}
