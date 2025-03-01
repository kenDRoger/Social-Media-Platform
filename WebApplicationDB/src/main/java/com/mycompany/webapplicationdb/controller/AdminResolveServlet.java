/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.controller;

import com.mycompany.webapplicationdb.exception.BadRequestException;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import com.mycompany.webapplicationdb.model.Messages;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "AdminResolveServlet", urlPatterns = {"/AdminResolveServlet"})
public class AdminResolveServlet extends HttpServlet {

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
            throws ServletException, IOException, UnauthorizedAccessException, BadRequestException, DatabaseOperationException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        UnauthorizedAccessException.checkAccessAdmin(session);
        
        String subject = request.getParameter("subject");
        BadRequestException.checkIfValidRequests(subject);
        
        Messages messages = new Messages();
        
        messages.deleteMessage(subject);
        
        //redirect to admin
        response.sendRedirect("admin/admin.jsp");
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            processRequest2(request, response);
        } catch (UnauthorizedAccessException ex) {
            ex.setAttributesForAdmin(request.getSession(), request, ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (BadRequestException ex) {
            ex.setAttributes(request.getSession(), request, ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (DatabaseOperationException ex) {
            ex.setAttributes(request.getSession(), request, ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
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
