
<%@page import="com.mycompany.webapplicationdb.exception.UnauthorizedAccessException"%>
<%@page import="com.mycompany.webapplicationdb.model.Account"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.webapplicationdb.model.Accounts"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    //mema nalang mag kopya
    try {
        UnauthorizedAccessException.checkAccessAdmin(session);
    } catch (UnauthorizedAccessException e) {
        e.setAttributesForAdmin(session, request, e);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
    }

    Accounts accounts = new Accounts();
    ArrayList<Account> users = accounts.getAccountsByRole("user");
    ArrayList<Account> admins = accounts.getAccountsByRole("admin");

    String userRole = (String) session.getAttribute("user_role");

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/admin/admin.jsp">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/create.jsp">Create Account</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/update.jsp">Update Account</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/delete.jsp">Delete Account
                        <li><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Update Accounts</h1>
        <%            if (users == null || users.isEmpty()) {
        %>
        <h2>No Users</h2>
        <%} else {%>
        <form action="${pageContext.request.contextPath}/AdminUpdateAccountsServlet" method="post">
            <div class="accounts-block">
                <input type="submit" value="Update Accounts">
            <c:if test="${not empty error}">
                <p style="color:red;">${error}</p>
            </c:if>
                <h2>List of Users</h2>
                <table border="1">
                    <tr><th>Username</th><th>Password</th><th>Role</th><th>New Username</th><th>New Password</th><th>New User Role</th></tr>
                            <%
                                for (Account user : users) {
                                    if (user != null) {
                            %>
                    <tr>
                        <td><%= user.getUsername()%></td>
                        <td><%= user.getPassword()%></td>
                        <td><%= user.getUserRole()%></td>
                        <!-- TODO: Double check -->
                        <td><input type="text" name="username_<%= user.getUsername()%>" value="<%= user.getUsername()%>"></td>
                        <td><input type="password" name="password_<%= user.getUsername()%>" value="<%= user.getPassword()%>"></td>
                        <td>
                            <select name="user_role_<%= user.getUsername()%>" required>
                                <option value="user"selected>User</option>
                                <%if ("super_admin".equals(userRole)) {
                                %>
                                <option value="admin">Admin</option>
                                <%
                                    }
                                %>

                            </select>
                        </td>

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
                        <td><input type="text" name="username_<%= user.getUsername()%>" value="<%= user.getUsername()%>"></td>
                        <td><input type="password" name="password_<%= user.getUsername()%>" value="<%= user.getPassword()%>"></td>
                        <td>
                            <select name="user_role_<%= user.getUsername()%>" required>
                                <option value="admin"selected>Admin</option>
                                <option value="user">User</option>
                            </select>
                        </td>

                    </tr>
                    <%}
                        }%>
                </table>

                <%}%>

            </div>
            
        </form>
        <%}%>

    </body>
</html>
