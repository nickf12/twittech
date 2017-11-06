<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <title> Twittech </title>
    <link rel="stylesheet" type="text/css" href="css/structure.css"/>
    <link rel="stylesheet" type="text/css" href="css/header.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/jquery.validation/1.16.0/jquery.validate.js"></script>

    <script><%@include file="js/websocket.js"%></script>
    <script src="js/content.js"></script>

</head>

<body>

<!-- Begin Wrapper -->
<div id="wrapper">

    <!-- Begin Header -->
    <div id="header">
        <span><%@include file="res/icons/logo.svg"%>Twittech</span>
    </div>
    <!-- End Header -->

    <!-- Begin Navigation -->
    <div id="navigation">

        <c:if test="${sessionScope.user != null}">
            <jsp:include page="ViewMenuLogged.jsp"/>
        </c:if>
        <c:if test="${sessionScope.user == null}">
            <jsp:include page="ViewMenuNotLogged.jsp"/>
        </c:if>

    </div>
    <!-- End Navigation -->

    <!-- Begin Faux Columns -->
    <div id="faux">

        <!-- Begin Left Column -->
        <div id="leftcolumn">
            <%@include file="popularUsers.jsp"%>
        </div>
        <!-- End Left Column -->

        <!-- Begin Content Column -->
        <div id="content">
            <c:if test="${sessionScope.user != null}">
                <jsp:include page="ViewHome.jsp"/>
            </c:if>
            <c:if test="${sessionScope.user == null}">
                <jsp:include page="ViewLoginForm.jsp"/>
            </c:if>
        </div>
        <!-- End Content Column -->

        <!-- Begin Right Column -->
        <div id="rightcolumn">

        </div>
        <!-- End Right Column -->

    </div>
    <!-- End Faux Columns -->

    <!-- Begin Footer -->
    <div id="footer">

        ©Twittech 2017

    </div>
    <!-- End Footer -->

</div>
<!-- End Wrapper -->
</body>
</html>