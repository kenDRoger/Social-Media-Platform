<%-- mema nalang magkopya
    Document   : login
    Created on : 02 24, 25, 6:05:45 PM
    Author     : ken
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
    </head>
    <body>
        <h1>Login</h1>
        <form action="LoginServlet" method="post">
            <label>Username: <input type="text" name="username" required/></label>
            <label>Password: <input type="password" name="password" required/></label>
            <input type="submit" value="Login" />
            <c:if test="${not empty error}">
                <p style="color:red;">${error}</p>
            </c:if>
            <br>
            <p><a href="signup.jsp">Sign Up</a></p>
        </form>
    </body>
</html>
