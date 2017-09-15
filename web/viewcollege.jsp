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
<link rel="stylesheet" href="resources/style.css"
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
    msg.setMessage(msg.getMessage() + "Attribute for dorms missing.");
  }
  NewsFeedItemModel news[] = (NewsFeedItemModel[]) request.getAttribute("news");
  if (news == null) {
    news = new NewsFeedItemModel[0];  // This is really bad
    msg.setMessage(msg.getMessage() + "Attribute for news missing.");
  }
%>

<div class="container">
  <h1>College <%=college.getRunId()%>
  </h1>
  <!-- Display a message if defined -->

  <form action="updateCollege" method="post">
    <input type="hidden" name="runid" value="<%=college.getRunId()%>">
    <input type="hidden" name="server" value="<%=server%>">

    <table class="table table-condensed">
      <tbody>
      <tr>
        <td>Server</td>
        <td><%=server%>
        </td>
      </tr>
      <tr>
        <td>Day</td>
        <td><%=college.getCurrentDay()%>
        </td>
      </tr>
      <tr>
        <td>Funding</td>
        <td>$<%=college.getAvailableCash()%>
        </td>
      </tr>
      <%
        for (int i = 0; i < dorms.length; i++) {
      %>
      <tr>
        <td>Dorm</td>
        <td><%=dorms[i].getName()%>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
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
    <p></p>
    <input type="submit" class="btn btn-info" name="nextDayButton" value="Next Day">
    <input type="submit" class="btn btn-info" name="refreshButton" value="Refresh">
  </form>
  <div class="alert alert-success">
    <strong>Info</strong> <%=msg.getMessage()%>
  </div>
</div>
</body>
</html>
