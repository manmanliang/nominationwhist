var game = new Object();
var xmlHttp = new Array();
var user = null;

game.id = null;
game.players = new Array();
game.rounds = new Array();
game.round.current = null;
game.round.count = null;
game.hand = null;
game.trick = null;

poll = new Object();
poll.active = new Array();
poll.interval = 500;
poll.phase = "loading";
poll.player = null;

updated = new Object();
updated.poll = false;
updated.rounds = false;
updated.hand = false;
updated.trick = false;

function onLoadEventHandler() {
	xmlHttp['hand'].call();
	xmlHttp['trick'].call();
}

function gameUpdatedEventHandler() {
	// Acts in response to the game state being
	// updated by a poll event
	
	// Determine what phase of the game we are in
	// and the currently active player
	updatePhase();
	updateActivePlayer();

	// Update any UI components whose backing data has been 
	// marked updated
	updateUI();
	
	// Schedule any required polls to be executed
	if (updated.poll) {
		reschedulePolls();
	}
}

function reschedulePolls() {
	while (poll.active.length > 0) {
		clearInterval(poll.active.shift());
	}
		
	if (poll.player == null || game.players[poll.player] != user) {
		var pollId = null;
		
		switch (poll.phase) {
			case "bid":
			case "trumps":
				pollId = setInterval(xmlHttp['round'].call(), poll.interval);
				poll.active.push(pollId);
				break;
			case "trick":
				pollId = setInterval(xmlHttp['trick'].call(), poll.interval); 
				poll.active.push(pollId);
				break;
		}
	}
	
	updated.poll = false;
}

function updatePhase() {
	// Inspect the game data to determine what phase of the game 
	// we are currently in
	var previousPhase = poll.phase;

	if (game.hand == null || game.trick == null) {
		poll.phase = "loading";
	}
	else if (game.round.current == game.round.count
		&& game.rounds[game.round.current].finished == true) {
		poll.phase = "finished";
	}
	else if (game.rounds[game.round.current].bids.length < game.players.length) {
		poll.phase = "bid";
	}
	else if (game.rounds[game.round.current].trumps == null) {
		poll.phase = "trumps";
	else {
		poll.phase = "trick";
	}
    
    updated.poll = poll.phase != previousPhase;
}

function updateUI() {
	// TODO: refactor the code below to update the UI components
	// Determine which UI components to display based on phase
	// and actingPlayer.  
	// Inspect the game data 'updated' flags to determine which
	// UI components must be re-rendered.

	
	// Bid snippet
		var html = "";
	<c:forEach var="bid" begin="0" end="gameState.currentRound.numberOfState">
		html = html + "<a href=\"javascript:AJAXPost(bidSetStateChange, '<c:url value="/bid"/>', '{&quot;id&quot;:${game.id}, &quot;bid&quot;:${bid}}');\">${bid}</a>"
 	</c:forEach>
	document.getElementById("bidsDiv").innerHTML = html;
	
	
	// Trumps snippet
		var suits = new Array("SPADES", "HEARTS", "DIAMONDS", "CLUBS");
	var html = "";
	for (suit in suits) {
		html = 	html + "<a href=\"javascript:AJAXPost(trumpsSetStateChange,
											   		  '<c:url value="/set-trumps"/>', 
											   		  '{&quot;id&quot;:${game.id}, 
											   		  &quot;trumps&quot;:&quot;suit&quot;}');\">suit</a>";
	}
	document.getElementById("setTrumpsDiv").innerHTML = html;
	
	
	// Play card snippet
		var cards = document.getElementById("handDiv").getElementByTagName("img");
	
	for (card in cards) {
		card.onClick = "AJAXPost(playCardStateChange,
								 '<c:url value="/play-card"/>', 
								 '{&quot;id&quot;:${game.id}, 
								 &quot;card&quot;:&quot;card.alt&quot;}');";
	}


	// Disable card snippet
		var cards = document.getElementById("handDiv").getElementByTagName("img");
	
	for (card in cards) {
		card.onClick = "";
	}
	
	// Round scores snippet
	document.getElementById("scoresTestBaseString").innerHTML = output;
            
    for (i = 0; i < ${fn:length(game.players)}; i++) {
    	if (!scores.currentRound.bids[i]) {
	    	document.getElementById("scoresDiv").getElementById("player" + i + "Bid").innerHTML = "";
	        gameState.currentRound.bids[i] = "";
	    } else {
			document.getElementById("scoresDiv").getElementById("player" + i + "Bid").innerHTML = scores.currentRound.bids[i];
	        gameState.currentRound.bids[i] = scores.currentRound.bids[i];
	    }
    }
           	
    document.getElementById("scoresDiv").getElementById("trumps").innerHTML = scores.currentRound.trumps;
    gameState.currentRound.trumps = scores.currentRound.trumps;
    document.getElementById("scoresDiv").getElementById("playerToBid").innerHTML = ${game.players[scores.currentRound.playerToBid]};
    gameState.currentRound.playerToBid = scores.currentRound.playerToBid;
    document.getElementById("scoresDiv").getElementById("numberOfCards").innerHTML = scores.currentRound.numberOfCards;
    gameState.currentRound.numberOfCards = scores.currentRound.numberOfCards;
    document.getElementById("scoresDiv").getElementById("bidWinner").innerHTML = ${game.players[scores.currentRound.bidWinner]};
    
    // Hand snippet
        	var imgHTML = "";
        for (card in output) {
        	imgHTML = imgHTML + "<img src = \"images/" + output[card] + ".png"\" alt=\"output[card]\"/>"
        }
        document.getElementById("handDiv").innerHTML = imgHTML;
        
    // Trick snippet
    	// Add the images for the ones we have a value for
    var imgHTML = "";
    for (var i=0; i < output.length; i++) {
    	if (output[i] != null) {
        	var imgToUpdate = document.getElementById("player" + i);
            imgHTML = "images/" + output[i] + ".png";
	        imgToUpdate.src = imgHTML;
            imgToUpdate.style.visibility = "visible";
        }
    }

    for (var i = 0; i < ${fn:length(game.players)}; i++) {
    	var imgToUpdate = document.getElementById("player" + i);
      	if (!output[i]) {
			imgToUpdate.style.visibility = "hidden";
        	imgToUpdate.src = "";
	        gameState.currentRound.playersPlayed[i] = "";
	    } else {
	    	imgToUpdate.src = "images/" + output[i] + ".png";
            imgToUpdate.style.visibility = "visible";
	        gameState.currentRound.playersPlayed[i] = output[i];
	    }
    }
           	
    
}

xmlHttp['round'] = new JSONCallback();
xmlHttp['round'].callback = function(output) {
	// Update game state based on JSON output
    game.rounds[output['idx']] = output;
    game.round.current = output['idx'];
    updated.rounds = true;

	// Update the currently active player    
	var previousPlayer = poll.player;
    poll.player = 
    
    gameUpdatedEventHandler();
}

xmlHttp['hand'] = new JSONCallback();
xmlHttp['hand'].callback = function(output) {
	// Update game state based on JSON output
    game.hand = output;
    updated.hand = true;

    gameUpdatedEventHandler();
}

xmlHttp['trick'] = new JSONCallback();
xmlHttp['trick'].callback = function(output) {
	// Update game state based on JSON output
    game.trick = output;
    updated.trick = true;
 
    gameUpdatedEventHandler();
}
        

xmlHttp['bid'] = new JSONCallback();
xmlHttp['bid'].callback = function(output) {}

xmlHttp['trumps'] = new JSONCallback();
xmlHttp['trumps'].callback = function(output) {}

xmlHttp['playCard'] = new JSONCallback();
xmlHttp['playCard'].callback = function(output) {}



