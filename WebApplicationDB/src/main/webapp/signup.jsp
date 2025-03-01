<%-- 
    Document   : signup
    Created on : 02 24, 25, 6:11:43 PM
    Author     : ken
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Signup Page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
    </head>
    <body>
        <h1>Sign Up</h1>
        <form action="SignUpServlet" method="POST">
            <label>Username: <input type="text" name="username" /></label><br>
            <label>Password: <input type="password" name="password" /></label><br>
            <input type="submit" value="Sign Up" />
            <c:if test="${not empty error}">
                <p style="color:red;">${error}</p>
            </c:if>
            <p>Already a member? <a href="login.jsp">Login</a></p>
            
        </form>
    </body>
</html>
