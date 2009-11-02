function onLoadEventHandler() {
	xmlHttp['gameStart'].call('{"id":' + game.id + ',\"phase\":-1}');
}

xmlHttp['gameStart'] = new JSONCallback();
xmlHttp['gameStart'].callback = function(output) {

	// Update game state based on JSON output
	game.phase = output.phase;

	if (output.players) {
		game.players = output.players;
	}

    // Players update
    updatePlayerUI();
	
    if (game.phase == -1) {
		setTimeout("xmlHttp['gameStart'].call('{\"id\":' + game.id + ',\"phase\":-1}')", 2000);
	} else {
        document.getElementById("trick").style.display = '';
		xmlHttp['update'].call('{\"id\":' + game.id + ', \"phase\":-1}');
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
    document.getElementById("scores").innerHTML = scoresTableHTML;    
    
    // Update Trick UI
    var trickHTML = "";
    for (var i = 0; i < game.players.length; i++) {
        trickHTML = trickHTML + "<div class=\"trickElement\"><p>" + game.players[i] + "</p>";
        trickHTML = trickHTML + "<img id=\"player" + i + "TrickCard\" src=\"\" height=\"96\" width=\"72\" style=\"visibility: hidden;\"/>"
        trickHTML = trickHTML + "</div>";
    }
    document.getElementById("trick").innerHTML = trickHTML;    
    trickHTML = "";
    for (var i = 0; i < game.players.length; i++) {
        trickHTML = trickHTML + "<img id=\"player" + i + "PreviousTrickCard\" src=\"\" height=\"64\" width=\"48\" style=\"visibility: hidden;\"/> "
    }
    document.getElementById("previousTrick").innerHTML = trickHTML;    

    // Update Final Scores UI
    var fsColumnCount = game.players.length * 2;
    var finalScoresHTML = "<table><th colspan=\"" + fsColumnCount + "\">Final Scores</th>";
    finalScoresHTML = finalScoresHTML + "<tr>";
    for (var i = 0; i < game.players.length; i++) {
        finalScoresHTML = finalScoresHTML + "<td>" + game.players[i] + "</td><td id=\"player" + i + "FinalScore\"></td>"
    }
    finalScoresHTML = finalScoresHTML + "</tr>";
    finalScoresHTML = finalScoresHTML + "</table>";
    document.getElementById("finalScores").innerHTML = finalScoresHTML;
}