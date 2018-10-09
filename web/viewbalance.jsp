<%--
  Created by IntelliJ IDEA.
  User: stevesuchcicki
  Date: 9/28/18
  Time: 1:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page import="com.endicott.edu.models.NewsFeedItemModel" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.StudentModel" %>
<%@ page import="com.endicott.edu.models.NewsType" %>
<%@ page import="com.endicott.edu.models.NewsLevel" %>

<html>
<head>
    <title>Title</title>
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
    NewsFeedItemModel news[] = (NewsFeedItemModel[]) request.getAttribute("news");
    if (news == null) {
        news = new NewsFeedItemModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + "Attribute for news missing.");
    }
    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>

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
                    <li><a href="viewBuilding">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li><a href="viewFaculty">Faculty</a></li>
                    <li class="active"><a href="viewBalance">Balance $<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewAdmin">Admin</a></li>
                    <li><a href="about.jsp">About</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="col-sm-6 col-sm-offset-3" align="center">
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
</body>
</html>
