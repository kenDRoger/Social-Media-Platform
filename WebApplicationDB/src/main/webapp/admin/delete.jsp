<%@page import="com.mycompany.webapplicationdb.model.Account"%>
<%@page import="com.mycompany.webapplicationdb.model.Accounts"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>

<%
    session = request.getSession();
    if (session.getAttribute("username") == null) {
        //TODO: handle unexpected access

    }

    Accounts accounts = new Accounts();
    ArrayList<Account> users = accounts.getAccountsByRole("user");
    ArrayList<Account> admins = accounts.getAccountsByRole("admin");

    String userRole = "";
    if (request.getSession(false) != null) {
        userRole = (String) session.getAttribute("user_role");
    } else {
        //TODO: handle unexpected access
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Accounts</title>
       <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <ul>
                <li><a href="admin.jsp">Home</a></li>
                <li><a href="create.jsp">Create Account</a></li>
                <li><a href="update.jsp">Update Account</a></li>
                <li><a href="delete.jsp">Delete Account
                <li><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Delete Bulk Accounts</h1>
        <%            if (users == null || users.isEmpty()) {
        %>
        <h2>No Users</h2>
        <%} else {%>
        <form action="${pageContext.request.contextPath}/AdminDeleteAccountsServlet" method="post">
            <div class="accounts-block">
                <h2>List of Users</h2>
                <table border="1">
                    <tr><th>Username</th><th>Password</th><th>Role</th><th>Delete</th></tr>
                            <%
                                for (Account user : users) {
                                    if (user != null) {
                            %>
                    <tr>
                        <td><%= user.getUsername()%></td>
                        <td><%= user.getPassword()%></td>
                        <td><%= user.getUserRole()%></td>
                        <td><input type="checkbox" name="accounts" value="<%= user.getUsername()%>"></td>
                    </tr>
                    <%
                            }
                        }
                        if (userRole.equals("super_admin")) {
                            if (admins == null || admins.isEmpty()) {
                            } else {                        //TODO: wait for instructions about what to put for admin block
                                for (Account user : admins) {
                    %>
                    <tr>
                        <td><%= user.getUsername()%></td>
                        <td><%= user.getPassword()%></td>
                        <td><%= user.getUserRole()%></td>
                        <td><input type="checkbox" name="accounts" value="<%= user.getUsername()%>"></td>
                    </tr>
                    <%}
                        }%>
                </table>
                <input type="submit" value="Delete Accounts">
                <c:if test="${not empty error}">
                    <p style="color:red;">${error}</p>
                </c:if>
                <%}%>
            </div>
        </form>
        <%}%>
    </body>
</html>
