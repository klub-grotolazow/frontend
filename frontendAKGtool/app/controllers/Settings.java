package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import java.io.FileOutputStream;
import java.util.Properties;

import models.Setting;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Messages;
import utils.Utils;
import views.html.settings;
import views.html.index;

public class Settings extends Controller {
	public static final String API_URL = "apiUrl";
	public static final String PROPERTIES_FILE = "config.properties";
	private static Form<Setting> settingForm = Form.form(Setting.class);
	
	//Loading saved settings from session scope or from the properties file
	public static Result getSettings(){
		try{
			return ok(settings.render(settingForm, Utils.getApiUrl()));
		}catch(Exception exception){
			return ok(index.render());
		}
	}
	
	//Saving the settings to the session scope and the properties file
	public static Result saveSettings(){
		Form<Setting> boundForm = settingForm.bindFromRequest();
		Setting setting = boundForm.get();
		//Utils.putApiUrl(setting);
		
		if(setting.apiUrl != ""){
			Properties properties = new Properties();
			FileOutputStream output;
			try{
				output = new FileOutputStream(PROPERTIES_FILE);
				properties.clear();
				if(setting.apiUrl != null){
				properties.setProperty(API_URL, setting.apiUrl);
				properties.store(output, Messages.PROPERTIRS_FILE_COMMENT);
				Controller.session().put(API_URL, properties.getProperty(API_URL));
				}
				Controller.flash(Messages.SUCCESS, Messages.SAVED_MESSAGE);
			} catch(Exception exception){
				Controller.flash(Messages.ERROR, Messages.ERROR_SAVING_SETTINGS + setting + setting.apiUrl);
			}
			} else{
				Controller.flash().put(Messages.WARNING, Messages.EMPTY_FORM + setting + setting.apiUrl);
			}
		
		return ok(index.render());
	}

}
