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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
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

//    ArrayList<ItemModel> sortedItems = new ArrayList<ItemModel>();
//
//    for(int i = 0; i < items.length; i++){
//        if(items[i].getUnlocked()){
//            sortedItems.add(items[i]);
//        }
//    }
//
//    for(int i = 0; i < items.length; i++){
//        if(items[i].getUnlocked().equals(false)){
//            sortedItems.add(items[i]);
//        }
//    }


    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
%>


<style>
    img{
        width:50px;
        height: 50px;
    }
    .disabled{
        opacity: 0.2;
    }
</style>

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
                    <li><a href="viewGates">Objectives</a></li>
                    <li class="active"><a href="viewStore">Store</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewBalance">$<%=numberFormatter.format(college.getAvailableCash())%></a></li>
                    <%-- This is commented out because it was throwing a null pointer,
                         theres a warning on the getCollegeDate declaration it may throw one.
                         This was to test the fire pop up upgrade button.
                    <li><a> <%=new SimpleDateFormat("MMM dd").format(CollegeManager.getCollegeDate(college.getRunId()))%> </a></li>
                    --%>
                    <li><a href="viewAdmin">Admin</a></li>
                    <li><a href="about.jsp">About</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <%--<div class="well well-sm">--%>
        <%
            for (int i = 0; i < items.length; i++){
                if((i%3) == 0){
        %>
            <br>
            <br>
            <div class="row">
                <div align="center" class="col-sm-4">
                    <div class="well">
                    <%
                        if(items[i].getUnlocked() && items[i].getCost() < college.getAvailableCash()){
                    %>
                    <img src="resources/images/<%=items[i].getImageName()%>">
                    <%
                        }
                        else{
                    %>
                    <img src="resources/images/<%=items[i].getLockedImageName()%>">
                    <%
                        }
                    %>
                    <h4><%=items[i].getName()%></h4>
                    <h5><%=items[i].getDescription()%></h5>
                    <p>$<%=numberFormatter.format(items[i].getCost())%></p>
                    <%
                        if(items[i].getUnlocked().equals(false)){
                    %>
                        <input type="submit" class="btn btn-info" name="locked" value="Locked Level <%=items[i].getGateNum()%>" disabled>
                    <%
                        }
                       else if(items[i].getCost() < college.getAvailableCash() && items[i].getPurchased().equals(false)){
                    %>
                        <input type="submit" class="btn btn-info" name="<%="buyItem" + i%>" value="Buy">
                    <%
                        }
                        else if(items[i].getCost() > college.getAvailableCash()){
                    %>
                        <input type="submit" class="btn btn-info" name="<%="buyItem" + i%>" value="Buy" disabled>
                    <%
                        }
                        else if(items[i].getPurchased().equals(true)){
                    %>
                        <input type="submit" class="btn btn-info" name="purchased" value="Purchased" disabled>
                    <%
                        }
                    %>
                    </div>
                </div>
                <%
                    if((i+1) < items.length){
                %>
                <div align="center"class="col-sm-4">
                    <div class="well">
                    <%
                        if(items[i+1].getUnlocked() && items[i+1].getCost() < college.getAvailableCash()){
                    %>
                    <img src="resources/images/<%=items[i+1].getImageName()%>">
                    <%
                        }
                    else{
                    %>
                    <img src="resources/images/<%=items[i+1].getLockedImageName()%>">
                    <%
                        }
                    %>
                    <h4><%=items[i+1].getName()%></h4>
                    <h5><%=items[i+1].getDescription()%></h5>
                    <p>$<%=numberFormatter.format(items[i+1].getCost())%></p>
                    <%
                        if(items[i+1].getUnlocked().equals(false)){
                    %>
                        <input type="submit" class="btn btn-info" name="locked" value="Locked Level <%=items[i+1].getGateNum()%>" disabled>
                    <%
                        }
                        else if(items[i+1].getCost() < college.getAvailableCash() && items[i+1].getPurchased().equals(false)){
                    %>
                        <input type="submit" class="btn btn-info" name="<%="buyItem" + (i+1)%>" value="Buy">
                    <%
                    }
                        else if(items[i+1].getCost() > college.getAvailableCash()){
                    %>
                        <input type="submit" class="btn btn-info" name="<%="buyItem" + (i+1)%>" value="Buy" disabled>
                    <%
                    }
                        else if(items[i+1].getPurchased().equals(true)){
                    %>
                        <input type="submit" class="btn btn-info" name="purchased" value="Purchased" disabled>
                    <%
                        }
                    %>
                    </div>
                </div>
                <%
                    }
                    if((i+2) < items.length){
                %>
                <div align="center" class="col-sm-4">
                    <div class="well">
                    <%
                        if(items[i+2].getUnlocked() && items[i+2].getCost() < college.getAvailableCash()){
                    %>
                    <img src="resources/images/<%=items[i+2].getImageName()%>">
                    <%
                    }
                    else{
                    %>
                    <img src="resources/images/<%=items[i+2].getLockedImageName()%>">
                    <%
                        }
                    %>
                    <h4><%=items[i+2].getName()%></h4>
                    <h5><%=items[i+2].getDescription()%></h5>
                    <p>$<%=numberFormatter.format(items[i+2].getCost())%></p>
                    <%
                        if(items[i+2].getUnlocked().equals(false)){
                    %>
                        <input type="submit" class="btn btn-info" name="locked" value="Locked Level <%=items[i+2].getGateNum()%>" disabled>
                    <%
                        }
                        else if(items[i+2].getCost() < college.getAvailableCash() && items[i+2].getPurchased().equals(false)){
                    %>
                        <input type="submit" class="btn btn-info" name="<%="buyItem" + (i+2)%>" value="Buy">
                    <%
                    }
                        else if(items[i+2].getCost() > college.getAvailableCash()){
                    %>
                        <input type="submit" class="btn btn-info" name="<%="buyItem" + (i+2)%>" value="Buy" disabled>
                    <%
                    }
                        else if(items[i+2].getPurchased().equals(true)){
                    %>
                        <input type="submit" class="btn btn-info" name="purchased" value="Purchased" disabled>
                    <%
                        }
                    %>
                    </div>
                </div>
                <%
                        }
                    }
                %>
            </div>
        <%
            }
        %>
    <%--</div>--%>

</form>
</body>
</html>
