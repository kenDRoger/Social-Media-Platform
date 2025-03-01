/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.webapplicationdb.controller;

import com.mycompany.webapplicationdb.ValueValidation;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.model.Account;
import com.mycompany.webapplicationdb.model.Accounts;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SignUpServlet", urlPatterns = {"/SignUpServlet"})
public class SignUpServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws DatabaseOperationException 
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, ValueValidation.InvalidUserNameLengthException, ValueValidation.InvalidPasswordLengthException, ValueValidation.EmptyUserNameException, ValueValidation.InvalidUserNameException, ValueValidation.EmptyPasswordException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();        

        // 2. GET PARAMETERS
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate for empty username or password   
        ValueValidation.validateUserName(username);
        ValueValidation.validatePassword(password);
        
        Accounts accounts = new Accounts();
        
        // Validate if username is already in database
        if (accounts.getUser(username) != null) {
            request.setAttribute("error", "Username already exists.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }
        
        // 6. SET TO DATABASE
        accounts.addUser(new Account(username, password, "user"));
        //tester TODO: test login of new user
        System.out.println("Created new user");
        

        // 7. REDIRECT LOGIC
        session.setAttribute("username", username);
        session.setAttribute("user_role", "user");

        response.sendRedirect("landing.jsp");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException e){
            //TODO: add exception handling
        } catch (ValueValidation.InvalidUserNameLengthException ex) {
            request.setAttribute("error", "Username must be 30 characters or less.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        } catch (ValueValidation.InvalidPasswordLengthException ex) {
            request.setAttribute("error", "Password must be 30 characters or less.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        } catch (ValueValidation.EmptyUserNameException ex) {
            request.setAttribute("error", "Username cannot be empty.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        } catch (ValueValidation.InvalidUserNameException ex) {
            request.setAttribute("error", "Username can only contain alphanumeric characters.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        } catch (ValueValidation.EmptyPasswordException ex) {
            request.setAttribute("error", "Password cannot be empty.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
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
