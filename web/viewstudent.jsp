<%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 9/15/2017
  Time: 7:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.*" %>
<%--
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
                    <li><a href="viewFaculty?runid=<%=college.getRunId()%>&server=<%=server%>">Faculty</a></li>
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
            <div class="row">
                <div class="col-md-2">
                    <img class="img-responsive" src="resources/images/student.png">
                </div>
                <div class="col-md-10">
                    <h2><%=students.length%> Students</h2>
                    <br/>
                    <%
                        int nSick = 0;
                        int studentGoodCount = 0;
                        // loop thru students and count sick ones.
                        for (int i = 0; i < students.length; i++) {
                            if(students[i].getNumberHoursLeftBeingSick() == 0){
                                studentGoodCount++;
                            } else {
                                nSick++;
                            }
                        }
                    %>
                    <%=nSick%> Students Sick
                </div>
            </div>
        </div>
        <h4>Student Body Happiness</h4>
        <% if (college.getStudentBodyHappiness() >= 80) { %>
        <div class="progress">
            <div class="progress-bar progress-bar-success" role="progressbar"
                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:<%=college.getStudentBodyHappiness()%>%">
                <%=college.getStudentBodyHappiness()%>%
            </div>
        </div>
        <%
        } else if (college.getStudentBodyHappiness() >= 50 && college.getStudentBodyHappiness() < 80 ){
        %>
        <div class="progress">
            <div class="progress-bar progress-bar-warning" role="progressbar"
                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:<%=college.getStudentBodyHappiness()%>%">
                <%=college.getStudentBodyHappiness()%>%
            </div>
        </div>
        <% } else if (college.getStudentBodyHappiness() < 50){
        %>
        <div class="col-sm-8">
        <div class="progress">
            <div class="progress-bar progress-bar-danger" role="progressbar"
                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:<%=college.getStudentBodyHappiness()%>%">
                <%=college.getStudentBodyHappiness()%>%
            </div>
        </div>
        </div>
        <% } %>
        <!-- Display a message if defined -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">
        <input type="hidden" name="server" value="<%=server%>">
        <p></p>
        <div class="col-sm-8">
            <div class="well well-sm">
                <h4>Students</h4>
                <div class="pre-scrollable">
                    <table class="table table-condensed">
                        <tbody>
                        <%
                            for (int i = 0; i < students.length; i++) {
                        %>
                        <tr>
                            <td>
                                <li class="list-group-item"><%=students[i].getName()%></li>
                            </td>
                            <td>
                                <a href="#<%=i%>" class="btn btn-info" data-toggle="collapse">Details</a>
                                <div id="<%=i%>" class="collapse">
                                    <div class="well well-sm">
                                    ID Number: <%=students[i].getIdNumber()%><br>
                                    Dorm: <%=students[i].getDorm()%><br>
                                    Happiness: <%=students[i].getHappinessLevel()%><br>
                                    Gender: <%=students[i].getGender()%> <br>
                                    Hours Sick: <%=students[i].getNumberHoursLeftBeingSick()%><br>
                                    <% if(students[i].isAthlete()){ %>
                                        Team: <%= students[i].getTeam()%> <br>
                                        Athletic Ability <%=students[i].getAthleticAbility()%> <br>
                                    <% } %>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
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

