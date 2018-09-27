<%--
  Created by IntelliJ IDEA.
  User: jeffreythor
  Date: 9/28/17
  Time: 4:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.FacultyModel" %>
<%@ page import="com.endicott.edu.models.NewsFeedItemModel" %>
<html>
<head>
    <title>College World Faculty</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="resources/style.css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    <!-- JQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

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
    UiMessage msg = (UiMessage) request.getAttribute("message");
    if (msg == null) {
        msg = new UiMessage();
    }
    CollegeModel college = (CollegeModel) request.getAttribute("college");
    if (college == null) {
        college = new CollegeModel();
        msg.setMessage("Attribute for college missing.");
    }
    FacultyModel faculty[] = (FacultyModel[]) request.getAttribute("faculty");
    if (faculty == null) {
        faculty = new FacultyModel[0];
        faculty[0] = new FacultyModel("Professor Sam Smith", "Dean", "Biology", 110000, "LSB311", "unknown");
        msg.setMessage(msg.getMessage() + " Attribute for faculty missing.");
    }
%>

<form action="viewFaculty" method="post">

    <input type="hidden" name="runid" value="<%=college.getRunId()%>">

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
                    <li><a href="viewCollege"><%=college.getRunId()%></a></li>
                    <li><a href="viewStudent">Students</a></li>
                    <li><a href="viewDorm">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li class="active"><a href="viewFaculty">Faculty</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewAdmin">Admin</a></li>
                    <li><a href="about.jsp">About</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>


    <div class="container">
        <div class="jumbotron">
            <div class="row">
                <div class="col-md-2">
                    <img class="img-responsive" src="resources/images/student.png">
                </div>
                <div class="col-md-10">

                    <h2>Faculty</h2>
                    <h3><%=faculty.length%> faculty members</h3>
                </div>
            </div>
        </div>

        <div class="well well-sm">
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>Name</th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < faculty.length; i++) {
                %>
                <tr>
                    <td><%=faculty[i].getFacultyName()%>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <div class="col-sm-4">
            <div class="well well-sm">
                <div class="form-group">
                    <input type="submit" class="btn btn-info" name="addFaculty" value="Add Faculty">
                </div>
                <div class="form-group">
                    <div class="form-group">
                        <input type="text" class="form-control" id="facultyNameToDelete" name="facultyNameToDelete"
                               placeholder="Enter faculty name.">
                    </div>
                    <input type="submit" class="btn btn-danger" name="removeSingleFaculty" value="Delete Faculty">
                </div>
            </div>
        </div>

    </div>

</form>

</body>
</html>
