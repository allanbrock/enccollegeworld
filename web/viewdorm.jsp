<%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 9/15/2017
  Time: 7:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page import="com.endicott.edu.datalayer.DormSimTalker" %><%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 8/25/2017
  Time: 8:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>College World Dorm</title>
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
    DormitoryModel dorms[] = (DormitoryModel[]) request.getAttribute("dorms");
    if (dorms == null) {
        dorms = new DormitoryModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for dorms missing.");
    }
    NewsFeedItemModel news[] = (NewsFeedItemModel[]) request.getAttribute("news");
    if (news == null) {
        news = new NewsFeedItemModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + "Attribute for news missing.");
    }

    DormitoryModel availableDorms[] = (DormitoryModel[]) request.getAttribute("availableDorms");
    if (availableDorms == null) {
        availableDorms = new DormitoryModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + "Attribute for news missing.");
    }
%>


<form action="viewDorm" method="post">

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
                    <li><a href="viewStudent?runid=<%=college.getRunId()%>&server=<%=server%>">Students</a></li>
                    <li class="active"><a href="viewDorm?runid=<%=college.getRunId()%>&server=<%=server%>">Dorms</a></li>
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
                    <img class="img-responsive" src="resources/images/dorm.png">
                </div>
                <div class="col-md-10">
                    <h2><%
                        int openBeds = 0;
                        int filledBeds = 0;
                        for (DormitoryModel d : dorms){
                            if(d.getHoursToComplete() == 0){
                                int numStudents = d.getNumStudents();
                                int capacity = d.getCapacity();
                                openBeds += capacity - numStudents;
                                filledBeds += d.getNumStudents();
                            }
                        }
                    %>
                        <%=openBeds%> Open Beds</h2>
                        <%=filledBeds%> Filled Beds</h2>
                </div>
            </div>
        </div>
        <!-- Display a message if defined -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">
        <input type="hidden" name="server" value="<%=server%>">
        <p></p>
        <div class="well well-sm">
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>Dorm Name</th>
                        <th>Total Beds</th>
                        <th>Filled Beds</th>
                        <th>Current Disaster</th>
                        <th>Status</th>
                        <th></th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < dorms.length; i++) {
                %>
                <tr>
                    <td><%=dorms[i].getName()%>
                    </td>
                    <td><%=dorms[i].getCapacity()%>
                    </td>
                    <td><%=dorms[i].getNumStudents()%>
                    </td>
                    <td><%=dorms[i].getCurDisaster()%>
                    </td>
                    <td><%=dorms[i].checkIfBeingBuilt()%>
                    </td>
                    <td>
                        <!-- the name of the button is encoding version of dorm name, value of button is "Sell" -->
                        <input type="submit" class="btn btn-info" name=<%=Base64.getEncoder().encodeToString((dorms[i].getName()).getBytes())%> value="Sell">
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>

        </div>

        <!-- Add Dorm -->
        <div class="col-sm-4">
            <div class="well well-sm">
                <div class="form-group">
                    <label for="dormType">Select a dorm type to add</label>
                    <select class="form-control" id="dormType" name="dormType">
                        <option>Small</option>
                        <option>Medium</option>
                        <option>Large</option>
                    </select>
                    <div class="form-group">
                        <input type="text" class="form-control" id="dormName" name="dormName"
                               placeholder="Enter dorm name.">
                    </div>
                    <!-- Button -->
                    <input type="submit" class="btn btn-info" name="addDorm" value="Add Dorm">
                </div>
            </div>
        </div>

        <!-- DORM NEWS -->
        <div class="row">
            <div class="col-sm-6">
                <div class="well well-sm">
                    <h3><p style="color: purple"><%=college.getRunId()%> Resident News</h3>
                    <div class="pre-scrollable">
                        <ul class="list-group">
                            <%
                                for (int i = news.length - 1; i >= 0; i--) {
                                    if (news[i].getNoteType() == NewsType.RES_LIFE_NEWS) {
                            %>
                            <li class="list-group-item"> Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
                            </li>
                            <% }
                            } %>
                        </ul>
                    </div>
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

