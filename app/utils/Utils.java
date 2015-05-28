package utils;

/*
 * @Author(name="Lukas Pecak")
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import play.mvc.Controller;

import java.util.Arrays;

import models.Course;
import models.Setting;

public class Utils {
	public static final String 		CURRENT_USER_ID = "userName";
	public static final String 		DRAFT_COURSE = "draft_course";
	public static final String 		USERS_JSON_TABLE = "users";
	public static final String		API_URL = "apiUrl";
	public static final String		NEW = "new";
	public static final String 		PROPERTIES_FILE = "config.properties";
	public static final int 		WAIT_FOR_RESPONSE = 5000;
	
	//Get the authentication header string for REST request
	public static String getAuthenticationHeader(){
		//Implement here!
		return "";
	}
	
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
			Controller.flash(Messages.SUCCESS, Messages.SAVED_MESSAGE + "  Api server location is -> " + setting.apiUrl);
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

	//Spliting a coma separated string into string list
	@SuppressWarnings("unchecked")
	public static List<String> toStringList(String css){
		if((css.length() == 0) || (css == null)){
			return new ArrayList<String>();
		} else{
			return Arrays.asList(css.split("\\s*,\\s*"));
		}
	}
	
	//Concatenate in a coma separated string from a string list
	@SuppressWarnings("unchecked")
	public static String toCSS(List<String> list){
		StringBuilder builder = new StringBuilder();
		if((list == null) || (list.size() == 0)){
			return "";
		} else{
			for(int i=0; i<list.size(); i++){
				if(i != 0) builder.append(",");
				builder.append(list.get(i));
			}
		return builder.toString();
		}
	}
	
	//Converts the list to css sting and return the proper object 
	public static Course prepareForHtml(Course course){
		String members = null;
		if(course.members_ids  != null){
			members = Utils.toCSS(course.members_ids);
			course.members_ids.clear();
			course.members_ids.add(members);
		} else{
			course.members_ids = new ArrayList<String>();
			course.members_ids.add("");
		}
		return course;
	}
	
	//Generate the random id key
	public static String generateKey() {
		SecureRandom random = new SecureRandom();
		String key = new  BigInteger(130, random).toString(32);
		System.out.println("Generated key is : " +key);
		return key;
	}
	
}
