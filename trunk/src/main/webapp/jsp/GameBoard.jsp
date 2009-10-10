<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/xmlhttp.js"/>"></script>
    <script type="text/javascript">
      	var handXmlHttp;
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
                    var imgHTML = "";
                    for (card in hand) {
                        imgHTML = imgHTML + "<img src = \"images/" + hand[card] + ".png\"/>"
                    }
                    document.getElementById("handDiv").innerHTML = imgHTML;
                }
                else
                {
                    document.getElementById("handDiv").innerHTML = "<p>" + handXmlHttp.status + ": Data is out of data. Updating, please wait...</p>";
                }
            }
        }
    </script>    
    <script type="text/javascript">
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

                    // Clear out the old images
                    var trickImgs = document.getElementById("trickDiv").getElementsByTagName("img");
                    for (var i=0; i < trickImgs.length; i++) {
                    	trickImgs[i].style.visibility = "hidden";
                        trickImgs[i].src = "";
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
                }
                else
                {
                    document.getElementById("trickDiv").innerHTML = "<p>" + trickXmlHttp.status + ": Data is out of data. Updating, please wait...</p>";
                }
            }
        }
    </script>
    <script type="text/javascript">
        function dataRefresh()
        {
            handXmlHttp = loadXMLDoc(handStateChange, '<c:url value="/hand"/>');
            trickXmlHttp = loadXMLDoc(trickStateChange, '<c:url value="/trick"/>');
        }
    </script>    
    <style>
    	body { background: #008000; }
    </style>
  </head>
  <body onload="dataRefresh();">    
    <h1>Lets Play Nommy</h1>
    <hr>
    </br>
    <p>Here are the player cards:</p>
    <h2>Player 1</h2>
    <div id="handDiv"></div>
    <p>Here are the trick cards:</p>
    <div id="trickDiv">
    	<img id="player0" src="" height="96" width="72" style="visibility: hidden;"/>
    	<img id="player1" src="" height="96" width="72" style="visibility: hidden;"/>
    	<img id="player2" src="" height="96" width="72" style="visibility: hidden;"/>
    	<img id="player3" src="" height="96" width="72" style="visibility: hidden;"/>
    </div>
  </body>
</html>
