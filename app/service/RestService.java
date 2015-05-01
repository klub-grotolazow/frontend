package service;

import com.google.gson.Gson;

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
	
	// Call the REST api ******************************************************************************************************
	public static WSResponse callREST(String requestUrl, String requestJson, Class requestJsonClass, Boolean contentJson, restServiceEnum httpMetod){
		String url = Utils.getApiUrl();
		if(requestUrl != null) url += requestUrl;
		System.out.println("url is : "+url);
		String request = null;
		
		/*if((requestJson != null) && (requestJsonClass != null)){
			Gson gson = new Gson();
			request = gson.toJson(request, requestJsonClass);
		}*/
		
		if((httpMetod != null) && (contentJson != null)){
			System.out.println("In not null");
			try{
				Promise<WSResponse> result = null;
				
				if(contentJson.equals(true)){
					System.out.println("In content json");
					// For GET method ________________________________________________________________
					if(httpMetod.equals(restServiceEnum.GET)){
						System.out.println("In get");
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
						System.out.println("in put :  url is"+url+" and request is -> "+ request );
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
				System.out.println("in cactch!");
				return null;
			}
		}
		return null;
	}
}
