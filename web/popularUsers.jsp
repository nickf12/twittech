<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="popularUsersContainer">
    <c:if test="${not empty requestScope.users}">
        <h2>Popular Profiles</h2>
        <link rel="stylesheet" href="/css/popularUsers.css"/>
        <script src="/js/popularUsers.js"/>
        <c:forEach items="${requestScope.users}" var="user">
            <div class="row" onclick="author(${user.getUserId()})">
                <img class="avatar" height="25px" src="${user.getImage()}" onerror="this.src = '/res/icons/logo.svg'">
                <span>${user.getUser()}</span>
            </div>
        </c:forEach>
        <button onclick="getPopularUsers(${requestScope.users.size()})">More</button>
    </c:if>
</div>