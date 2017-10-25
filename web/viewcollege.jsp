<%@ page import="com.endicott.edu.models.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.models.DormitoriesModel" %>
<%@ page import="com.endicott.edu.models.models.DormitoryModel" %>
<%@ page import="com.endicott.edu.models.models.StudentModel" %>
<%@ page import="com.endicott.edu.models.models.NewsFeedItemModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<title>Enc College World</title>
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
            <h2>Balance $<%=college.getAvailableCash()%>
            </h2>
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
                <div class="progress">
                    <div class="progress-bar progress-bar-danger" role="progressbar"
                         aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:<%=college.getStudentBodyHappiness()%>%">
                        <%=college.getStudentBodyHappiness()%>%
                    </div>
                </div>
                <% } %>
            <p>Day <%=college.getCurrentDay()%>
            </p>
            <input type="submit" class="btn btn-info" name="nextDayButton" value="Next Day">
        </div>

        <!-- Hidden Parameters That Will Be Passed in Request! -->
        <input type="hidden" name="runid" value="<%=college.getRunId()%>">
        <input type="hidden" name="server" value="<%=server%>">

        <!-- Newsfeed -->
        <p></p>
        <div class="row">
            <div class="col-sm-8">
                <div class="well well-sm">
                    <div class="pre-scrollable">
                        <h3><%=college.getRunId()%> News</h3>
                        <ul class="list-group">
                            <%
                                for (int i = news.length - 1; i >= 0; i--) {
                            %>
                            <li class="list-group-item"> Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
                            </li>
                            <% } %>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="well well-sm">
                    <h3>Financial News</h3>
                    <ul class="list-group">
                        <li class="list-group-item"> Coming soon!
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Server -->
        <div class="row">
            <div class="col-sm-8">
                <div class="well well-sm">
                    Server: <%=server%>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-8">
                <div class="alert alert-success">
                    <strong>Info</strong> <%=msg.getMessage()%>
                </div>
            </div>
        </div>

    </div> <!-- container -->
</form>
</div>
</body>
</html>
