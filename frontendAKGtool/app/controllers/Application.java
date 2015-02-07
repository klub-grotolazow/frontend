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
        return ok(index.render());
    }

}
