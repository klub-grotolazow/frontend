package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import models.Setting;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.UsersService;
import utils.Utils;
import views.html.settings;
import views.html.index;

@Security.Authenticated(Secured.class)
public class Settings extends Controller {
	public static final String API_URL = "apiUrl";
	public static final String PROPERTIES_FILE = "config.properties";
	public static final Form<Setting> settingForm = Form.form(Setting.class);
	
	//Loading saved settings from session scope or from the properties file
	public static Result getSettings(){
		try{		
			return ok(settings.render(UsersService.getUser(session().get("userName")), session().get("role"), settingForm, Utils.getApiUrl())); 
		}catch(Exception exception){
			return ok(index.render(UsersService.getUser(session().get("userName")), session().get("role")));
		}
	}
	
	//Saving the settings to the session scope and the properties file
	public static Result saveSettings(){
		Form<Setting> boundForm = settingForm.bindFromRequest();
		Setting setting = boundForm.get();
		Utils.putApiUrl(setting);
		return ok(index.render(UsersService.getUser(session().get("userName")), session().get("role")));
	}

}
