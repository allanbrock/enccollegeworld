<%--
  Created by IntelliJ IDEA.
  User: jeffreythor
  Date: 9/28/17
  Time: 4:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.text.NumberFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.endicott.edu.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.FacultyModel" %>
<%@ page import="com.endicott.edu.datalayer.FacultyDao" %>
<%@ page import="com.endicott.edu.models.NewsFeedItemModel" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.endicott.edu.simulators.CollegeManager" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.endicott.edu.simulators.FacultyManager" %>
<%@ page import="com.endicott.edu.simulators.PopupEventManager" %>
<html>
<head>
    <title>College World Faculty</title>
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
    List<FacultyModel> faculty = FacultyDao.getFaculty(college.getRunId());
    if (faculty == null) {
        faculty = new ArrayList<FacultyModel>();
        faculty.add(new FacultyModel("Professor Sam Smith", "Dean", "Biology", "LSB311", college.getRunId(), 100000)); // Default salary val for now
        msg.setMessage(msg.getMessage() + " Attribute for faculty missing.");
    }
    ArrayList<Integer> salaryOptions = FacultyManager.getSalaryOptions();
    if (salaryOptions == null) {
        salaryOptions = new ArrayList<Integer>();
    }

    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);

    PopupEventManager popupManager = (PopupEventManager) session.getAttribute("popupManager");

    if(popupManager == null){
        popupManager = new PopupEventManager();
        msg.setMessage(msg.getMessage() + "Attribute for Popup Manager is missing.");
    }
%>

<form action="viewFaculty" method="post">

    <input type="hidden" name="runid" value="<%=college.getRunId()%>">

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
                    <li><a href="viewCollege"><%=college.getRunId()%></a></li>
                    <li><a href="viewStudent">Students</a></li>
                    <li><a href="viewBuilding">Buildings</a></li>
                    <li><a href="viewSports">Sports</a></li>
                    <li class="active"><a href="viewFaculty">Faculty</a></li>
                    <li><a href="viewGates">Gates</a></li>>
                    <li><a href="viewStore">Store</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewBalance">$<%=numberFormatter.format(college.getAvailableCash())%></a></li
                    <li><a> <%=new SimpleDateFormat("MMM dd").format(CollegeManager.getCollegeDate(college.getRunId()))%> </a></li>
                    <li><a href="viewAdmin">Admin</a></li>
                    <li><a href="about.jsp">About</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>


    <div class="container">
        <div class="jumbotron">
            <div class="row">
                <div class="col-md-2">
                    <img class="img-responsive" src="resources/images/student.png">
                </div>
                <div class="col-md-10">

                    <h2>Faculty</h2>
                    <h3><%=faculty.size()%> faculty members</h3>
                </div>
            </div>
        </div>

        <div class="well well-sm">
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>Name</th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < faculty.size(); i++) {
                %>
                <tr>
                    <td><%=faculty.get(i).getFacultyName()%>
                    </td>
                    <td>
                        <a href="#<%=i%>" class="btn btn-info" data-toggle="collapse">Details</a>
                        <div id="<%=i%>" class="collapse">
                            <div class="well well-sm">
                                Title: <%=faculty.get(i).getTitle()%><br>
                                Faculty ID: <%=faculty.get(i).getFacultyID()%><br>
                                Department: <%=faculty.get(i).getDepartment()%><br>
                                Salary: <%=String.valueOf(faculty.get(i).getSalary())%> <br>
                                Happiness: <%=String.valueOf(faculty.get(i).getHappiness())%><br>
                                Performance: <%=String.valueOf(faculty.get(i).getPerformance())%><br>
                            </div>
                        </div>
                    </td>
                    <td>
                        <input type="submit" class="btn btn-info" name="<%="facultyRaise" + i%>" value="Give Raise" style="text-decoration-color: #000099">
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <div class="col-sm-4">
            <div class="well well-sm">
                <div class="form-group">
                    <label id="salaryLabel" style="color: darkblue">Pick an annual salary if you would like to add a new faculty member</label>
                </div>
                <div class="form-group">
                    <select class="form-control" id="salaryDropdown" name="salaryDropdown">
                        <% for(int i = 0; i < salaryOptions.size(); i++) { %>
                        <tr>
                            <option><%= "$" + salaryOptions.get(i) %></option>
                        </tr>
                        <% } %>
                    </select>
                    <br>
                    <input type="submit" class="btn btn-info" name="addFaculty" value="Add Faculty">
                </div>
            </div>
            <div class="well well-sm">
                <div class="form-group">
                    <label id="removeFacultyLabel" style="color: darkblue">Remove a faculty member by entering their ID</label>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" id="removeFacultyID" name="removeFacultyID">
                    <br>
                    <input type="submit" class="btn btn-info" name="removeFaculty" value="Fire Faculty">
                </div>
            </div>
        </div>
    </div>
</form>

</body>
</html>
