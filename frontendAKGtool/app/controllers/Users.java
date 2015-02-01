package controllers;

import models.User;
import models.UserAccount;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.SampleData;
import views.html.users.usersList;
import views.html.users.userDetails;
import views.html.users.signIn;

public class Users extends Controller {
	private static final Form<User> userForm = Form.form(User.class);
	private static final Form<UserAccount> userAccountForm = Form.form(UserAccount.class);
	
	public static Result getUsersList() {
        return ok(usersList.render(SampleData.getSampleUsers(10)));
        //usersList.render(SampleData.getSampleUsers(10))
    }
	
	public static Result saveUser(){
		return TODO;
	}
	
	public static Result newUser(){
		return ok(userDetails.render(userForm));
	}
	
	public static Result signIn(){
		return ok(signIn.render(userAccountForm));
	}
	
	public static Result verifyUser(){
		return TODO;
	}
}
