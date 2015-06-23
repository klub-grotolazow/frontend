package controllers;

/*
 * @Author(name="Lukas Pecak")
 */

import play.mvc.*;
import play.mvc.Http.*;
import service.SecurityService;
import utils.Messages;
import utils.Utils;
import models.*;

public class Secured extends Security.Authenticator {
	
	// Two overriden methods from the play package ************************************************************************************************************
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get(SecurityService.USER_NAME);
    }
      
    @Override
    public Result onUnauthorized(Context ctx) {
    	Controller.flash(Messages.ERROR, Messages.FORBIDDEN_ACCESS);
        return redirect(routes.Application.index());
    }
    
    // Check if is SuperUser *******************************************************************************************************************************
    public static boolean isSuperUser() {
        return SecurityService.checkRole(SystemRoleEnum.SuperUser);//SystemRoleEnum.SuperUser.toString().equals(Controller.session().get(SecurityService.ROLES));
    }
    
    // Check is is SuperUser *******************************************************************************************************************************
    public static boolean isCourseManager() {
        return SecurityService.checkRole(SystemRoleEnum.CourseManager);//SystemRoleEnum.CourseManager.toString().equals(Controller.session().get(SecurityService.ROLES));
    }
    
    // Check if is SuperUser *******************************************************************************************************************************
    public static boolean isWarehouseMan() {
        return SecurityService.checkRole(SystemRoleEnum.Warehouseman);//SystemRoleEnum.WarehouseManager.toString().equals(Controller.session().get(SecurityService.ROLES));
    }
    
    // Check if is SuperUser *******************************************************************************************************************************
    public static boolean isInstructor() {
        return SecurityService.checkRole(SystemRoleEnum.Instructor);//SystemRoleEnum.Instructor.toString().equals(Controller.session().get(SecurityService.ROLES));
    }
    
    // Check if is SuperUser *******************************************************************************************************************************
    public static boolean isAccounter() {
        return SecurityService.checkRole(SystemRoleEnum.Accounter);//SystemRoleEnum.PaymentManager.toString().equals(Controller.session().get(SecurityService.ROLES));
    }
    
    // Check if is SuperUser *******************************************************************************************************************************
    public static boolean isCourseMember() {
        return SecurityService.checkRole(SystemRoleEnum.CourseMember);//SystemRoleEnum.Student.toString().equals(Controller.session().get(SecurityService.ROLES));
    }
    
    // Check if is SuperUser *******************************************************************************************************************************
    public static boolean isCandidate() {
        return SecurityService.checkRole(SystemRoleEnum.Candidate);//SystemRoleEnum.ClubMemeber.toString().equals(Controller.session().get(SecurityService.ROLES));
    }
    
    // Check if has any role *******************************************************************************************************************************
    public static boolean hasAnyRole() {
    	if((SecurityService.checkRole(SystemRoleEnum.SuperUser)) ||
    			(SecurityService.checkRole(SystemRoleEnum.CourseManager)) ||
    			(SecurityService.checkRole(SystemRoleEnum.Warehouseman)) ||
    			(SecurityService.checkRole(SystemRoleEnum.Instructor)) ||
    			(SecurityService.checkRole(SystemRoleEnum.Accounter)) ||
    			(SecurityService.checkRole(SystemRoleEnum.CourseMember)) ||
    			(SecurityService.checkRole(SystemRoleEnum.Candidate))){
    			return true;
    	}
        return false;
    }
    
    // Check if has no role
    public static boolean hasNoRole(){
    	if(Utils.getRoles().length() == 0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    
}