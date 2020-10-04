<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meals</title>
    <link href="<%=request.getContextPath()%>/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<h1>Список еды</h1>
<a href="<%=request.getContextPath()%>">Home</a>

<table>
    <tr>
        <th>Описание</th>
        <th>Дата</th>
        <th>Калории</th>
        <th colspan="2">Действия</th>
    </tr>

    <c:forEach var="meal" items="${mealsAttrubute}">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr id="${meal.excess ? "redTd" : "greenTd"}">
            <td>${meal.description}</td>
            <fmt:parseDate var="parsedDate" value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm"/>
            <fmt:formatDate var="formattedDate" value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
            <td>${formattedDate}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?id=${meal.id}&action=update">Обновить</a></td>
            <td><a href="meals?id=${meal.id}&action=delete">Удалить</a></td>
        </tr>
    </c:forEach>

</table>

<form action="meals" method="post">
    <button type="submit">Новый прием пищи.</button>
</form>

</body>
</html>
