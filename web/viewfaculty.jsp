<%--
  Created by IntelliJ IDEA.
  User: jeffreythor
  Date: 9/28/17
  Time: 4:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.endicott.edu.models.ui.UiMessage" %>
<%@ page import="com.endicott.edu.models.models.CollegeModel" %>
<%@ page import="com.endicott.edu.models.models.DormitoriesModel" %>
<%@ page import="com.endicott.edu.models.models.DormitoryModel" %>
<%@ page import="com.endicott.edu.models.models.NewsFeedItemModel" %>
<html>
<head>
    <title>College World Faculty</title>
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
    DormitoryModel dorms[] = (DormitoryModel[]) request.getAttribute("dorms");
    if (dorms == null) {
        dorms = new DormitoryModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for dorms missing.");
    }
%>

<form action="updateCollege" method="post">

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
                    <li><a href="viewCollege?runid=<%=college.getRunId()%>&server=<%=server%>"><%=college.getRunId()%></a></li>
                    <li><a href="viewStudent?runid=<%=college.getRunId()%>&server=<%=server%>">Students</a></li>
                    <li><a href="viewDorm?runid=<%=college.getRunId()%>&server=<%=server%>">Dorms</a></li>
                    <li><a href="viewSports?runid=<%=college.getRunId()%>&server=<%=server%>">Sports</a></li>
                    <li class="active"><a href="viewFaculty?runid=<%=college.getRunId()%>&server=<%=server%>">Faculty</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="viewAdmin?runid=<%=college.getRunId()%>&server=<%=server%>">Admin</a></li>
                    <li><a href="welcome.jsp"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container">

        <h1>Bootstrap grid examples</h1>
        <p class="lead">Basic grid layouts to get you familiar with building within the Bootstrap grid system.</p>

        <h3>Five grid tiers</h3>
        <p>There are five tiers to the Bootstrap grid system, one for each range of devices we support. Each tier starts at a minimum viewport size and automatically applies to the larger devices unless overridden.</p>

        <div class="row">
            <div class="col-4">.col-4</div>
            <div class="col-4">.col-4</div>
            <div class="col-4">.col-4</div>
        </div>

        <div class="row">
            <div class="col-sm-4">.col-sm-4</div>
            <div class="col-sm-4">.col-sm-4</div>
            <div class="col-sm-4">.col-sm-4</div>
        </div>

        <div class="row">
            <div class="col-md-4">.col-md-4</div>
            <div class="col-md-4">.col-md-4</div>
            <div class="col-md-4">.col-md-4</div>
        </div>

        <div class="row">
            <div class="col-lg-4">.col-lg-4</div>
            <div class="col-lg-4">.col-lg-4</div>
            <div class="col-lg-4">.col-lg-4</div>
        </div>

        <div class="row">
            <div class="col-xl-4">.col-xl-4</div>
            <div class="col-xl-4">.col-xl-4</div>
            <div class="col-xl-4">.col-xl-4</div>
        </div>

        <h3>Three equal columns</h3>
        <p>Get three equal-width columns <strong>starting at desktops and scaling to large desktops</strong>. On mobile devices, tablets and below, the columns will automatically stack.</p>
        <div class="row">
            <div class="col-md-4">.col-md-4</div>
            <div class="col-md-4">.col-md-4</div>
            <div class="col-md-4">.col-md-4</div>
        </div>

        <h3>Three unequal columns</h3>
        <p>Get three columns <strong>starting at desktops and scaling to large desktops</strong> of various widths. Remember, grid columns should add up to twelve for a single horizontal block. More than that, and columns start stacking no matter the viewport.</p>
        <div class="row">
            <div class="col-md-3">.col-md-3</div>
            <div class="col-md-6">.col-md-6</div>
            <div class="col-md-3">.col-md-3</div>
        </div>

        <h3>Two columns</h3>
        <p>Get two columns <strong>starting at desktops and scaling to large desktops</strong>.</p>
        <div class="row">
            <div class="col-md-8">.col-md-8</div>
            <div class="col-md-4">.col-md-4</div>
        </div>

        <h3>Full width, single column</h3>
        <p class="text-warning">No grid classes are necessary for full-width elements.</p>

        <hr>

        <h3>Two columns with two nested columns</h3>
        <p>Per the documentation, nesting is easy—just put a row of columns within an existing column. This gives you two columns <strong>starting at desktops and scaling to large desktops</strong>, with another two (equal widths) within the larger column.</p>
        <p>At mobile device sizes, tablets and down, these columns and their nested columns will stack.</p>
        <div class="row">
            <div class="col-md-8">
                .col-md-8
                <div class="row">
                    <div class="col-md-6">.col-md-6</div>
                    <div class="col-md-6">.col-md-6</div>
                </div>
            </div>
            <div class="col-md-4">.col-md-4</div>
        </div>

        <hr>

        <h3>Mixed: mobile and desktop</h3>
        <p>The Bootstrap v4 grid system has five tiers of classes: xs (extra small), sm (small), md (medium), lg (large), and xl (extra large). You can use nearly any combination of these classes to create more dynamic and flexible layouts.</p>
        <p>Each tier of classes scales up, meaning if you plan on setting the same widths for xs and sm, you only need to specify xs.</p>
        <div class="row">
            <div class="col-12 col-md-8">.col-12 .col-md-8</div>
            <div class="col-6 col-md-4">.col-6 .col-md-4</div>
        </div>
        <div class="row">
            <div class="col-6 col-md-4">.col-6 .col-md-4</div>
            <div class="col-6 col-md-4">.col-6 .col-md-4</div>
            <div class="col-6 col-md-4">.col-6 .col-md-4</div>
        </div>
        <div class="row">
            <div class="col-6">.col-6</div>
            <div class="col-6">.col-6</div>
        </div>

        <hr>

        <h3>Mixed: mobile, tablet, and desktop</h3>
        <p></p>
        <div class="row">
            <div class="col-12 col-sm-6 col-lg-8">.col-12 .col-sm-6 .col-lg-8</div>
            <div class="col-6 col-lg-4">.col-6 .col-lg-4</div>
        </div>
        <div class="row">
            <div class="col-6 col-sm-4">.col-6 .col-sm-4</div>
            <div class="col-6 col-sm-4">.col-6 .col-sm-4</div>
            <div class="col-6 col-sm-4">.col-6 .col-sm-4</div>
        </div>

    </div> <!-- /container -->

</form>

</body>
</html>
