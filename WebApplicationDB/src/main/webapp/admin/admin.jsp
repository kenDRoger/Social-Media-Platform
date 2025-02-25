<%-- Document : admin Created on : 02 24, 25, 3:10:37 PM Author : ken --%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.webapplicationdb.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Admin Home Page</title>
    </head>
    <body>
        <h1>Admin Home Page</h1>
        <div class="guests-block">
            <h2>List of Guests</h2>
            <table border="1">
                <!--TODO: Iterate through the list of guests using JDBCModel object and its function -->

                <tr><th>Username</th><th>Role</th></tr>
                        <%
                            String userRole = "";
                            if (request.getSession(false) != null) {
                                userRole = (String) session.getAttribute("user_role");
                            } else {
                                //TODO: handle unexpected access
                            }

                            List<User> users = (List<User>) session.getAttribute("guests");//Must use diamond operator with Datatype

                            if (users != null) {
                                for (User user : users) {

                        %>
                <tr>
                    <td><%= user.getUsername()%></td>
                    <td><%= user.getUserRole()%></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
            <div>
                <button onclick="window.location.href = 'create.jsp'">Create User</button>
                <button onclick="window.location.href = 'update.jsp'">Update User</button>
                <button onclick="window.location.href = 'delete.jsp'">Delete User</button>
            </div>
        </div>
        <%if (userRole.equals("super_admin")) {%>
        <div class="admin-block">
            <h2>List of Admins</h2>
            <table border="1">
                <%
                    List<User> admins = (List<User>) session.getAttribute("admins");

                    for (User user : admins) {
                %>
                <tr>
                    <td><%= user.getUsername()%></td>
                    <td><%= user.getUserRole()%></td>
                </tr>
                <%
                    }%>

            </table>
            <div>
                <button onclick="window.location.href = 'create.jsp'">Create User</button>
                <button onclick="window.location.href = 'update.jsp'">Update User</button>
                <button onclick="window.location.href = 'delete.jsp'">Delete User</button>
            </div>
        </div>
        <%
            }
        %>


    </body>
</html>
