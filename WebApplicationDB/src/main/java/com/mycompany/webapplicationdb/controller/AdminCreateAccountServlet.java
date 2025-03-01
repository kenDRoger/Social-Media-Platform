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

import com.mycompany.webapplicationdb.ValueValidation;
import com.mycompany.webapplicationdb.exception.BadRequestException;
import static com.mycompany.webapplicationdb.exception.BadRequestException.checkIfValidRequests;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessAdmin;
import com.mycompany.webapplicationdb.model.Account;
import com.mycompany.webapplicationdb.model.Accounts;

/**
 *
 * @author ken
 */
@WebServlet(name = "AdminCreateUserServlet", urlPatterns = { "/AdminCreateUserServlet" })
public class AdminCreateAccountServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, UnauthorizedAccessException,
            ValueValidation.InvalidUserNameLengthException, ValueValidation.InvalidPasswordLengthException,
            ValueValidation.InvalidUserRoleException, ValueValidation.EmptyUserNameException,
            ValueValidation.InvalidUserNameException, ValueValidation.EmptyPasswordException,
            ValueValidation.EmptyUserRoleException, BadRequestException {
        response.setContentType("text/html;charset=UTF-8");
        // Handle unauthorized access
        HttpSession session = request.getSession();
        checkAccessAdmin(session);
        // handle invalid parameters
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        String userRole = (String) request.getParameter("user_role");

        // Validate input based on sql constraints
        ValueValidation.validateUserName(username);
        ValueValidation.validatePassword(password);
        ValueValidation.validateUserRole(userRole);

        checkIfValidRequests(username, password, userRole);

        Accounts accounts = new Accounts();
        // Validate if username is already in database
        if (accounts.getUser(username) != null) {
            request.setAttribute("error", "Username already exists.");
            request.getRequestDispatcher("admin/create.jsp").forward(request, response);
            return;
        }

        // Create user using
        accounts.addAccount(new Account(username, password, userRole));

        // redirect
        response.sendRedirect("admin/admin.jsp");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException ex) {
            ex.setAttributes(request.getSession(), request, ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (UnauthorizedAccessException ex) {
            ex.setAttributesForAdmin(request.getSession(), request, ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (BadRequestException ex) {
            ex.setAttributes(request.getSession(), request, ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (ValueValidation.InvalidUserNameLengthException | ValueValidation.InvalidUserNameException ex) {
            request.setAttribute("error", "Username is invalid.");
            request.getRequestDispatcher("admin/create.jsp").forward(request, response);
        } catch (ValueValidation.InvalidPasswordLengthException ex) {
            request.setAttribute("error", "Password is invalid.");
            request.getRequestDispatcher("admin/create.jsp").forward(request, response);
        } catch (ValueValidation.InvalidUserRoleException ex) {
            request.setAttribute("error", "User role is invalid.");
            request.getRequestDispatcher("admin/create.jsp").forward(request, response);
        } catch (ValueValidation.EmptyUserNameException ex) {
            request.setAttribute("error", "Username is empty.");
            request.getRequestDispatcher("admin/create.jsp").forward(request, response);
        } catch (ValueValidation.EmptyPasswordException ex) {
            request.setAttribute("error", "Password is empty.");
            request.getRequestDispatcher("admin/create.jsp").forward(request, response);
        } catch (ValueValidation.EmptyUserRoleException ex) {
            request.setAttribute("error", "User role is empty.");
            request.getRequestDispatcher("admin/create.jsp").forward(request, response);
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
