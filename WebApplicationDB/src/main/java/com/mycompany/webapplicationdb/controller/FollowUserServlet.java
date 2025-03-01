/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.webapplicationdb.controller;

import com.mycompany.webapplicationdb.exception.SameUserFoundException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.FullFollowsException;
import com.mycompany.webapplicationdb.exception.NoUserFoundException;
import com.mycompany.webapplicationdb.exception.AlreadyFollowedException;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessUser;
import com.mycompany.webapplicationdb.model.Following;
import com.mycompany.webapplicationdb.model.Follows;

/**
 *
 * @author ken
 */
@WebServlet(name = "FollowUserServlet", urlPatterns = { "/FollowUserServlet" })
public class FollowUserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException            if a servlet-specific error occurs
     * @throws IOException                 if an I/O error occurs
     * @throws NoUserFoundException
     * @throws FullFollowsException
     * @throws AlreadyFollowedException
     * @throws UnauthorizedAccessException
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, UnauthorizedAccessException,
            SameUserFoundException, NoUserFoundException, FullFollowsException, AlreadyFollowedException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        checkAccessUser(session);

        // get request parameters
        String username = request.getParameter("newUser");
        String currUser = (String) request.getParameter("currUser");

        checkIfValidRequests(username, currUser);

        // call model
        Following following = new Following();
        if (username.equals(currUser)) {
            throw new SameUserFoundException();
        }
        if (!following.ifUsernameExists(username)) {
            throw new NoUserFoundException();
        }
        Follows follows = following.getFollowsByUsername(currUser);
        if (follows == null) {
            throw new NoUserFoundException();
        }
        follows.addFollow(username);

        // redirect to users.jsp success unfollow
        request.setAttribute("successFollow", "Successfully followed: " + username);
        request.getRequestDispatcher("users.jsp").forward(request, response);
    }

    private void checkIfValidRequests(String username, String currUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkIfValidRequests'");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException e) {
            // TODO: handle catch
        } catch (UnauthorizedAccessException e) {
        } catch (SameUserFoundException e) {
        } catch (NoUserFoundException e) {
        } catch (FullFollowsException e) {
        } catch (AlreadyFollowedException e) {
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
