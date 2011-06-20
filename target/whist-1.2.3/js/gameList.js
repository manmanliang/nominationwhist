function onLoadEventHandler() {
	gameListCall();
}

function gameListCallback(output) {
    
    var gameListHTML;
    var player;

    var listLength;

    if (output.runningGames) {
        listLength = output.runningGames.length;
        
        gameListHTML = "<ul>";
        for (i = 0; i < listLength; i++) {
            gameListHTML = gameListHTML + "<li>Game (" + output.runningGames[i].creationDate + ") Players: ";
            for (player in output.runningGames[i].players) {
                gameListHTML = gameListHTML + output.runningGames[i].players[player] + " ";
            }
            
            gameListHTML = gameListHTML + "- ";
            if (output.runningGames[i].roundNum == 0) {
                gameListHTML = gameListHTML + "Not Started";
            } else {
                gameListHTML = gameListHTML + "Round " + output.runningGames[i].roundNum;
            }
            
            gameListHTML = gameListHTML + "&nbsp;&nbsp;";
            gameListHTML = gameListHTML + "<form method=\"POST\" action=\"join-game\">";
            gameListHTML = gameListHTML + "<input type=\"hidden\" name=\"id\" value=\"" + output.runningGames[i].id + "\" />";
            gameListHTML = gameListHTML + "<input type=\"submit\" value=\"Rejoin Game\"/></form>";
            gameListHTML = gameListHTML + "<form method=\"POST\" action=\"delete-game\">";
            gameListHTML = gameListHTML + "<input type=\"hidden\" name=\"id\" value=\"" + output.runningGames[i].id + "\" />";
            gameListHTML = gameListHTML + "<input type=\"submit\" value=\"Delete Game\"/></form>";
            gameListHTML = gameListHTML + "</li>";
        }
        gameListHTML = gameListHTML + "</ul>";
        
        $("#runningGamesList").html(gameListHTML);
        $("#runningGames").show();
    } else {
        $("#runningGamesList").empty();
        $("#runningGames").hide();
    }
        
    if (output.newGames) {
        listLength = output.newGames.length;
        
        gameListHTML = "<ul>";
        for (i = 0; i < listLength; i++) {
            gameListHTML = gameListHTML + "<li>Game (" + output.newGames[i].creationDate + "):";
            for (player in output.newGames[i].players) {
                gameListHTML = gameListHTML + output.newGames[i].players[player] + " ";
            }

            gameListHTML = gameListHTML + "- ";
            if (output.newGames[i].roundNum == 0) {
                gameListHTML = gameListHTML + "Not Started";
            } else {
                gameListHTML = gameListHTML + "Round " + output.newGames[i].roundNum;
            }
            
            gameListHTML = gameListHTML + "&nbsp;&nbsp;";
            gameListHTML = gameListHTML + "<form method=\"POST\" action=\"join-game\">";
            gameListHTML = gameListHTML + "<input type=\"hidden\" name=\"id\" value=\"" + output.newGames[i].id + "\" />";
            gameListHTML = gameListHTML + "<input type=\"submit\" value=\"Join Game\"/></form>";
            gameListHTML = gameListHTML + "</li>";
        }
        gameListHTML = gameListHTML + "</ul>";

        $("#newGamesList").html(gameListHTML);
        $("#newGames").show();
    } else {
        $("#newGamesList").empty();
        $("#newGames").hide();
    }
    
    setTimeout("gameListCall()", gameListUpdateDelay);
}