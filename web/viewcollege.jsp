<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page import="com.endicott.edu.models.NewsFeedItemModel" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.StudentModel" %>
<%@ page import="com.endicott.edu.models.NewsType" %>
<%@ page import="com.endicott.edu.models.NewsLevel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<title>Enc College World</title>
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
    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>


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
                    <li class="active"><a
                            href="viewCollege?runid=<%=college.getRunId()%>&server=<%=server%>"><%=college.getRunId()%>
                    </a></li>
                    <li><a href="viewStudent?runid=<%=college.getRunId()%>&server=<%=server%>">Students</a></li>
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

        <!-- jumbotron -->
        <div class="jumbotron">
            <h2>Balance $<%=numberFormatter.format(college.getAvailableCash())%>
            </h2>

            <p>Day <%=college.getCurrentDay()%>
            </p>
            <% if (college.getAvailableCash() <= 0) { %>
            <h2><p class="text-danger">Bankrupt</h2>
            <input type="submit" class="btn btn-info" disabled name="nextDayButton" value="Next Day">
            <%} else {%>
            <input type="submit" class="btn btn-info" name="nextDayButton" value="Next Day">
            <%}%>
        </div> <!-- jumbotron -->

        <div class="row">
            <!-- Happiness -->
            <div class="col-sm-3">
                <div class="well well-sm">
                    <% if(college.getStudentBodyHappiness() > 50) { %>
                    <h2>&#9786;
                        <small>Student Happiness</small>
                    </h2>
                    <% } else { %>
                    <h2>&#9785;
                        <small>Student Happiness</small>
                    </h2>
                    <% } %>
                    <% if (college.getStudentBodyHappiness() >= 80) { %>
                    <div class="progress">
                        <div class="progress-bar progress-bar-success" role="progressbar"
                             aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                             style="width:<%=college.getStudentBodyHappiness()%>%">
                            <%=college.getStudentBodyHappiness()%>%
                        </div>
                    </div>
                    <%
                    } else if (college.getStudentBodyHappiness() >= 50 && college.getStudentBodyHappiness() < 80) {
                    %>
                    <div class="progress">
                        <div class="progress-bar progress-bar-warning" role="progressbar"
                             aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                             style="width:<%=college.getStudentBodyHappiness()%>%">
                            <%=college.getStudentBodyHappiness()%>%
                        </div>
                    </div>
                    <% } else if (college.getStudentBodyHappiness() < 50) {
                    %>
                    <div class="progress">
                        <div class="progress-bar progress-bar-danger" role="progressbar"
                             aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                             style="width:<%=college.getStudentBodyHappiness()%>%">
                            <%=college.getStudentBodyHappiness()%>%
                        </div>
                    </div>
                    <% } %>
                    <br>
                    <a href="#happinessDetails" class="btn btn-info" data-toggle="collapse">Details</a>
                    <div id="happinessDetails" class="collapse">
                        College Reputation
                        <div class="progress">
                            <div class="progress-bar progress-bar-success" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:<%=college.getReputation()%>%">
                                <%=college.getReputation()%>%
                            </div>
                        </div>
                        Student/faculty Ratio
                        <div class="progress">
                            <div class="progress-bar progress-bar-success" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:<%=100 - college.getStudentFacultyRatio()%>%">
                                <%=college.getStudentFacultyRatio()%>
                            </div>
                        </div>
                        Tuition
                        <div class="progress">
                            <div class="progress-bar progress-bar-success" role="progressbar"
                                 aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"
                                 style="width:<%=100 - college.getYearlyTuitionCost()/1000%>%">
                                $<%=college.getYearlyTuitionCost()%>
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
                        <h1>100%
                        </h1>
                        <h3>Retention Rate</h3>
                    </div>
                </div>
            </div>

            <!-- Ranking -->
            <div class="col-sm-3">
                <div class="well well-sm">
                    <div class="text-center">
                        <h1>#33
                        </h1>
                        <h3>Ranking</h3>
                    </div>
                    <br>
                    <a href="#rankingDetails" class="btn btn-info" data-toggle="collapse">Details</a>
                    <div id="rankingDetails" class="collapse">
                        Details coming soon!
                    </div>
                </div>
            </div>
        </div>

        <!-- Hidden Parameters That Will Be Passed in Request! -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">
        <input type="hidden" name="server" value="<%=server%>">



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
                                    if (news[i].getNoteType() == NewsType.COLLEGE_NEWS) {
                            %>
                            <li class="list-group-item"> Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
                            </li>
                            <% }
                                if (news[i].getNoteLevel() == NewsLevel.GOOD_NEWS) {

                            %>
                            <li class="list-group-item">
                                <!-- change this to user up or down arrow depending on money -->
                                <span class="glyphicon glyphicon-thumbs-up"></span>
                                Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
                            </li>
                            <% } else if (news[i].getNoteLevel() == NewsLevel.BAD_NEWS) {
                            %>

                            <li class="list-group-item">
                                <!-- change this to user up or down arrow depending on money -->
                                <span class="glyphicon glyphicon-thumbs-down"></span>
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
                    Server: <%=server%>
                </div>
            </div>

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
