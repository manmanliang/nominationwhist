<html>
  <head>
    <title>Whist Game</title>
  </head>
  <body bgcolor="rgb(0,128,0)">
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <h1>Lets Play Nommy</h1>
    <hr>
    </br>
    <p>Here are the player cards:</p>
    <h2>Player 1</h2>
    <p>
      <c:forEach var="card" items="${playerOne}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
    <h2>Player 2</h2>
    <p>
      <c:forEach var="card" items="${playerTwo}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
    <h2>Player 3</h2>
    <p>
      <c:forEach var="card" items="${playerThree}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
    <h2>Player 4</h2>
    <p>
      <c:forEach var="card" items="${playerFour}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
  </body>
</html>
