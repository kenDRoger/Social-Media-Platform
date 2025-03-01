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

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import com.mycompany.webapplicationdb.model.Messages;

/**
 *
 * @author ken
 */
@WebServlet(name = "SubmitHelpServlet", urlPatterns = {"/SubmitHelpServlet"})
public class SubmitHelpServlet extends HttpServlet {

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
            throws ServletException, IOException, DatabaseOperationException, UnauthorizedAccessException {
        response.setContentType("text/html;charset=UTF-8");
        // check if session object has attribute username
        HttpSession session = request.getSession();
        
        //tester
        System.out.println("username:"+(String)session.getAttribute("username"));
        System.out.println("role:"+(String)session.getAttribute("user_role"));
        
        UnauthorizedAccessException.checkAccessUser(session);
        //TODO: double check any null pointers

        //get current username
        //get all request parameters
        String currUser = (String) request.getParameter("username");
        if(currUser == null){
            System.out.println("currUser null");
            return;
        }
        
        //parameters
        String subject = (String) request.getParameter("subject");
        String content = (String) request.getParameter("content");
        
        //add to messages
        Messages messages = new Messages();
        messages.addMessage(currUser,subject,content);
        
        //redirect TODO: redirects to 
        request.setAttribute("successMessage", "Successfully Submitted");
        request.getRequestDispatcher("help.jsp").forward(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            processRequest2(request, response);
        } catch (DatabaseOperationException ex) {
            //TODO: handle exception
            System.out.println("DatabaseOperationException");
        } catch (UnauthorizedAccessException ex) {
            System.out.println("UnauthorizedAccessException");
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
