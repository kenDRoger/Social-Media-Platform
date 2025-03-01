/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.ValueValidation;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import com.mycompany.webapplicationdb.model.Account;
import com.mycompany.webapplicationdb.model.Accounts;

/**
 *
 * @author ken
 */
@WebServlet(name = "AdminUpdateAccountsServlet", urlPatterns = {"/AdminUpdateAccountsServlet"})
public class AdminUpdateAccountsServlet extends HttpServlet {

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
            throws ServletException, IOException, DatabaseOperationException, UnauthorizedAccessException,
            ValueValidation.InvalidUserNameLengthException, ValueValidation.EmptyUserNameException,
            ValueValidation.InvalidUserNameException, ValueValidation.InvalidPasswordLengthException,
            ValueValidation.EmptyPasswordException, ValueValidation.InvalidUserRoleException,
            ValueValidation.EmptyUserRoleException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        UnauthorizedAccessException.checkAccessAdmin(session);

        Accounts accounts = new Accounts();
        // get parameters
        Enumeration<String> paramNames = request.getParameterNames();

        // get unupdated accounts
        ArrayList<Account> beforeUpdate = new ArrayList<>();

        ArrayList<Account> afterUpdate = new ArrayList<>();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith("username_")) {

                String oldUsername = paramName.substring(9); // Extract original username
                //tester
                System.out.println("oldUsername:" + oldUsername);

                Account oldaccount = accounts.findAccountByUsername(oldUsername);
                beforeUpdate.add(oldaccount);

                String newUsername = request.getParameter("username_" + oldUsername);
                String newPassword = request.getParameter("password_" + oldUsername);
                String newUserRole = request.getParameter("user_role_" + oldUsername);
                ValueValidation.validateUserName(newUsername);
                ValueValidation.validatePassword(newPassword);
                ValueValidation.validateUserRole(newUserRole);
                Account account = new Account(newUsername, newPassword, newUserRole);
                afterUpdate.add(account);
                // Update the user in the database
                accounts.updateAccount(oldUsername, account);
            }
        }

        request.setAttribute("result", beforeUpdate);
        request.setAttribute("result1", afterUpdate);
        request.setAttribute("message", "Before Update accounts: " + beforeUpdate.size());
        request.setAttribute("message1", "After Update accounts: " + afterUpdate.size());
        request.getRequestDispatcher("admin/result.jsp").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            processRequest2(request, response);
        } catch (DatabaseOperationException ex) {
            // TODO: handle exceptions
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnauthorizedAccessException ex) {
            ex.setAttributesForAdmin(request.getSession(), request, ex);
        } catch (ValueValidation.InvalidUserNameLengthException ex) {
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.EmptyUserNameException ex) {
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.InvalidUserNameException ex) {
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.InvalidPasswordLengthException ex) {
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.EmptyPasswordException ex) {
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.InvalidUserRoleException ex) {
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValueValidation.EmptyUserRoleException ex) {
            Logger.getLogger(AdminUpdateAccountsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
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
