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
<%@ page import="com.endicott.edu.simulators.BuildingManager" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.endicott.edu.datalayer.BuildingDao" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.endicott.edu.simulators.GateManager" %>
<%@ page import="com.endicott.edu.simulators.TutorialManager" %>
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

<!-- solution from https://www.experts-exchange.com/questions/20683436/Using-anchors-in-JSP-code.html-->
<% if( request.getParameter("hash") != null ) { %>
<script>
    location.hash = "<%=request.getParameter("hash")%>";
</script>
<% }%>

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
    String sortByType = (String) request.getAttribute("sortByType");
    List<BuildingModel> buildings;
    if(sortByType == null  || sortByType.equals("All Buildings")){
        buildings = BuildingDao.getBuildings(college.getRunId());
    }
    else{
        buildings = BuildingManager.getBuildingListByType(sortByType, college.getRunId());
    }

    NewsFeedItemModel news[] = (NewsFeedItemModel[]) request.getAttribute("news");
    if (news == null) {
        news = new NewsFeedItemModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + "Attribute for news missing.");
    }

    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);

    String beginPurchase = (String) request.getAttribute("beginBuildingPurchase");
    String wasBuildingTypeSelected = (String) request.getAttribute("wasBuildingTypeSelected");
    String buildingSize = (String) request.getAttribute("buildingSize");
    String randomName = (String) request.getAttribute("randomName");
    TutorialModel tip = TutorialManager.getCurrentTip("viewBuildings", college.getRunId());

    String haveEntertainmentCenter = "false";
    String haveHealthCenter = "false";
    String haveLibrary = "false";
    String haveBaseballDiamond = "false";
    String haveFootballStadium = "false";
    String haveHockeyRink = "false";

    GateManager gateManager = new GateManager();
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
                    <li class="active"><a href="/viewBuilding">Buildings</a></li>
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

    <%--This shows the user if they have enough spots for the students.
        It shows if there are enough beds, plates, and desks at the college.--%>
    <div class="container">
        <div class="jumbotron" style="background-color: aliceblue">
            <div class="row">
                <div class="col-md-2">
                    <img class="img-responsive" src="resources/images/bed.png">
                </div>
                <div class="col-md-2">
                    <h4><%
                        int openBeds = 0;
                        int filledBeds = 0;
                        for (BuildingModel b : buildings){
                            if(b.isBuilt()){
                                if(b.getKindOfBuilding().equals(BuildingModel.getDormConst())) {
                                    int numStudents = b.getNumStudents();
                                    int capacity = b.getCapacity();
                                    openBeds += capacity - numStudents;
                                    filledBeds += b.getNumStudents();
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
                        for (BuildingModel b : buildings){
                            if(b.isBuilt()){
                                if(b.getKindOfBuilding().equals(BuildingModel.getDiningConst())) {
                                    int numStudents = b.getNumStudents();
                                    int capacity = b.getCapacity();
                                    availablePlates += capacity - numStudents;
                                    takenPlates += b.getNumStudents();
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
                        for (BuildingModel b : buildings){
                            if(b.isBuilt()){
                                if(b.getKindOfBuilding().equals(BuildingModel.getAcademicConst())) {
                                    int numStudents = b.getNumStudents();
                                    int capacity = b.getCapacity();
                                    openDesks += capacity - numStudents;
                                    filledDesks += b.getNumStudents();
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
        <input type="hidden" name="runId" value="<%=college.getRunId()%>">
        <p></p>

        <div class="well well-sm" style="background: aliceblue;">
            <div class="col-sm-5" style=" margin-top: 30px;">
        <div class="form-group">
            <%--Sorting dropdown--%>
            <label for="buildingSize">Filter by Building Type</label>
            <select class="form-control" id="sortByBuildingType" name="sortByBuildingType" style="width: 160px;">
                <option value="All Buildings">All Buildings</option>
                <option value="Administrative Building">Administrative Building</option>
                <option value="Academic Center">Academic Center</option>
                <option value="Baseball Diamond">Baseball Diamond</option>
                <option value="Dining Hall">Dining Hall</option>
                <option value="Dormitory">Dormitory</option>
                <option value="Football Stadium">Football Stadium</option>
                <option value="Hockey Rink">Hockey Rink</option>
                <option value="Entertainment Center">Entertainment Center</option>
                <option value="Health Center">Health Center</option>
                <option value="Library">Library</option>
            </select>
            <!-- Button -->
            <input type="submit" class="btn btn-info" name="startSortByBuildingType" value="Sort" style="margin-top: 5px;">
        </div>
            </div>
            <!--Tips-->
            <%if (tip != null){%>
            <div class="col-sm-5" style="float: right;">
                <div class="well well-lg" style="float: right; vertical-align: top; display: inline-block;  background: white;">
                    <%if (!tip.getImage().equals("")){%>
                    <img class="img-responsive" src="resources/images/<%=tip.getImage()%>">
                    <%}%>
                    <p><%=tip.getBody()%></p>
                    <input type="submit" class="btn btn-info" name="nextTip" value="Next Tip">
                    <input type="submit" class="btn btn-info" name="hideTips" value="Hide Tips">
                </div>
            </div>
            <%}%>
            <%if (tip == null){%>
            <input type="submit" class="btn btn-info" name="showTips" value="Show Tips">
            <%}%>

            <%--Table where all the buildings are displayed with their stats--%>
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <%--Column headers--%>
                        <th>Building Name</th>
                        <th>Building Type</th>
                        <th>Size</th>
                        <th>Open Spots</th>
                        <th>Quality</th>
                        <th>Current Disaster</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int b = 0; b < buildings.size(); b++) {
                %>
                <tr>
                    <%--This is each ROW in the table--%>
                    <td style="vertical-align:middle; font-size:110%; max-width:60px; word-wrap:break-word;"><%=buildings.get(b).getName()%>
                    </td>
                    <%--Picture--%>
                    <td>
                        <%if(buildings.get(b).getHoursToComplete() > 0){%>
                            <img class="img-responsive" src="resources/images/underconstruction.png" style="width:60px; height:60px; display: block; margin: 0 auto;">
                        <%}else{%>
                            <img class="img-responsive" src="resources/images/<%=buildings.get(b).getKindOfBuilding()%>.png" style="width:60px; height:60px; display: block; margin: 0 auto;">
                        <%}%>
                        <%=buildings.get(b).getKindOfBuilding()%>
                    </td>
                    <td style="vertical-align:middle; font-size:110%;"><%=buildings.get(b).getSize()+ " (" +buildings.get(b).getCapacity() +")"%>
                    </td>
                    <td style="vertical-align:middle; font-size:110%;"><%=buildings.get(b).getCapacity() - buildings.get(b).getNumStudents()%>
                    </td>
                    <%--Progress bar for quality--%>
                    <td style="vertical-align:middle;">
                        <div class="progress">
                            <div class="progress-bar progress-bar-info" role="progressbar"
                                 aria-valuemin="0" aria-valuemax="100" style="width:<%=buildings.get(b).getShownQuality()%>%">
                                <%=buildings.get(b).getShownQualityString()%>%
                            </div>
                        </div>
                    </td>
                    <td style="vertical-align:middle; font-size:110%;"><%=buildings.get(b).getCurDisaster()%>
                    </td>
                    <td style="vertical-align:middle; font-size:110%;"><%=buildings.get(b).checkIfBeingBuilt()%>
                    </td>
                    <%--Upgrade and repair buttons.
                        They should only show when:
                        - The building ISN'T maximum size
                        - The building has a size parameter
                        - The buiding ISN'T under construction
                        - The college has enough money to purchase the building--%>
                    <td style="vertical-align:middle;">
                        <%if(!( buildings.get(b).getSize().equals("Extra Large") || buildings.get(b).getSize().equals("N/A")
                                || buildings.get(b).getHoursToComplete() > 0 || buildings.get(b).getUpgradeCost() > college.getAvailableCash())){%>
                            <input style="horiz-align: left; font-size: 75%" type="submit" class="btn btn-info" name="<%="upgradeBuilding" + b%>" value="Upgrade ($<%=buildings.get(b).getUpgradeCost()%>)">
                        <%}%>
                        <%if(buildings.get(b).getRepairCost() > college.getAvailableCash() || buildings.get(b).getRepairCost() > 0
                                && !(buildings.get(b).getHoursToComplete() > 0) && buildings.get(b).isUpgradeComplete()){%>
                            <input style="margin-top: 5px; horiz-align: left; font-size: 75%" type="submit" class="btn btn-info" name="<%="repairBuilding" + b%>" value="Repair ($<%=buildings.get(b).getRepairCost()%>)">
                        <%}%>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>

        </div>

        <!-- Add Dorm -->
        <div class="col-sm-4" >
            <div class="well well-sm" style="background: aliceblue;">
                <div id="purchase">
                    <!-- if they don't have enough money for the cheapest building they can't try to purchase -->
                    <%if(college.getAvailableCash() <= 50000){%>
                    <h4>You don't have enough money to buy a new building :(</h4>
                    <%}else{%>
                    <!-- if they haven't hit begin purchase, only one option i s visible -->
                <% if(beginPurchase == "false"){%>
                    <h4>Purchase a new Building</h4>
                    <input type="submit" class="btn btn-info" name="beginBuildingPurchase" value="Begin">
                <%}else if(wasBuildingTypeSelected == "false"){ %>
                    <div class="form-group">
                        <label for="buildingSize">Select a building type</label>
                        <select class="form-control" id="buildingSize" name="buildingSize">
                            <option value="Academic Center">Academic Center</option>
                            <option value="Dining Hall">Dining Hall</option>
                            <option value="Dormitory">Dormitory</option>
                            <%for (int b = 0; b < buildings.size(); b++) {
                                if(buildings.get(b).getKindOfBuilding().equals("ENTERTAINMENT")){
                                    haveEntertainmentCenter = "true";
                                }
                                if(buildings.get(b).getKindOfBuilding().equals("HEALTH")){
                                    haveHealthCenter = "true";
                                }
                                if(buildings.get(b).getKindOfBuilding().equals("LIBRARY")){
                                    haveLibrary = "true";
                                }
                                if(buildings.get(b).getKindOfBuilding().equals("BASEBALL DIAMOND")){
                                    haveBaseballDiamond = "true";
                                }
                                if(buildings.get(b).getKindOfBuilding().equals("FOOTBALL STADIUM")){
                                    haveFootballStadium = "true";
                                }
                                if(buildings.get(b).getKindOfBuilding().equals("HOCKEY RINK")){
                                    haveHockeyRink = "true";
                                }
                            }
                                if(college.getAvailableCash() > 150000){
                                if(haveBaseballDiamond.equals("false")){%>
                                <option value="Baseball Diamond" <%if(!gateManager.testGate(college.getRunId(), "Baseball diamond")){%>disabled<%}%>>Baseball Diamond</option>
                            <%}
                            if(haveFootballStadium.equals("false")){%>
                                <option value="Football Stadium" <%if(!gateManager.testGate(college.getRunId(), "Football stadium")){%>disabled<%}%>>Football Stadium</option>
                            <%}
                            if(haveHockeyRink.equals("false")){%>
                                <option value="Hockey Rink" <%if(!gateManager.testGate(college.getRunId(), "Hockey rink")){%>disabled<%}%>>Hockey Rink</option>
                            <%}
                                }%>
                            <%
                                if(college.getAvailableCash() > 250000){
                                if(haveLibrary.equals("false")){%>
                                <option value="Library" <%if(!gateManager.testGate(college.getRunId(), "Library")){%>disabled<%}%>>Library</option>
                            <%}
                            if(haveHealthCenter.equals("false")){%>
                                <option value="Health Center" <%if(!gateManager.testGate(college.getRunId(), "Health Center")){%>disabled<%}%>>Health Center</option>
                            <%}
                            if(haveEntertainmentCenter.equals("false")){%>
                                <option value="Entertainment Center" <%if(!gateManager.testGate(college.getRunId(), "Entertainment Center")){%>disabled<%}%>>Entertainment Center</option>
                            <%}
                            }%>
                        </select>
                    </div>
                        <!-- Button -->
                    <input type="submit" class="btn btn-info" name="selectBuildingType" value="Select">
                <%}else if(wasBuildingTypeSelected == "true"){ %>
                    <!-- if the building selected is a building with a size-->
                    <div class="form-group">
                        <%if(buildingSize.equals("Dormitory") || buildingSize.equals("Dining Hall") ||
                                buildingSize.equals("Academic Center")){%>
                            <label for="buildingSize" > Select a building size</label >
                            <select class="form-control" id = "buildingSize" name = "buildingSize" >
                                <!--if they can afford everything they can see everything-->
                                <%if(college.getAvailableCash() > 50000){%>
                                    <option> $50,000 - Small (50) </option >
                                <%}if(college.getAvailableCash() > 150000){%>
                                    <option > $150,000 - Medium (200) </option >
                                <%}if(college.getAvailableCash() > 350000){%>
                                    <%if(gateManager.testGate(college.getRunId(), "Large Size")){%>
                                        <option > $350,000 - Large (500) </option >
                                    <%}%>
                                <%}if(college.getAvailableCash() > 650000){%>
                                    <%if(gateManager.testGate(college.getRunId(), "Extra Large Size")){%>
                                        <option > $650,000 - Extra Large (1000) </option >
                                    <%}%>
                                <%}%>
                            </select >
                        <%}else if(buildingSize.equals("Football Stadium") || buildingSize.equals("Baseball Diamond")
                                || buildingSize.equals("Hockey Rink") && college.getAvailableCash() > 150000){%>
                            <select class="form-control" id = "buildingSize" name = "buildingSize" >
                                <option > $150,000 - Medium </option >
                            </select >
                        <%}%>
                        <h4>Confirm Purchase of <%=buildingSize%></h4>
                        <div class="form-group">
                            <%if(randomName != null){%>
                            <input type="text" class="form-control" id="buildingName" name="buildingName"
                               value="<%=randomName%> Hall">
                            <%}else{%>
                            <input type="text" class="form-control" id="buildingName" name="buildingName"
                                   placeholder="Enter Building Name">
                            <%}%>
                            <!--random name choosing button-->
                            <input type="submit" class="btn btn-info" id="randomBuildingName" name="randomBuildingName" value="Random Name" style="margin-top: 5px;">
                        </div>
                        <!-- Button -->
                        <input type="submit" class="btn btn-info" id="purchaseBuilding" name="purchaseBuilding" value="Purchase Building">
                    </div>
                <%}%>
                    <%}%>
                </div>
            </div>
        </div>

        <!-- DORM NEWS -->
        <div class="row">
            <div class="col-sm-6" style="margin-left: 150px;">
                <div class="well well-sm" style="background: aliceblue;">
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

    <%--Gate progress Jumbotron--%>
    <div class="container">
        <div class="jumbotron" style="background-color: aliceblue">
            <div class="row">
                <div class="col-md-12">
                <div class="col-md-2" style="margin-right: 3%;">
                    <h4>Large Size</h4>
                    <img class="img-responsive" src="resources/images/EXTRA_LARGE_DORM_k.png">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                            aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Large Size")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Large Size")%>%
                        </div>
                    </div>
                </div>
                <div class="col-md-2" style="margin-right: 3%">
                    <h4>Extra Large Size</h4>
                    <img class="img-responsive" src="resources/images/EXTRA_LARGE_DORM_k.png">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Extra Large Size")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Extra Large Size")%>%
                        </div>
                    </div>
                </div>
                <div class="col-md-2" style="margin-right: 3%">
                    <h4>Library</h4>
                    <img class="img-responsive" src="resources/images/LIBRARY_k.png" style="margin-top: 37px; margin-bottom: 38px;">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Library")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Library")%>%
                        </div>
                    </div>
                </div>
                <div class="col-md-2" style="margin-right: 3%">
                    <h4>Health Center</h4>
                    <img class="img-responsive" src="resources/images/HEALTH_k.png" style=" margin-top: 37px; margin-bottom: 38.5px;">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Health Center")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Health Center")%>%
                        </div>
                    </div>
                </div>
                </div>
                <div class="col-md-12">
                <div class="col-md-2" style="margin-right: 3%">
                    <h4>Entertainment Center</h4>
                    <img class="img-responsive" src="resources/images/ENTERTAINMENT_k.png" style="margin-bottom: 28px;">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Entertainment Center")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Entertainment Center")%>%
                        </div>
                    </div>
                </div>
                <div class="col-md-2" style="margin-right: 3%;">
                    <h4>Football Stadium</h4>
                    <img class="img-responsive" src="resources/images/FOOTBALL%20STADIUM_k.png">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Football stadium")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Football stadium")%>%
                        </div>
                    </div>
                </div>
                <div class="col-md-2" style="margin-right: 3%;">
                    <h4>Baseball Diamond</h4>
                    <img class="img-responsive" src="resources/images/BASEBALL%20DIAMOND_k.png" style="margin-top: 33px; margin-bottom: 34px;">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Baseball diamond")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Baseball diamond")%>%
                        </div>
                    </div>
                </div>
                <div class="col-md-2" style="margin-right: 3%;">
                    <h4>Hockey Rink</h4>
                    <img class="img-responsive" src="resources/images/HOCKEY%20RINK_k.png" style="margin-top: 41px; margin-bottom: 47px;">
                    <h5>Total progress:</h5>
                    <div class="progress">
                        <div class="progress-bar progress-bar-info" role="progressbar"
                             aria-valuemin="0" aria-valuemax="100" style="border-radius: 5px; width:<%=gateManager.getGateProgress(college.getRunId(), "Hockey rink")%>%; height:25px">
                            <%=gateManager.getGateProgress(college.getRunId(), "Hockey rink")%>%
                        </div>
                    </div>
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

