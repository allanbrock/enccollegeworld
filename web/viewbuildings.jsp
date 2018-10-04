<%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 9/15/2017
  Time: 7:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page import="com.endicott.edu.simulators.CollegeManager" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.endicott.edu.datalayer.BuildingDao" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>College World Building</title>
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
    UiMessage msg = (UiMessage) request.getAttribute("message");
    if (msg == null) {
        msg = new UiMessage();
    }
    CollegeModel college = (CollegeModel) request.getAttribute("college");
    if (college == null) {
        college = new CollegeModel();
        msg.setMessage("Attribute for college missing.");
    }
//    BuildingModel buildings[] = (BuildingModel[]) request.getAttribute("buildings");
//    if (buildings == null) {
//        buildings = new BuildingModel[0];  // This is really bad
//        msg.setMessage(msg.getMessage() + " Attribute for buildings missing.");
//    }
    BuildingModel buildings[] = BuildingDao.getBuildingsArray(college.getRunId());
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
    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>


<form action="viewBuilding" method="post">

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
                    <li class="active"><a href="viewDorm">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li><a href="viewFaculty">Faculty</a></li>
                    <li><a href="viewBalance">Balance $<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a> <%=new SimpleDateFormat("MM/dd/yyyy").format(CollegeManager.getCollegeDate(college.getRunId()))%> </a></li>
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
                    <img class="img-responsive" src="resources/images/bed.png">
                </div>
                <div class="col-md-2">
                    <h4><%
                        int openBeds = 0;
                        int filledBeds = 0;
                        for (BuildingModel d : buildings){
                            if(d.getHoursToComplete() == 0){
                                if(d.getKindOfBuilding().equals("Dorm")) {
                                    int numStudents = d.getNumStudents();
                                    int capacity = d.getCapacity();
                                    openBeds += capacity - numStudents;
                                    filledBeds += d.getNumStudents();
                                }
                            }
                        }
                    %>
                        <%=openBeds%> Open Beds</h4>
                        <%=filledBeds%> Filled Beds</h4>
                </div>
                <div class="col-md-2">
                    <img class="img-responsive" src="resources/images/plate.png">
                </div>
                <div class="col-md-2">
                    <h4><%
                        int availablePlates = 0;
                        int takenPlates = 0;
                        for (BuildingModel d : buildings){
                            if(d.getHoursToComplete() == 0){
                                if(d.getKindOfBuilding().equals("Dining")) {
                                    int numStudents = d.getNumStudents();
                                    int capacity = d.getCapacity();
                                    availablePlates += capacity - numStudents;
                                    takenPlates += d.getNumStudents();
                                }
                            }
                        }
                    %>
                        <%=availablePlates%> Available Plates</h4>
                        <%=takenPlates%> Taken Plates</h4>
                </div>
                <div class="col-md-2">
                    <img class="img-responsive" src="resources/images/desk.png">
                </div>
                <div class="col-md-2">
                    <h4><%
                        int openDesks = 0;
                        int filledDesks = 0;
                        for (BuildingModel d : buildings){
                            if(d.getHoursToComplete() == 0){
                                if(d.getKindOfBuilding().equals("Academic")) {
                                    int numStudents = d.getNumStudents();
                                    int capacity = d.getCapacity();
                                    openDesks += capacity - numStudents;
                                    filledDesks += d.getNumStudents();
                                }
                            }
                        }
                    %>
                        <%=openDesks%> Open Desks</h4>
                        <%=filledDesks%> Filled Desks</h2>
                </div>
            </div>
        </div>
        <!-- Display a message if defined -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">
        <p></p>
        <div class="well well-sm">
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>Building Name</th>
                        <th>Building Type</th>
                        <th>Size</th>
                        <th>Open Spots</th>
                        <th>Quality</th>
                        <th>Current Disaster</th>
                        <th>Status</th>
                        <th></th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < buildings.length; i++) {
                %>
                <tr>
                    <td><%=buildings[i].getName()%>
                    </td>
                    <td><%=buildings[i].getKindOfBuilding()%>
                    </td>
                    <td><%=buildings[i].getSize()+ " (" +buildings[i].getCapacity() +")"%>
                    </td>
                    <td><%=buildings[i].getCapacity() - buildings[i].getNumStudents()%>
                    </td>
                    <td><%=buildings[i].getReputation()%>
                    </td>
                    <td><%=buildings[i].getCurDisaster()%>
                    </td>
                    <td><%=buildings[i].checkIfBeingBuilt()%>
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
                    <label for="buildingType">Select a building type</label>
                    <select class="form-control" id="buildingType" name="buildingType">
                        <option>Academic Center</option>
                        <option>Dining Hall</option>
                        <option>Dormitory</option>
                        <option>Entertainment Center</option>
                        <option>Health Center</option>
                        <option>Library</option>
                        <option>Sports Center</option>
                    </select>

                    <%%>
                        <label for="buildingSize" > Select a building size</label >
                    <%if(college.getNumberStudentsAdmitted()<700){%>
                        <select class="form-control" id = "buildingSize" name = "buildingSize" >
                            <option > Small </option >
                            <option > Medium </option >
                        </select >
                    <%}else if(college.getNumberStudentsAdmitted()<1500){%>
                        <select class="form-control" id = "buildingSize" name = "buildingSize" >
                            <option > Small </option >
                            <option > Medium </option >
                            <option > Large </option >
                        </select >
                    <%}else{%>
                        <select class="form-control" id = "buildingSize" name = "buildingSize" >
                            <option > Small </option >
                            <option > Medium </option >
                            <option > Large </option >
                            <option > Extra Large </option >
                        </select >
                    <%}%>
                    <div class="form-group">
                        <input type="text" class="form-control" id="buildingName" name="buildingName"
                               placeholder="Enter building name.">
                    </div>
                    <!-- Button -->
                    <input type="submit" class="btn btn-info" name="purchaseBuilding" value="Purchase Building">
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

