

<%
    //TODO: handle unexpected access

    String currUser = (String) session.getAttribute("username");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Help Page</title>
       <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <ul>
                <li><a href="landing.jsp">Home</a></li>
                <li><a href="profile.jsp">Profile</a></li>
                <li><a href="users.jsp">Manage Followed Users</a></li>
                <li><a href="help.jsp">Help</a></li>
                <li><a href="LogoutServlet">Logout</a></li>
            </ul>
        </nav>
        <h1>Contact Admin</h1>

        <form action="SubmitHelpServlet" method="post">
            <label for="subject">Subject:</label><br>
            
            <input type="hidden" id="username" name="username" value="<%= currUser%>">
            <input type="text" id="subject" name="subject" required><br><br>

            <label for="content">Message:</label><br>
            <textarea id="content" name="content" rows="5" cols="40" required></textarea><br><br>

            <input type="submit" value="Send Message">
            <!<!-- TODO: Add success Message, redirect from SubmitHelpServlet -->
        </form>
    </body>
</html>

