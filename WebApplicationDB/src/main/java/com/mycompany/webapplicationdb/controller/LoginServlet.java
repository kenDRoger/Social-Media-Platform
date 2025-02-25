/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.webapplicationdb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;
import com.mycompany.webapplicationdb.model.JDBCModel;

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

        //TODO: handle inexpected access(siguro check lang if nakalogin as user from session obj)

        // get the username and password from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // get username and password from database
        JDBCModel model;
        Map<String, String> map = new HashMap<>();
        String userRole = "";
        try{
            model = new JDBCModel();
            map = model.getCredentials();
            userRole = model.getUserRole(username);
        } catch (DatabaseConnectionFailedException ex) {
            //TODO: handle exception(web.xml then add an error page)
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
        
        // set session object for authentication in each page
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        session.setAttribute("user_role", userRole);

        // check if user is guest
        if (userRole.equals("guest")) {
            // redirect to admin page
            response.sendRedirect("landing.jsp");
            return;
        }

        // check if user is admin
        if (userRole.equals("admin") || userRole.equals("super_admin")) {
            // redirect to admin page
            response.sendRedirect("admin/admin.jsp");
            return;
        }

        //TODO: lalagay pa ba to?
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        try {
            processRequest(request, response);
        } catch (DatabaseConnectionFailedException ex) {
            request.setAttribute("error", "Database connection failed");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
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
        try {
            processRequest(request, response);
        } catch (DatabaseConnectionFailedException ex) {
            request.setAttribute("error", "Database connection failed");
            request.getRequestDispatcher("/error.jsp").forward(request, response);        }
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
