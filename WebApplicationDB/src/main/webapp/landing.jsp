<%@page import="java.util.Comparator"%> <%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.webapplicationdb.model.PostData"%> <%@page
    import="com.mycompany.webapplicationdb.model.Posts"%> <%@page
        import="com.mycompany.webapplicationdb.model.Following"%> <%@page
            import="com.mycompany.webapplicationdb.model.PostsList"%> <%@page
                contentType="text/html" pageEncoding="UTF-8"%> 

                <%
                    session
                            = request.getSession();
                    if (session.getAttribute("username") == null) { //TODO:handle unexpected access 
                        response.sendRedirect("login.jsp");
                        return;
                    } //get allthe posts 
                    PostsList postsList = new PostsList(); //get all the follows ofcurrent user 
                    Following following = new Following();
                    String currUser = (String) session.getAttribute("username");
                    String[] listOfFollows = following.getFollowsByUsername(currUser).getFollows();
                    //compile all the postsof the follows of user 
                    ArrayList<PostData> postDataList = new ArrayList<PostData>(); // Corrected array size 
                    for (int i = 0; i < listOfFollows.length; i++) {
                        if (listOfFollows[i] != null) {
                            String followedUser = listOfFollows[i];
                            PostData[] posts = postsList.getPostsByUsername(followedUser).getPosts();
                            for (PostData postdata : posts) {
                                if (postdata != null) {
                                    postDataList.add(postdata);
                                }
                            }
                        }
                    } // Sort by id (latest first), moving nulls to the end 
                    postDataList.sort(new Comparator<PostData>() {
                        @Override
                        public int compare(PostData p1, PostData p2
                        ) { // Handlenull values:movenulls to the end 
                            if (p1 == null && p2 == null) {
                                return 0;
                            }
                            if (p1 == null) {
                                return 1;
                            }
                            if (p2 == null) {
                                return -1;
                            } // Sort by id in descending order(latest first
                            return Integer.compare(p2.getId(), p1.getId());
                        }
                    }
                    ); %>
                <!DOCTYPE html>
                <html>
                    <head>
                        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                        <title>Landing Page</title>
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
                        <% if (postsList != null && !postsList.isEmpty()) {%>
                        <!-- Posts Section -->
                        <div class="posts">
                            <h2>Followed Users' Posts</h2>
                            <%
                                for (PostData post : postDataList) {
                                    if (post != null) {%>
                            <div class="post">
                                <h3><%= post.getTitle()%></h3>
                                <p><%= post.getContent()%></p>
                                <small
                                    >Posted by <%= post.getUsername()%> on <%=post.getDate_created()%></small
                                >
                            </div>
                            <% }
                                }%>
                        </div>
                        <%
                        } else { %>
                        <p>No posts to show. Follow users to see their posts here.</p>
                        <% }%>
                    </body>
                </html>

