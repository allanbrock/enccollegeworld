<%@ page import="com.endicott.edu.models.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.models.DormitoriesModel" %>
<%@ page import="com.endicott.edu.models.models.DormitoryModel" %>
<%@ page import="com.endicott.edu.models.models.NewsFeedItemModel" %><%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 8/25/2017
  Time: 8:10 AM
  To change this template use File | Settings | File Templates.
--%>
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
                    <li class="active"><a href="viewCollege?runid=<%=college.getRunId()%>&server=<%=server%>"><%=college.getRunId()%></a></li>
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
  <div class="jumbotron">
    <h2>Balance $<%=college.getAvailableCash()%> </h2>
      <h3>Student Body Happiness</h3>
      <div class="progress">
          <div class="progress-bar" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width:50%">
              <span class="sr-only">50% Complete</span>
          </div>
      </div>
    <p>Day <%=college.getCurrentDay()%></p>
    <input type="submit" class="btn btn-info" name="nextDayButton" value="Next Day">
  </div>
  <!-- Display a message if defined -->
    <input type="hidden" name="runid" value="<%=college.getRunId()%>">
    <input type="hidden" name="server" value="<%=server%>">
    <p></p>
    <div class="well well-sm">
      <h3>News</h3>
      <ul class="list-group">
        <%
          for (int i = news.length - 1; i >= 0; i--) {
        %>
        <li class="list-group-item"> Day <%=news[i].getHour() / 24%> - <%=news[i].getMessage()%>
        </li>
        <% } %>
      </ul>
    </div>
    <table class="table table-condensed">
      <tbody>
      <tr>
        <td>Server</td>
        <td><%=server%>
        </td>
      </tr>
      </tbody>
    </table>
</div>
  </form>
  <div class="alert alert-success">
    <strong>Info</strong> <%=msg.getMessage()%>
  </div>
</div>
</body>
</html>
