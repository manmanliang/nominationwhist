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

    // Players update
    updatePlayerUI();
	
    if (game.phase == -1) {
        setTimeout("gameStartCall()", gameStartUpdateDelay);
	} else {
        $("#trick").show();
        updateCall(-1);
	}
}

function updatePlayerUI() {    
    // Update Scores UI
    var scoresTableHTML = "<table id=\"scoresTable\"><tr><th rowspan=\"2\">Cards</th><th rowspan=\"2\">Trumps</th>";
    for (var i = 0; i < game.players.length; i++) {
        scoresTableHTML = scoresTableHTML + "<th id=\"player" + i + "ScoreColumn\" colspan=\"3\">" + game.players[i] + "</th>";
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
        trickHTML = trickHTML + "\"><tr><td>" + game.players[i] + "</td></tr>";
        trickHTML = trickHTML + "<tr><td><img id=\"player" + i + "TrickCard\" src=\"\" height=\"96\" width=\"72\"/></td></tr>";
        trickHTML = trickHTML + "</table>";
    }
    $("#trick").html(trickHTML);
    trickHTML = "";
    for (var i = 0; i < game.players.length; i++) {
        trickHTML = trickHTML + "<table class=\"trickElement\" id=\"player" + i + "PreviousTrickElement\" style=\"visibility: hidden;\">";
        trickHTML = trickHTML + "<tr><td><img id=\"player" + i + "PreviousTrickCard\" src=\"\" height=\"64\" width=\"48\"/></td></tr>";
        trickHTML = trickHTML + "<tr><td>" + game.players[i] + "</td></tr></table>";
    }
    $("#previousTrick").html(trickHTML);

    // Update Final Scores UI
    var fsColumnCount = game.players.length * 2;
    var finalScoresHTML = "<table><th colspan=\"" + fsColumnCount + "\">Final Scores</th>";
    finalScoresHTML = finalScoresHTML + "<tr>";
    for (var i = 0; i < game.players.length; i++) {
        finalScoresHTML = finalScoresHTML + "<td>" + game.players[i] + "</td><td id=\"player" + i + "FinalScore\"></td>"
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
        playerStatsHTML = playerStatsHTML + "<tr><td colspan=\"" + statTypes.length + "\">" + game.players[i] + "</td></tr>"
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
}

function getCardElementPositions() {
    var positions = new Array();
    
    switch(game.players.length) {
    case 2:
        positions[userId] = "left: 47%; bottom: 2px;";
        positions[(userId + 1) % 2] = "left: 47%; top: 2px;";
        break;
    case 3:
        positions[userId] = "left: 47%; bottom: 2px;";
        positions[(userId + 1) % 3] = "left: 2px; top: 75px;";
        positions[(userId + 2) % 3] = "right: 2px; top: 75px;";
        break;
    case 4:
        positions[userId] = "left: 47%; bottom: 2px;";
        positions[(userId + 1) % 4] = "left: 2px; top: 125px;";
        positions[(userId + 2) % 4] = "left: 47%; top: 2px;";
        positions[(userId + 3) % 4] = "right: 2px; top: 125px;";
    }
    
    return positions;
}