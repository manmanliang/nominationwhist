// Acts in response to the game state being
// updated by an ajax request
function gameUpdatedEventHandler() {
	// Update any UI components whose backing data has been 
	// marked updated
	updateUI();
	
	if (userId != game.activePlayer) {
        setTimeout("updateCall(game.phase)", gameStateUpdateDelay);
	}
}

// Inspect the game data 'updated' flags to determine which
// Game Information components must be re-rendered.
function updateGameState() {
    // Active Player Update
    if (game.activePlayer != null) {
        for (var i = 0; i < game.players.length; i++) {
            $("#player" + i + "ScoreColumn").removeClass();            
        }
        $("#player" + game.activePlayer + "ScoreColumn").addClass('active');
    }

    // Hand update
    if (updated.hand) {
        var imgHTML = "";
        for (card in game.hand) {
            // Use title attr to store the unique name of this card for passing to the server
        	imgHTML = imgHTML + " " + 
                      "<img id = \"" + game.hand[card] + 
                      "\" title = \"" + game.hand[card] + 
                      "\" src = \"" + imagesDir + game.hand[card] + ".png\"/>";
        }
        $("#hand").html(imgHTML);
       	updated.hand = false;
	}
	        
	// Round update
	if (updated.round) {
		var idx = game.round.current;
		
	    for (i = 0; i < game.players.length; i++) {
            $("#player" + i + "currentBid").text((game.rounds[idx].bids[i] != null) ? game.rounds[idx].bids[i] : "");
	    }
        $("#currentTrumps").text((game.rounds[idx].trumps) ? game.rounds[idx].trumps.toTitleCase() : "");
        $("#currentCards").text(game.rounds[idx].numberOfCards);

		updated.round = false;
	}
    // Previous Round update
    if (updated.previousRound) {
        if (game.rounds[game.round.current - 1]) {
            var idx = game.round.current - 1;
            
            for (i = 0; i < game.players.length; i++) {
                $("#player" + i + "prevBid").text(game.rounds[idx].bids[i]);
                $("#player" + i + "prevTricks").text(game.rounds[idx].tricksWon[i]);
                $("#player" + i + "prevScore").text(game.rounds[idx].scores[i]);
            }
            $("#prevTrumps").text(game.rounds[idx].trumps.toTitleCase());
            $("#prevCards").text(game.rounds[idx].numberOfCards);
        }
		updated.previousRound = false;
    }

    // Trick update before game end check
    if (updated.trick) {
	    for (var i = 0; i < game.players.length; i++) {
		   	if (!game.trick.tricksWon[i]) {
		    	$("#player" + i + "currentTricks").text("");
		    } else {
				$("#player" + i + "currentTricks").text(game.trick.tricksWon[i]);
		    }
        }
    }

	// Game finished update
	if (game.phase == 3) {
		// Game is finished, clear board and print final scores
        $("#trick").hide();
        $("#message").text("Game finished");

	    for (var i = 0; i < game.players.length; i++) {
			$("#player" + i + "FinalScore").text(game.rounds[game.round.current].scores[i]);
            $("#player" + i + "currentScore").text(game.rounds[game.round.current].scores[i]);
		}
        $("#finalScores").show();

       	// we're done set current player to us so we don't do any more polling and return
       	game.activePlayer = userId;
        return
    }
	
    // Trick update after game end check
    if (updated.trick) {
	    for (var i = 0; i < game.players.length; i++) {
            var trickListToShow;
            
            if (game.showPreviousTrickCards) {
                trickListToShow = game.trick.previousCards;
            } else {
                trickListToShow = game.trick.cards;

                if (game.trick.previousCards) {
                    $("#player" + i + "PreviousTrickCard").attr('src', imagesDir + game.trick.previousCards[i] + '.png');
                    $("#player" + i + "PreviousTrickElement").css('visibility', 'visible');
                }
            }
            
	      	if (!trickListToShow[i]) {
				$("#player" + i + "TrickElement").css('visibility', 'hidden');
	        	$("#player" + i + "TrickCard").attr('src', '');
		    } else {
		    	$("#player" + i + "TrickCard").attr('src', imagesDir + trickListToShow[i] + '.png');
		        $("#player" + i + "TrickElement").css('visibility', 'visible');
		    }
	    }
	   	updated.trick = false;
	}

    // Set the message string
    var bidsToBeMade = false;
    for (var i = 0; i < game.players.length; i++) {
        if (game.rounds[game.round.current].bids[i] == null) {
            bidsToBeMade = true;
            break;
        }
    }

    if (bidsToBeMade) {
        if (game.activePlayer == userId) {
            $("#message").text("Please select your bid");
        } else {
            $("#message").text(game.players[game.activePlayer] + " is bidding");
        }
    } else if (game.rounds[game.round.current].trumps == null) {
        var highestBidder = game.rounds[game.round.current].highestBidder;
        if (game.activePlayer == userId) {
            $("#message").text("Please choose trumps");
        } else {
            $("#message").text(game.players[game.activePlayer] + " is choosing trumps");
        }
    } else {
        // Must be in a trick
        if (game.activePlayer == userId) {
            $("#message").text("Please play a card");
        } else {
            $("#message").text(game.players[game.activePlayer] + "'s turn to play a card");
        }        
    }
    $("#message").css('visibility', 'visible');

}

function updateUI() {
	// Check UIs to update if we are the current player
	if (userId == game.activePlayer) {	
		// Bid UI Update
		if (game.phase == 0) {
			var html = "";
			for (var num = 0; num <= game.rounds[game.round.current].numberOfCards; num++) {
				html = html + "<a href=\"javascript:bid(" + num + ");\"><img src=\"" + imagesDir + num + ".png\"/></a> ";
			}
			$("#bidUI").html(html);
            $("#trick").hide();
            $("#bidUI").show();
		}
	
		// Trumps UI Update
		if (game.phase == 1) {
			// Trumps snippet
			var suits = new Array("SPADES", "HEARTS", "DIAMONDS", "CLUBS", "NO-TRUMPS");
			var html = "";
			for (suit in suits) {
				html = 	html + "<a href=\"javascript:setTrumps('" + suits[suit] + "');\"><img src=\"" + imagesDir + suits[suit] + ".png\"/></a> ";
			}
			$("#trumpsUI").html(html);
            $("#trick").hide();
            $("#trumpsUI").show();
		}

		// Play card UI Update
		if (game.phase == 2) {
			$("#hand img").click(playCard).css('cursor', 'pointer');
		}
	}
}

function updateCallback(output) {
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
            timer.showPreviousTrickCards = setTimeout("showCurrentTrickCards()", showCurrentTrickCardsDelay);
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
	    		    
    updateGameState();
    gameUpdatedEventHandler();
}

function bid(bid) {
	// Clear the UI and record our bid
	$("#bidUI").hide();
	$("#trick").show();
	$("#player" + userId + "currentBid").text(bid);
	$("#message").css('visibility', 'hidden');
	
	$.ajax({url: 'bid', data: JSON.stringify({id:game.id, bid:bid}), 
            success: bidCallback, progressAction: "Sending bid"});
}
function bidCallback(output) {
	if (output.result == 0) {
		$("#bidUI").text("");

		updateCall(game.phase);
	} else {
		// Show the ui again along with an error and clear our unacceptable bid
		$("#player" + userId + "currentBid").text("");
        $("#trick").hide();
		$("#bidUI").show();
		$("#message").css('visibility', 'visible');
		writeMessage(output.errorMessage);
	}
}

function setTrumps(trumps) {
	// Clear the UI and record our trumps choice
	$("#trumpsUI").hide();
	$("#trick").show();
	$("#currentTrumps").text(trumps.toTitleCase());
    
    $.ajax({url: 'trumps', data: JSON.stringify({id:game.id, trumps:trumps}),
            success: setTrumpsCallback, progressAction: "Sending trumps"});
}
function setTrumpsCallback(output) {
	if (output.result == 0) {
		$("#trumpsUI").text("");
		
		updateCall(game.phase);
	} else {
		// Show the ui again along with an error and clear our unacceptable trumps choice
		$("#currentTrumps").text("");
        $("#trick").hide();
		$("#trumpsUI").show();
		$("#message").text("Please choose trumps");
		writeMessage(output.errorMessage);
	}
}

function playCard() {
	// Now remove the onClick handler
	$("#hand img").unbind('click', playCard).css('cursor', 'default');

    // If we are showing previous cards, stop
    if (timer.showPreviousTrickCards) {
        clearTimeout(timer.showPreviousTrickCards);
        showCurrentTrickCards();
    }
    
	// Remove the card from our hand and add it to the trick
	$(this).hide();
	$("#player" + userId + "TrickCard").attr('src', $(this).attr('src'));
	$("#player" + userId + "TrickElement").css('visibility', 'visible');
    
    game.playCardAttempt = $(this).attr("title");
	
	$.ajax({url: 'playCard', data: JSON.stringify({id:game.id, card:$(this).attr("title")}),
            success: playCardCallback, progressAction: "Playing Card"});
}
function playCardCallback(output) {
	if (output.result == 0) {
        $("#" + output.card).remove();
		
		updateCall(game.phase);
	} else {
		// Show the ui again with error and put the card back into our hand from the trick
		$("#player" + userId + "TrickElement").css('visibility', 'hidden');
		$("#player" + userId + "TrickCard").attr('src', '');
		$("#" + game.playCardAttempt).show();
		writeMessage(output.errorMessage);
		
		// Re-instate onclick handler
		$("#hand img").click(playCard).css('cursor', 'pointer');
	}
}

function regulariseMessages() {
	var messages = $("#message").css('color', 'black');
    
    timer.messages = null;
}

function writeMessage(message) {
    $("#message").css('visibility', 'hidden');
    $("#message").css('color', '#b91114');
    $("#message").text(message);
    $("#message").css('visibility', 'visible');
        
    if (timer.messages != null) {
        clearTimeout(timer.messages);
    }
    timer.messages = setTimeout("regulariseMessages()", regulariseMessagesDelay);
}

function showCurrentTrickCards() {
    game.showPreviousTrickCards = false;
    timer.showPreviousTrickCards = null;
    updated.trick = true;
    updateGameState();
}

function toggleStatsKey() {
    if ($("#statsKeyLink").text() == "Show Stats Key") {
        $("#statsKeyLink").text("Hide Stats Key");
        $("#stats").hide();
        $("#statsKey").show();
    } else {
        $("#statsKeyLink").text("Show Stats Key");
        $("#statsKey").hide();
        $("#stats").show();
    }
}

String.prototype.toTitleCase = function() {
    return this.replace(/([\w&`'‘’"“.@:\/\{\(\[<>_]+-? *)/g, function(match, p1, index, title) {
        if (index > 0 && title.charAt(index - 2) !== ":" &&
        	match.search(/^(a(nd?|s|t)?|b(ut|y)|en|for|i[fn]|o[fnr]|t(he|o)|vs?\.?|via)[ \-]/i) > -1)
            return match.toLowerCase();
        if (title.substring(index - 1, index + 1).search(/['"_{(\[]/) > -1)
            return match.charAt(0) + match.charAt(1).toUpperCase() + match.substr(2);
        if (title.substring(index - 1, index + 1).search(/[\])}]/) > -1)
            return match;
        return match.charAt(0).toUpperCase() + match.substr(1).toLowerCase();
    });
};