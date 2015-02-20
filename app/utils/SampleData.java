package utils;

/*
 * @Author(name="Lukas Pecak")
 */

import java.util.ArrayList;
import java.util.List;

import models.User;

public class SampleData {
	public static List<User> getSampleUsers(int usersNumber){
		List<User> result = new ArrayList<User>();
		for(int i=0; i<usersNumber; i++){
			User user = new User("firstName"+i, "lastName"+i, "email"+i, "peselNr"+i, "idCardNr"+i, "feeStatus"+i,i);
			result.add(user);
		}
		return result;
	}
}
