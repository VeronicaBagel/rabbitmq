<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>RabbitMQ Task</title>
</head>
<body>
<spring:url value="/result" var="seeResults" />

Thank you for leaving comment <br />
<button onclick="location.href='${seeResults}'">See the processed result</button> <br />
<c:choose>
    <c:when test="${not empty result}">
        You said: ${result.commentContent}
    </c:when>
</c:choose>
</body>
</html>