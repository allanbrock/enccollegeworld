<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>About</title>
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

</head>
<body>
<%
    String collegeId = (String) request.getSession().getAttribute("runid");
%>
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
                    <li><a href="viewCollege"><%=collegeId%></a></li>
                    <li><a href="viewStudent">Students</a></li>
                    <li><a href="viewDorm">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li><a href="viewFaculty">Faculty</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewAdmin">Admin</a></li>
                    <li class="active"><a href="about.jsp">About</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="container">
            <h2>Fall 2018</h2>
            <ul class="list-group">
                <li class="list-group-item">Alex Groenewold</li>
                <li class="list-group-item">Tyler Ouellette</li>
                <li class="list-group-item">Marie Urmson</li>
                <li class="list-group-item">Jeremy Barr</li>
                <li class="list-group-item">CJ Mustone</li>
                <li class="list-group-item">Joseph Moss</li>
                <li class="list-group-item">Stephen Hoadley</li>
            </ul>

            <h2>Fall 2017</h2>
            <ul class="list-group">
                <li class="list-group-item">Mazlin Higbee</li>
                <li class="list-group-item">Nick Dos Santo</li>
                <li class="list-group-item">Jeremy Doski</li>
                <li class="list-group-item">Cam Bleck</li>
                <li class="list-group-item">Stephen Hoadley</li>
                <li class="list-group-item">Connor Frazier</li>
                <li class="list-group-item">Allison Flood</li>
                <li class="list-group-item">Karen Litwinczyk</li>
                <li class="list-group-item">Nick Scrivani</li>
                <li class="list-group-item">Chris Seidl</li>
                <li class="list-group-item">Jeff Thor</li>
                <li class="list-group-item">Derek Yannone</li>
            </ul>
        </div>
    </div>

</div>
</body>
</html>
