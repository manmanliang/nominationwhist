<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/xmlhttp.js"/>"></script>
    <script type="text/javascript">
      	var handxmlhttp;
        function handStateChange()
        {
            if (handxmlhttp.readyState==4)
            {// 4 = "loaded"
                if (handxmlhttp.status==200)
                {// 200 = OK
                    try {
                        var hand = JSON.parse(handxmlhttp.responseText);
                    } catch (e) {
                        alert("An exception occurred in the script. Error name: " + e.name 
                          + ". Error message: " + e.message + ". Response was " + handxmlhttp.responseText); 
                    }
                    var imgHTML = "";
                    for (card in hand) {
                        imgHTML = imgHTML + "<img src = \"images/" + hand[card] + ".png\"/>"
                    }
                    document.getElementById("handDiv").innerHTML = imgHTML;
                }
                else
                {
                    document.getElementById("handDiv").innerHTML = "<p>" + handxmlhttp.status + ": Data is out of data. Updating, please wait...</p>";
                }
            }
        }
    </script>    
    <style>
    	body { background: #008000; }
    </style>
  </head>
  <body onload="handxmlhttp = loadXMLDoc(handStateChange, '<c:url value="/hand"/>');">    
    <h1>Lets Play Nommy</h1>
    <hr>
    </br>
    <p>Here are the player cards:</p>
    <h2>Player 1</h2>
    <div id="handDiv"></div>
  </body>
</html>
