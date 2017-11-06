<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script>
    var userID = ${user.userId};
</script>

<script><%@include file="/js/profile.js"%></script>
<script><%@include file="/js/tweet.js"%></script>

<link rel="stylesheet" href="css/profile.css"/>
<link rel="stylesheet" href="css/message.css"/>

<div id="profileContainer">
    <div class="column">
        <h1>${sessionScope.user.getUser()}'s Profile
            <span class="editProfile" onclick="showContent('PersonalData')"><%@include file="/res/icons/edit.svg"%></span>
        </h1>
        <img src="${personalData.getImage()}"/>
    </div>

    <div class="column">
        <c:if test="${not empty personalData.getAge()}"><div><span class="key">Age</span></span><span class="value">${personalData.getAge()}</span></div></c:if>
        <c:if test="${not empty personalData.getGender()}"><div><span class="key">Gender</span></span><span class="value">${personalData.getGender()}</span></div></c:if>
        <c:if test="${not empty personalData.getLanguage()}"><div><span class="key">Language</span></span><span class="value">${personalData.getLanguage()}</span></div></c:if>
        <c:if test="${not empty personalData.getJob()}"><div><span class="key">Job</span></span><span class="value">${personalData.getJob()}</span></div></c:if>
        <c:if test="${not empty personalData.getCompany()}"><div><span class="key">Company</span></span><span class="value">${personalData.getCompany()}</span></div></c:if>
        <c:if test="${not empty personalData.getDescription()}"><div><span class="key">Description</span></span><span class="value">${personalData.getDescription()}</span></div></c:if>
    </div>

    <div id="follow">
        <div class="row follow">
            <span class="column"><p>Followers</p>${follower.size()}</span>
            <span class="column"><p>Following</p>${following.size()}</span>
        </div>
    </div>

    <div id="profileMessages">

    </div>
</div>
