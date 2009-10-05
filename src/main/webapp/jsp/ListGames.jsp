<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <style>
    	body { background: #008000; }
    </style>
  </head>
  <body>
	<h1>List of Games</h1>
	<p>User: ${user}</p>
	
	<form method="POST" action="create-game">
      <fieldset>
        <legend>Create Game</legend>
        <input type="submit" value="Create Game" />
      </fieldset>
    </form>
    
    <ul>
    <c:forEach var="game" items="${games}">
      <li>Game (${game.creationDate}): ${game.playerOne}, ${game.playerTwo}, ${game.playerThree}, ${game.playerFour}</li>
    </c:forEach>
    </ul>
  </body>
</html>
