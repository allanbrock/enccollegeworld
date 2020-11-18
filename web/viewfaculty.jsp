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
<%@ page import="com.endicott.edu.datalayer.FacultyDao" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.endicott.edu.simulators.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.endicott.edu.models.*" %>
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
<style>
    .icon{
        width: 60px;
        height: 60px;
    }
</style>

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
        Boolean isFemale;
        double r = Math.random();
        if(r < 0.5)
            isFemale = true;
        else
            isFemale = false;
        faculty.add(new FacultyModel( "Dean", "Biology", college.getRunId(), 100000, isFemale)); // Default salary val for now
        msg.setMessage(msg.getMessage() + " Attribute for faculty missing.");
    }
    ArrayList<Integer> salaryOptions = FacultyManager.getSalaryOptions();
    if (salaryOptions == null) {
        salaryOptions = new ArrayList();
    }

    String[] titleOptions = FacultyManager.getTitleOptions();
    if (titleOptions == null) {
        titleOptions = new String[FacultyManager.getTitleOptions().length];
    }

    String[] departmentOptions = FacultyManager.getDepartmentOptionStrings();
    if (departmentOptions == null) {
        departmentOptions = new String[FacultyManager.getDepartmentOptionStrings().length];
    }

    String[] lockedDepartmentNames = DepartmentManager.getLockedDepartmentNames();
    if(lockedDepartmentNames == null){
        lockedDepartmentNames = new String[DepartmentManager.getLockedDepartmentNames().length];
    }

    NumberFormat numberFormatter = NumberFormat.getInstance();
    numberFormatter.setGroupingUsed(true);
    TutorialModel tip = TutorialManager.getCurrentTip("viewFaculty", college.getRunId());

    PopupEventManager popupManager = (PopupEventManager) session.getAttribute("popupManager");

    if(popupManager == null){
        popupManager = new PopupEventManager();
        msg.setMessage(msg.getMessage() + "Attribute for Popup Manager is missing.");
    }

%>

<form action="viewFaculty" method="post">

    <input type="hidden" name="runId" value="<%=college.getRunId()%>">

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
                    <li><a href="viewGates">Objectives</a></li>>
                    <li><a href="viewStore">Store</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewBalance">$<%=numberFormatter.format(college.getAvailableCash())%></a></li>
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
                <div class="col-md-5">

                    <h2>Faculty</h2>
                    <h3><%=faculty.size()%> faculty members</h3>
                </div>
                <!-- Tips -->
                <%if (tip != null){%>
                <div class="col-md-5">
                    <div class="well well-lg" style="background: white">
                        <%if (!tip.getImage().equals("")){%>
                        <img class="img-responsive" src="resources/images/<%=tip.getImage()%>">
                        <%}%>
                        <p><%=tip.getBody()%></p>
                    </div>
                    <input type="submit" class="btn btn-info" name="nextTip" value="Next Tip">
                    <input type="submit" class="btn btn-info" name="hideTips" value="Hide Tips">
                </div>
                <%}%>
                <%if (tip == null){%>
                <input type="submit" class="btn btn-info" name="showTips" value="Show Tips">
                <%}%>
            </div>
        </div>

        <div class="well well-sm">
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>School of Arts and Sciences</th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < faculty.size(); i++) {
                %>
                <%
                    if(faculty.get(i).getDepartmentName().equals("Arts and Sciences")) {
                %>
                <tr>
                    <td><img class='icon' src='https://avataaars.io/?avatarStyle=Transparent&topType=LongHairFrida&accessoriesType=Kurt&hairColor=BrownDark&facialHairType=Blank&facialHairColor=Brown&clotheType=ShirtScoopNeck&clotheColor=White&eyeType=Default&eyebrowType=FlatNatural&mouthType=Tongue&skinColor=Brown'/></td>
                    <td><%=faculty.get(i).getName()%>
                    </td>
                    <td>

                        <a href="#<%=i%>" class="btn btn-info" data-toggle="collapse">Details</a>
                        <div id="<%=i%>" class="collapse">
                            <div class="well well-sm">
                                Title: <%=faculty.get(i).getTitle()%><br>
                                Faculty ID: <%=faculty.get(i).getFacultyID()%><br>
                                Department: <%=faculty.get(i).getDepartmentName()%><br>
                                Happiness: <%=String.valueOf(faculty.get(i).getHappiness())%><br>
                                Performance: <%=String.valueOf(faculty.get(i).getPerformance())%><br>
                            </div>
                        </div>
                        <label id="facultySalary" style="color: black"><%="Salary: $" + String.valueOf(faculty.get(i).getSalary())%> </label>
                    </td>
                    <td>
                        <input type="submit" class="btn btn-info" name="<%="facultyRaise" + i%>" value="Give Raise" style="text-decoration-color: #000099">
                        <input type="submit" class="btn btn-info" name="<%="removeFaculty" + i%>" value="Fire Faculty">
                        <%if(faculty.get(i).getUnderPerforming()){%>
                        <label id="underPerformingFaculty"><%=FacultyManager.generateUnderperformingScenario(faculty.get(i).getName())%></label>
                        <%}%>
                    </td>
                </tr>
                <%}%>
                <% } %>
                </tbody>
            </table>
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>School of Business</th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < faculty.size(); i++) {
                %>
                <%
                    if(faculty.get(i).getDepartmentName().equals("Business")) {
                %>
                <tr>
<%--                    <img class="img-responsive" src="resources/images/fun.png">--%>
                    <td><img class= 'icon' src='https://avataaars.io/?avatarStyle=Transparent&topType=LongHairMiaWallace&accessoriesType=Round&hairColor=Platinum&facialHairType=MoustacheMagnum&facialHairColor=Black&clotheType=ShirtCrewNeck&clotheColor=Red&eyeType=Happy&eyebrowType=SadConcerned&mouthType=Tongue&skinColor=Tanned'/></td>
                    <td><%=faculty.get(i).getName()%>
                    </td>
                    <td>
                        <a href="#<%=i%>" class="btn btn-info" data-toggle="collapse">Details</a>
                        <div id="<%=i%>" class="collapse">
                            <div class="well well-sm">
                                Title: <%=faculty.get(i).getTitle()%><br>
                                Faculty ID: <%=faculty.get(i).getFacultyID()%><br>
                                Department: <%=faculty.get(i).getDepartmentName()%><br>
                                Happiness: <%=String.valueOf(faculty.get(i).getHappiness())%><br>
                                Performance: <%=String.valueOf(faculty.get(i).getPerformance())%><br>
                            </div>
                        </div>
                        <label id="facultySalary1" style="color: black"><%="Salary: $" + String.valueOf(faculty.get(i).getSalary())%> </label>
                    </td>
                    <td>
                        <input type="submit" class="btn btn-info" name="<%="facultyRaise" + i%>" value="Give Raise" style="text-decoration-color: #000099">
                        <input type="submit" class="btn btn-info" name="<%="removeFaculty" + i%>" value="Fire Faculty">
                        <%if(faculty.get(i).getUnderPerforming()){%>
                        <label id="underPerformingFaculty"><%=FacultyManager.generateUnderperformingScenario(faculty.get(i).getName())%></label>
                        <%}%>
                    </td>
                </tr>
                <%
                    }
                %>
                <% } %>
                </tbody>
            </table>
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>School of Nursing</th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < faculty.size(); i++) {
                %>
                <%
                    if(faculty.get(i).getDepartmentName().equals("Nursing")){
                %>
                <tr>
                    <td><img class= 'icon' src='https://avataaars.io/?avatarStyle=Transparent&topType=LongHairStraight&accessoriesType=Prescription01&hairColor=Platinum&facialHairType=Blank&facialHairColor=Red&clotheType=Overall&clotheColor=Black&eyeType=Wink&eyebrowType=AngryNatural&mouthType=Concerned&skinColor=Black'/></td>
                    <td><%=faculty.get(i).getName()%>
                    </td>
                    <td>
                        <a href="#<%=i%>" class="btn btn-info" data-toggle="collapse">Details</a>
                        <div id="<%=i%>" class="collapse">
                            <div class="well well-sm">
                                Title: <%=faculty.get(i).getTitle()%><br>
                                Faculty ID: <%=faculty.get(i).getFacultyID()%><br>
                                Department: <%=faculty.get(i).getDepartmentName()%><br>
                                Happiness: <%=String.valueOf(faculty.get(i).getHappiness())%><br>
                                Performance: <%=String.valueOf(faculty.get(i).getPerformance())%><br>
                            </div>
                        </div>
                        <label id="facultySalary2" style="color: black"><%="Salary: $" + String.valueOf(faculty.get(i).getSalary())%> </label>
                    </td>
                    <td>
                        <input type="submit" class="btn btn-info" name="<%="facultyRaise" + i%>" value="Give Raise" style="text-decoration-color: #000099">
                        <input type="submit" class="btn btn-info" name="<%="removeFaculty" + i%>" value="Fire Faculty">
                        <%if(faculty.get(i).getUnderPerforming()){%>
                        <label id="underPerformingFaculty"><%=FacultyManager.generateUnderperformingScenario(faculty.get(i).getName())%></label>
                        <%}%>
                    </td>
                </tr>
                <%
                    }
                %>
                <% } %>
                </tbody>
            </table>
            <table class="table table-condensed">
                <thread>
                    <tr>
                        <th>School of Sports Science and Fitness</th>
                    </tr>
                </thread>
                <tbody>
                <%
                    for (int i = 0; i < faculty.size(); i++) {
                %>
                <%
                    if(faculty.get(i).getDepartmentName().equals("Sports Science and Fitness")){
                %>
                <tr>
                    <td><img class= 'icon' src='https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortRound&accessoriesType=Round&hairColor=Black&facialHairType=BeardMagestic&facialHairColor=Brown&clotheType=Hoodie&clotheColor=Black&eyeType=WinkWacky&eyebrowType=Angry&mouthType=Vomit&skinColor=Pale'/></td>
                    <td><%=faculty.get(i).getName()%>
                    </td>
                    <td>
                        <a href="#<%=i%>" class="btn btn-info" data-toggle="collapse">Details</a>
                        <div id="<%=i%>" class="collapse">
                            <div class="well well-sm">
                                Title: <%=faculty.get(i).getTitle()%><br>
                                Faculty ID: <%=faculty.get(i).getFacultyID()%><br>
                                Department: <%=faculty.get(i).getDepartmentName()%><br>
                                Happiness: <%=String.valueOf(faculty.get(i).getHappiness())%><br>
                                Performance: <%=String.valueOf(faculty.get(i).getPerformance())%><br>
                            </div>
                        </div>
                        <label id="facultySalary3" style="color: black"><%="Salary: $" + String.valueOf(faculty.get(i).getSalary())%> </label>
                    </td>
                    <td>
                        <input type="submit" class="btn btn-info" name="<%="facultyRaise" + i%>" value="Give Raise" style="text-decoration-color: #000099">
                        <input type="submit" class="btn btn-info" name="<%="removeFaculty" + i%>" value="Fire Faculty">
                        <%if(faculty.get(i).getUnderPerforming()){%>
                        <label id="underPerformingFaculty"><%=FacultyManager.generateUnderperformingScenario(faculty.get(i).getName())%></label>
                        <%}%>
                    </td>
                </tr>
                <%
                    }
                %>
                <% } %>
                </tbody>
            </table>




























































        </div>


        <div class="col-sm-4">
            <div class="well well-sm">
                <div class="form-group">
                    <label id="salaryLabel" style="color: darkblue">Pick an annual salary, a department, and a position if you would like to add a new faculty member</label>
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
                    <select class="form-control" id="departmentDropdown" name="departmentDropdown">
                        <% for(int i = 0; i < departmentOptions.length; i++) { %>
                        <tr>
                            <option><%= departmentOptions[i] %></option>
                        </tr>
                        <% } %>
                    </select>
                    <br>
                    <input type="submit" class="btn btn-info" name="addFaculty" value="Add Faculty"><br>
                    <br>
                </div>
                <div class="form-group">
                    <%if(DepartmentManager.getNewDepartmentReady()){%>
                        <select class="form-control" id="newDepartmentDropdown" name="newDepartmentDropdown">
                            <% for(int i = 0; i < lockedDepartmentNames.length; i++) { %>
                            <tr>
                                <option><%= lockedDepartmentNames[i] %></option>
                            </tr>
                            <% } %>
                        </select><br>
                        <input type="submit" class="btn btn-info" name="addDepartment" value="Add Department"><br>
                    <%}%>
                </div>
            </div>
        </div>
    </div>
    <br>

    <div class="container">
        <h3 id="departmentRatings" style="text-align: center">Academic Department Ratings</h3>
        <table class="table table-condensed">
            <thread>
                <tr>
                    <th>Department names</th>
                </tr>
            </thread>
            <tbody>
            <%
                HashMap<String, Integer> departmentRatingsMap = DepartmentManager.getRatingsForDepartments(college.getRunId());
                for (String s : departmentRatingsMap.keySet()) {
                    if(!s.equals("Overall Academic Happiness")){
            %>
                        <tr>
                            <td><%=s + ": "%></td>
                            <td><%=departmentRatingsMap.get(s)%></td>
                        </tr>
            <%      }
                }
                String a = "Overall Academic Happiness";
            %>
            <td><%="Overall Academic Rating: "%></td>
            <td><%=departmentRatingsMap.get(a)%></td>
            </tbody>
        </table>
    </div>
</form>
<script>
    function checkTitle(){

    }
</script>

</body>
</html>
