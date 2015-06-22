package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import play.mvc.*;
import play.mvc.Http.*;
import utils.Messages;
import models.*;

public class Secured extends Security.Authenticator {
	
	// Two overriden methods from the play package ************************************************************************************************************
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("userName");
    }
   
    
    @Override
    public Result onUnauthorized(Context ctx) {
    	Controller.flash(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
        return redirect(routes.Application.login());
    }
    
    // Check if it is SuperUser *******************************************************************************************************************************
    public static boolean isSuperUser() {
        return SystemRoleEnum.SuperUser.toString().equals(Controller.session().get("role"));
    }
    
    // Check if it is SuperUser *******************************************************************************************************************************
    public static boolean isCourseManager() {
        return SystemRoleEnum.CourseManager.toString().equals(Controller.session().get("role"));
    }
    
    // Check if it is SuperUser *******************************************************************************************************************************
    public static boolean isWarehouseManager() {
        return SystemRoleEnum.WarehouseManager.toString().equals(Controller.session().get("role"));
    }
    
    // Check if it is SuperUser *******************************************************************************************************************************
    public static boolean isInstructor() {
        return SystemRoleEnum.Instructor.toString().equals(Controller.session().get("role"));
    }
    
    // Check if it is SuperUser *******************************************************************************************************************************
    public static boolean isPaymentManager() {
        return SystemRoleEnum.PaymentManager.toString().equals(Controller.session().get("role"));
    }
    
    // Check if it is SuperUser *******************************************************************************************************************************
    public static boolean isStudent() {
        return SystemRoleEnum.Student.toString().equals(Controller.session().get("role"));
    }
    
    // Check if it is SuperUser *******************************************************************************************************************************
    public static boolean isClubMember() {
        return SystemRoleEnum.ClubMemeber.toString().equals(Controller.session().get("role"));
    }
    
    
}