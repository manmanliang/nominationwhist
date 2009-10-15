<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/xmlhttp.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/whist.js"/>"></script>
    <script>
    	user = ${user}
    	
    	game.id = ${game.id};
      <c:forEach var="player" begin="0" end="${fn:length(game.players) - 1}">
    	game.players[${player}] = '${game.players[player]}';
      </c:forEach>
      <c:forEach var="round" begin="0" end="${fn:length(rounds) - 1}">
    	game.rounds[${round}] = '${rounds[round]}';
      </c:forEach>
      	game.round.current = ${fn:length(rounds) - 1};
      	game.round.count = ${roundCount};
    	
    	xmlHttp['round'].init("<c:url value="/round?id=${game.id}" />", "roundDiv");
    	xmlHttp['hand'].init("<c:url value="/hand?id=${game.id}" />", "handDiv");
    	xmlHttp['trick'].init("<c:url value="/trick?id=${game.id}" />", "trickDiv");
    	xmlHttp['bid'].init("<c:url value="/bid?id=${game.id}" />");
    	xmlHttp['trumps'].init("<c:url value="/trumps?id=${game.id}" />");
    	xmlHttp['playCard'].init("<c:url value="/play-card?id=${game.id}" />");
    </script>   
    <style>
    	body { background: #008000; }
    </style>
  </head>
  <body onload="onLoadEventHandler();">    
    <h1>Lets Play Nommy</h1>
    <hr>
    </br>
    <p>Here are the player cards:</p>
    <h2>Player 1</h2>
    <div id="handDiv"></div>
    <p>Here are the trick cards:</p>
    <div id="trickDiv">
 	  	<c:forEach var="player" begin="0" end="${fn:length(game.players) - 1}">
 	    	<img id="player${player}" src="" height="96" width="72" style="visibility: hidden;"/>
 	    </c:forEach>
    </div>
    <p>Scores</p>
    <div id="scoresDiv">
    	<p>Base String is:</p><div id="scoresTestBaseString"></div>
    	<p>Number of cards is:</p><div id="numberOfCards"></div>
    	<p>Trumps are:</p><div id="trumps"></div>
 	  	<c:forEach var="player" begin="0" end="${fn:length(game.players) - 1}">
 		   	<p>${game.players[player]}'s bid is:</p><div id="player${player}Bid" class="playerBid"></div>
 	    </c:forEach>
 	    <p>Player to bid is:</p><div id="playerToBid"></div>
 	    <p>Player to choose trumps is:</p><div id="bidWinner"></div>
    </div>
    <p>Actions</p>
      <c:if test="${game.players[0] eq user && fn:length(game.rounds) == 0}">
        <form method="POST" action="start-game">
 	    	<fieldset>
        		<legend>Start Game</legend>
        		<input type="hidden" name="id" value="${game.id}" />
        		<input type="submit" value="Start Game" />
      		</fieldset>            
        </form>
      </c:if>
      <div id="bidsDiv"></div>
      <div id="setTrumpsDiv"></div>
  </body>
</html>

 