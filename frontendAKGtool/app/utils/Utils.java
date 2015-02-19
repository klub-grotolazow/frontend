package utils;

/*
 * @Author(name="Lukas Pecak")
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import play.mvc.Controller;
import models.Setting;

public class Utils {
	public static final String USERS_JSON_TABLE = "users";
	public static final String API_URL = "apiUrl";
	public static final String PROPERTIES_FILE = "config.properties";
	
	//Getting the api url from the properties file
	public static String getApiUrl(){
		String result;
		if(Controller.session().containsKey(API_URL)){
			return Controller.session().get(API_URL);
		} else{
		try{
			FileInputStream input = new FileInputStream(PROPERTIES_FILE);
			java.util.Properties properties = new java.util.Properties();
			properties.load(input);
			Setting setting = new Setting();
			setting.apiUrl = properties.getProperty(API_URL);
			result = setting.apiUrl;
			Controller.session().put(API_URL, result);
		}catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.CANT_LOAD_FILE);
			result = "";
			Controller.session().put(API_URL, result);
		}
		}
		return result;
	}
	
	//Saving the api url
	public static void putApiUrl(Setting setting){
		if(setting.apiUrl != ""){
		Properties properties = new Properties();
		FileOutputStream output;
		try{
			output = new FileOutputStream(PROPERTIES_FILE);
			properties.clear();
			properties.setProperty(API_URL, setting.apiUrl);
			properties.store(output, Messages.PROPERTIRS_FILE_COMMENT);
			Controller.session().put(API_URL, properties.getProperty(API_URL));
			Controller.flash(Messages.SUCCESS, Messages.SAVED_MESSAGE);
		} catch(Exception exception){
			Controller.flash(Messages.ERROR, Messages.ERROR_SAVING_SETTINGS);
		}
		} else{
			Controller.flash().put(Messages.WARNING, Messages.EMPTY_FORM);
		}
	}
	
	//Wrapping the json collection message
	public static String wrapJsonTable(String json){
		StringBuilder builder = new StringBuilder();
		builder.append("{ \"")
				.append(USERS_JSON_TABLE)
				.append("\":")
				.append(json)
				.append("}");
		return builder.toString();
	}


}
