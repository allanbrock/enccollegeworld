<%--
  Created by IntelliJ IDEA.
  User: Jeremy
  Date: 10/16/2018
  Time: 4:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.endicott.edu.simulators.CollegeManager" %>
<%@ page import="com.endicott.edu.models.GateModel" %>
<%@ page import="com.endicott.edu.simulators.GateManager" %>
<%@ page import="com.endicott.edu.models.StudentModel" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
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
    }
    StudentModel students[] = (StudentModel[]) request.getAttribute("students");
    if (students == null) {
        students = new StudentModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for students missing.");
    }
    GateModel gates[] = (GateModel[]) request.getAttribute("gates");
    if(gates == null) {
        gates = new GateModel[0]; // This is really bad
        msg.setMessage(msg.getMessage() + "Attribute for news missing.");
    }

    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>

<form action="viewGates" method="post">

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
                    <li class="active"><a href="viewCollege"><%=college.getRunId()%></a></li>
                    <li><a href="viewStudent">Students</a></li>
                    <li><a href="viewBuilding">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li><a href="viewFaculty">Faculty</a></li>
                    <li class="active"><a href="viewGates">Objectives</a></li>
                    <li><a href="viewStore">Store</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewBalance">$<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                    <li><a> <%=new SimpleDateFormat("MMM dd").format(CollegeManager.getCollegeDate(college.getRunId()))%> </a></li>
                    <li><a href="viewAdmin">Admin</a></li>
                    <li><a href="viewAbout">About</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>
</form>

<%-- Gates --%>
<div class="container">

    <div class="jumbotron">
        <div class="row">
            <div class="col-md-2">
                <img class="img-responsive" src="resources/images/star.png">
            </div>
            <div class="col-md-10">

                <h2>You are on Level <%=GateManager.getGateLevel(college.getRunId())%></h2>
                <% int studentsNeeded = Math.max(GateManager.getGateGoal(GateManager.getGateLevel(college.getRunId())+1) - students.length, 0); %>
                <h3>You need <%=studentsNeeded%> students to get to the next level.</h3>
                <div class="progress" style="margin-bottom:0">
                    <div class="progress-bar progress-bar-success" role="progressbar"
                        aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                        style="width:<%=GateManager.getOverallGateProgress(college.getRunId())%>%">
                        <%=students.length%> / <%=GateManager.getGateGoal(GateManager.getGateLevel(college.getRunId())+1)%> students
                    </div>
                </div>
            </div>
        </div>
    </div>

    <h3> Upcoming Objectives: </h3>

    <div class="well well-sm">
        <div class="gateList">
            <%--<h3>Current Objectives(<%=gates.length%>):</h3>--%>
            <div class="pre-scrollable" style="max-height: 750px">
                <ul class="list-group">
                    <%
                        for(int i = 1; i <= 5; i++) {
                            if (i <= GateManager.getGateLevel(college.getRunId())) continue;
                    %>
                        <h3> Level <%=i%>&nbsp;&nbsp;&nbsp;&nbsp;<%=GateManager.getGateGoal(i)%> Students </h3>
                    <%
                            for(GateModel gate : gates) {
                                if(!GateManager.testGate(college.getRunId(), gate.getKey()) && gate.getLevel() == i) {
                    %>
                    <li class="list-group-item">
                        <div class="col-md-2" style="width: 100px">
                            <img class="img-responsive" style="" src=<%=gate.getIconPath()%>>
                        </div>
                        <h4><strong><%=gate.getKey()%></strong> </h4>
                        <p><%=gate.getDescription()%></p>
                        <%--<div class="progress" style="margin-bottom:0">--%>
                            <%--<div class="progress-bar progress-bar-success" role="progressbar"--%>
                                 <%--aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"--%>
                                 <%--style="width:<%=GateManager.getGateProgress(college.getRunId(),gate.getKey())%>%">--%>
                                <%--<%=GateManager.getGateProgress(college.getRunId(),gate.getKey())%>%--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    </li>
                    <%
                                }
                            }
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>

    <h3> Completed Objectives: </h3>

    <div class="well well-sm">
        <div class="gateList">
            <%--<h3>Current Objectives(<%=gates.length%>):</h3>--%>
            <div class="pre-scrollable" style="max-height: 750px">
                <ul class="list-group">
                    <%
                        for(GateModel gate : gates) {
                            if(GateManager.testGate(college.getRunId(), gate.getKey())) {
                    %>
                    <li class="list-group-item">
                        <div class="col-md-2" style="width: 100px">
                            <img class="img-responsive" style="" src=<%=gate.getIconPath()%>>
                        </div>
                        <h4><strong><%=gate.getKey()%></strong></h4>
                        <p><%=gate.getDescription()%></p>
                        <div class="progress" style="margin-bottom:0">
                            <div class="progress-bar progress-bar-success" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:100%">
                                100%
                            </div>
                        </div>
                    </li>
                    <%
                            }
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</div>

</body>
</html>
