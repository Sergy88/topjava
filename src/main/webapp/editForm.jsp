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
    <p>
    <label>Дата</label>
    <input name="dateTime" type="datetime-local" value="${meal.dateTime}">
    </p>
    <p>
    <label>Описание</label>
    <input name="description" type="text" value="${meal.description}">
    </p>
    <p>
    <label>Калории</label>
    <input name="cal" type="number" value="${meal.calories}"/>
    </p>
    <input type="hidden" name="id" value="${meal.id}"/>
    <button type="submit">Подтвердить</button>
    <button onclick="window.history.back()" type="button">
        Отмена</button>
</form>
</body>
</html>
