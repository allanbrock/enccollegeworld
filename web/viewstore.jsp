<%--
  Created by IntelliJ IDEA.
  User: stevesuchcicki
  Date: 10/22/18
  Time: 12:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.*" %>
<%@ page import="com.endicott.edu.simulators.CollegeManager" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <title>College World Store</title>
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

    ItemModel items[] = (ItemModel[]) request.getAttribute("items");
    if (items == null) {
        items  = new ItemModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for students missing.");
    }

    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>

<form action="viewStore" method="post">

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
                    <li><a href="viewCollege"><%=college.getRunId()%>
                    </a></li>
                    <li><a href="viewStudent">Students</a></li>
                    <li><a href="viewBuilding">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li><a href="viewFaculty">Faculty</a></li>
                    <li><a href="viewGates">Gates</a></li>
                    <li><a href="viewBalance">Balance $<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                    <li class="active"><a href="viewStore">Store</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a> <%=new SimpleDateFormat("MM/dd/yyyy").format(CollegeManager.getCollegeDate(college.getRunId()))%> </a></li>
                    <li><a href="viewAdmin">Admin</a></li>
                    <li><a href="about.jsp">About</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>


    <div class="well well-sm">
        <h1 align="center">Available Items</h1>
        <table class="table table-condensed">
            <thread>
                <tr>
                    <th>Item Name</th>
                    <th>Item Cost</th>
                    <th></th>
                </tr>
            </thread>
            <tbody>
                <%
                    for(int i = 0; i < items.length; i++){
                %>
                <tr>
                    <td style="vertical-align:middle; font-size:120%;"><%=items[i].getName()%>
                    </td>
                    <td style="vertical-align:middle; font-size:120%;"><%=items[i].getCost()%>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

    </div>


    <div class="well well-sm">
        <h1 align="center">Purchased Items</h1>
        <table class="table table-condensed">
            <thread>
                <tr>
                    <th>Item Name</th>
                    <th></th>
                    <th></th>
                </tr>
            </thread>
            <tbody>

            </tbody>
        </table>

    </div>
</form>
</body>
</html>
