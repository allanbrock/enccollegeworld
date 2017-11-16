<%--
  Created by IntelliJ IDEA.
  User: jeffreythor
  Date: 10/31/17
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%--<%@ page import="AdminModel" %>--%>
<%@ page import="com.endicott.edu.models.NewsFeedItemModel" %>
<html>
<head>
    <title>College World Admin</title>
    <link rel="stylesheet" href="resources/style.css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
          integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
            integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
            crossorigin="anonymous"></script>

    <style>
        /*body {*/
            /*padding-top: 3.5rem;*/
        /*}*/

        /*
         * Typography
         */

        h1 {
            padding-bottom: 9px;
            margin-bottom: 20px;
            border-bottom: 1px solid #eee;
        }

        /*
         * Sidebar
         */

        .sidebar {
            position: fixed;
            top: 51px;
            bottom: 0;
            left: 0;
            z-index: 1000;
            padding: 20px 0;
            overflow-x: hidden;
            overflow-y: auto; /* Scrollable contents if viewport is shorter than content. */
            border-right: 1px solid #eee;
        }

        .sidebar .nav {
            margin-bottom: 20px;
        }

        .sidebar .nav-item {
            width: 100%;
        }

        .sidebar .nav-item + .nav-item {
            margin-left: 0;
        }

        .sidebar .nav-link {
            border-radius: 0;
        }

        /*
         * Dashboard
         */

        /* Placeholders */
        .placeholders {
            padding-bottom: 3rem;
        }

        .placeholder img {
            padding-top: 1.5rem;
            padding-bottom: 1.5rem;
        }
    </style>

</head>

<%
    String server = (String) request.getAttribute("server");
    UiMessage msg = (UiMessage) request.getAttribute("message");
    if (msg == null) {
        msg = new UiMessage();
    }
    CollegeModel college = (CollegeModel) request.getAttribute("college");
    if (college == null) {
        college = new CollegeModel();
        msg.setMessage("Attribute for college missing.");
    }
%>
<body>

<form action="updateCollege" method="post">
    <header>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>
            <div class="collapse navbar-collapse" id="myNavbar">
                <ul class="nav navbar-nav">
                    <li><a href="viewCollege?runid=<%=college.getRunId()%>&server=<%=server%>"><%=college.getRunId()%></a></li>
                    <li><a href="viewStudent?runid=<%=college.getRunId()%>&server=<%=server%>">Students</a></li>
                    <li><a href="viewDorm?runid=<%=college.getRunId()%>&server=<%=server%>">Dorms</a></li>
                    <li><a href="viewSports?runid=<%=college.getRunId()%>&server=<%=server%>">Sports</a></li>
                    <li><a href="viewFaculty?runid=<%=college.getRunId()%>&server=<%=server%>">Faculty</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="active"><a href="viewAdmin?runid=<%=college.getRunId()%>&server=<%=server%>">Admin</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>

    </header>

    <body>

    <div class="container-fluid">
        <div class="row">
            <%--<nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">--%>
                <%--<ul class="nav nav-pills flex-column">--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link active" href="#">Overview <span class="sr-only">(current)</span></a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Reports</a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Analytics</a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Export</a>--%>
                    <%--</li>--%>
                <%--</ul>--%>

                <%--<ul class="nav nav-pills flex-column">--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Nav item</a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Nav item again</a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">One more nav</a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Another nav item</a>--%>
                    <%--</li>--%>
                <%--</ul>--%>

                <%--<ul class="nav nav-pills flex-column">--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Nav item again</a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">One more nav</a>--%>
                    <%--</li>--%>
                    <%--<li class="nav-item">--%>
                        <%--<a class="nav-link" href="#">Another nav item</a>--%>
                    <%--</li>--%>
                <%--</ul>--%>
            <%--</nav>--%>

            <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">

                <h2>Colleges (will be populated in next push)</h2>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>College Name</th>
                            <th>Students</th>
                            <th>Dorms</th>
                            <th>Age</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Lorem</td>
                            <td>ipsum</td>
                            <td>dolor</td>
                            <td>sit</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>amet</td>
                            <td>consectetur</td>
                            <td>adipiscing</td>
                            <td>elit</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>Integer</td>
                            <td>nec</td>
                            <td>odio</td>
                            <td>Praesent</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>libero</td>
                            <td>Sed</td>
                            <td>cursus</td>
                            <td>ante</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>dapibus</td>
                            <td>diam</td>
                            <td>Sed</td>
                            <td>nisi</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>Nulla</td>
                            <td>quis</td>
                            <td>sem</td>
                            <td>at</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>nibh</td>
                            <td>elementum</td>
                            <td>imperdiet</td>
                            <td>Duis</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>sagittis</td>
                            <td>ipsum</td>
                            <td>Praesent</td>
                            <td>mauris</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>Fusce</td>
                            <td>nec</td>
                            <td>tellus</td>
                            <td>sed</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>augue</td>
                            <td>semper</td>
                            <td>porta</td>
                            <td>Mauris</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        <tr>
                            <td>massa</td>
                            <td>Vestibulum</td>
                            <td>lacinia</td>
                            <td>arcu</td>
                            <td><button type="button" class="btn btn-danger">
                                Delete
                            </button></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>

    </body>

</form>

</body>
</html>
