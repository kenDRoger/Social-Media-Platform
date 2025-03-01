<%@page import="com.mycompany.webapplicationdb.model.Follows"%>
<%@page import="com.mycompany.webapplicationdb.model.Following"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<%@ page import="java.util.ArrayList" %> 

<% //TODO: handle unexpected access 

    //get all the follows of current user
    Following following = new Following();
    String currUser = (String) session.getAttribute("username");
    Follows follows = following.getFollowsByUsername(currUser);
    String[] followedUsers = follows.getFollows();
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Users Page</title>
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
        <%if (!follows.ifFollowsEmpty()) {%>
        <h1>Your Followed Users</h1>

        <table>
            <tr>
                <th>Username</th>
                <th>Action</th>
            </tr>
            <%
                for (String user : followedUsers) {
                    if (user != null) {
            %>
            <tr>
                <td><%= user%></td>
                <td>
                    <form
                        action="UnfollowUserServlet"
                        method="post"
                        style="display: inline"
                        >
                        <input type="hidden" name="username" value="<%= user%>" />
                        <input type="hidden" name="currUser" value="<%= currUser%>" />
                        <input type="submit" value="Unfollow" />
                    </form>
                </td>
            </tr>
            <% }
                }%>
        </table>
        <%} else {%>
        <h1>No Followed Users...</h1>
        <%
            }
        %>
    <c:if test="${not empty successUnfollow}">
        <p style="color:green;">${successUnfollow}</p>
    </c:if>
    <%if (!follows.ifFollowsFull()) {%>
    <h2>Follow a New User</h2>
    <form action="FollowUserServlet" method="post">
        <label for="newUser">Username:</label>
        <input type="text" id="newUser" name="newUser" required />

        <input type="hidden" name="currUser" value="<%= currUser%>" />
        <input type="submit" value="Follow" />
        <!<!-- TODO: implement error forward from FollowUserServlet -->
        <c:if test="${not empty errorFollow}">
            <p style="color:red;">${errorFollow}</p>
        </c:if>
        <c:if test="${not empty successFollow}">
            <p style="color:green;">${successFollow}</p>
        </c:if>
    </form>
    <%}%>
</body>
</html>
