/**
 * mema nalang mang kopya
 * @author ken
 */

package com.mycompany.webapplicationdb.exception;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UnauthorizedAccessException extends Exception{

    //TODO: maybe heree yung static method for validating unexpected access?
    public static void checkAccessAdmin(HttpSession session) throws UnauthorizedAccessException {
        Object b = session.getAttribute("username");
        Object a = session.getAttribute("user_role");
        if (b == null || a == null) {
            throw new UnauthorizedAccessException("You are not authorized to access this page because you are not logged in.");
        }
        
        if (((String)a).equals("admin")) {
            return ;
        }
        if (((String)a).equals("super_admin")) {
            return ;
        }

        throw new UnauthorizedAccessException("You are not authorized to access this page because you are not an admin.");
    }

    public static void checkAccessUser(HttpSession session) throws UnauthorizedAccessException{
        
        Object b = session.getAttribute("username");
        Object a = session.getAttribute("user_role");
        if (b == null || a == null) {
            throw new UnauthorizedAccessException("You are not authorized to access this page because you are not logged in.");
        }
        
        if (((String)a).equals("user")) {
            return;
        }

        throw new UnauthorizedAccessException("You are not authorized to access this page because you are not a user.");
    }
    public UnauthorizedAccessException(String error) {
        super(error);
    }
    
    public void setAttributesForAdmin(HttpSession session, HttpServletRequest request, UnauthorizedAccessException e){
        request.setAttribute("error", "Unauthorized Access");
        request.setAttribute("code", "UnauthorizedAccessException");
        request.setAttribute("message", this.getMessage());
        request.setAttribute("causes", new String[]{"User is not an admin", "No account is in use"});
        request.setAttribute("exception", e);
        Map<String, String> navigation = new HashMap<String, String>();
        String userRole = (String) session.getAttribute("user_role");
        if (userRole != null) {
            if (userRole.equals("admin")) {
                navigation.put("Home", request.getContextPath() + "/admin/admin.jsp");
            } else if (userRole.equals("user")) {
                navigation.put("Home", request.getContextPath() + "/landing.jsp");
            }
        }
        navigation.put("Login", request.getContextPath() + "/login.jsp");
        request.setAttribute("navigation", navigation);
    }
    
    public void setAttributesForUser(HttpSession session, HttpServletRequest request, UnauthorizedAccessException e){
        request.setAttribute("error", "Unauthorized Access");
        request.setAttribute("code", "UnauthorizedAccessException");
        request.setAttribute("message", this.getMessage());
        request.setAttribute("causes", new String[]{"User is not a user", "No account is in use"});
        request.setAttribute("exception", e);
        Map<String, String> navigation = new HashMap<String, String>();
        String userRole = (String) session.getAttribute("user_role");
        //tester
        System.out.println("userrole:"+userRole);
        if (userRole != null) {
            if (userRole.equals("admin") || userRole.equals("super_admin")) {
                navigation.put("Home", request.getContextPath() + "/admin/admin.jsp");
            } else if (userRole.equals("user")) {
                navigation.put("Home", request.getContextPath() + "/landing.jsp");
            }
        }
        navigation.put("Login", request.getContextPath() + "/login.jsp");
        request.setAttribute("navigation", navigation);
    }
}
