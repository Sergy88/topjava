<%--
  Created by IntelliJ IDEA.
  User: mrser
  Date: 01.10.2020
  Time: 22:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meals</title>
    <link href="<%=request.getContextPath()%>/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>There will be meals</h1>

<table>
    <tr>
        <th>Описание</th>
        <th>Дата</th>
        <th>Каллори</th>
        <th>Превышение</th>
    </tr>

    <c:forEach var="meal" items="${mealsAttrubute}">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo" scope="page"/>

        <tr id="${meal.excess?"redTd":"greenTd"}">
            <td>${meal.description}</td>
            <fmt:parseDate var="parsedDate" value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm"></fmt:parseDate>
            <fmt:formatDate var="formattedDate" value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate>
            <td>${formattedDate}</td>
            <td>${meal.calories}</td>
            <td>${meal.excess?"да":"нет"}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
