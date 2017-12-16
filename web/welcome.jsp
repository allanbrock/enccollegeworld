<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %><%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 8/25/2017
  Time: 8:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Great College World</title>
<link rel="stylesheet" href = "resources/style.css">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>

</head>
<body>
<%
  UiMessage msg = (UiMessage) request.getAttribute("message");
  if (msg == null) {
      msg = new UiMessage();
      msg.setMessage("");
  }
  CollegeModel college = (CollegeModel) request.getAttribute("college");
  if (college == null) {
      college = new CollegeModel();
  }
  String hostOfSim = (String) request.getAttribute("host");
  if (hostOfSim == null) {
    hostOfSim = "http://localhost:8080/enccollegesim/";
  } else {
    hostOfSim = hostOfSim;
  }
%>
<p></p>
<p></p>
<div class="container">

  <form action="welcome" method="post">
  <div class="jumbotron">
    <h1>Endicott College World</h1>
    <p>A simulation of college life: students, dorms, sporting events, financials, unexpected events and more.</p>
    <div class="form-group">
      <input type="text" name="runid" class="form-control" id="runid" placeholder="Enter college ID">
    </div>
    <div class="container">
      <input type="submit" class="btn btn-info" name="button" value="Open College">
      <input type="submit" class="btn btn-info" name="button" value="Create College">
      <input type="submit" class="btn btn-info" name="button" value="Delete College">
    </div>
  </div>

<div class="container">
  <!-- Display a message if defined -->
  <% if (msg.getMessage().length() > 0) { %>
  <div class="alert alert-danger">
    <strong>Info</strong> <%=msg.getMessage()%>
  </div>
  <% } %>


  <div class="form-group">
    <label for="server">Server:</label>
    <input type="text" class="form-control" id="server" name="server" value="<%=hostOfSim%>">
  </div>

</form>
</div>
</body>
</html>
