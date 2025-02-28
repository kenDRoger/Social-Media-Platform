/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.webapplicationdb.controller;

import com.mycompany.webapplicationdb.ValueValidation;
import com.mycompany.webapplicationdb.exception.BadRequestException;
import static com.mycompany.webapplicationdb.exception.BadRequestException.checkIfValidRequests;
import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessAdmin;
import com.mycompany.webapplicationdb.model.Accounts;
import com.mycompany.webapplicationdb.model.Account;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ken
 */
@WebServlet(name = "AdminCreateUserServlet", urlPatterns = {"/AdminCreateUserServlet"})
public class AdminCreateAccountServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseConnectionFailedException, UnauthorizedAccessException, BadRequestException, ValueValidation.InvalidUserNameException, ValueValidation.InvalidPasswordException, ValueValidation.InvalidUserRoleException {
        response.setContentType("text/html;charset=UTF-8");
        
        //TODO: Handle unauthorized access
        HttpSession session = request.getSession();
        checkAccessAdmin(session);
        
        //handle invalid parameters
        String username = (String)request.getParameter("username");
        String password = (String)request.getParameter("password");
        String userRole = (String)request.getParameter("user_role");
        checkIfValidRequests(username,password,userRole);
        
        //TODO: Validate input based on sql constraints
        ValueValidation.validateUserName(username);
        ValueValidation.validatePassword(password);
        ValueValidation.validateUserRole(userRole);
        
        //TODO: Create user using 
        Accounts accounts = new Accounts();
        accounts.addUser(new Account(username, password, userRole));
        
        //redirect
        response.sendRedirect("admin/admin.jsp");        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            processRequest2(request, response);
        } catch (DatabaseConnectionFailedException ex) {
            //TODO: DatabaseConnectionFailedException
            System.out.println("Type of Exception: "+ex.getClass());
        } catch (UnauthorizedAccessException ex) {
            //TODO: UnauthorizedAccessException
            System.out.println("unauthoized access");
        } catch (BadRequestException ex) {
            //TODO: BadRequestException
            System.out.println("bad request");
        } catch (ValueValidation.InvalidUserNameException ex) {
            //TODO: InvalidUserNameException
            Logger.getLogger(AdminCreateAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.InvalidPasswordException ex) {
            //TODO: InvalidPasswordException
            Logger.getLogger(AdminCreateAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.InvalidUserRoleException ex) {
            //TODO: InvalidUserRoleException
            Logger.getLogger(AdminCreateAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
