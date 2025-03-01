<%-- Document : admin Created on : 02 24, 25, 3:10:37 PM Author : ken --%>
<%@page import="com.mycompany.webapplicationdb.model.Message"%>
<%@page import="com.mycompany.webapplicationdb.model.Messages"%>
<%@page import="com.mycompany.webapplicationdb.model.Accounts"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.webapplicationdb.model.Account"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    session = request.getSession();
    if (session.getAttribute("username") == null) {
        //TODO: handle unexpected access

    }

    Accounts accounts = new Accounts();
    ArrayList<Account> users = accounts.getAccountsByRole("user");
    ArrayList<Account> admins = accounts.getAccountsByRole("admin");

    ArrayList<Message> messages = new Messages().get5LatestMessages();

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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Admin Home Page</title>
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <ul>
                <li><a href="admin.jsp">Home</a></li>
                <div>
                    <button onclick="window.location.href = 'create.jsp'">Create Account</button>
                    <button onclick="window.location.href = 'update.jsp'">Update Account</button>
                    <button onclick="window.location.href = 'delete.jsp'">Delete Account</button>
                </div>
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
                        %>
                <tr>
                    <td><%= user.getUsername()%></td>
                    <td><%= user.getUserRole()%></td>
                </tr>
                <%

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
                        %>
                <tr>
                    <td><%= user.getUsername()%></td>
                    <td><%= user.getUserRole()%></td>
                </tr>
                <%}%>

            </table>
        </div>
        <%}
            }
System.out.println("Messages null:"+messages == null);
System.out.println("Messages isempty:"+messages.isEmpty());
            if (messages == null || messages.isEmpty()) {%>
            <h2>No Messages to Admins</h2>
            <%}else{%>
        <div class="messages-block">
            <h2>List of Messages to Admins</h2>
            <table border="1">
                <tr>
                    <th>Subject</th>
                    <th>Content</th>
                    <th>Date Created</th>
                    <th>Resolve</th>
                </tr>
                <%
                    for (Message message : messages) {

                        if (message != null) {


                %>
                <tr>
                    <td><%= message.getSubject()%></td>
                    <td><%= message.getContent()%></td>
                    <td><%= message.getDate_created().toString()%></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/ResolveServlet" method="post">
                            <input type="hidden" name="subject" value="<%= message.getSubject()%>" />
                            <input type="submit" value="Resolve" />
                        </form>
                    </td>
                </tr>
                <%}
                    }%>
            </table>
        </div>
        <%}%>

    </body>
</html>
