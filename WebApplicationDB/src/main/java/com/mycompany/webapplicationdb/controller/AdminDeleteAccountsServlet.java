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

import com.mycompany.webapplicationdb.exception.BadRequestException;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.UnauthorizedAccessException;
import static com.mycompany.webapplicationdb.exception.UnauthorizedAccessException.checkAccessAdmin;
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
        //TODO: handle unexpected access
        HttpSession session = request.getSession();
        checkAccessAdmin(session);
        
        Object[] accountsParam = request.getParameterValues("accounts");
        if(accountsParam==null || accountsParam.length==0){
            request.setAttribute("error", "No selected Accounts");
            request.getRequestDispatcher("admin/delete.jsp").forward(request, response);
        }
        BadRequestException.checkIfValidRequests(accountsParam);
        
        Accounts accounts = new Accounts();
        accounts.deleteBulkAccounts(accountsParam);

        //TODO: handle valid parameters
        
        
        //redirect
        response.sendRedirect("admin/admin.jsp");
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            processRequest2(request, response);
        } catch (DatabaseOperationException ex) {
            //TODO: handle exceptions
            
            
        } catch (UnauthorizedAccessException ex) {
        } catch (BadRequestException ex) {
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
