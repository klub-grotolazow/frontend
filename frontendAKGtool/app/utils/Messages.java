package utils;

/*
 * @Author(name="Lukas Pecak")
 */


public class Messages {
	//flash message type
	public static final String WARNING = 	"warning";
	public static final String ERROR = 		"error"; 
	public static final String SUCCESS = 	"success";
	
	//User details messages
	public static final String CORRECT_FORM = 				"Please correct the form below.";
	public static final String ERROR_ADDING_USER = 			"Error! Problem with adding user! Details : Cann not connect to server. Check if the server is running or the server url is crorrect.";
	public static final String SUCCESS_ADING_USER = 		"Success! User added to list!";
	public static final String ERROR_ADDING_USER_DETAILS = 	"Error! Problem with adding user! Details : ";
	
	//User list messages
	public static final String CANT_LOAD_USERS =			"Can not load users!";
	
	//Settings messages
	public static final String CANT_LOAD_FILE = 			"Can't load properties file !";
	public static final String ERROR_SAVING_SETTINGS = 		"Problem occured while saving system settings!";
	public static final String PROPERTIRS_FILE_COMMENT = 	"Update apiUrl";
	public static final String SAVED_MESSAGE = 				"System properties saved!";
	public static final String EMPTY_FORM = 				"There are no data in the form. Settings not changed!";
}
