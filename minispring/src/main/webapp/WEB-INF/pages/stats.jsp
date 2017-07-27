<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stats</title>
    </head>
    <body>
        <h1>Time of day: ${timeOfDay}</h1>
        <h2>And also: <c:out value="${timeOfDay}"/></h2>
    </body>
</html>
