<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.mycompany.webapplicationdb.exception.UnauthorizedAccessException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>

<%
//mema nalang mag kopya
    try {
        UnauthorizedAccessException.checkAccessAdmin(session);
    } catch (UnauthorizedAccessException e) {
        e.setAttributesForAdmin(session, request, e);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Account</title>
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
        <h1>Create a New Account</h1>

        <%
            //TODO: handle unexpected access
            // Get user role from session
            String userRole = (String) session.getAttribute("user_role");


        %>
        <div class="accounts-block">
            <form action="${pageContext.request.contextPath}/AdminCreateAccountServlet" method="post">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required><br><br>

                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required><br><br>

                <label for="user_role">User Role:</label>
                <select id="user_role" name="user_role" required>
                    <%                if ("super_admin".equals(userRole)) {
                    %>
                    <option value="admin">Admin</option>
                    <option value="user">User</option>
                    <%
                    } else if ("admin".equals(userRole)) {
                    %>
                    <option value="user">User</option>
                    <%
                        }
                    %>
                </select><br><br>

                <input type="submit" value="Create Account">
                <c:if test="${not empty error}">
                    <p style="color:red;">${error}</p>
                </c:if>
            </form>
        </div>

    </body>
</html>
