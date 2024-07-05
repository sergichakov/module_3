<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 6/26/24
  Time: 4:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${result}</title>
</head>
<body>
<h1>${result}</h1>

<button onclick="window.location.href='/'">Начать игру заново</button>
<br>Статистика<br>
IP address: ${ipAddress}<br>
Имя игрока: ${playerName}<br>
Количество пройденных игр: ${numberOfGames}
</body>
</html>
