/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.webapplicationdb.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;
import com.mycompany.webapplicationdb.model.Accounts;
import com.mycompany.webapplicationdb.model.JDBCModel;
import com.mycompany.webapplicationdb.model.MySQLCredentials;
import com.mycompany.webapplicationdb.model.User;

/**
 *
 * @author ken
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseConnectionFailedException {
        response.setContentType("text/html;charset=UTF-8");

        // get the username and password from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // get username and password from database
        JDBCModel model;
        Map<String, String> map = new HashMap<>();
        String userRole = "";

        // check if session object has attribute username

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            // TODO: handle unexpected access(siguro check lang if nakalogin as user from
            // session obj)

        }
        // check if session object has attribute username(if user is already logged in)
        if (session.getAttribute("username") != null) {
            try {
                model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                map = model.getCredentials();
                userRole = model.getUserRole(username);
            } catch (DatabaseConnectionFailedException ex) {
                // TODO: handle exception(web.xml then add an error page)
                request.setAttribute("error", "Database connection failed");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }

            // check if username exists
            if (!map.containsKey(username)) {
                request.setAttribute("error", "Username does not exist");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            // check if password is correct
            if (!map.get(username).equals(password)) {
                request.setAttribute("error", "Password is incorrect");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // set attribute for authentication in each page
            session.setAttribute("username", username);
            session.setAttribute("user_role", userRole);
        }

        // check if user is guest
        if (userRole.equals("guest")) {
            // TODO: set request attribute for landing page

            // forward to landing page
            request.getRequestDispatcher("landing.jsp").forward(request, response);
            return;
        }

        // get list of users
        JDBCModel model2;
        model2 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        Accounts guests = new Accounts();
        for (User user : model2.getAccountsByRole("guest")) {
            guests.addUser(user);
        }

        // set request attribute for admin.jsp
        request.setAttribute("guests", guests);

        // check if user is admin
        if (userRole.equals("admin")) {
            // TODO: handle exception(web.xml then add an error page)
            request.setAttribute("error", "Database connection failed");
            request.getRequestDispatcher("/error.jsp").forward(request, response);

            // forward to admin page
            request.getRequestDispatcher("admin/admin.jsp").forward(request, response);
            return;
        }

        // check if user is super admin
        if (userRole.equals("super_admin")) {
            // get list of users
            JDBCModel model3;
            model3 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
            Accounts admins = new Accounts();
            // TODO: Additional code for when super admin is confirmed to be able to CRUD
            for (User user : model3.getAccountsByRole("admin")) {
                admins.addUser(user);
            }

            // set attribute for admin.jsp
            request.setAttribute("users", admins);
            // TODO: web.xml then add an error page
            request.setAttribute("error", "Database connection failed");
            request.getRequestDispatcher("/error.jsp").forward(request, response);

            // forward to admin page
            request.getRequestDispatcher("admin/super_admin.jsp").forward(request, response);
            return;
        }
    }

    protected void processRequestException(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DatabaseConnectionFailedException ex) {
            request.setAttribute("error", "Database connection failed");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequestException(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequestException(request, response);
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
