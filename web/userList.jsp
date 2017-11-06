<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="userListContainer" class="column">
    <c:forEach var="u" items="${users}"><div class="row">${u.user}</div></c:forEach>
</div>
