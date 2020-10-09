<%--
  Created by IntelliJ IDEA.
  User: mrser
  Date: 09.10.2020
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Java Enterprise (Topjava)</title>
</head>
<body>
<h3>Проект <a href="https://github.com/JavaWebinar/topjava" target="_blank">Java Enterprise (Topjava)</a></h3>
<hr>
Сейчас авторизован под id ${loginId}
<h2>Авторизация</h2>

<form action="" method="post">
    <select name="login">
        <option value="2">Admin</option>
        <option value="1">User</option>
    </select>
    <p><input type="submit" value="Отправить"></p>
</form>
<ul>
    <li><a href="users">Users</a></li>
    <li><a href="meals">Meals</a></li>
</ul>
</body>
</html>

