<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   
<script>
/*$(document).ready(function(){
    $("#registerForm").validate({
    	submitHandler: function(form) {
    		$('#content').load('RegisterController',$("#registerForm").serialize());
    }
    });
});*/
</script>

<script src="js/register.js"></script>
<link rel="stylesheet" href="css/register.css"/>

<form action="" method="post" id="registerForm">
    <label>UserName</label>
    <input type="text"
           name="username"
           value="${user.getUser()}"
           placeholder="Must contain 8 letters at least!"
           required minlength="8"/>
    <c:if test="${not empty user.error && user.getError()[0] == 1}">
        <span class="error"> The username already exists in our DB! </span>
    </c:if>
    <br>
    <label>Password</label>
    <input type="password"
           name="password"
           placeholder="Must contain a number, upper and lower case at least!"
           required minlength="8" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"/><br>
    <label>Confirm Password</label>
    <input type="password" required/>
    <span id="passMessage"></span>
    <br>
    <label>Email Address</label>
    <input type="email" name="email" value="${user.getMail()}" required email/>
    <c:if test="${user.getError()[1] == 1}">
        <span class="error"> The mail account already exists in our DB! </span>
    </c:if>
    <br>
    <input type="submit" value="SignUp" disabled/>
</form>

</body>
</html>