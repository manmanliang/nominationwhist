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
      	<c:forEach var="round" begin="0" end="${fn:length(game.rounds) - 1}">
			game.rounds[${round}] = '${game.rounds[round]}';
		</c:forEach>
	  </c:if>
      	game.round.current = ${fn:length(game.rounds) - 1};
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
    <h1>Nomination Whist</h1>
 	<h2>Player ${user}</h2>
    <hr>
    <div id="finalScores" style="display:none;"></div>
 	<div id="gameArea">
 	<div id="topSection">
 	<div id="scoresPane"><div id="scores"></div><div id="messages"></div></div>
 	<div id="table">
	 	<c:if test="${game.players[0] eq user && fn:length(game.rounds) == 0}">
		<p>Actions</p>
	    <form method="POST" action="start-game">
	 	  <fieldset>
	        <legend>Start Game</legend>
	          <input type="hidden" name="id" value="${game.id}" />
	       	  <input type="submit" value="Start Game" />
	      </fieldset>            
	    </form>
	    </c:if>
		<div id="bidUI" style="display:none"></div>
		<div id="trumpsUI" style="display:none"></div>
		<div id="trick" style="display:none"></div>
 	</div>
 	</div>
 	<div id="hand"></div>
    </div>
  </body>
</html>