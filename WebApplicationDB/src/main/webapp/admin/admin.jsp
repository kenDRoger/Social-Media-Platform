<%-- Document : admin Created on : 02 24, 25, 3:10:37 PM Author : ken --%>
<%@page import="com.mycompany.webapplicationdb.model.Accounts"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.webapplicationdb.model.User"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session = request.getSession();
    if(session.getAttribute("username") == null){
        //TODO: handle unexpected access
        
    }
    
    Accounts accounts = new Accounts();
    ArrayList<User> guests = accounts.getAccountsByRole("guest");
    ArrayList<User> admins = accounts.getAccountsByRole("admin");
%>
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
                <tr><th>Username</th><th>Role</th></tr>
                        <%
                            String userRole = "";
                            if (request.getSession(false) != null) {
                                userRole = (String) session.getAttribute("user_role");
                            } else {
                                //TODO: handle unexpected access
                            }

                            if (guests != null) {
                                for (User user : guests) {

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
        <div class="super_admin-block">
            <h2>List of Admins</h2>
            <table border="1">
                <%
                    //TODO: wait for instructions about what to put for admin block
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
