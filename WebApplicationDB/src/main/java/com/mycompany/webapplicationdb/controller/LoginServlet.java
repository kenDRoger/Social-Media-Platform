/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.controller;

import com.mycompany.webapplicationdb.ValueValidation.EmptyPasswordException;
import com.mycompany.webapplicationdb.ValueValidation.EmptyUserNameException;
import com.mycompany.webapplicationdb.ValueValidation.InvalidPasswordLengthException;
import com.mycompany.webapplicationdb.ValueValidation.InvalidUserNameException;
import com.mycompany.webapplicationdb.ValueValidation.InvalidUserNameLengthException;
import com.mycompany.webapplicationdb.exception.BadRequestException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.model.Account;
import com.mycompany.webapplicationdb.model.Accounts;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mycompany.webapplicationdb.ValueValidation.validatePassword;
import static com.mycompany.webapplicationdb.ValueValidation.validateUserName;

/**
 *
 * @author ken
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {

    // New method for processrequest to handle the exceptions
    protected void processRequest2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DatabaseOperationException, BadRequestException,
            InvalidUserNameLengthException, EmptyUserNameException, InvalidUserNameException,
            InvalidPasswordLengthException, EmptyPasswordException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        // 2. GET PARAMETERS
        // get the username and password from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        BadRequestException.checkIfValidRequests(username, password);
        validateUserName(username);
        validatePassword(password);

        // 3. GET DATABASE DATA
        // 4. INITIALIZE MODELS
        Accounts accounts = new Accounts();
        Map<String, String> map = accounts.getCredentials();

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

        Account account = accounts.findAccountByUsername(username);
        String userRole = account.getUserRole();
        // set attribute for authentication in each page
        session.setAttribute("username", username);
        session.setAttribute("user_role", userRole);

        // 6. REDIRECT LOGIC
        // check if user is user
        if (userRole.equals("user")) {
            // forward to landing page
            response.sendRedirect("landing.jsp");
            return;
        }

        // check if user is admin
        if (userRole.equals("admin")) {
            response.sendRedirect("admin/admin.jsp");
            return;
        }

        // check if user is super admin
        if (userRole.equals("super_admin")) {
            // forward to admin page
            response.sendRedirect("admin/admin.jsp");
            return;
        }
    }

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
            throws ServletException, IOException {
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException ex) {
            // TODO: Double check navigation
            request.setAttribute("error", "Database connection failed - LoginServlet");
            request.setAttribute("title", "Database Connection Failed");
            request.setAttribute("message", "The database connection failed. Please try again later.");
            request.setAttribute("causes", new String[] {
                    "The database may be down for maintenance.",
                    "The database may be experiencing heavy load.",
                    "There may be a problem with the database connection."
            });
            Map<String, String> navigation = new HashMap<>();
            navigation.put("Try again later", "login.jsp");
            request.setAttribute("navigation", navigation);
            request.setAttribute("code", "500");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (BadRequestException ex) {
        } catch (InvalidUserNameLengthException e) {
        } catch (EmptyUserNameException e) {
        } catch (InvalidUserNameException e) {
        } catch (InvalidPasswordLengthException e) {
        } catch (EmptyPasswordException e) {
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
