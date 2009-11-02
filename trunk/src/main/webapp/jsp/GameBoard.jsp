<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <script type="text/javascript" src="<c:url value="/js/vars.js"/>"></script>
    <script>
    	user = '${user}';
    	userId = ${userIndex};
    	
    	game.id = ${game.id};
      <c:forEach var="player" begin="0" end="${fn:length(game.players) - 1}">
    	game.players[${player}] = '${game.players[player]}';
      </c:forEach>
      <c:if test="${fn:length(game.rounds) > 0}">
		game.rounds = ${rounds};
	  </c:if>
      	game.round.count = ${roundCount};
    </script>   
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/xmlhttp.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/whistInit.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/whist.js"/>"></script>
    <script>
    	xmlHttp['gameStart'].init("<c:url value="/gameStart?id=${game.id}" />", "messages");
    	xmlHttp['update'].init("<c:url value="/update?id=${game.id}" />", "messages");
    	xmlHttp['bid'].init("<c:url value="/bid?id=${game.id}" />", "messages");
    	xmlHttp['trumps'].init("<c:url value="/trumps?id=${game.id}" />", "messages");
    	xmlHttp['playCard'].init("<c:url value="/play-card?id=${game.id}" />", "messages");
    </script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/whist.css"/>" />
  </head>
  <body onload="onLoadEventHandler();">
 	<div id="header">
 		<div id="scores"></div>
    	<h1>Nomination Whist</h1>
    	<h3>Player ${user}</h3>
    </div>
    <hr>
	<div id="messagesPane">
		<div id="messagesArea">
	 		<h3>Messages</h3>
 			<div id="messages"></div>
 		</div>
 	</div>
	<div id="gamePane">
	 	<div id="finalScores" style="display: none;"></div>
    	<div id="gameTable">
	    	<p id="status"></p>
	 		<c:if test="${game.players[0] eq user && fn:length(game.rounds) == 0}">
    		<form method="POST" action="start-game">
        	  	<input type="hidden" name="id" value="${game.id}" />
       	  		<input type="submit" value="Start Game" />
    		</form>
    		</c:if>
			<div id="bidUI" style="display: none"></div>
			<div id="trumpsUI" style="display: none"></div>
			<div id="trick" style="display: none"></div>
            <div id="previousTrick"></div>
		</div>
	</div>
 	<div id="hand"></div>
  </body>
</html>