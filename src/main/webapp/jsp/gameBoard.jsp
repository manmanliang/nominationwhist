<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <script type="text/javascript" src="<c:url value="/js/defaultVals.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/vars.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jQueryGlobals.js"/>"></script>
    <script type="text/javascript">
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
      	game.trick.trickNum = ${trickNum};
      	
      	AJAXTimeout = ${AJAXTimeout};
      	
      	imagesDir = "<c:url value="/images/"/>";
    </script>   
    <script type="text/javascript" src="<c:url value="/js/whistInit.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/whist.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
    <script type="text/javascript">
    	$(function() {
    		onLoadEventHandler();
    	});
    </script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>" />
  </head>
  <body>
 	<div id="header">
 		<div id="scores"></div>
    	<h1>Nomination Whist</h1>
    	<h3>Player ${user}</h3>
    </div>
    <hr>
    <div id="gameArea">
		<div id="messagesPane">
 			<div id="messages">
 				<p id="message">
			 		<c:if test="${fn:length(game.rounds) == 0}">
			 		<c:if test="${game.players[0] eq user}">
			 			Please click "Start Game" when ready
    				</c:if>
			 		<c:if test="${game.players[0] ne user}">
			 			Waiting for ${game.players[0]} to start the game
    				</c:if>
    				</c:if>
 				</p>
 			</div>
	 		<div id="statsPane">
	 			<p id="statsKeyLinkPara"><a href="javascript:toggleStatsKey()" id="statsKeyLink">Show Stats Key</a></p>
 				<h3>Player Stats</h3>
		 		<div id="stats"></div>	
		 		<div id="statsKey" style="display: none">
	 				<p><img src="<c:url value="/images/win.png"/>"/> = Percentage of games won</p>
	 				<p><img src="<c:url value="/images/correctBid.png"/>"/> = Percentage of bids correct</p>
	 				<p><img src="<c:url value="/images/favBid.png"/>"/> = Most common bid</p>
	 				<p><img src="<c:url value="/images/favTrumps.png"/>"/> = Most common trumps called</p>
	 				<p>Note: Stats are only generated for games that have finished and have more than 1 player.</p>
	 			</div>	
 			</div>
	 	</div>
		<div id="gamePane">
		 	<div id="finalScores" style="display: none;"></div>
    		<div id="gameTable">
		 		<c:if test="${game.players[0] eq user && fn:length(game.rounds) == 0}">
    			<form method="POST" action="start-game">
    	    	  	<input type="hidden" name="id" value="${game.id}" />
    	   	  		<input type="submit" value="Start Game" />
    			</form>
    			</c:if>
				<div id="bidUI" style="display: none;"></div>
				<div id="trumpsUI" style="display: none;"></div>
				<div id="trick" style="display: none;"></div>
    	        <div id="previousTrick"></div>
			</div>
		</div>	
 		<div id="hand"></div>
 	</div>
 	
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="gameArea" />
 	</jsp:include>
  </body>
</html>
