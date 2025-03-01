<%-- 
    Document   : 404
    Created on : 02 25, 25, 4:06:04 PM
    Author     : ken
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Page Not Found</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <div class="container">
        <h1>404</h1>
        <p>Oops! The page you’re looking for doesn’t exist.</p>
        <button type="button" onclick="window.history.back();">Go back home</button>
    </div>
</body>
</html>

