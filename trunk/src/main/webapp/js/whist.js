var xmlHttp = new Array();

var game = new Object();
game.pollInterval = 500;

var user = null;

function onLoadEventHandler() 
{
	xmlHttp['round'].call();
	xmlHttp['hand'].call();
	xmlHttp['trick'].call();
}

function gameChangeEventHandler()
{
	var phase = gamePhase();
	var actingPlayer = actingPlayer(phase);

	updateUI(phase, actingPlayer);
	
	if (actingPlayer != user) {
		switch (phase) {
			case "bid":
				setTimeout(xmlHttp['round'].call(), game.pollInterval); 
				break;
			case "trick":
				setTimeout(xmlHttp['trick'].call(), game.pollInterval); 
				break;
		}
	}
}

function gamePhase() {
	// TODO: refactor code below to determine game phase
	// Inspect the game data to determine what phase of the game 
	// we are currently in
	
    if (game.card == 0) {
        return;
    }
    
    // Check if we have a full set of bids
    for (var i = 0; i < gameState.currentRound.bids.length; i++) {
        if (gameState.currentRound.bids[i] == "") {
			// We are missing at least 1 bid
			if (gameState.currentRound.playerToBid == ${user}) {
				// We are currently the one to bid, so show ui
				showBidUI();
			} else {
				// We are waiting for someone else to bid
				dataRefresh("scores");
			}
			
			return;
        }
    }
    
    // Check to see if we have trumps
    if (gameState.currentRound.trumps == "") {
    	// Trumps are not decided yet
    	if (gameState.bidWinner == ${user}) {
    		showSetTrumpsUI();
    	} else {
    		dataRefresh("scores");
    	}
    	
    	return;
    }
    
    //Must be in the middle of a round of tricks
    if (gameState.playerToPlay == ${user}) {
    	allowCardPlay();
    } else {
    	stopCardPlay();
    	dataRefresh("trick");
    }
  
  	// Bidding test  
    if ((numberOfBids != ${fn:length(game.players)} && playerToBid != ${user}) ||
     	(numberOfBids == ${fn:length(game.players)} && scores.currentRound.trumps == "" && scores.currentRound.bidWinner != ${user})) {
    }
}

function actingPlayer(phase) {
	// TODO: refactor code below to determine the currently acting player
	// Returns true if the game is waiting on the current
	// user to act, false otherwise
	
    if (output.length == ${fn:length(game.players)} ||
    	gameState.currentRound.playerToPlay == ${user}) {
    }
}

function updateUI(phase, actingPlayer) {
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
	// TODO: refactor the code below to update the game state with round data
	// Update game state based on JSON output
    gameState.currentRound.bidWinner = scores.currentRound.bidWinner;
    gameState.currentRound.playerTurn = scores.currentRound.playerTurn;
    gameState.gameFinished = scores.gameFinished;
    
    gameChangeEventHandler();
}

xmlHttp['hand'] = new JSONCallback();
xmlHttp['hand'].callback = function(output)
{
	// TODO: refactor the code below to update the game state with hand data
	// Update game state based on JSON output
    gameState.cardCount = output.length;

    gameChangeEventHandler();
}

xmlHttp['trick'] = new JSONCallback();
xmlHttp['trick'].callback = function(output)
{
	// TODO: refactor the code below to update the game state with trick data
	// Update game state based on JSON output
 
    gameChangeEventHandler();
}
        

xmlHttp['bid'] = new JSONCallback();
xmlHttp['bid'].callback = function(output) {}

xmlHttp['trumps'] = new JSONCallback();
xmlHttp['trumps'].callback = function(output) {}

xmlHttp['playCard'] = new JSONCallback();
xmlHttp['playCard'].callback = function(output) {}



