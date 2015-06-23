package utils;

/*
 * @Author(name="Lukas Pecak")
 */


public class Messages {
	
	//Login actions
	public static final String FORBIDDEN_ACCESS	=			"Niestety nie masz uprawnień do wykonania tej akcji! Zaloguj jako uprawniony użytkownik! ";
	public static final String FORBIDDEN_REMOTE_ACCESS	=	"Nie masz uprawnień do wykonania zdalnej akcji! ";
	public static final String LOGOUT_SUCCESS	=			"Użytkownik został prawidłowo wylogowany! ";
	public static final String LOGIN_SUCCESS_BEGIN	=		"Zalogowany jako ";
	public static final String LOGIN_SUCCESS_END	=		" z uprawnieniami - ";
	public static final String ERROR_AUTHENTICATING_USER =	"Podczas próby uwierzytelnienia użytkownika wystąpił błąd! ";
	public static final String ERROR_USER_NAME_PASSWORD	=	"Niepoprawna nazwa użytkownika lub hasło";
	public static final String ERROR_SIGNUP	=				"Podczas rejestracji użytkownika wystąpił błąd ! ";
	public static final String ERROR_LOGIN	=				"Podczas logowania wystąpił nieokreślony błąd ! ";
	public static final String SUCCESS_LOGIN =				"Logowanie prawidłowe. Witaj ";
	public static final String SUCCESS_SIGNUP =				"Pomyślnie zarejestrowano uzytkownika ";
	public static final String WARNING_SIGNUP =				"Popraw dane! Wprowadzone dane są niepoprawne.  ";
	
	public static String getLogginSuccess(String userName, String role){
		return new StringBuilder().append(LOGIN_SUCCESS_BEGIN).append(userName).append(LOGIN_SUCCESS_END).append(role).toString();
	}
	
	//flash message type
	public static final String WARNING = 									"warning";
	public static final String ERROR = 										"error"; 
	public static final String SUCCESS = 									"success";
	public static final String FORBIDDEN =									"forbidden";
	
	//Keys
	public static final String EDIT_USER_KEY = 							"editUser";
		
	//User details messages
	public static final String CORRECT_FORM = 							"Popraw dane w formularzu ! ";
	public static final String ERROR_ADDING_USER = 						"Błąd! Nie można dodać użytkownika! Info : Nie można połączyć z serwerm! Sprawdź czy serwer działa i czy wprowadzony adres serwera jest poprawny! ";
	public static final String SUCCESS_ADING_USER = 					"Pomyślnie dodano użytkownika! ";
	public static final String SUCCESS_UPDATE_USER = 					"Pomyślnie aktualizowano użutkownika! ";
	public static final String ERROR_ADDING_USER_DETAILS = 				"Błąd! Problem z dodadnie użytkonika! Info : ";
	public static final String SUCCESS_DELETED =			 			"Pomyślnie usunięto użytkownika - ";
	public static final String ERROR_DELETE =				 			"Wystąpił błąd podas próby usunięcia użytkownika! ";
	public static final String ERROR_GETTING_USER = 					"Wystąpił błąd podczas próby wyświtlenia użytkoników! ";
	public static final String USER_NOT_FOUND =							"Błąd! Nie znaleziono użytkownika. ";
	
	//Course details messages
	public static final String SUCCESS_ADDING_COURSE =					"Dane kursu zostały pomyślnie zapisane!" ;
	public static final String ERROR_ADDING_COURSE = 					"Nastąpił błąd podczas dodawania użytkownika ! ";
	public static final String ERROR_LOADING_COURSES =					"Błąd podczas ładowania kursów! Sprawdź połączenie z serwerem api! ";
	public static final String ERROR_LOADING_COURSE_VIEW =				"Błąd podczas ładowania widoku kursów! Sprawdź czy wybrany kurs istnieje w bazie ! ";
	public static final String ERROR_SAVING_COURSE_DRAFT = 				"Błąd komunikcji z serwerem! Nie można dodać grupy!";
	public static final String COURSE_NOT_FOUND =						"Błąd! Nie znaleziono kursu! ";
	public static final String SUCCESS_DELETED_COURSE =					"Pomyślnie usunięto kurs - ";
	public static final String ERROR_DELETE_COURSE =					"Wystąpił błąd podas próby usunięcia kursu! ";
	public static final String SUCCESS_UPDATE_COURSE = 					"Pomyślnie aktualizowano dane kursu! ";
	
	//Course Meeting messages
	public static final String ERROR_LOADING_MEETINGS =					"Błąd podczas ładowania spotkań kursowych! Sprawdź połączenie z serwerem api! ";
	public static final String ERROR_ADDING_COURSE_MEETING = 			"Nastąpił błąd podczas dodawania informacji o spotkaniu do kursu ! ";
	public static final String ERROR_SAVING_COURSE_MEETING = 			"Nastąpił błąd podczas zapisywaniu inforamcji o spotkaniu ! ";
	public static final String WARNING_CORRECT_MEETING_FORM_DATA = 		"Popraw lub uzupełnij dane w formularzu ! ";
	public static final String ERROR_DELETING_MEETING = 				"Nieznany błąd podczas próby usunięcia spotkania kursowego! ";
	
	//User list messages
	public static final String CANT_LOAD_USERS =						"Nie można załadować użytkowników! ";
	
	//Settings messages
	public static final String CANT_LOAD_FILE = 						"Nie można załadować pliku konfiguracji! ";
	public static final String ERROR_SAVING_SETTINGS =	 				"Wystąpił błąd podczas zapisywania konfiguracji! ";
	public static final String PROPERTIRS_FILE_COMMENT =	 			"Update apiUrl ";
	public static final String SAVED_MESSAGE = 							"Zapisano zmiany w konfiguracji systemu! ";
	public static final String EMPTY_FORM = 							"Nie wprowadzono danych! Dane nie zostały zmienione. ";
	
	//Equipments messages
	public static final String ERROR_DELETING_EQUIPMENT = 				"Podczas próby usunięcia sprzętu wystąpił problem: ";
	public static final String ERROR_SAVING_EQUIPMENT = 				"Podczas próby zapisania informacji o sprzęcie wystąpił problem: ";
	public static final String SUCCESS_SAVING_EQUIPMENT = 				"Pomyślnie zapisano informacje o sprzęcie - ";
	public static final String SUCCESS_DELETED_EQUIPMENT =			 	"Pomyślnie usunięto sprzęt - ";
	public static final String ERROR_BOOKING_EQUIPMENT =			 	"Blokada magazynu! Skontaktuj się ze skarbnikiem klubu!";
	public static final String ERROR_BOOKING_EQUIPMENT_ALRADY_BOOKED =	"Sprzęt zosta wcześniej zarezerwowany przez kogoś innego!";
	public static final String SUCCESS_BOOKIN_EQUIPMENT =			 	"Pomyślnie zarezerwowano sprzęt!";
	
	//Equipment list messages
	public static final String CANT_LOAD_EQUIPMENTS =					"Nie można załadować listy sprzętu! ";
	
	//Payments list messages
	public static final String CANT_LOAD_PAYMENTS =						"Nie można załadować płatności! ";
	public static final String ERROR_SAVING_PAYMENT = 					"Podczas próby zapisania płatności wystąpił problem: ";
	public static final String SUCCESS_SAVING_PAYMENT = 				"Pomyślnie zapisano informacje o płatności.";
	public static final String SUCCESS_DELETED_PAYMENT =			 	"Pomyślnie usunięto płatność.";
	public static final String ERROR_DELETING_PAYMENT = 				"Podczas próby usunięcia płatności wystąpił problem: ";
}
