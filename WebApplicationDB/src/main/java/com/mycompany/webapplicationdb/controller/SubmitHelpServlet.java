/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.controller;

import static com.mycompany.webapplicationdb.exception.BadRequestException.checkIfValidRequests;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.BadRequestException;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import com.mycompany.webapplicationdb.model.Messages;
import com.mycompany.webapplicationdb.ValueValidation;
import com.mycompany.webapplicationdb.ValueValidation.EmptyContentException;
import com.mycompany.webapplicationdb.ValueValidation.InvalidContentLengthException;

/**
 *
 * @author ken
 */
@WebServlet(name = "SubmitHelpServlet", urlPatterns = { "/SubmitHelpServlet" })
public class SubmitHelpServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException              if a servlet-specific error occurs
     * @throws IOException                   if an I/O error occurs
     * @throws z
     * @throws InvalidContentLengthException
     * @throws EmptyContentException
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, UnauthorizedAccessException,
            BadRequestException, InvalidContentLengthException, EmptyContentException {
        response.setContentType("text/html;charset=UTF-8");
        // check if session object has attribute username
        HttpSession session = request.getSession();
        checkAccessUser(session);

        // get current username
        // get all request parameters
        String currUser = (String) request.getParameter("username");
        // parameters
        String subject = (String) request.getParameter("subject");
        String content = (String) request.getParameter("content");
        checkIfValidRequests(currUser, subject, content);
        ValueValidation.validateContent(content);

        // add to messages
        Messages messages = new Messages();
        messages.addMessage(currUser, subject, content);

        request.setAttribute("successMessage", "Successfully Submitted");
        request.getRequestDispatcher("help.jsp").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException ex) {
            // TODO: handle exception
            System.out.println("DatabaseOperationException");
        } catch (UnauthorizedAccessException ex) {
            ex.setAttributesForUser(request.getSession(), request, ex);
        } catch (BadRequestException e) {
        } catch (InvalidContentLengthException e) {
        } catch (EmptyContentException e) {
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
