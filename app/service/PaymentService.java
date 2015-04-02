package service;

import java.util.ArrayList;
import java.util.List;

import models.Equipment;
import models.Payment;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import utils.Messages;
import utils.Urls;
import utils.Utils;

import com.google.gson.Gson;

/*
 * @Author(name="Lukas Pecak")
 */

public class PaymentService {
	
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
	
}
