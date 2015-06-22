package service;

import com.google.gson.Gson;

import models.User;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import utils.Urls;
import utils.Utils;

/*
 * @Author(name="Lukas Pecak")
 */

public class RestService {
	public enum restServiceEnum {GET, POST, PUT, DELETE}
	public static final String AUTHORIZATION = 	"Authorization";
	
	public static WSResponse callRESTsignup(User user){
		String url = Utils.getApiUrl() + Urls.SIGNUP_URL;
		user = UsersService.createUserStructure(user);
		String request = new Gson().toJson(user);
		try{
			Promise<WSResponse> result = null;
			result = WS	.url(url)
					.setContentType(Urls.CONTENT_TYPE_JSON)
					.setHeader(AUTHORIZATION, SecurityService.getSignupHeader(user))
					.post(request);
			return result.get(Utils.WAIT_FOR_RESPONSE);		
		} catch(Exception exception){
			return null;
		}
	}
	
	// Call the REST api ******************************************************************************************************
	@SuppressWarnings("rawtypes")
	public static WSResponse callREST(String requestUrl, String requestJson, Class requestJsonClass, Boolean contentJson, restServiceEnum httpMetod){
		String url = Utils.getApiUrl();
		if(requestUrl != null) url += requestUrl;
		if((httpMetod != null) && (contentJson != null)){
			try{
				Promise<WSResponse> result = null;
				if(contentJson.equals(true)){
					// For GET method ________________________________________________________________
					if(httpMetod.equals(restServiceEnum.GET)){
						result = WS	.url(url)
									.setContentType(Urls.CONTENT_TYPE_JSON)
									//.setAuth(Utils.getAuthenticationHeader())
									.get();
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
					
					// For POST method _______________________________________________________________
					if(httpMetod.equals(restServiceEnum.POST)){
						result = WS	.url(url)
								.setContentType(Urls.CONTENT_TYPE_JSON)
								//.setAuth(Utils.getAuthenticationHeader())
								.post(requestJson);
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
					
					// For PUT method ________________________________________________________________
					if(httpMetod.equals(restServiceEnum.PUT)){
						result = WS	.url(url)
								.setContentType(Urls.CONTENT_TYPE_JSON)
								//.setAuth(Utils.getAuthenticationHeader())
								.put(requestJson);									
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
					
					// For DELETE method ______________________________________________________________
					if(httpMetod.equals(restServiceEnum.DELETE)){
						result = WS	.url(url)
								.setContentType(Urls.CONTENT_TYPE_JSON)
								//.setAuth(Utils.getAuthenticationHeader())
								.delete();
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
				} else{ //*************************************************************************************************************************************
					// For GET method without content header___________________________________________
					if(httpMetod.equals(restServiceEnum.GET)){
						result = WS	.url(url)
									//.setAuth(Utils.getAuthenticationHeader())
									.get();
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
					
					// For POST method without content header__________________________________________
					if(httpMetod.equals(restServiceEnum.POST)){
						result = WS	.url(url)
								//.setAuth(Utils.getAuthenticationHeader())
								.post(requestJson);
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
					
					// For PUT method without content header___________________________________________
					if(httpMetod.equals(restServiceEnum.PUT)){
						result = WS	.url(url)
								//.setAuth(Utils.getAuthenticationHeader())
								.put(requestJson);
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
					
					// For DELETE method without content header________________________________________
					if(httpMetod.equals(restServiceEnum.DELETE)){
						result = WS	.url(url)
								//.setAuth(Utils.getAuthenticationHeader())
								.delete();
						return result.get(Utils.WAIT_FOR_RESPONSE);
					}
				}
				
			} catch(Exception exception){
				return null;
			}
		}
		return null;
	}
}
