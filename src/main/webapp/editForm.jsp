<%--
  Created by IntelliJ IDEA.
  User: mrser
  Date: 02.10.2020
  Time: 14:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Редактирование еды.</title>
</head>
<body>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" class="ru.javawebinar.topjava.model.Meal"
             scope="request"/>
<fmt:parseDate var="parsedDate" value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm"/>
<fmt:formatDate var="formattedDate" value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
<h1>Редактирование: ${meal.description} ${formattedDate}</h1>
<form action="meals" method="post">
    <p>Заполните новые данные:</p>
    <label>Дата</label>
    <input name="dateTime" type="datetime-local" value="${meal.dateTime}">
    <label>Описание</label>
    <input name="description" type="text" value="${meal.description}">
    <label>Калории</label>
    <input name="cal" type="text" value="${meal.calories}"/>
    <input type="hidden" name="id" value="${meal.id}"/>
    <button type="submit">Подвтердить</button>
</form>


</body>
</html>
