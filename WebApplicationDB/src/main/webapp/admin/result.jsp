<%@page import="com.mycompany.webapplicationdb.exception.UnauthorizedAccessException"%>
<%@page import="com.mycompany.webapplicationdb.model.Account"%>
<%@page import="java.util.ArrayList"%>
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
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Results Page</title>
        <link rel="stylesheet" type="text/css" href="styles.css">
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
        <div class="container">
            <%
                String message = (String) request.getAttribute("message");
                if (message != null) {
            %>
            <h1><%= message%></h1>
            <% } %>

            <%
                ArrayList<Account> result = (ArrayList<Account>) request.getAttribute("result");
                if (result != null && !result.isEmpty()) {
            %>
            <table>
                <tr>
                    <th>Username</th>
                    <th>Password</th>
                    <th>Role</th>
                </tr>
                <% for (Account account : result) {
                        if (account != null) {%>
                <tr>
                    <td><%= account.getUsername()%></td>
                    <td><%= account.getPassword()%></td>
                    <td class="small"><%= account.getUserRole()%></td>
                </tr>
                <% }
                    }%>
            </table>
            <% } else { %>
            <p class="small">No results found.</p>
            <% } %>
            <%
                String message1 = (String) request.getAttribute("message1");
                if (message1 != null) {
            %>
            <h1><%= message1%></h1>
            <% } %>

            <%
                ArrayList<Account> result1 = (ArrayList<Account>) request.getAttribute("result1");
                if (result1 != null && !result1.isEmpty()) {
            %>
            <table>
                <tr>
                    <th>Username</th>
                    <th>Password</th>
                    <th>Role</th>
                </tr>
                <% for (Account account : result1) {
                        if (account != null) {
                %>
                <tr>
                    <td><%= account.getUsername()%></td>
                    <td><%= account.getPassword()%></td>
                    <td class="small"><%= account.getUserRole()%></td>
                </tr>
                <% }
                    } %>
            </table>
            <% }%>

        </div>
    </body>
</html>
