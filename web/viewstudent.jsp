<%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 9/15/2017
  Time: 7:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.endicott.edu.simulators.CollegeManager" %>
<%@ page import="com.endicott.edu.simulators.TutorialManager" %>
<%@ page import="java.util.Collections" %>
<%@ page import="static java.lang.Integer.parseInt" %>
<%@ page import="com.endicott.edu.simulators.StudentManager" %>
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
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="resources/style.css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    <!-- JQuery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
          integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
            integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
            crossorigin="anonymous"></script>

    <%!
        /**
         *
         * @param num       value to base scale off of
         * @param in_min    min value for inputs range
         * @param in_max    max value for inputs range
         * @param out_min   min value for outputs range
         * @param out_max   max value for outputs range
         * @return          scaled value
         */
        public int scale(int num, int in_min, int in_max, int out_min, int out_max) {
            return (num - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        }
    %>

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
        StudentModel students[] = (StudentModel[]) request.getAttribute("students");
        if (students == null) {
            students  = new StudentModel[0];  // This is really bad
            msg.setMessage(msg.getMessage() + " Attribute for students missing.");
        }

        TutorialModel tip = TutorialManager.getCurrentTip("viewStudent", college.getRunId());
//    TutorialModel tutorials[] = (TutorialModel[]) request.getAttribute("tutorials");
//    if (tutorials == null) {
//        tutorials = new TutorialModel[0];
//        msg.setMessage("Attribute for tutorial missing.");
//    }
        StudentModel student = students[StudentManager.getStudentIndex()];
        NumberFormat numberFormatter = NumberFormat.getInstance();
        numberFormatter.setGroupingUsed(true);
    %>

    <style>
        .studentContainer {
            display: grid;
            margin-right: 20px;
            grid-template-columns: repeat(10,10%);
        }

        .studentElement {
            width: 55px;
            height: 55px;
            margin: 10px 15px 20px 15px;
            border-radius: 50%;
            background: #aaa;
            white-space: nowrap;
        }

        .studentElement:hover {
            background: #888;
            width: 60px;
            height: 60px;
            margin: 7.5px 10px 17.5px 12px;
        }

        .studentElement img {
            width:80%;
            margin: 5px;
        }

        .studentElement p {
            font-size: xx-small;
            text-align: center;
            margin-top: 5px;
        }

        .speech-bubble {
            position: relative;
            background: #5bc0de;
            border-radius: .4em;
            margin: 15px;
            color: white;
            padding: 10px;
        }

        .speech-bubble:after {
            content: '';
            position: absolute;
            left: 0;
            top: 70%;
            width: 0;
            height: 0;
            border: 1.125em solid transparent;
            border-right-color: #5bc0de;
            border-left: 0;
            border-bottom: 0;
            margin-top: -0.562em;
            margin-left: -1.125em;
        }

    </style>
    <script>
        function post(path, params, method) {
            method = method || "post"; // Set method to post by default if not specified.

            // The rest of this code assumes you are not using a library.
            // It can be made less wordy if you use one.
            var form = document.createElement("form");
            form.setAttribute("method", method);
            form.setAttribute("action", path);

            for(var key in params) {
                if(params.hasOwnProperty(key)) {
                    var hiddenField = document.createElement("input");
                    hiddenField.setAttribute("type", "hidden");
                    hiddenField.setAttribute("name", key);
                    hiddenField.setAttribute("value", params[key]);

                    form.appendChild(hiddenField);
                }
            }

            document.body.appendChild(form);
            form.submit();
        }

        function select(e) {
            post(window.location.href, {index: e.id})
            document.getElementById("studentName").value = "<%=students[StudentManager.getStudentIndex()].getName()%>";
        }
    </script>

</head>
<body>

<form id="selectForm" style="visibility: hidden"><input name="index" type="hidden"/></form>

<form action="viewStudent" method="post">

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
                    <li class="active"><a href="viewStudent">Students</a></li>
                    <li><a href="viewBuilding">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li><a href="viewFaculty">Faculty</a></li>
                    <li><a href="viewGates">Objectives</a></li>
                    <li><a href="viewStore">Store</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewBalance">$<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                    <li><a> <%=new SimpleDateFormat("MMM dd").format(CollegeManager.getCollegeDate(college.getRunId()))%> </a></li>
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
                <div class="col-md-5" style="padding: 10px">
                    <h2><%=student.getName()%></h2>
                    <div class="speech-bubble">
                        <strong><%=StudentManager.getStudentFeedback(student, college.getRunId())%></strong>
                    </div>
                </div>

                <!-- Tips -->
                <%if (tip != null){%>
                <div class="col-md-5" style="float: right; margin-bottom: 10px">
                    <div class="well well-lg" style="background: white">
                        <%if (!tip.getImage().equals("")){%>
                        <img style="width: 15%; float:left; padding: 5px" class="img-responsive" src="resources/images/<%=tip.getImage()%>">
                        <%}%>
                        <p><%=tip.getBody()%></p>
                    </div>
                    <input type="submit" class="btn btn-info" name="nextTip" value="Next Tip">
                    <input type="submit" class="btn btn-info" name="hideTips" value="Hide Tips">
                </div>
                <%}%>
                <%if (tip == null){%>
                <input type="submit" class="btn btn-info" name="showTips" value="Show Tips">
                <%}%>

                <div class="col-sm-8">
                    <div class="well well-sm">
                        <h4><%=students.length%> Students</h4>
                        <div class="pre-scrollable" style="max-height: 500px">
                            <div class="studentContainer">
                                <%
                                    for (int i = 0; i < students.length; i++) {
                                        int happiness = students[i].getHappinessLevel();
                                %>

                                <div class = "studentElement" id="<%=i%>"
                                     style = "
                                         <%if(i == StudentManager.getStudentIndex()) {%>
                                             background: #666;
                                         <%}%>
                                             box-shadow: 0 0 5px 3px rgb(
                                         <%=scale(happiness, 50, 100, 255,   0)%>,
                                         <%=scale(happiness,  0,  50,   0, 255)%>,
                                             0
                                             );
                                             "
                                     onclick="select(this)"
                                >
                                    <img src="resources/images/student.png">
                                    <p><%=students[i].getName().split(" ")[1]%>, <%=students[i].getName().split(" ")[0].charAt(0)%></p>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-4">
                    <div class="well well-sm" >
                        <div class="pre-scrollable" style="max-height: 539px">

                            <table>
                                <%--<%student = students[StudentManager.getStudentIndex()];%>--%>
                                <tr>
                                    <td>Student: </td>
                                    <td><%=student.getName()%></td>
                                </tr>
                                <tr>
                                    <td>ID Number: </td>
                                    <td><%=student.getIdNumber()%></td>
                                </tr>
                                <tr>
                                    <td>Gender: </td>
                                    <td><%=student.getGender()%></td>
                                </tr>
                                <tr>
                                    <td>Advisor: </td>
                                    <td><%=student.getAdvisor().getFacultyName()%></td>
                                </tr>
                                <% if(!student.getTeam().equals("")){ %>
                                <tr>
                                    <td>Team: </td>
                                    <td> <%= student.getTeam()%> </td>
                                </tr>
                                <tr>
                                    <td> Athletic Ability: </td>
                                    <td> <%=student.getAthleticAbility()%> </td>
                                </tr>
                                <% } %>

                                <% if (student.getNumberHoursLeftBeingSick() > 0) { %>
                                <tr>
                                    <td>Sick for:</td>
                                    <td><%=student.getNumberHoursLeftBeingSick()%> more hours</td>
                                <tr/>
                                <% } %>
                                <tr><td>Overall Happiness</td><td><%=student.getHappinessLevel()%></td></tr>
                                <tr><td>Academic Happiness</td><td><%=student.getAcademicHappinessRating()%></td></tr>
                                <tr><td>Advisor Happiness</td><td><%=student.getAdvisorHappinessHappinessRating()%></td></tr>
                                <tr><td>Health Happiness</td><td><%=student.getHealthHappinessRating()%></td></tr>
                                <tr><td>Money Happiness</td><td><%=student.getMoneyHappinessRating()%></td></tr>
                                <tr><td>Fun Happiness</td><td><%=student.getFunHappinessRating()%></td></tr>
                                <tr><td>Dining Hall Happiness</td><td><%=student.getDiningHallHappinessRating()%></td></tr>
                                <tr><td>Academic Center Happiness</td><td><%=student.getAcademicCenterHappinessRating()%></td></tr>
                                <tr><td>Dorm Happiness</td><td><%=student.getDormHappinessRating()%></td></tr>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <%--<h4>Student Body Happiness</h4>--%>
        <%--<% if (college.getStudentBodyHappiness() >= 80) { %>--%>
        <%--<div class="progress">--%>
            <%--<div class="progress-bar progress-bar-success" role="progressbar"--%>
                 <%--aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:<%=college.getStudentBodyHappiness()%>%">--%>
                <%--<%=college.getStudentBodyHappiness()%>%--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<%--%>
        <%--} else if (college.getStudentBodyHappiness() >= 50 && college.getStudentBodyHappiness() < 80 ){--%>
        <%--%>--%>
        <%--<div class="progress">--%>
            <%--<div class="progress-bar progress-bar-warning" role="progressbar"--%>
                 <%--aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:<%=college.getStudentBodyHappiness()%>%">--%>
                <%--<%=college.getStudentBodyHappiness()%>%--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<% } else if (college.getStudentBodyHappiness() < 50){--%>
        <%--%>--%>
        <%--<div class="col-sm-8">--%>
            <%--<div class="progress">--%>
                <%--<div class="progress-bar progress-bar-danger" role="progressbar"--%>
                     <%--aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:<%=college.getStudentBodyHappiness()%>%">--%>
                    <%--<%=college.getStudentBodyHappiness()%>%--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<% } %>--%>
        <!-- Display a message if defined -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">
        <p></p>
    </div>
</form>
<%--<div class="container">--%>
    <%--<div class="alert alert-success">--%>
        <%--<strong>Info</strong> <%=msg.getMessage()%>--%>
    <%--</div>--%>
<%--</div>--%>
</div>
</body>

