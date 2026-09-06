<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Taller Intro Spring - Discografia</title>
</head>
<body>
    <h1>Sistema de Gestion de Discografia Musical</h1>
    <p>Bienvenido al sistema. Seleccione un modulo para comenzar:</p>
    <ul>
        <li><a href="${pageContext.request.contextPath}/artists">Gestion de Artistas</a></li>
        <li><a href="${pageContext.request.contextPath}/tracks">Gestion de Canciones (Tracks)</a></li>
    </ul>
</body>
</html>
