package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import utils.SampleData;

import views.html.*;
import views.html.users.usersList;

public class Users extends Controller {
	public static Result getUsersList() {
        return ok(usersList.render(SampleData.getSampleUsers(10)));
        //usersList.render(SampleData.getSampleUsers(10))
    }
}
