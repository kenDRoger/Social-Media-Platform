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

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.NoUserFoundException;
import com.mycompany.webapplicationdb.model.Following;
import com.mycompany.webapplicationdb.model.Follows;

/**
 *
 * @author ken
 */
@WebServlet(name = "UnfollowUserServlet", urlPatterns = { "/UnfollowUserServlet" })
public class UnfollowUserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException     if a servlet-specific error occurs
     * @throws IOException          if an I/O error occurs
     * @throws NoUserFoundException
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, NoUserFoundException {
        response.setContentType("text/html;charset=UTF-8");
        // TODO: handle unexcpected access

        // TODO: handlee null parameteers

        // get request parameters
        String username = request.getParameter("username");
        String currUser = (String) request.getParameter("currUser");

        // call model
        Following following = new Following();
        Follows follows = following.getFollowsByUsername(currUser);
        follows.removeFollow(username);

        // redirect to users.jsp success unfollow
        request.setAttribute("successUnfollow", "Successfully unfollowed: " + username);
        request.getRequestDispatcher("users.jsp").forward(request, response);

    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException e) {

        } catch (NoUserFoundException e) {
            // TODO Auto-generated catch block
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
        processRequest(request, response);
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
