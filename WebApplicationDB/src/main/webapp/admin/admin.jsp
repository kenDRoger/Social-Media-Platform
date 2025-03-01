<%-- Document : admin Created on : 02 24, 25, 3:10:37 PM Author : ken --%>
<%@page import="com.mycompany.webapplicationdb.exception.UnauthorizedAccessException"%>
<%@page import="com.mycompany.webapplicationdb.model.Message"%>
<%@page import="com.mycompany.webapplicationdb.model.Messages"%>
<%@page import="com.mycompany.webapplicationdb.model.Accounts"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.webapplicationdb.model.Account"%>
<%@page import="javax.servlet.http.HttpSession"%>
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

    ArrayList<Message> messages = new Messages().get5LatestMessages();

    String userRole = (String) session.getAttribute("user_role");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Admin Home Page</title>
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


        <h1>Admin Home Page</h1>

        <%            if (users == null || users.isEmpty()) {
        %>
        <h2>No Users</h2>

        <%} else {%>
        <div class="users-block">
            <h2>List of Users</h2>
            <table border="1">
                <tr><th>Username</th><th>Role</th></tr>
                        <%
                            for (Account user : users) {
                                if (user != null) {
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

        </div>
        <%}%>


        <%            if (userRole.equals("super_admin")) {
                if (admins == null || admins.isEmpty()) {
        %>
        <h2>No Admins</h2>
        <%
        } else {
        %>
        <div class="super_admin-block">
            <h2>List of Admins</h2>
            <table border="1">
                <tr><th>Username</th><th>Role</th></tr>
                        <%                            //TODO: wait for instructions about what to put for admin block
                            for (Account user : admins) {
                                if (user != null) {
                        %>
                <tr>
                    <td><%= user.getUsername()%></td>
                    <td><%= user.getUserRole()%></td>
                </tr>
                <%
                        }
                    }%>

            </table>
        </div>
        <%}
            }
            System.out.println("Messages null:" + messages == null);
            System.out.println("Messages isempty:" + messages.isEmpty());
            if (messages == null || messages.isEmpty()) {%>
        <h2>No Messages to Admins</h2>
        <%} else {%>
        <div class="messages-block">
            <h2>List of Messages to Admins</h2>
            <table border="1">
                <tr>
                    <th>Subject</th>
                    <th>Content</th>
                    <th>Date Created</th>
                    <th>Resolve</th>
                </tr>
                <%if (messages != null) {
                        for (Message message : messages) {

                            if (message != null) {
                %>
                <tr>
                    <td><%= message.getSubject()%></td>
                    <td><%= message.getContent()%></td>
                    <td><%= message.getDate_created().toString()%></td>
                    <td><form action="${pageContext.request.contextPath}/AdminResolveServlet" method="post">
                            <input type="hidden" name="subject" value="<%= message.getSubject()%>" />
                            <input type="submit" value="Resolve" />
                        </form></td>
                </tr>
                <%}
                        }
                    }%>
            </table>
        </div>
        <%}%>

    </body>
</html>
