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
<%@ page import="com.endicott.edu.models.models.FacultyModel" %>
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

    <style>
        body {
            min-height: 75rem; /* Can be removed; just added for demo purposes */
        }

        .navbar {
            margin-bottom: 0;
        }

        .jumbotron {
            padding-top: 6rem;
            padding-bottom: 6rem;
            margin-bottom: 0;
            background-color: #fff;
        }

        .jumbotron p:last-child {
            margin-bottom: 0;
        }

        .jumbotron-heading {
            font-weight: 300;
        }

        .jumbotron .container {
            max-width: 40rem;
        }

        .album {
            min-height: 50rem; /* Can be removed; just added for demo purposes */
            padding-top: 3rem;
            padding-bottom: 3rem;
            background-color: #f7f7f7;
        }

        .card {
            float: left;
            width: 33.333%;
            padding: .75rem;
            margin-bottom: 2rem;
            border: 0;
        }

        .card > img {
            margin-bottom: .75rem;
        }

        .card-text {
            font-size: 85%;
        }

        footer {
            padding-top: 3rem;
            padding-bottom: 3rem;
        }

        footer p {
            margin-bottom: .25rem;
        }
    </style>
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
    FacultyModel faculty[] = (FacultyModel[]) request.getAttribute("faculty");
    if (faculty == null) {
        faculty = new FacultyModel[0];  // This is really bad
        msg.setMessage(msg.getMessage() + " Attribute for faculty missing.");
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



    <%--<div class="container">--%>
        <%--<div class="jumbotron">--%>
            <%--<h2>Students</h2>--%>
            <%--<p></p>--%>
            <%--<h3><%=faculty.length%> faculty</h3>--%>
        <%--</div>--%>
        <%--<!-- Display a message if defined -->--%>
        <%--<input type="hidden" name="runid" value="<%=college.getRunId()%>">--%>
        <%--<input type="hidden" name="server" value="<%=server%>">--%>
        <%--<p></p>--%>
        <%--<div class="well well-sm">--%>
            <%--<table class="table table-condensed">--%>
                <%--<tbody>--%>
                <%--<h4>Faculty</h4>--%>
                <%--<%--%>
                    <%--for (int i = 0; i < faculty.length; i++) {--%>
                <%--%>--%>
                <%--<tr>--%>
                    <%--<li class="list-group-item"> <%=faculty[i].getIdNumber()%>--%>
                    <%--</li>--%>
                <%--</tr>--%>
                <%--<% } %>--%>
                <%--</tbody>--%>
            <%--</table>--%>

        <%--</div>--%>
    <%--</div>--%>

    <section class="jumbotron text-center">
        <div class="container">
            <h1 class="jumbotron-heading">Faculty</h1>
            <p class="lead text-muted">This is a list of all the faculty currently employed at <%=college.getRunId()%></p>
            <p class="lead text-muted">There are currently <%=faculty.length%> faculty members employed</p>
            <p>
                <!-- TO-DO -->
                <!-- Add functionality to add faculty button -->
                <a href="#" class="btn btn-primary">Add a new faculty member</a>
            </p>
        </div>
    </section>


    <!-- TO-DO -->
    <!-- Populate 'album' indexes properly -->
    <div class="album text-muted">
        <div class="container">

            <div class="row">
                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap"> <!-- Possible image of faculty member could go here? -->
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>
                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>
                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>

                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>
                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>
                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>

                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>
                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>
                <div class="card">
                    <img data-src="holder.js/100px280?theme=thumb" alt="Card image cap">
                    <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                </div>
            </div>

        </div>
    </div>



</form>

</body>
</html>
