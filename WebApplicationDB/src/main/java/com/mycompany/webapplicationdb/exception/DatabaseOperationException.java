package com.mycompany.webapplicationdb.exception;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DatabaseOperationException extends Exception {

    public DatabaseOperationException(String message, Throwable cause) {
        super(message,cause);
    }
    //add String message as parameters in const
    
    public void setAttributes(HttpSession session, HttpServletRequest request, DatabaseOperationException e){
        request.setAttribute("error", "Database Related Error");
        request.setAttribute("code", "DatabaseOperationException");
        request.setAttribute("message", this.getMessage());
        request.setAttribute("causes", new String[]{"Database connection failed", "Database query failed"});
        request.setAttribute("exception", e);
        Map<String, String> navigation = new HashMap<String, String>();
        String userRole = (String) session.getAttribute("user_role");
        if (userRole != null) {
            if (userRole.equals("admin")) {
                navigation.put("Home", request.getContextPath() + "/admin/admin.jsp");
            } else if (userRole.equals("user")) {
                navigation.put("Home", request.getContextPath() + "/landing.jsp");
                navigation.put("Submit a Ticket", request.getContextPath() + "/help.jsp");
            }
        }
        navigation.put("Login", request.getContextPath() + "/login.jsp");
        request.setAttribute("navigation", navigation);
    }
}
