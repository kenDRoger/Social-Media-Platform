package com.mycompany.webapplicationdb.exception;

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
    
}
