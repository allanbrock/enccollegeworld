<%--
  Created by IntelliJ IDEA.
  User: jeffreythor
  Date: 10/31/17
  Time: 3:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.text.NumberFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%--<%@ page import="AdminModel" %>--%>
<%@ page import="com.endicott.edu.models.NewsFeedItemModel" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.endicott.edu.ui.ViewAdminServlet" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>College World Admin</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
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
<body>
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
    CollegeModel colleges[] = (CollegeModel[]) request.getAttribute("colleges");
    if (colleges == null) {
        colleges = new CollegeModel[0];
        msg.setMessage(msg.getMessage() + "No colleges found!");
    }
    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>


<form action="viewAdmin" method="post">
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
                        <li><a href="viewCollege"><%=college.getRunId()%>
                        </a></li>
                        <li><a href="viewStudent">Students</a></li>
                        <li><a href="viewBuilding">Buildings</a></li>
                        <li><a href="viewSports">Sports</a></li>
                        <li><a href="viewFaculty">Faculty</a></li>
                        <li><a href="viewGates">Gates</a></li>
                        <li><a href="viewStore">Store</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="viewBalance">$<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                        <li class="active"><a href="viewAdmin">Admin</a></li>
                        <li><a href="about.jsp">About</a></li>
                        <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                    </ul>
                </div>
            </div>
        </nav>

    </header>

    <body>

    <div class="container-fluid">
        <div class="row">


            <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">

                <h2>College Ranking</h2>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>College</th>
                            <th></th>
                            <th>Days Old</th>
                            <th>Balance</th>
                            <th>Students</th>
                        </tr>
                        </thead>
                        <tbody>

                        <%for (CollegeModel tmp : colleges) { %>
                        <tr>
                            <td><%=tmp.getRunId()%>
                            </td>
                            <td><input type="submit" class="btn btn-info" name="<%=tmp.getRunId()%>" value="Delete"
                                <% if (college.getRunId().compareTo(tmp.getRunId()) == 0) {%>
                                       disabled
                                <% }%>
                            ></td>
                            <td><%=tmp.getCurrentDay()%>
                            </td>
                            <td>$<%=tmp.getAvailableCash()%>
                            </td>
                            <td><%=tmp.getNumberStudentsAdmitted()%>
                            </td>

                        </tr>
                        <%}%>
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
