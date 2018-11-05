<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page import="com.endicott.edu.models.NewsFeedItemModel" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.StudentModel" %>
<%@ page import="com.endicott.edu.models.NewsType" %>
<%@ page import="com.endicott.edu.models.NewsLevel" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.endicott.edu.simulators.CollegeManager" %>
<%@ page import="com.endicott.edu.simulators.PopupEventManager" %>
<%@ page import="com.endicott.edu.simulators.GateManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.endicott.edu.simulators.TutorialManager" %>

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
    GateModel gates[] = (GateModel[]) request.getAttribute("gates");
    if(gates == null) {
        gates = new GateModel[0]; // This is really bad
        msg.setMessage(msg.getMessage() + "Attribute for news missing.");
    }
    NewsFeedItemModel news[] = (NewsFeedItemModel[]) request.getAttribute("news");
    if (news == null) {
        news = new NewsFeedItemModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + "Attribute for news missing.");
    }
    StudentModel students[] = (StudentModel[]) request.getAttribute("students");
    if (students == null) {
        students = new StudentModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for students missing.");
    }
    FloodModel floods[] = (FloodModel[]) request.getAttribute("floods");
    if (floods == null) {
        floods = new FloodModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for floods missing.");
    }

    PopupEventManager popupManager = (PopupEventManager) session.getAttribute("popupMan");

    if(popupManager == null){
        popupManager = new PopupEventManager();
        msg.setMessage(msg.getMessage() + "Attribute for Popup Manager is missing.");
    }

    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);

    TutorialModel tip = TutorialManager.getCurrentTip("viewCollege", college.getRunId());
%>

<html>
<title>Enc College World</title>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="resources/style.css">
<link rel="stylesheet" href="resources/jquery.circliful.css">

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
      integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

<!-- JQuery -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
      integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
        integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
        crossorigin="anonymous"></script>

<!-- Circular progress bars -->
<script src="resources/js/jquery.circliful.min.js"></script>
<script>
    $( document ).ready(function() {
        $('#happinessCircle').circliful();
        $('#retentionCircle').circliful();
    });
</script>

<% if (college.getCurrentDay() <= 1) { %>
<script type="text/javascript">
    $(document).ready(function(){
        $("#newCollegePopUp").modal('show');
    });
</script>
<% } %>

<!-- displays modal for events if there are any -->
<% if (popupManager.isQueueInitiated()) { %>
<script type="text/javascript">
    $(document).ready(function(){
        $("#eventPopUp").modal('show');
    });
</script>

</head>
<% } %>
<body>


<form action="viewCollege" method="post">

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
                    <li><a href="viewGates">Objectives</a></li>
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

    <div class="modal fade" id="eventPopUp" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <!-- Creates a modal body for each event in the list-->
            <% for (PopupEventModel event:popupManager.getEventsList()) {%>
                <div class="modal-body">
                    <h3><%=event.getTitle()%></h3>
                    <div>
                        <img style="float:left; margin-right:1em;" src="<%=event.getImagePath()%>" alt="<%=event.getAltImageText()%> "width="100" height="100">
                        <%if(event.getType() == 1){%>
                            <p>
                                <%=event.getDescription()%><br>
                                <input type="submit" class="btn btn-info" style="position: absolute; right: 1em; bottom: 1em;"  name="<%= event.getAcknowledgeButtonCallback()%>" value="<%=event.getAcknowledgeButtonText()%>">
                                <div style="clear: both;"></div>
                            </p>
                        <%}else{%>
                           <p>
                                <%=event.getDescription()%><br>
                                <input type="submit" class="btn btn-info" style="position: absolute; right: 1em; bottom: 1em;" name="<%= event.getRightButtonCallback()%>" value="<%=event.getRightButtonText()%>">
                                <input type="submit" class="btn btn-info" style="position: absolute; right: 10em; bottom: 1em;" name="<%= event.getLeftButtonCallback()%>" value="<%=event.getLeftButtonText()%>">
                                <div style="clear: both;"></div>
                            </p>
                        <%}%>
                    </div>
                </div>
            <%};%>
            <!-- We're not showing the done button because we want each event acknowledged.
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Done</button>
            </div>
            -->
        </div>

    </div>
    </div>




    <div class="container">

        <!-- jumbotron -->
        <div class="jumbotron">
            <div class="row">
                <div class="col-md-3">
                    <h2>Balance $<%=numberFormatter.format(college.getAvailableCash())%>
                    </h2>

                    <p>Day <%=college.getCurrentDay()%>
                    </p>
                    <% if (college.getAvailableCash() <= 0) { %>
                        <h2><p class="text-danger">Bankrupt</h2>
                        <input type="submit" class="btn btn-info" disabled name="nextDayButton" value="Next Day">
                        <input type="submit" class="btn btn-info" disabled name="nextWeekButton" value="Next Week">
                        <input type="submit" class="btn btn-info" disabled name="nextMonthButton" value="Next Month">
                    <%} else {%>
                        <input type="submit" class="btn btn-info" name="nextDayButton" value="Next Day">
                        <input type="submit" class="btn btn-info" name="nextWeekButton" value="Next Week">
                        <!-- input type="submit" class="btn btn-info" name="nextMonthButton" value="Next Month" -->
                    <%}%>
                </div>

                <!-- Tips -->
                <%if (tip != null){%>
                <div class="col-md-5">
                    <h4 style="color:blue">Tip</h4>
                    <div class="well well-lg">
                        <p><%=tip.getBody()%></p>
                    </div>
                    <input type="submit" class="btn btn-info" name="nextTip" value="Next Tip">
                    <input type="submit" class="btn btn-info" name="hideTips" value="Hide Tips">
                </div>
                <%}%>
                <%if (tip == null){%>
                <input type="submit" class="btn btn-info" name="showTips" value="Show Tips">
                <%}%>

                <!-- Flood -->
                <%
                    for(int i = 0; i < floods.length; i++ ){
                        FloodModel f = floods[i];
                        String dormName = f.getDormName(); %>
                    <h4> Dorm <%=dormName%> is flooded.</h4>
                <%
                    }
                %>


            </div>
        </div> <!-- jumbotron -->

        <div class="row">
            <!-- Happiness -->
            <div class="col-sm-2">
                <div class="well well-sm">
                    <%
                        String barType = "progress-bar-success";
                    %>
                    <h2><small>Happiness</small></h2>
                    <!-- progress circle -->
                    <div id="happinessCircle"
                         data-dimension="250"
                         data-text=""
                         data-info="Happiness"
                         data-width="12"
                         data-fontsize="12"
                         data-percent="<%=college.getStudentBodyHappiness()%>"
                         data-fgcolor="#61a9dc"
                         data-bgcolor="#eee"
                         data-fill="#ddd">
                    </div>
                    <br>
                    <a href="#happinessDetails" class="btn btn-info" data-toggle="collapse">Details</a>
                    <div id="happinessDetails" class="collapse">
                        The happiness of the students depends on their health, academic success,
                        tuition bills, and how much fun they are having.
                    </div>
                </div>
            </div>

            <!-- Retention Rate -->
            <div class="col-sm-2">
                <div class="well well-sm">
                    <h2><small>Retention Rate</small></h2>
                    <!-- progress circle: retention rate -->
                    <div id="retentionCircle"
                         data-dimension="250"
                         data-text=""
                         data-info="Retention Rate"
                         data-width="12"
                         data-fontsize="12"
                         data-percent="<%=college.getRetentionRate()%>"
                         data-fgcolor="#61a9dc"
                         data-bgcolor="#eee"
                         data-fill="#ddd">
                    </div>
                    <br>
                    <a href="#retentionDetails" class="btn btn-info" data-toggle="collapse">Details</a>
                    <div id="retentionDetails" class="collapse">
                        The rate is the percentage of students that remained at the college during the last week.
                    </div>
                </div>
            </div>

            <!-- Number of Students -->
            <div class="col-sm-2">
                <div class="well well-sm">
                    <div class="text-center">
                        <h2><%=students.length%>
                        </h2>
                        <h4>Students</h4>
                    </div>
                </div>
                <div class="well well-sm">
                    <div class="text-center">
                        <h2><%=college.getNumberStudentsAccepted()%>
                        </h2>
                        <h4>Accepted Students</h4>
                    </div>
                    <br>
                    <a href="#acceptedDetails" class="btn btn-info" data-toggle="collapse">Details</a>
                    <div id="acceptedDetails" class="collapse">
                        Accepted students enter the college on the first of the month.
                    </div>
                </div>
            </div>


            <!-- Student Faculty Ratio -->
            <div class="col-sm-2">
                <div class="well well-sm">
                    <div class="text-center">
                        <h2><%=college.getStudentFacultyRatio()%>
                        </h2>
                        <h4>Student Faculty Ratio</h4>
                    </div>
                </div>

                    <div class="well well-sm">
                        <div class="form-group">
                            <select class="form-control" id="collegeMode" name="collegeMode">
                                <option value="Play"<% if (college.getMode() == CollegeMode.PLAY) {%> selected <% } %> >Play</option>
                                <option value="Demo" <% if (college.getMode() == CollegeMode.DEMO) {%> selected <% } %> >Demo</option>
                                <option value="Demo Fire"<% if (college.getMode() == CollegeMode.DEMO_FIRE) {%> selected <% } %> >Demo Fire</option>
                                <option value="Demo Plague"<% if (college.getMode() == CollegeMode.DEMO_PLAGUE) {%> selected <% } %> >Demo Plague</option>
                                <option value="Demo Riot"<% if (college.getMode() == CollegeMode.DEMO_RIOT) {%> selected <% } %> >Demo Riot</option>
                            </select>
                        </div>
                        <input type="submit" class="btn btn-info" name="changeCollegeMode" value="Change Mode">
                    </div>

            </div>


            <!-- Tuition -->
            <div class="col-sm-3">
                <form id = "tuitionForm">
                    <div class="well well-sm">
                        <h3>Tuition is $<%=college.getYearlyTuitionCost()%></h3>
                        <form class="form-inline">
                            <div class="form-group">

                                <label class="sr-only" >Amount (in dollars)</label>
                                <div class="input-group">
                                    <div class="input-group-addon">$</div>
                                    <input type="number" name="tuitionValue" class="form-control" id="tuitionValue" placeholder="Amount">
                                    <div class="input-group-addon">.00</div>
                                </div>
                            </div>
                            <!--This bit of css hides the arrows on the above text box
                                these are called spin boxes. If this causes problems
                                just remove the code in the <style> tag!-->
                            <style>
                                input::-webkit-outer-spin-button,
                                input::-webkit-inner-spin-button {
                                    /* display: none; <- Crashes Chrome on hover */
                                    -webkit-appearance: none;
                                    margin: 0; /* <-- Apparently some margin are still there even though it's hidden */
                                }
                            </style>

                            <input type="submit" class="btn btn-primary\" name="updateTuitionButton" value="Update Tuition">
                        </form>
                    </div>
                </form>
            </div>
        </div>

        <!-- Hidden Parameters That Will Be Passed in Request! -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">

        <!-- Newsfeed -->
        <p></p>
        <div class="row">
            <div class="col-sm-6">
                <div class="well well-sm">
                    <h3><p class="text-primary"><%=college.getRunId()%> News</h3>
                    <div class="pre-scrollable">
                        <ul class="list-group">
                            <%
                                for (int i = news.length - 1; i >= 0; i--) {
                                if (news[i].getNoteLevel() == NewsLevel.GOOD_NEWS) {

                            %>
                            <li class="list-group-item">
                                <!-- change this to user up or down arrow depending on money -->
                                <span class="glyphicon glyphicon-thumbs-up" style="color:lawngreen"></span>
                                Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
                            </li>
                            <% } else if (news[i].getNoteLevel() == NewsLevel.BAD_NEWS) {
                            %>

                            <li class="list-group-item">
                                <!-- change this to user up or down arrow depending on money -->
                                <span class="glyphicon glyphicon-thumbs-down"style="color:red"></span>
                                Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
                            </li>
                            <% }
                            } %>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="well well-sm">
                    <h3><p class="text-success">Financial News</h3>
                    <div class="pre-scrollable">
                        <ul class="list-group">
                            <%
                                for (int i = news.length - 1; i >= 0; i--) {
                                    if (news[i].getNoteType() == NewsType.FINANCIAL_NEWS) {
                                        if (news[i].getAmount() > 0) {
                            %>
                            <li class="list-group-item">
                                <!-- change this to user up or down arrow depending on money -->
                                <span class="glyphicon glyphicon-arrow-up" style="color:lawngreen"></span>
                                Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%><span style="color:green"> $<%=news[i].getAmount()%></span>
                            </li>
                            <% } else if (news[i].getAmount() < 0) {
                            %>

                            <li class="list-group-item">
                                <!-- change this to user up or down arrow depending on money -->
                                <span class="glyphicon glyphicon-arrow-down" style="color:red"></span>
                                Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%><span style="color:red"> $<%=-news[i].getAmount()%></span>
                            </li>
                            <% } else {
                            %>

                            <li class="list-group-item">
                                <!-- change this to user up or down arrow depending on money -->
                                <span class="glyphicon glyphicon-arrow-down" style="color:red"></span>
                                Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
                            </li>


                            <% }
                            }
                            }%>
                        </ul>
                    </div>
                </div>


            </div>
        </div>


        <div class="row">


        </div>

        <!-- Mode -->
        <div class="row">
        </div>

    </div> <!-- container -->
</form>
</div>
</body>
</html>
