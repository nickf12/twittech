<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="authorId" value="${author.id}"></c:set>

<link rel="stylesheet" href="css/message.css">
<link rel="stylesheet" href="css/author.css">

<script><%@include file="js/author.js"%></script>
<script><%@include file="/js/tweet.js"%></script>
<script src="js/userfollower.js"></script>

<div id="authorContainer">
    <div id="authorProfile">
        <div class="column"><img src="${author.image}" onerror="this.src = '/res/icons/logo.svg' ">
            <c:if test="${sessionScope.user.userId != null && sessionScope.user.userId != author.id}">
                <c:if test="${isFollow}">
                    <span class="follow-btn followed column" onclick="unFollow(${author.id})"><%@include file="/res/icons/register.svg"%>Follow</span>
                </c:if>
                <c:if test="${!isFollow}">
                    <span class="follow-btn column" onclick="setUserFollower(${author.id})"><%@include file="/res/icons/register.svg"%>Unfollow</span>
                </c:if>
            </c:if></div>
        <div id="authorDetails">
            <c:if test="${sessionScope.user.getAdmin() == true}">
                <span class="remove-btn" onclick="showRemoveDialog(${author.id})"><%@include file="/res/icons/close.svg"%>Remove user</span>
            </c:if>
            <c:if test="${not empty author.name}"><div><span class="key">User Name</span><span class="value">${author.name}</span></div></c:if>
            <c:if test="${not empty author.age}"><div><span class="key">Age</span><span class="value">${author.age}</span></div></c:if>
            <c:if test="${not empty author.gender}"><div><span class="key">Gender</span><span class="value">${author.gender}</span></div></c:if>
            <c:if test="${not empty author.language}"><div><span class="key">Language</span><span class="value">${author.language}</span></div></c:if>
            <c:if test="${not empty author.job}"><div><span class="key">Job</span><span class="value">${author.job}</span></div></c:if>
            <c:if test="${not empty author.company}"><div><span class="key">Company</span><span class="value">${author.company}</span></div></c:if>
            <c:if test="${not empty author.description}"><div><span class="key">Description</span><span class="value">${author.description}</span></div></c:if>
        </div>
    </div>

    <div class="row follow">
        <span class="column"><p>Followers</p>${follower.size()}</span>
        <span class="column"><p>Following</p>${following.size()}</span>
    </div>

    <div id="authorMessages">
    </div>
</div>