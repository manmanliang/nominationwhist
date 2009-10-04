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
    <h2>Player 1 - ${playerOne.name}</h2>
    <p>
      <c:forEach var="card" items="${playerOne.cards}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
    <h2>Player 2 - ${playerTwo.name}</h2>
    <p>
      <c:forEach var="card" items="${playerTwo.cards}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
    <h2>Player 3 - ${playerThree.name}</h2>
    <p>
      <c:forEach var="card" items="${playerThree.cards}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
    <h2>Player 4 - ${playerFour.name}</h2>
    <p>
      <c:forEach var="card" items="${playerFour.cards}">
        <img src="images/<c:out value="${card}"/>.png">
      </c:forEach>
    </p>
  </body>
</html>
