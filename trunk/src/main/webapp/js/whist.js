var pollInterval = 300;
var gameState = new Object();
gameState.previousRound = new Object();
gameState.previousRound.bids = new Array();
gameState.previousRound.scores = new Array();
gameState.currentRound = new Object();
gameState.currentRound.bids = new Array();
gameState.currentRound.scores = new Array();
gameState.currentRound.playersPlayed = new Array();

var handXmlHttp;
var handTimer;
function handStateChange()
{
    if (handXmlHttp.readyState==4)
    {// 4 = "loaded"
        if (handXmlHttp.status==200)
        {// 200 = OK
            try {
                var hand = JSON.parse(handXmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + handXmlHttp.responseText); 
            }
            
            if (hand.length > 0) {
                var imgHTML = "";
                for (card in hand) {
                    imgHTML = imgHTML + "<img src = \"images/" + hand[card] + ".png"\" alt=\"hand[card]\"/>"
                }
                document.getElementById("handDiv").innerHTML = imgHTML;
                gameState.cardCount = hand.length;
                stateChange();
            } else {
                handTimer = setTimeout(dataRefresh("hand", pollInterval));
            }
        }
        else
        {
            document.getElementById("handDiv").innerHTML = "<p>" + handXmlHttp.status + ": Data is out of data. Updating, please wait...</p>";
        }
    }
}

var trickXmlHttp;
function trickStateChange()
{
    if (trickXmlHttp.readyState==4)
    {// 4 = "loaded"
        if (trickXmlHttp.status==200)
        {// 200 = OK
            try {
                var trick = JSON.parse(trickXmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + trickXmlHttp.responseText); 
            }

            // Add the images for the ones we have a value for
            var imgHTML = "";
            for (var i=0; i < trick.length; i++) {
            	if (trick[i] != null) {
                    var imgToUpdate = document.getElementById("player" + i);
                    imgHTML = "images/" + trick[i] + ".png";
	                imgToUpdate.src = imgHTML;
                    imgToUpdate.style.visibility = "visible";
                }
            }

            for (var i = 0; i < ${fn:length(game.players)}; i++) {
                var imgToUpdate = document.getElementById("player" + i);
            	if (!trick[i]) {
		           	imgToUpdate.style.visibility = "hidden";
        	        imgToUpdate.src = "";
	            	gameState.currentRound.playersPlayed[i] = "";
	            } else {
	                imgToUpdate.src = "images/" + trick[i] + ".png";
                    imgToUpdate.style.visibility = "visible";
	            	gameState.currentRound.playersPlayed[i] = trick[i];
	            }
            }
           	
            if (trick.length == ${fn:length(game.players)} ||
            	gameState.currentRound.playerToPlay == ${user}) {
                stateChange();
            } else {
                trickTimer = setTimeout(dataRefresh("trick", pollInterval));
            }
        }
        else
        {
            document.getElementById("trickDiv").innerHTML = "<p>" + trickXmlHttp.status + ": Data is out of data. Updating, please wait...</p>";
        }
    }
}
        
var scoresXmlHttp;
var scoresTimer;
function scoresStateChange()
{
    if (scoresXmlHttp.readyState==4)
    {// 4 = "loaded"
        if (scoresXmlHttp.status==200)
        {// 200 = OK
            try {
                var scores = JSON.parse(scoresXmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + scoresXmlHttp.responseText); 
            }

            document.getElementById("scoresTestBaseString").innerHTML = scoresXmlHttp.responseText;
            
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
            gameState.currentRound.bidWinner = scores.currentRound.bidWinner;
            gameState.currentRound.playerTurn = scores.currentRound.playerTurn;
            gameState.gameFinished = scores.gameFinished;
            
            
            if ((numberOfBids != ${fn:length(game.players)} && playerToBid != ${user}) ||
                (numberOfBids == ${fn:length(game.players)} && scores.currentRound.trumps == "" && scores.currentRound.bidWinner != ${user})) {
                scoresTimer = setTimeout(dataRefresh("scores", pollInterval));
            } else {
            	stateChange();
            }
        }
        else
        {
            document.getElementById("scoresDiv").innerHTML = "<p>" + scoresXmlHttp.status + ": Data is out of data. Updating, please wait...</p>";
        }
    }
}

var bidSetXmlHttp;
function bidSetStateChange()
{
    if (bidSetXmlHttp.readyState==4)
    {// 4 = "loaded"
        if (bidSetXmlHttp.status==200)
        {// 200 = OK
            try {
                var bidResponse = JSON.parse(bidSetXmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + bidSetXmlHttp.responseText); 
            }
        }
        else
        {
        }
    }
}

var trumpsSetXmlHttp;
function trumpsSetStateChange()
{
    if (trumpsSetXmlHttp.readyState==4)
    {// 4 = "loaded"
        if (trumpsSetXmlHttp.status==200)
        {// 200 = OK
            try {
                var trumpsResponse = JSON.parse(trumpsSetXmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + trumpsSetXmlHttp.responseText); 
            }
        }
        else
        {
        }
    }
}

var playCardXmlHttp;
function playCardStateChange()
{
    if (playCardXmlHttp.readyState==4)
    {// 4 = "loaded"
        if (playCardXmlHttp.status==200)
        {// 200 = OK
            try {
                var playCardResponse = JSON.parse(playCardXmlHttp.responseText);
            } catch (e) {
                alert("An exception occurred in the script. Error name: " + e.name 
                  + ". Error message: " + e.message + ". Response was " + playCardXmlHttp.responseText); 
            }
        }
        else
        {
        }
    }
}

function showBidUI()
{
	var html = "";
	<c:forEach var="bid" begin="0" end="gameState.currentRound.numberOfState">
		html = html + "<a href=\"javascript:AJAXPost(bidSetStateChange, '<c:url value="/bid"/>', '{&quot;id&quot;:${game.id}, &quot;bid&quot;:${bid}}');\">${bid}</a>"
 	</c:forEach>
	document.getElementById("bidsDiv").innerHTML = html;
}

function showSetTrumpUI()
{
	var suits = new Array("SPADES", "HEARTS", "DIAMONDS", "CLUBS");
	var html = "";
	for (suit in suits) {
		html = 	html + "<a href=\"javascript:AJAXPost(trumpsSetStateChange,
											   		  '<c:url value="/set-trumps"/>', 
											   		  '{&quot;id&quot;:${game.id}, 
											   		  &quot;trumps&quot;:&quot;suit&quot;}');\">suit</a>";
	}
	document.getElementById("setTrumpsDiv").innerHTML = html;
}

function allowCardPlay()
{
	var cards = document.getElementById("handDiv").getElementByTagName("img");
	
	for (card in cards) {
		card.onClick = "AJAXPost(playCardStateChange,
								 '<c:url value="/play-card"/>', 
								 '{&quot;id&quot;:${game.id}, 
								 &quot;card&quot;:&quot;card.alt&quot;}');";
	}
}

function stopCardPlay()
{
	var cards = document.getElementById("handDiv").getElementByTagName("img");
	
	for (card in cards) {
		card.onClick = "";
	}
}

function dataRefresh(type)
{
    if (type == "hand") {
        handXmlHttp = AJAXGet(handStateChange, '<c:url value="/hand?id=${param.id}"/>');
    } else if (type == "trick") {
        trickXmlHttp = AJAXGet(trickStateChange, '<c:url value="/trick?id=${param.id}"/>');
    } else if (type == "scores") {
        scoresXmlHttp = AJAXGet(scoresStateChange, '<c:url value="/score?id=$param.id}"/>');
    }
}

function stateChange()
{
	// Cancel all polls
	if (handTimer) { clearTimeout(handTimer); }
	if (trickTimer) { clearTimeout(trickTimer); }
	if (scoresTimer) { clearTimeout(scoresTimer); }

	// See if the game has finished
	if (gameState.finished) {
		return;
	}
		
    // Check if we have a hand
    if (gameState.cardCount == 0) {
        dataRefresh("hand");
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
}