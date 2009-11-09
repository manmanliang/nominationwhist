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
            document.getElementById("player" + i + "currentScore").innerHTML = game.rounds[game.round.current].scores[i];
		}
        document.getElementById("finalScores").style.display = '';

       	// set active player to be us as we want no more ui polls from other players events
       	game.activePlayer = userId;
	}
	
    // Active Player Update
    if (game.activePlayer != null) {
        for (var i = 0; i < game.players.length; i++) {
            document.getElementById("player" + i + "ScoreColumn").className = "";            
        }
        document.getElementById("player" + game.activePlayer + "ScoreColumn").className = "active";
    }

    // Hand update
    if (updated.hand) {
        var imgHTML = "";
        for (card in game.hand) {
        	imgHTML = imgHTML + " " + cardHTML(game.hand[card]);
        }
        document.getElementById("hand").innerHTML = imgHTML;
       	updated.hand = false;
	}
	        
	// Round update
	if (updated.round) {
		var idx = game.round.current;
		
	    for (i = 0; i < game.players.length; i++) {
            document.getElementById("player" + i + "currentBid").innerHTML = (game.rounds[idx].bids[i] != null) ? game.rounds[idx].bids[i] : "";
	    }
        document.getElementById("currentTrumps").innerHTML = (game.rounds[idx].trumps) ? prettify(game.rounds[idx].trumps) : "";
        document.getElementById("currentCards").innerHTML = game.rounds[idx].numberOfCards;

		updated.round = false;
	}
    // Previous Round update
    if (updated.previousRound) {
        if (game.rounds[game.round.current - 1]) {
            var idx = game.round.current - 1;
            
            for (i = 0; i < game.players.length; i++) {
                document.getElementById("player" + i + "prevBid").innerHTML = game.rounds[idx].bids[i];
                document.getElementById("player" + i + "prevTricks").innerHTML = game.rounds[idx].tricksWon[i];
                document.getElementById("player" + i + "prevScore").innerHTML = game.rounds[idx].scores[i];
            }
            document.getElementById("prevTrumps").innerHTML = prettify(game.rounds[idx].trumps);
            document.getElementById("prevCards").innerHTML = game.rounds[idx].numberOfCards;
        }
		updated.previousRound = false;
    }

    // Trick update
    if (updated.trick) {
	    for (var i = 0; i < game.players.length; i++) {
		   	if (!game.trick.tricksWon[i]) {
		    	document.getElementById("player" + i + "currentTricks").innerHTML = "";
		    } else {
				document.getElementById("player" + i + "currentTricks").innerHTML = game.trick.tricksWon[i];
		    }

	    	var imgToUpdate = document.getElementById("player" + i + "TrickCard");
	    	var trickElement = document.getElementById("player" + i + "TrickElement");
            var trickListToShow;
            
            if (game.showPreviousTrickCards) {
                trickListToShow = game.trick.previousCards;
            } else {
                trickListToShow = game.trick.cards;

                if (game.trick.previousCards) {
                    document.getElementById("player" + i + "PreviousTrickCard").src = "images/" + game.trick.previousCards[i] + ".png";
                    document.getElementById("player" + i + "PreviousTrickElement").style.visibility = "visible";
                }
            }
            
	      	if (!trickListToShow[i]) {
				trickElement.style.visibility = "hidden";
	        	imgToUpdate.src = "";
		    } else {
		    	imgToUpdate.src = "images/" + trickListToShow[i] + ".png";
		        trickElement.style.visibility = "visible";
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
				html = html + "<a href=\"javascript:bid(" + num + ");\"><img src=\"images/" + num + ".png\"/></a> ";
			}
			document.getElementById("bidUI").innerHTML = html;
            document.getElementById("trick").style.display = 'none';
            document.getElementById("bidUI").style.display = '';
		}
	
		// Trumps UI Update
		if (game.phase == 1) {
			// Trumps snippet
			var suits = new Array("SPADES", "HEARTS", "DIAMONDS", "CLUBS", "NO-TRUMPS");
			var html = "";
			for (suit in suits) {
				html = 	html + "<a href=\"javascript:setTrumps('" + suits[suit] + "');\"><img src=\"images/" + suits[suit] + ".png\"/></a> ";
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

    // Set the message string
    var message = document.getElementById("message");
    if (game.players.length > game.rounds[game.round.current].bids.length) {
        if (game.activePlayer == userId) {
            message.innerHTML = "Please select your bid";
        } else {
            message.innerHTML = game.players[game.activePlayer] + " is bidding";
        }
    } else if (game.rounds[game.round.current].trumps == null) {
        var highestBidder = game.rounds[game.round.current].highestBidder;
        if (game.activePlayer == userId) {
            message.innerHTML = "Please choose trumps";
        } else {
            message.innerHTML = game.players[game.activePlayer] + " is choosing trumps";
        }
    } else {
        // Must be in a trick
        if (game.activePlayer == userId) {
            message.innerHTML = "Please play a card";
        } else {
            message.innerHTML = game.players[game.activePlayer] + "'s turn to play a card";
        }        
    }
    message.style.visibility = 'visible';

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
   		game.round.current = output.round.idx;

	    updated.round = true;
	}
	if (output.trick) {
        if (output.trick.trickNum != game.trick.trickNum) {
            // Changed tricks, show previous trick briefly
            game.showPreviousTrickCards = true;
            timer.showPreviousTrickCards = setTimeout("showCurrentTrickCards()", 1000);
        }
        
	    game.trick = output.trick;
        
	    updated.trick = true;
	}

    // On round change update previous round
    if (output.previousRound) {
        game.rounds[output.previousRound.idx] = output.previousRound;
        updated.previousRound = true;
    }
    
	// Update the currently active player    
	game.activePlayer = output.activePlayer;
	    		    
    updateUI();
    if (userId != game.activePlayer) {
        setTimeout("xmlHttp['update'].call('{\"id\":' + game.id + ', \"phase\":' + game.phase + '}')", 500);
	}

}

function bid(bid) {
	// Clear the UI and record our bid
	document.getElementById("bidUI").style.display = 'none';
	document.getElementById("trick").style.display = '';
	document.getElementById("player" + userId + "currentBid").innerHTML = bid;
	document.getElementById("message").style.visibility = 'hidden';
	
	xmlHttp['bid'].call('{"id":' + game.id + ', "bid":' + bid + '}');
}
xmlHttp['bid'] = new JSONCallback();
xmlHttp['bid'].callback = function(output) {
	if (output.result == 0) {
		document.getElementById("bidUI").innerHTML = "";
		
		xmlHttp['update'].call('{\"id\":' + game.id + ', \"phase\":' + game.phase + '}');
	} else {
		// Show the ui again along with an error and clear our unacceptable bid
		document.getElementById("player" + userId + "currentBid").innerHTML = "";
        document.getElementById("trick").style.display = 'none';
		document.getElementById("bidUI").style.display = '';
		document.getElementById("message").style.visibility = 'visible';
		writeMessage(output.errorMessage);
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
		
		xmlHttp['update'].call('{\"id\":' + game.id + ', \"phase\":' + game.phase + '}');
	} else {
		// Show the ui again along with an error and clear our unacceptable trumps choice
		document.getElementById("currentTrumps").innerHTML = "";
        document.getElementById("trick").style.display = 'none';
		document.getElementById("trumpsUI").style.display = '';
		document.getElementById("message").innerHTML = "Please choose trumps";
		writeMessage(output.errorMessage);
	}
}

function playCard(card) {
    // If we are showing previous cards, stop
    if (timer.showPreviousTrickCards) {
        clearTimeout(timer.showPreviousTrickCards);
        showCurrentTrickCards();
    }
    
	// Remove the card from our hand and add it to the trick
	document.getElementById(card).style.display = 'none';
	document.getElementById("player" + userId + "TrickCard").src = "images/" + card + ".png";
	document.getElementById("player" + userId + "TrickElement").style.visibility = "visible";
	
	// Now remove the onClick handler
	removeHandOnClickHandler();

	xmlHttp['playCard'].call('{"id":' + game.id + ', "card":"' + card + '"}');
}
xmlHttp['playCard'] = new JSONCallback();
xmlHttp['playCard'].callback = function(output) {
	if (output.result == 0) {
		document.getElementById("hand").removeChild(document.getElementById(output.card));
		
		xmlHttp['update'].call('{\"id\":' + game.id + ', \"phase\":' + game.phase + '}');
	} else {
		// Show the ui again along with an error and clear our unacceptable trumps choice
		document.getElementById("player" + userId + "TrickElement").style.visibility = "hidden";
		document.getElementById("player" + userId + "TrickCard").src = "";
		document.getElementById(output.card).style.display = '';
		writeMessage(output.errorMessage);
		
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
		handHTML = handHTML + " <a href = \"javascript:playCard('" + cards[i].id + "');\">";
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
		handHTML = handHTML + " " + cardHTML(cards[i].id, cards[i].style.display);
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
    var pretty = "";
    
    if (string) {
        var stringWordArray = string.split("-");
        for (word in stringWordArray) {
            var prettyWord = stringWordArray[word].substr(0,1);
            var tmp = stringWordArray[word].substr(1);
            prettyWord = prettyWord.toUpperCase();
            prettyWord = prettyWord + tmp.toLowerCase();
            pretty = pretty + " " + prettyWord;
        }
    }
    
    return pretty;
}

function regulariseMessages() {
	var messages = document.getElementById("message").style.color = "black";
    
    timer.messages = null;
}

function writeMessage(message) {
    var messageId = document.getElementById("message");
    
    messageId.visibility = "hidden";
    messageId.style.color="#b91114";
    messageId.innerHTML = message;
    messageId.visibility = "visible";
        
    if (timer.messages != null) {
        clearTimeout(timer.messages);
    }
    timer.messages = setTimeout("regulariseMessages()", 1500);
}

function showCurrentTrickCards() {
    game.showPreviousTrickCards = false;
    timer.showPreviousTrickCards = null;
    updated.trick = true;
    updateUI();
}

function toggleStatsKey() {
    var statsKeyLink = document.getElementById("statsKeyLink");
    
    if (statsKeyLink.innerHTML == "Show Stats Key") {
        statsKeyLink.innerHTML = "Hide Stats Key";
        document.getElementById("stats").style.display = "none";
        document.getElementById("statsKey").style.display = "";
    } else {
        statsKeyLink.innerHTML = "Show Stats Key";
        document.getElementById("statsKey").style.display = "none";
        document.getElementById("stats").style.display = "";
    }
}