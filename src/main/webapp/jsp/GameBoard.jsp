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
      	game.trick.trickNum = ${trickNum};
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
 				<h3>Player Stats</h3>
		 		<div id="stats">
	 				<p>Coming soon...</p>
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
 	<div id="instructions" style="display: none;">
 		<p id="instructionsInternalLink">
 			<a href="javascript:showInstructions('gameInstructions')" id="gameInstructionsLink">Rules</a>
 			<a href="javascript:showInstructions('boardInstructions')" id="boardInstructionsLink">Game Screen Help</a>
 			| <a href="javascript:toggleInstructions()">Hide Instructions</a>
 		</p>
 		<div id="gameInstructions">
	 		<h2>Game Rules</h2>
	 		<p>Nomination whist is played as a series of rounds in which:</p>
	 		<ol>
	 			<li>each player bids</li>
	 			<li>a player chooses trumps</li>
	 			<li>tricks are played until all cards have been played</li>
	 			<li>scores are added up</li>
	 		</ol>
	 		<h3>Bidding</h3>
	 		<p>Rounds starts with each player bidding, starting with the player to the left of the previous first bidder</p>
	 		<p>In each round the last player to bid may not make a bid which would result in the total bids being equal to the number of cards dealt to each player</p>
	 		<p>No player may bid zero more than 3 times in a row</p>
	 		<h3>Choosing Trumps</h3>
	 		<p>After all players have made their bid, the player who bid the highest then chooses trumps</p>
	 		<h3>Tricks</h3>
	 		<p>Once trumps have been chosen each player takes turns playing a card starting with the player who bid first, followed in consecutive tricks by the person who won the previous trick</p>
	 		<p>After the first player has played their card, subsequent players must:</p>
	 		<ol>
	 			<li>Play a card of the same suit as the first card in the trick if they can</li>
	 			<li>Otherwise play a card of a different suit, trumps or not</li>
	 		</ol>
	 		<p>The winner of the trick is the person who played the highest card in the suit that was played first unless any trumps have been played in which case the highest trump card wins</p>
	 		<h3>Scoring</h3>
	 		<p>For each round each player scores 1 point for every trick they won, plus 10 extra points if they won the same number of tricks as they won</p>
	 	</div>
		<div id="boardInstructions" style="display: none;">
	 		<h2>Game Board</h2>
	 		<p>This page explains the sections of the game area and what they mean:</p>
	 		<h3>Header</h3>
	 		<p>On the left is the name of the player and a link to these instructions</p>
 			<p>On the right is a scoreboard showing the following information the previous round followed by the current round consisting of:</p>
 			<ul>
 				<li>The number of cards in the round</li>
 				<li>What trumps were chosen</li>
 				<li>And for each player:</li>
 				<ul>
 					<li>Their bid</li>
 					<li>The number of tricks they won</li>
 					<li>Their total score</li>
 				</ul>
 			</ul>
	 		<h3>Main Area</h3>
 			<p>On the left is the game table holding the cards of this trick, and bottom right the cards that were played in the previous trick</p>
 			<p>On the right is a space for:</p>
 			<ol>
 				<li>Status messages from the server about the game</li>
 				<li>Chat messages from other players - To write a message, type in the box at the bottom and press return</li>
 			</ol>
 			<h3>Hand</h3>
 			<p>At the bottom of the screen are your current cards</p>
		</div>
	</div>
 	<div id="footer">
 		<hr>
		<p id="footer-right">Version: ${version}</p>
 		<p><a href="javascript:toggleInstructions()" id="instructionsLink">Show Instructions</a></p>
 	</div>
  </body>
</html>