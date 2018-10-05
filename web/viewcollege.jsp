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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    PopupEventManager popupManager = (PopupEventManager) request.getAttribute("popupMan");

    if(popupManager == null){
        popupManager = new PopupEventManager();
        msg.setMessage(msg.getMessage() + "Attribute for Popup Manager is missing.");
    }
    popupManager.newPopupEvent("Test Event", "Test Event", "This event is a test of the popup system! Press 'Ok!' to dismiss for now", "Ok!");

    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>

<html>
<title>Enc College World</title>
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
<% if (college.getCurrentDay() <= 1) { %>
<script type="text/javascript">
    $(document).ready(function(){
        $("#newCollegePopUp").modal('show');
    });
</script>x
<% } %>

<!-- displays modal for events if there are any -->
<% if (popupManager.getNumberOfEvents() >= 1) { %>
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
                    <li><a href="viewBalance">Balance $<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a> <%=new SimpleDateFormat("MM/dd/yyyy").format(CollegeManager.getCollegeDate(college.getRunId()))%> </a></li>
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
                <div class="modal-header">
                    <!-- hard coded title to avoid accessing ArrayList becuase of out of bounds exceptions-->
                    <h4 class="modal-title"><%=popupManager.getNextEvent().getTitle()%></h4>
                    <%--<h4 class="modal-title">Test event</h4>--%>
                </div>
                <div class="modal-body">
                    <!-- viewCollege - a popup should have the name of the servlet to call (viewDorms, viewCollege... -->
                    <p><%=popupManager.getNextEvent().getDescription()%></p>
                    <%--<p>This event is a test of the Popup Event system</p>--%>
                    <!-- the popup may or maynot have buttons. -->
                    <!-- each button needs a name and value (both strings) -->
                    <input type="button" class="btn btn-info" name="acknowledgeButton" value="TEST">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><%=popupManager.getNextEvent().getAcknowledgeButtonText()%></button>
                        <%--<button type="button" class="btn btn-default" data-dismiss="modal">Ok!</button>--%>
                </div>
            </div>

        </div>
    </div>



    <div class="container">

        <!-- jumbotron -->
        <div class="jumbotron">
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
                <input type="submit" class="btn btn-info" name="nextMonthButton" value="Next Month">
            <%}%>
            <br>
            <br>
            <!-- This button is just to demonstrate a second popup modal until PopUpManager is fully implimented -->
            <%--<button type="button" class="btn btn-info" onclick="<%popupManager.newPopupEvent("Test Event", "Test Event", "This event is a test of the popup system", "Ok!");%>" data-toggle="modal" data-target="#eventPopUp">test event</button>--%>
            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#eventPopUp">test event</button>

            <!-- Flood -->
            <%
                for(int i = 0; i < floods.length; i++ ){
                    FloodModel f = floods[i];
                    String dormName = f.getDormName(); %>
                <h4> Dorm <%=dormName%> is flooded.</h4>
            <%
                }
            %>

        </div> <!-- jumbotron -->

        <div class="row">
            <!-- Happiness -->
            <div class="col-sm-3">
                <div class="well well-sm">
                    <%
                        String barType = "progress-bar-success";
                    %>
                    <% if(college.getStudentBodyHappiness() > 50) { %>
                    <h2>&#9786;
                        <small>Student Happiness</small>
                    </h2>
                    <% } else { %>
                    <h2>&#9785;
                        <small>Student Happiness</small>
                    </h2>
                    <% } %>
                    <% if (college.getStudentBodyHappiness() >= 60) { %>
                    <div class="progress">
                        <div class="progress-bar progress-bar-success" role="progressbar"
                             aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                             style="width:<%=college.getStudentBodyHappiness()%>%">
                            <%=college.getStudentBodyHappiness()%>%
                        </div>
                    </div>
                    <%
                    } else if (college.getStudentBodyHappiness() >= 30) {
                    %>
                    <div class="progress">
                        <div class="progress-bar progress-bar-warning" role="progressbar"
                             aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                             style="width:<%=Math.max(college.getStudentBodyHappiness(),20)%>%">
                            <%=college.getStudentBodyHappiness()%>%
                        </div>
                    </div>
                    <% } else {
                    %>
                    <div class="progress">
                        <div class="progress-bar progress-bar-danger" role="progressbar"
                             aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                             style="width:<%=Math.max(college.getStudentBodyHappiness(),20)%>%">
                            <%=college.getStudentBodyHappiness()%>%
                        </div>
                    </div>
                    <% } %>
                    <br>
                    <a href="#happinessDetails" class="btn btn-info" data-toggle="collapse">Details</a>
                    <div id="happinessDetails" class="collapse">
                        College Reputation
                        <%  if (college.getReputation() < 30) {
                            barType = "progress-bar-danger";
                        } else if (college.getReputation() < 60) {
                            barType = "progress-bar-warning";
                        } else {
                            barType = "progress-bar-success";
                        }
                        %>
                        <div class="progress">
                            <div class="progress-bar <%=barType%>" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:<%=Math.max(college.getReputation(),20)%>%">
                                <%=college.getReputation()%>%
                            </div>
                        </div>
                        Student/Faculty Ratio
                        <%  if (college.getStudentFacultyRatioRating() < 30) {
                            barType = "progress-bar-danger";
                        } else if (college.getStudentFacultyRatioRating() < 60) {
                            barType = "progress-bar-warning";
                        } else {
                            barType = "progress-bar-success";
                        }
                        %>
                        <div class="progress">
                            <div class="progress-bar <%=barType%>" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:<%=Math.max(college.getStudentFacultyRatioRating(),20)%>%">
                                <%=college.getStudentFacultyRatioRating()%>%
                            </div>
                        </div>
                        Tuition
                        <%  if (college.getYearlyTuitionRating() < 30) {
                                barType = "progress-bar-danger";
                            } else if (college.getYearlyTuitionRating() < 60) {
                                barType = "progress-bar-warning";
                            }  else {
                            barType = "progress-bar-success";
                        }
                        %>
                        <div class="progress">
                            <div class="progress-bar <%=barType%>" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:<%=Math.max(college.getYearlyTuitionRating(),20)%>%">
                                <%=college.getYearlyTuitionRating()%>%
                            </div>
                        </div>
                        Student Health
                        <%  if (college.getStudentHealthRating() < 30) {
                            barType = "progress-bar-danger";
                        } else if (college.getStudentHealthRating() < 60) {
                            barType = "progress-bar-warning";
                        }  else {
                            barType = "progress-bar-success";
                        }
                        %>
                        <div class="progress">
                            <div class="progress-bar <%=barType%>" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:<%=Math.max(college.getStudentHealthRating(),20)%>%">
                                <%=college.getStudentHealthRating()%>%
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Number of Students -->
            <div class="col-sm-3">
                <div class="well well-sm">
                    <div class="text-center">
                        <h1><%=students.length%>
                        </h1>
                        <h3>Students</h3>
                    </div>
                </div>
            </div>

            <!-- Retention Rate -->
            <div class="col-sm-3">
                <div class="well well-sm">
                    <div class="text-center">
                        <h1><%=college.getRetentionRate()%>%
                        </h1>
                        <h3>Retention Rate</h3>
                    </div>
                </div>
            </div>

            <!-- Ranking -->
            <div class="col-sm-3">
                <div class="well well-sm">
                    <div class="text-center">
                        <h1>?
                        </h1>
                        <h3>Ranking</h3>
                    </div>
                    <br>
                    <a href="#rankingDetails" class="btn btn-info" data-toggle="collapse">Details</a>
                    <div id="rankingDetails" class="collapse">
                        Coming in FA 2018!
                    </div>
                </div>
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

            <!-- Tuition -->
            <div class="col-sm-6">
                <form id = "tuitionForm">
                    <div class="well well-sm">
                        <h3>Tuition: $<%=college.getYearlyTuitionCost()%></h3>
                        <p>Update Tuition</p>
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
                                just remove the coode in the <style> tag!-->
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

            <!-- Student Faculty Ratio -->
            <div class="col-sm-3">
                <div class="well well-sm">
                    <div class="text-center">
                        <h1><%=college.getStudentFacultyRatio()%>
                        </h1>
                        <h3>Student Faculty Ratio</h3>
                    </div>
                </div>
            </div>
        </div>

        <!-- Server -->
        <div class="row">
            <div class="col-sm-6">
                <div class="well well-sm">
                    <strong>Info</strong> <%=msg.getMessage()%>
                </div>
            </div>
        </div>

    </div> <!-- container -->
</form>
</div>
</body>
</html>
