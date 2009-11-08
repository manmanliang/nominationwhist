var xmlHttp = new Array();

function onLoadEventHandler() {
	xmlHttp['gameList'].call();
}

xmlHttp['gameList'] = new JSONCallback();
xmlHttp['gameList'].callback = function(output) {
    
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
        
        document.getElementById("runningGamesList").innerHTML = gameListHTML;
        document.getElementById("runningGames").style.display = "";
    } else {
        document.getElementById("runningGamesList").innerHTML = "";
        document.getElementById("runningGames").style.display = "none";
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

        document.getElementById("newGamesList").innerHTML = gameListHTML;
        document.getElementById("newGames").style.display = "";
    } else {
        document.getElementById("newGamesList").innerHTML = "";
        document.getElementById("newGames").style.display = "none";
    }
    
    setTimeout("xmlHttp['gameList'].call()", 2000);
}