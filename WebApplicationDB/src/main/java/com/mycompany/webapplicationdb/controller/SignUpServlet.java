/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.webapplicationdb.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vince
 */
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
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // 1. HANDLE UNEXPECTED ACCESS(Session)(for other than login and signup)
        HttpSession session = request.getSession();
        

        // 2. GET PARAMETERS
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 3. GET DATABASE DATA
        

        // 4. INITIALIZE MODELS

        // 5. SERVLET LOGIC

        // Validate for empty username or password
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Username and password cannot be empty.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }
        
        // Check if the username and password length is within varchar(30)
        if(username.length() > 30 || password.length() > 30) {
            request.setAttribute("error", "Username and password must be 30 characters or less.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        

        // Validate for valid username format (alphanumeric only)
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            request.setAttribute("error", "Username can only contain alphanumeric characters.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Validate password strength (at least 8 characters, 1 uppercase, 1 lowercase, 1 digit)
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            request.setAttribute("error", "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, and a digit.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }
        // 6. SET TO DATABASE
        

        // 7. REDIRECT LOGIC
        session.setAttribute("username", username);
        session.setAttribute("user_role", "guest");

        response.sendRedirect("landing.jsp");

        
        

    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (Exception e){
            //TODO: add exception handling
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
