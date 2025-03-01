/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.exception;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ken
 */
public class BadRequestException extends Exception {

    /**
     * Param should be
     *
     * @param nonnull
     * @throws com.mycompany.webapplicationdb.exception.BadRequestException
     */
    public static void checkIfValidRequests(Object... nonnull) throws BadRequestException {
        if (nonnull == null) {
            throw new BadRequestException("Request has Empty Parameters");
        }

        for (Object obj : nonnull) {
            if (obj == null || obj.toString().isEmpty()) {
                throw new BadRequestException("Request invalid");
            }
        }
    }

    public BadRequestException(String message) {
        super(message);
    }
    
    public void setAttributes(HttpSession session, HttpServletRequest request, BadRequestException e){
        request.setAttribute("error", "Bad Request");
        request.setAttribute("code", "BadRequestException");
        request.setAttribute("message", this.getMessage());
        request.setAttribute("causes", new String[]{"Missing parameters", "Invalid request format"});
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
}
