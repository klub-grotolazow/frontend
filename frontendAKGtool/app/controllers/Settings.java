package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

import models.Setting;
import models.User;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.settings;
import views.html.index;

public class Settings extends Controller {
	private static Properties properties = new Properties();
	private static FileOutputStream output;
	private static FileInputStream input;
	private static Form<Setting> settingForm = Form.form(Setting.class);
	
	public static Result getSettings(){
		try{
			input = new FileInputStream("config.properties"); 
			properties.load(input);
			Setting setting = new Setting();
			setting.apiUrl = properties.getProperty("apiUrl");
			settingForm.fill(setting);
			return ok(settings.render(settingForm, setting.apiUrl));
		}catch(Exception exception){
			flash("error", "Can't load properties file !");
			return ok(index.render());
		}
	}
	
	public static Result saveSettings(){
		Form<Setting> boundForm = settingForm.bindFromRequest();
		Setting setting = boundForm.get();
		try{
			output = new FileOutputStream("config.properties");
			properties.clear();
			properties.setProperty("apiUrl", setting.apiUrl);
			
			properties.store(output, "Update apiUrl");
		} catch(Exception exception){
			flash("error","Problem occured while saving system settings!");
		}
		flash("success","System properties saved!");
		return ok(index.render());
	}

}
