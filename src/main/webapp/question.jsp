<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 6/26/24
  Time: 4:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${question}</title>
</head>
<body>
<h1>${question}</h1>
<button onclick="window.location.href='${answer_1_link}'">${answer_1}</button>
<button onclick="window.location.href='${answer_2_link}'">${answer_2}</button>
<br>Статистика<br>
IP address: ${ipAddress}<br>
Имя игрока: ${playerName}<br>
Количество пройденных игр: ${numberOfGames}
</body>
</html>
