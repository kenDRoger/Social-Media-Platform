/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mycompany.webapplicationdb.exception.BadRequestException;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessAdmin;
import com.mycompany.webapplicationdb.model.Account;
import com.mycompany.webapplicationdb.model.Accounts;

/**
 *
 * @author ken
 */
@WebServlet(name = "DeleteAccountsServlet", urlPatterns = {"/DeleteAccountsServlet"})
public class AdminDeleteAccountsServlet extends HttpServlet {

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
            throws ServletException, IOException, DatabaseOperationException, UnauthorizedAccessException, BadRequestException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        checkAccessAdmin(session);
        
        Object[] accountsParam = request.getParameterValues("accounts");
        if(accountsParam==null || accountsParam.length==0){
            request.setAttribute("error", "No selected Accounts");
            request.getRequestDispatcher("admin/delete.jsp").forward(request, response);
            return;
        }
        BadRequestException.checkIfValidRequests(accountsParam);

        Accounts accounts = new Accounts();
        //get a list of accounts of accountsParam
        ArrayList<Account> deletingAccounts = new ArrayList<>();
        for(Object account : accountsParam){
            if(account==null){
                throw new BadRequestException("Null account");
            }
            String name = (String) account;
            Account a = accounts.findAccountByUsername(name);
            if(a==null){
                throw new BadRequestException("Account not found");
            }
            deletingAccounts.add(a);
        }
        accounts.deleteBulkAccounts(accountsParam);

        request.setAttribute("message", "Deleted Accounts");
        request.setAttribute("result", deletingAccounts);
        request.getRequestDispatcher("admin/result.jsp").forward(request, response);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
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
