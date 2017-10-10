<%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 9/15/2017
  Time: 7:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.endicott.edu.models.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.models.*" %><%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 8/25/2017
  Time: 8:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<title>College World Student</title>
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
    StudentModel students[] = (StudentModel[]) request.getAttribute("students");
    if (students == null) {
        students  = new StudentModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for students missing.");
    }
%>


<form action="updateCollege" method="post">

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
                    <li><a href="viewCollege?runid=<%=college.getRunId()%>&server=<%=server%>"><%=college.getRunId()%>
                    </a></li>
                    <li class="active"><a href="viewStudent?runid=<%=college.getRunId()%>&server=<%=server%>">Students</a></li>
                    <li><a href="viewDorm?runid=<%=college.getRunId()%>&server=<%=server%>">Dorms</a></li>
                    <li><a href="viewSports?runid=<%=college.getRunId()%>&server=<%=server%>">Sports</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewAdmin?runid=<%=college.getRunId()%>&server=<%=server%>">Admin</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="jumbotron">
            <h2>Students</h2>
            <p></p>
            <h3><%=students.length%> students</h3>
        </div>
        <h4>Student Body Happiness</h4>
        <div class="progress">
            <div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:50%">
                <span class="sr-only">50% Complete</span>
            </div>
        </div>
        <!-- Display a message if defined -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">
        <input type="hidden" name="server" value="<%=server%>">
        <p></p>
        <div class="well well-sm">
            <table class="table table-condensed">
                <tbody>
                <h4>Students</h4>
                <%
                    for (int i = 0; i < students.length; i++) {
                %>
                <tr>
                    <li class="list-group-item"> <%=students[i].getIdNumber()%>
                    </li>
                    <%--<td ><%=students[i].getIdNumber()%>--%>
                    <%--</td>--%>
                </tr>
                <% } %>
                </tbody>
            </table>

        </div>
    </div>
</form>
<div class="container">
<div class="alert alert-success">
    <strong>Info</strong> <%=msg.getMessage()%>
</div>
</div>
</div>
</body>
</html>

