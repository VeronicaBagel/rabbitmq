<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>

<head>
    <title>RabbitMQ Task</title>
</head>

<body>
<form:form method="post" modelAttribute="comment" action="comment">

    <form:label path="commentContent">Comment: </form:label>
    <form:input path="commentContent" name="commentContent" id="commentContent" placeholder="Say what's on your mind" />

    <form:button id="loginButton" name="loginButton">Post a comment</form:button>


</form:form>

${result}
<c:choose>
    <c:when test="${not empty result}">
        Thank you for leaving a comment!
    </c:when>
</c:choose>

</body>

</html>