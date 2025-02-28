<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Account</title>
</head>
<body>
    <h1>Create a New Account</h1>

    <%
        //TODO: handle unexpected access
        
        // Get user role from session
        String userRole = (String) session.getAttribute("user_role");

        
    %>

    <form action="${pageContext.request.contextPath}/AdminCreateAccountServlet" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <label for="user_role">User Role:</label>
        <select id="user_role" name="user_role" required>
            <%
                if ("super_admin".equals(userRole)) {
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
    </form>

    <br>
    <a href="admin.jsp">Back to Admin Page</a>
</body>
</html>
