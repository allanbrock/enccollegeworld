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
<h1>Endicott College World</h1>

<!-- Display a message if defined -->
<h2><%=msg.getMessage()%></h2>

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
