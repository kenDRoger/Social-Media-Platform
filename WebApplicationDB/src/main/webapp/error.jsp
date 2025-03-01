<%-- 
    Document   : error
    Created on : Feb 25, 2025, 9:33:03 AM
    Author     : Vince
--%>

<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
    </head>
    <body>
        <header>
            <h1>${title}</h1>
        </header>
        <main>
            <section>
                <%
                    if (request.getAttribute("code") != null) {
                %>
                <p><strong>Error Code:</strong> ${requestScope.code}</p>
                <% } %>
                <%
                    if (request.getAttribute("message") != null) {
                %>
                <p><strong>Message:</strong> ${requestScope.message}</p>
                <% } %>
                <%
                    if (request.getAttribute("causes") != null) {
                %>
                <p><strong>Causes:</strong>
                    <ul>
                        <%
                            String[] causes = (String[]) request.getAttribute("causes");
                            if (causes != null) {
                                for (String cause : causes) {
                        %>
                            <li><%= cause %></li>
                        <%
                                }
                            }
                        %>
                    </ul>
                </p>
                <% } %>
                <%
                    if (request.getAttribute("exception") != null) {
                %>
                <h1><strong>Exception:</strong> ${requestScope.exception.class.name}</h1>
                <p><strong>Exception Message:</strong> ${requestScope.exception.message}</p>
                <% } %>
            </section>
        </main>
        <footer>
            <nav>
                <%
                    if (request.getAttribute("navigation") != null) {
                %>
                <ul>
                    <%
                        Map<String, String> navigation = (Map<String, String>) request.getAttribute("navigation");
                        for (Map.Entry<String, String> entry : navigation.entrySet()) {
                    %>
                        <li><a href="<%= entry.getValue() %>"><%= entry.getKey() %></a></li>
                    <%
                        }
                    %>
                </ul>
                <% } %>
            </nav>
        </footer>
    </body>
</html>

