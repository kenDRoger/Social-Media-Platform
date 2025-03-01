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
import com.mycompany.webapplicationdb.exception.NoPostFoundException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import com.mycompany.webapplicationdb.model.Posts;
import com.mycompany.webapplicationdb.model.PostsList;

/**
 *
 * @author ken
 */
@WebServlet(name = "DeletePostServlet", urlPatterns = { "/DeletePostServlet" })
public class DeletePostServlet extends HttpServlet {

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
     * @throws com.mycompany.webapplicationdb.exception.DatabaseOperationException
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, NoPostFoundException,
            UnauthorizedAccessException, BadRequestException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        checkAccessUser(session);

        // request parameters
        String postId = request.getParameter("postId");
        String currUser = request.getParameter("username");

        checkIfValidRequests(postId, currUser);

        if (postId == null || currUser == null) {
            throw new BadRequestException("Bad request");
        }

        int id = Integer.parseInt(postId);

        // get all the posts
        PostsList postsList;
        try {
            postsList = new PostsList();
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Unable to get all posts", e);
        }

        // get all posts of currUser
        Posts posts = postsList.getPostsByUsername(currUser);

        if (posts == null) {
            throw new NoPostFoundException("No post found of user " + currUser);
        }

        // delete the post
        try {
            posts.deletePost(id);
        } catch (DatabaseOperationException e) {
            throw new DatabaseOperationException("Unable to delete post", e);
        }

        // redirect
        response.sendRedirect("profile.jsp");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException e) {
            e.setAttributes(request.getSession(), request, e);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (NoPostFoundException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (UnauthorizedAccessException e) {
            e.setAttributesForUser(request.getSession(), request, e);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (BadRequestException e) {
            e.setAttributes(request.getSession(), request, e);
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
