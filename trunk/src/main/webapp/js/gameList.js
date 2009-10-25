var xmlHttp = new Array();

function onLoadEventHandler() {
	xmlHttp['gameList'].call();
}

xmlHttp['gameList'] = new JSONCallback();
xmlHttp['gameList'].callback = function(output) {
    
    var listLength = output.length;
    var gameListHTML = "";
    var player;
    
    for (i = 0; i < listLength; i++) {
        gameListHTML = gameListHTML + "<li>Game (" + output[i].creationDate + "):";
        for (player in output[i].players) {
            gameListHTML = gameListHTML + output[i].players[player] + " ";
        }
        gameListHTML = gameListHTML + "<form method=\"POST\" action=\"join-game\"><fieldset><legend>Join Game</legend>";
        gameListHTML = gameListHTML + "<input type=\"hidden\" name=\"id\" value=\"" + output[i].id + "\" />";
        gameListHTML = gameListHTML + "<input type=\"submit\" value=\"Join Game\"/></fieldset></form></li>";
    }
    document.getElementById("gameList").innerHTML = gameListHTML;
    
    setTimeout("xmlHttp['gameList'].call()", 2000);
}