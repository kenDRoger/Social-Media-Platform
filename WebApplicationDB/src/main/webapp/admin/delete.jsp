<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Accounts</title>
    </head>
    <body>
        <h1>Delete Bulk Accounts</h1>

        <%
            //TODO: handle unexpected access

            // Get user role from session
            String userRole = (String) session.getAttribute("user_role");


        %>

        

        <br>
        <a href="admin.jsp">Back to Admin Page</a>
    </body>
</html>
