/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.BadRequestException;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.NoUserFoundException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;

import static com.mycompany.webapplicationdb.exception.BadRequestException.checkIfValidRequests;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessUser;
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
     * @throws ServletException            if a servlet-specific error occurs
     * @throws IOException                 if an I/O error occurs
     * @throws NoUserFoundException
     * @throws UnauthorizedAccessException
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, NoUserFoundException,
            UnauthorizedAccessException, BadRequestException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        checkAccessUser(session);

        // get request parameters
        String username = request.getParameter("username");
        String currUser = request.getParameter("currUser");
        checkIfValidRequests(username, currUser);

        // call model
        Following following = new Following();
        Follows follows = following.getFollowsByUsername(currUser);
        if (follows == null) {
            throw new NoUserFoundException();
        }
        if (username == null) {
            throw new BadRequestException("Username parameter is null");
        }
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
            //TODO: handle exception
        } catch (NoUserFoundException e) {
        } catch (UnauthorizedAccessException e) {
            e.setAttributesForUser(request.getSession(), request, e);
        } catch (BadRequestException e) {
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
