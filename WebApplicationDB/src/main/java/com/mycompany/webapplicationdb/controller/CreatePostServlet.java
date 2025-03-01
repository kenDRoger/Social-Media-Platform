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

import com.mycompany.webapplicationdb.ValueValidation;
import com.mycompany.webapplicationdb.ValueValidation.EmptyContentException;
import com.mycompany.webapplicationdb.ValueValidation.EmptyTitleException;
import com.mycompany.webapplicationdb.ValueValidation.InvalidContentLengthException;
import com.mycompany.webapplicationdb.ValueValidation.InvalidTitleLengthException;
import com.mycompany.webapplicationdb.exception.BadRequestException;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessUser;
import com.mycompany.webapplicationdb.model.Posts;
import com.mycompany.webapplicationdb.model.PostsList;

/**
 *
 * @author ken
 */
@WebServlet(name = "CreatePostServlet", urlPatterns = { "/CreatePostServlet" })
public class CreatePostServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException                                                    if
     *                                                                             a
     *                                                                             servlet-specific
     *                                                                             error
     *                                                                             occurs
     * @throws IOException                                                         if
     *                                                                             an
     *                                                                             I/O
     *                                                                             error
     *                                                                             occurs
     * @throws
     * com.mycompany.webapplicationdb.exception.DatabaseOperationException
     * @throws EmptyTitleException
     * @throws InvalidTitleLengthException
     * @throws EmptyContentException
     * @throws InvalidContentLengthException
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, UnauthorizedAccessException,
            BadRequestException, InvalidTitleLengthException, EmptyTitleException, InvalidContentLengthException,
            EmptyContentException {
        response.setContentType("text/html;charset=UTF-8");
        // 1. HANDLE UNEXPECTED ACCESS(Session)
        // check if session object has attribute username
        HttpSession session = request.getSession();
        checkAccessUser(session);

        // get current username
        // get all request parameters
        String currUser = (String) request.getParameter("username");
        String title = (String) request.getParameter("title");
        String content = (String) request.getParameter("content");
        BadRequestException.checkIfValidRequests(currUser, title, content);
        ValueValidation.validateTitle(title);
        ValueValidation.validateContent(content);

        // TODO: double check any null pointers


        PostsList postsList = new PostsList();
        Posts posts = postsList.getPostsByUsername(currUser);

        if (posts != null) {
            posts.addPost(title, content);
        }

        response.sendRedirect("profile.jsp");

    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException e) {
            // TODO: handle exceptions
        } catch (UnauthorizedAccessException ex) {
            System.out.println("UNAUTHORIZED ACCESS");
        } catch (BadRequestException ex) {
        } catch (InvalidTitleLengthException e) {
        } catch (EmptyTitleException e) {
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
