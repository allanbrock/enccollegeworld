<%@ page import="com.endicott.edu.models.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.models.CollegeModel" %><%--
  Created by IntelliJ IDEA.
  User: abrocken
  Date: 8/25/2017
  Time: 8:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Enc College World</title>
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
%>
<p></p>
<p></p>
<div class="container">
  <div class="jumbotron">
    <h1>Endicott College World</h1>
    <p>A simulation of college life: students, dorms, sporting events, financials, unexpected events and more.</p>
  </div>
</div>

<!-- Display a message if defined -->
<div class="alert alert-success">
  <strong>Info</strong> <%=msg.getMessage()%>
</div>

<form action="welcome" method="post">
  <table align="center" bgcolor="#DDDDFF" border="1" width="40%">
    <tr>
      <td align="right">College ID
        <input type="text" name="runid" maxlength="10" value="<%=college.getRunId()%>">
      </td>
    </tr>
    <tr>
      <td align="right">Sim Server
        <input type="text" name="server" maxlength="64" value="http://localhost:8080/enccollegesim/"></td>
    </tr>
    <tr>
      <td colspan="1" align="center">
        <input type="submit" name="button" value="Open College">
        <input type="submit" name="button" value="Create College">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
