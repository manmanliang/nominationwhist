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
      <li>Game (${game.creationDate}):
        <c:forEach var="player" items="${game.players}">
          ${player}
        </c:forEach>
        <form method="POST" action="join-game">
 	    	<fieldset>
        		<legend>Join Game</legend>
        		<input type="hidden" name="id" value="${game.id}" />
        		<input type="submit" value="Join Game" />
      		</fieldset>            
        </form>
        <c:if test="${game.players[0] eq user}">
        <form method="POST" action="start-game">
 	    	<fieldset>
        		<legend>Start Game</legend>
        		<input type="hidden" name="id" value="${game.id}" />
        		<input type="submit" value="Start Game" />
      		</fieldset>            
        </form>
        </c:if>
      </li>
    </c:forEach>
    </ul>
  </body>
</html>
