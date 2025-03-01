<%-- mema nalang magkopya
    Document   : profile
    Created on : Feb 25, 2025, 7:24:13 AM
    Author     : Vince
--%>

<%@page import="com.mycompany.webapplicationdb.exception.UnauthorizedAccessException"%>
<%@page import="com.mycompany.webapplicationdb.model.Posts"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.webapplicationdb.model.PostData"%>
<%@page import="com.mycompany.webapplicationdb.model.PostsList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    try {
        UnauthorizedAccessException.checkAccessUser(session);
    } catch (UnauthorizedAccessException e) {
        e.setAttributesForUser(session, request, e);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return;
    }
    //get all the posts
    PostsList postsList = new PostsList();

    //get all posts of currUser
    String currUser = (String) session.getAttribute("username");
    Posts posts = null;
    if (postsList != null) {
        posts = postsList.getPostsByUsername(currUser);
    }

    if (posts != null)
        posts.updatePostOrder();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile Page</title>
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

        <h1>Welcome, <%= currUser%>!</h1>

        <!-- Form to create a new post -->
        <h2>Create a New Post</h2>
        <form action="CreatePostServlet" method="post">
            <input type="hidden" id="username" name="username" value="<%= currUser%>">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" required><br>

            <label for="content">Content:</label><br>
            <textarea id="content" name="content" rows="4" required></textarea><br>

            <button type="submit">Create Post</button>
            <c:if test="${not empty error}">
                <p style="color:red;">${error}</p>
            </c:if>
        </form>
        
        <%
            if (!posts.ifPostsNull()) {
                System.out.println("XXXX:" + postsList);

        %>
        <!-- Display the latest 5 posts -->
        <h2>Your Recent Posts</h2>
        <div class="posts">
            <%                if (posts != null && !postsList.isEmpty()) {
                    for (PostData post : posts.getPosts()) {
                        if (post != null) {
            %>
            <div class="post">
                <h3><%= post.getTitle()%></h3>
                <p><%= post.getContent()%></p>
                <small>Posted on <%= post.getDate_created()%></small>
                <form action="DeletePostServlet" method="post">
                    <input type="hidden" id="username" name="username" value="<%= currUser%>">
                    <input type="hidden" name="postId" value="<%= post.getId()%>">
                    <button type="submit" class="delete-btn">Delete</button>
                </form>
            </div>
            <%
                    }
                }
            } else {
            %>
            <p>You have no posts yet. Create one!</p>
            <%
                }
            } else {
            %>
            <p>You have no posts yet. Create one!</p>

        </div>

        <%}%>



    </body>
</html>
