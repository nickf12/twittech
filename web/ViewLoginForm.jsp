<%@ page import="sun.rmi.server.Dispatcher" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" session="false" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>
    $(document).ready(function () {
        <c:if test="${session.user != null}">
            showContent('home');
        </c:if>
    });
</script>

<link rel="stylesheet" href="css/login.css">
<script src="js/login.js"></script>

<form id="loginForm" action="" method="POST">
    <label>Username</label>
    <input type="text" name="user" value="${login.user}" required min-length="8">
    <c:if test="${login.error[0] == 1}">
        <span class="error"> Nonexistent username in our DB!</span>
    </c:if>
    <label>Password</label>
    <input type="password" name="password" required min-length="8">
    <c:if test="${login.error[1] == 1}">
        <span class="error"> Wrong password!</span>
    </c:if>
    <input type="submit" value="Login">
</form>

