package utils;

/*
 * @Author(name="Lukas Pecak")
 */


public class Messages {
	//flash message type
	public static final String WARNING = 					"warning";
	public static final String ERROR = 						"error"; 
	public static final String SUCCESS = 					"success";
	
	//Keys
	public static final String EDIT_USER_KEY = 				"editUser";
	
	//User details messages
	public static final String CORRECT_FORM = 				"Popraw dane w formularzu poniżej! ";
	public static final String ERROR_ADDING_USER = 			"Błąd! Nie można dodać użytkownika! Info : Nie można połączyć z serwerm! Sprawdź czy serwer działa i czy wprowadzony adres serwera jest poprawny! ";
	public static final String SUCCESS_ADING_USER = 		"Pomyślnie dodano użytkownika! ";
	public static final String SUCCESS_UPDATE_USER = 		"Pomyślnie aktualizowano użutkownika! ";
	public static final String ERROR_ADDING_USER_DETAILS = 	"Błąd! Problem z dodadnie użytkonika! Info : ";
	public static final String SUCCESS_DELETED =		 	"Pomyślnie usunięto użytkownika - ";
	public static final String ERROR_DELETE =			 	"Wystąpił błąd podas próby usunięcia użytkownika! ";
	public static final String ERROR_GETTING_USER = 		"Wystąpił błąd podczas próby wyświtlenia użytkoników! ";
	public static final String USER_NOT_FOUND =				"Błąd! Nie znaleziono użytkownika. ";
	
	//Course details messages
	public static final String ERROR_LOADING_COURSES =		"Błąd podczas ładowania kursów! Sprawdź połączenie z serwerem api! ";
	public static final String ERROR_LOADING_COURSE_VIEW =	"Błąd podczas ładowania widoku kursów! Sprawdź czy wybrany kurs istnieje w bazie ! ";
	public static final String COURSE_NOT_FOUND =			"Błąd! Nie znaleziono kursu! ";
	
	
	//User list messages
	public static final String CANT_LOAD_USERS =			"Nie można załadować użytkowników! ";
	
	//Settings messages
	public static final String CANT_LOAD_FILE = 			"Nie można załadować pliku konfiguracji! ";
	public static final String ERROR_SAVING_SETTINGS = 		"Wystąpił błąd podczas zapisywania konfiguracji! ";
	public static final String PROPERTIRS_FILE_COMMENT = 	"Update apiUrl ";
	public static final String SAVED_MESSAGE = 				"Zapisano zmiany w konfiguracji systemu! ";
	public static final String EMPTY_FORM = 				"Nie wprowadzono danych! Dane nie zostały zmienione. ";
}
