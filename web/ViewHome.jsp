<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/home.css">
<link rel="stylesheet" href="css/message.css">
<script><%@include file="/js/tweet.js"%></script>
<script><%@include file="/js/home.js"%></script>


<div id="homeContainer">
    <c:if test="${sessionScope.user != null}">
        <div id="newTweet">
            <textarea placeholder="Write a new tweet"></textarea>
            <button>Send</button>
        </div>
    </c:if>
    <div id="messagesContainer">
        <c:forEach var="tweet" items="${tweets}">
            <div class="message">
                <div class="author">
                    <img class="avatar">
                    <span>${tweet}</span>
                </div>
                <div class="content"></div>
                <div class="actions">
                    <button href="/"><img src="res/icons/retweet.svg"></button>
                    <button href="/"><img src="res/icons/flag.svg"></button>
                    <button href="/"><img src="res/icons/edit.svg"></button>
                </div>
            </div>
        </c:forEach>
    </div>
</div>