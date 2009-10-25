// Acts in response to the game state being
// updated by an ajax request
function gameUpdatedEventHandler() {
	// Update any UI components whose backing data has been 
	// marked updated
	updateUI();
	
	if (userId != game.activePlayer) {
		if (userAction == true) {
			userAction = false;
			xmlHttp['update'].call('{\"id\":' + game.id + ', \"phase\":' + game.phase + '}');	
		} else { 
			setTimeout("xmlHttp['update'].call('{\"id\":' + game.id + ', \"phase\":' + game.phase + '}')", 500);
		}
	}
}

// Inspect the game data 'updated' flags to determine which
// UI components must be re-rendered.
function updateUI() {
	// Game finished update
	if (game.phase == 3) {
		// Game is finished, print final scores
	    for (var i = 0; i < game.players.length; i++) {
			document.getElementById("player" + i + "FinalScore").innerHTML = game.rounds[game.round.current].scores[i];
		}
        document.getElementById("finalScores").style.display = '';
       	// set active player to be us as we want no more ui polls from other players events
       	game.activePlayer = userId;
	}
	
    // Hand update
    if (updated.hand) {
        var imgHTML = "";
        for (card in game.hand) {
        	imgHTML = imgHTML + cardHTML(game.hand[card]);
        }
        document.getElementById("hand").innerHTML = imgHTML;
       	updated.hand = false;
	}
	        
	// Round update
	if (updated.round) {
		var idx = game.round.current;
		
        if (idx > 0 && idx != game.round.currentLastPoll) {
            for (i = 0; i < game.players.length; i++) {
                document.getElementById("player" + i + "prevScore").innerHTML = game.rounds[idx].scores[i];
                document.getElementById("player" + i + "prevBid").innerHTML = game.rounds[game.round.currentLastPoll].bids[i];
            }
            document.getElementById("prevTrumps").innerHTML = prettify(game.rounds[game.round.currentLastPoll].trumps);
            document.getElementById("prevCards").innerHTML = game.rounds[game.round.currentLastPoll].numberOfCards;
        }
        
	    for (i = 0; i < game.players.length; i++) {
            document.getElementById("player" + i + "currentScore").innerHTML = (!game.rounds[idx].scores[i]) ? "" : game.rounds[idx].scores[i];
            document.getElementById("player" + i + "currentBid").innerHTML = (!game.rounds[idx].bids[i]) ? "" : game.rounds[idx].bids[i];
	    }
        
        document.getElementById("currentTrumps").innerHTML = (game.rounds[idx].trumps) ? prettify(game.rounds[idx].trumps) : "";
        
	    document.getElementById("currentCards").innerHTML = game.rounds[idx].numberOfCards;

		if (game.players.length > game.rounds[idx].bids.length) {
		    document.getElementById("status").innerHTML = "Player to bid is: " + game.players[game.activePlayer];
            document.getElementById("status").style.visibility = 'visible';
		}
		
	    var highestBidder = game.rounds[idx].highestBidder;
	    if (highestBidder != -1) {
		    document.getElementById("status").innerHTML = "Player to choose trumps is: " + game.players[game.rounds[idx].highestBidder];
            document.getElementById("status").style.visibility = 'visible';
		}
		updated.round = false;
	}

    // Trick update
    if (updated.trick) {
	    for (var i = 0; i < game.players.length; i++) {
            if (game.trick.prevTricksWon) {
                document.getElementById("player" + i + "prevTricks").innerHTML = game.trick.prevTricksWon[i];
            }
            
		   	if (!game.trick.tricksWon[i]) {
		    	document.getElementById("player" + i + "currentTricks").innerHTML = "";
		    } else {
				document.getElementById("player" + i + "currentTricks").innerHTML = game.trick.tricksWon[i];
		    }
	    
	    	var imgToUpdate = document.getElementById("player" + i + "TrickCard");
	      	if (!game.trick.cards[i]) {
				imgToUpdate.style.visibility = "hidden";
	        	imgToUpdate.src = "";
		    } else {
		    	imgToUpdate.src = "images/" + game.trick.cards[i] + ".png";
		        imgToUpdate.style.visibility = "visible";
		    }
	    }
	   	updated.trick = false;
	}

	// Check UIs to update if we are the current player
	if (userId == game.activePlayer) {	
		// Bid UI Update
		if (game.phase == 0) {
			var html = "";
			for (var num = 0; num <= game.rounds[game.round.current].numberOfCards; num++) {
				html = html + "<a href=\"javascript:bid(" + num + ");\">" + num + "</a> ";
			}
			document.getElementById("bidUI").innerHTML = html;
            document.getElementById("trick").style.display = 'none';
            document.getElementById("bidUI").style.display = '';
		}
	
		// Trumps UI Update
		if (game.phase == 1) {
			// Trumps snippet
			var suits = new Array("SPADES", "HEARTS", "DIAMONDS", "CLUBS");
			var html = "";
			for (suit in suits) {
				html = 	html + "<a href=\"javascript:setTrumps('" + suits[suit] + "');\">" + prettify(suits[suit]) + "</a> ";
			}
			document.getElementById("trumpsUI").innerHTML = html;
            document.getElementById("trick").style.display = 'none';
            document.getElementById("trumpsUI").style.display = '';
		}

		// Play card UI Update
		if (game.phase == 2) {
			setHandOnClickHandler();
		}
	}
	
}

xmlHttp['update'] = new JSONCallback();
xmlHttp['update'].callback = function(output) {

	// Update game state based on JSON output
	game.phase = output.phase;

	if (output.hand) {
	    game.hand = output.hand;
    
	    updated.hand = true;
	}
	if (output.round) {
   		game.rounds[output.round.idx] = output.round;
        game.round.currentLastPoll = game.round.current;
   		game.round.current = output.round.idx;

	    updated.round = true;
	}
	if (output.trick) {
	    game.trick = output.trick;
	    
	    updated.trick = true;
	}

	// Update the currently active player    
	game.activePlayer = output.activePlayer;
	    		    
    gameUpdatedEventHandler();
}

function bid(bid) {
	// Clear the UI and record our bid
	document.getElementById("bidUI").style.display = 'none';
	document.getElementById("trick").style.display = '';
	document.getElementById("player" + userId + "currentBid").innerHTML = bid;
	document.getElementById("status").style.visibility = 'hidden';
	
	xmlHttp['bid'].call('{"id":' + game.id + ', "bid":' + bid + '}');
}
xmlHttp['bid'] = new JSONCallback();
xmlHttp['bid'].callback = function(output) {
	if (output.result == 0) {
		document.getElementById("bidUI").innerHTML = "";
		
		userAction = true;
		game.activePlayer = null;
		gameUpdatedEventHandler();
	} else {
		// Show the ui again along with an error and clear our unacceptable bid
		document.getElementById("player" + userId + "currentBid").innerHTML = "";
        document.getElementById("trick").style.display = 'none';
		document.getElementById("bidUI").style.display = '';
		document.getElementById("status").style.visibility = 'visible';
		var messagesDiv = document.getElementById("messages");
		messagesDiv.innerHTML = messagesDiv.innerHTML + "<p>" + output.errorMessage + "</p>";
	}
}

function setTrumps(trumps) {
	// Clear the UI and record our trumps choice
	document.getElementById("trumpsUI").style.display = 'none';
	document.getElementById("trick").style.display = '';
	document.getElementById("currentTrumps").innerHTML = prettify(trumps);

	xmlHttp['trumps'].call('{"id":' + game.id + ', "trumps":"' + trumps + '"}');
}
xmlHttp['trumps'] = new JSONCallback();
xmlHttp['trumps'].callback = function(output) {
	if (output.result == 0) {
		document.getElementById("trumpsUI").innerHTML = "";
		
		userAction = true;		
		game.activePlayer = null;
	    gameUpdatedEventHandler();
	} else {
		// Show the ui again along with an error and clear our unacceptable trumps choice
		document.getElementById("currentTrumps").innerHTML = "";
        document.getElementById("trick").style.display = 'none';
		document.getElementById("trumpsUI").style.display = '';
		var messagesDiv = document.getElementById("messages");
		messagesDiv.innerHTML = messagesDiv.innerHTML + "<p>" + output.errorMessage + "</p>";
	}
}

function playCard(card) {
	// Remove the card from our hand and add it to the trick
	document.getElementById(card).style.display = 'none';
	document.getElementById("player" + userId + "TrickCard").src = "images/" + card + ".png";
	document.getElementById("player" + userId + "TrickCard").style.visibility = "visible";
	
	// Now remove the onClick handler
	removeHandOnClickHandler();

	xmlHttp['playCard'].call('{"id":' + game.id + ', "card":"' + card + '"}');
}
xmlHttp['playCard'] = new JSONCallback();
xmlHttp['playCard'].callback = function(output) {
	if (output.result == 0) {
		document.getElementById("hand").removeChild(document.getElementById(output.card));
		
		userAction = true;		
		game.activePlayer = null;
	    gameUpdatedEventHandler();
	} else {
		// Show the ui again along with an error and clear our unacceptable trumps choice
		document.getElementById("player" + userId + "TrickCard").style.visibility = "hidden";
		document.getElementById("player" + userId + "TrickCard").src = "";
		document.getElementById(output.card).style.display = '';
		var messagesDiv = document.getElementById("messages");
		messagesDiv.innerHTML = messagesDiv.innerHTML + "<p>" + output.errorMessage + "</p>";
		
		// Re-instate onclick handler
		setHandOnClickHandler();		
	}
}

function setHandOnClickHandler() {
	var handDiv = document.getElementById("hand");
	var cards = handDiv.getElementsByTagName("img");
	var cardsLength = cards.length;
	var handHTML = "";

	for (var i = 0; i < cardsLength; i++) {
		handHTML = handHTML + "<a href = \"javascript:playCard('" + cards[i].id + "');\">";
		handHTML = handHTML + cardHTML(cards[i].id, cards[i].style.display);
		handHTML = handHTML + "</a>";
	}		
	
	handDiv.innerHTML = handHTML;
}

function removeHandOnClickHandler() {
	var handDiv = document.getElementById("hand");
	var cards = handDiv.getElementsByTagName("img");
	var cardsLength = cards.length;
	var handHTML = "";

	for (var i = 0; i < cardsLength; i++) {
		handHTML = handHTML + cardHTML(cards[i].id, cards[i].style.display);
	}		
	
	handDiv.innerHTML = handHTML;
}

function cardHTML(card, displayState) {
	var html = "<img id = \"" + card + "\" src = \"images/" + card + ".png\"";

	if (displayState) {
		html = html + "style = \"display: " + displayState + ";\"";
	}
	
	html = html + "/>";
	
	return html;
}

function prettify(string) {
    if (string) {
        var pretty = string.substr(0,1);
        var tmp = string.substr(1);
        pretty = pretty.toUpperCase();
        pretty = pretty + tmp.toLowerCase();
    }
    
    return pretty;
}