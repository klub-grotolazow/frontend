package controllers;

/*
 * @Author(name="Lukas Pecak")
 */


import play.*;
import play.mvc.*;
import views.html.index;

@SuppressWarnings("unused")
public class Application extends Controller {

    public static Result index() {
    	response().setContentType("text/html; charset=UTF-8");
        return ok(index.render());
    }

}
