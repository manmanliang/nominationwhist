function onLoadEventHandler() {
    gameStartCall();
}

function gameStartCallback(output) {

	// Update game state based on JSON output
	game.phase = output.phase;

	if (output.players) {
		game.players = output.players;
        game.playersStats = output.playersStats;
	}

    game.creator = output.creator;
    game.availCompPlayers = output.availCompPlayers;
    
    // Players update
    updatePlayerUI();
	
    if (!timer.gameStartPollLock) {
        if (game.phase == -1) {
            timer.gameStartPoll = setTimeout("gameStartCall()", gameStartUpdateDelay);
        } else {
            $("#trick").show();
            updateCall(-1);
        }
    }
}

function updatePlayerUI() {    
    // Update Scores UI
    var scoresTableHTML = "<table id=\"scoresTable\"><tr><th rowspan=\"2\">Cards</th><th rowspan=\"2\">Trumps</th>";
    for (var i = 0; i < game.players.length; i++) {
        scoresTableHTML = scoresTableHTML + "<th id=\"player" + i + "ScoreColumn\" colspan=\"3\">" + game.players[i].shortName + "</th>";
    }
    scoresTableHTML = scoresTableHTML + "</tr><tr>";
    for (var i = 0; i < game.players.length; i++) {
        scoresTableHTML = scoresTableHTML + "<th>Bid</th><th>Won</th><th>Score</th>";
    }
    scoresTableHTML = scoresTableHTML + "</tr>";
    var rowTypes = new Array("prev", "current");
    for (rowType in rowTypes) {
        scoresTableHTML = scoresTableHTML + "<tr>";
        scoresTableHTML = scoresTableHTML + "<td id=\"" + rowTypes[rowType] + "Cards\">&nbsp;</td>";
        scoresTableHTML = scoresTableHTML + "<td id=\"" + rowTypes[rowType] + "Trumps\"></td>";
        for (var i = 0; i < game.players.length; i++) {
            scoresTableHTML = scoresTableHTML + "<td id=\"player" + i + rowTypes[rowType] + "Bid\"></td>";
            scoresTableHTML = scoresTableHTML + "<td id=\"player" + i + rowTypes[rowType] + "Tricks\"></td>";
            scoresTableHTML = scoresTableHTML + "<td id=\"player" + i + rowTypes[rowType] + "Score\"></td>";
        }
        scoresTableHTML = scoresTableHTML + "</tr>";
    }
    scoresTableHTML = scoresTableHTML + "</table>";
    $("#scores").html(scoresTableHTML);
    
    // Update Trick UI
    var trickHTML = "";
    var positions = getCardElementPositions();
    for (var i = 0; i < game.players.length; i++) {
        trickHTML = trickHTML + "<table class=\"trickElement\" id=\"player" + i + "TrickElement\" style=\"visibility: hidden;";
        if (positions) {
            trickHTML = trickHTML + " position: absolute; " + positions[i];
        }
        trickHTML = trickHTML + "\"><tr><td>" + game.players[i].shortName + "</td></tr>";
        trickHTML = trickHTML + "<tr><td><img id=\"player" + i + "TrickCard\" src=\"\" height=\"96\" width=\"72\"/></td></tr>";
        trickHTML = trickHTML + "</table>";
    }
    $("#trick").html(trickHTML);
    trickHTML = "";
    for (var i = 0; i < game.players.length; i++) {
        trickHTML = trickHTML + "<table class=\"trickElement\" id=\"player" + i + "PreviousTrickElement\" style=\"visibility: hidden;\">";
        trickHTML = trickHTML + "<tr><td><img id=\"player" + i + "PreviousTrickCard\" src=\"\" height=\"64\" width=\"48\"/></td></tr>";
        trickHTML = trickHTML + "<tr><td>" + game.players[i].shortName + "</td></tr></table>";
    }
    $("#previousTrick").html(trickHTML);

    // Update Final Scores UI
    var fsColumnCount = game.players.length * 2;
    var finalScoresHTML = "<table><th colspan=\"" + fsColumnCount + "\">Final Scores</th>";
    finalScoresHTML = finalScoresHTML + "<tr>";
    for (var i = 0; i < game.players.length; i++) {
        finalScoresHTML = finalScoresHTML + "<td>" + game.players[i].shortName + "</td><td id=\"player" + i + "FinalScore\"></td>"
    }
    finalScoresHTML = finalScoresHTML + "</tr>";
    finalScoresHTML = finalScoresHTML + "</table>";
    $("#finalScores").html(finalScoresHTML);
    
    // Update Player Stats UI
    var statTypes = new Array("win", "correctBid", "favBid", "favTrumps");
    var percentStatTypes = new Array("win", "correctBid");

    var playerStatsHTML = "<table><tr>";
    var playerStats;
    for (i in statTypes) {
        playerStatsHTML = playerStatsHTML + "<td><img src=\"" + imagesDir + statTypes[i] + ".png\"/></td>";
    }
    playerStatsHTML = playerStatsHTML + "</tr>";
    
    var playerStatValue;
    for (var i = 0; i < game.players.length; i++) {
        playerStatsHTML = playerStatsHTML + "<tr><td colspan=\"" + statTypes.length + "\">" + game.players[i].shortName + "</td></tr>"
        playerStatsHTML = playerStatsHTML + "<tr>";
        playerStats = game.playersStats[i];
        
        for (j in statTypes) {
            if (playerStats[statTypes[j]] == null) {
                playerStatValue = "-";
            } else {
                playerStatValue = (typeof playerStats[statTypes[j]] == 'string') ? playerStats[statTypes[j]].toTitleCase() : playerStats[statTypes[j]];
                for (k in percentStatTypes) {
                    if (statTypes[j] == percentStatTypes[k]) {
                        playerStatValue = playerStatValue + "%";
                        break;
                    }
                }
            }
            playerStatsHTML = playerStatsHTML + "<td id=\"player" + i + statTypes[j] + "Element\">" + playerStatValue + "</td>";
        }
        playerStatsHTML = playerStatsHTML + "</tr>";
    }
    playerStatsHTML = playerStatsHTML + "</table>";
    $("#stats").html(playerStatsHTML);
    
    // Update Game Creator UI
    if (game.creator.id == userId) {
        // Update Player Order UI
        var playerOrderHTML = "<ol id=\"playerOrderList\">";
        for (var i = 0; i < game.players.length; i++) {
            playerOrderHTML = playerOrderHTML + "<li id=\"" + game.players[i].id 
                                + "\">" + game.players[i].shortName;
            playerOrderHTML = playerOrderHTML + "<a href=\"javascript:removePlayer(" + game.players[i].id + ");\"><img src=\"" + imagesDir + "remove.png\"/></a>";
            playerOrderHTML = playerOrderHTML + "</li>";
        }
        playerOrderHTML = playerOrderHTML + "</ol>";
        $("#playerOrder").html(playerOrderHTML);
        
        // Update Computers Available UI
        var availableComputerPlayersHTML = "<ul id=\"availCompPlayersList\">";
        for (var i = 0; i < game.availCompPlayers.length; i++) {
            availableComputerPlayersHTML = availableComputerPlayersHTML + "<li class=\"computerPlayer\" id=\"" + game.availCompPlayers[i].id 
                                            + "\">" + game.availCompPlayers[i].shortName + "</li>";
        }
        availableComputerPlayersHTML = availableComputerPlayersHTML + "</ul>";
        $("#availableComputerPlayers").html(availableComputerPlayersHTML);
        $("#availableComputerPlayers li").click(addComputerPlayer);
    }
}

function getCardElementPositions() {
    var positions = new Array();
    
    switch(game.players.length) {
    case 2:
        positions[userIndex] = "left: 47%; bottom: 2px;";
        positions[(userIndex + 1) % 2] = "left: 47%; top: 2px;";
        break;
    case 3:
        positions[userIndex] = "left: 47%; bottom: 2px;";
        positions[(userIndex + 1) % 3] = "left: 2px; top: 75px;";
        positions[(userIndex + 2) % 3] = "right: 2px; top: 75px;";
        break;
    case 4:
        positions[userIndex] = "left: 47%; bottom: 2px;";
        positions[(userIndex + 1) % 4] = "left: 2px; top: 125px;";
        positions[(userIndex + 2) % 4] = "left: 47%; top: 2px;";
        positions[(userIndex + 3) % 4] = "right: 2px; top: 125px;";
    }
    
    return positions;
}

function addComputerPlayer() {    
    // Stop game start poll
    timer.gameStartPollLock = true;
    clearTimeout(timer.gameStartPoll);
    
	// Remove the available player and add it to the game
	$(this).hide();
	$("#playerOrderList").append($(this).html());
    
	$.ajax({url: 'addPlayer', data: JSON.stringify({id:game.id, player:parseInt($(this).attr("id"))}),
            success: addComputerCallback, progressAction: "Adding Player", playerId: parseInt($(this).attr("id"))});
}
function addComputerCallback(output) {
	if (output.result == 0) {
        $("#availableComputerPlayers #" + this.playerId).remove();
	} else {
		// Show the ui again with error and re-add computer player
		$("#availableComputerPlayers #" + this.playerId).show();
        $("#playerOrderList #" + this.playerId).remove();
		writeMessage(output.errorMessage);
	}
    timer.gameStartPollLock = false;
    timer.gameStartPoll = setTimeout("gameStartCall()", gameStartUpdateDelay);
}

function removePlayer() {
    // Stop game start poll
    timer.gameStartPollLock = true;
    clearTimeout(timer.gameStartPoll);

	// Remove the available player and add it to the game
	$(this).hide();
    if ($(this).hasClass("computerPlayer")) {
        $("#availableComputerPlayers").append($(this).html());
    }

	$.ajax({url: 'removePlayer', data: JSON.stringify({id:game.id, player:parseInt($(this).attr("id"))}),
            success: removePlayerCallback, progressAction: "Removing Player", playerId: parseInt($(this).attr("id"))});
}
function removePlayerCallback(output) {
	if (output.result == 0) {
        $("#playerOrderList #" + this.playerId).remove();
	} else {
		// Show the ui again with error and re-add computer player
		$("#playerOrderList #" + this.playerId).show();
        if ($("#playerOrderList #" + this.playerId).hasClass("computerPlayer")) {
            $("#availableComputerPlayers #" + this.playerId).remove();
        }
		writeMessage(output.errorMessage);
	}
    timer.gameStartPollLock = false;
    timer.gameStartPoll = setTimeout("gameStartCall()", gameStartUpdateDelay);
}