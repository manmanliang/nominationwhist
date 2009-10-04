<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
  </head>
  <body bgcolor="rgb(0,128,0)">
    <script type="text/javascript">
        function ajaxCall()
        {
            var xmlhttp;
            if (window.XMLHttpRequest)
            {
                xmlhttp=new XMLHttpRequest();
            }
            else if (window.ActiveXObject)
            {
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }
            else
            {
                alert("Your browser does not support XMLHTTP!");
            }
            xmlhttp.onreadystatechange=function()
            {               
                if(xmlhttp.readyState==4)
                {
                    try {
                        var hand = JSON.parse(xmlhttp.responseText);
                    } catch (e) {
                        alert("An exception occurred in the script. Error name: " + e.name 
                          + ". Error message: " + e.message + "response was " + xmlhttp.responseText); 
                    }
                    var imgHTML = "";
                    for (card in hand) {
                        imgHTML = imgHTML + "<img src = \"images/" + card + ".png\"/>"
                    }
                    alert("imgHTML is " + imgHTML);
                    document.getElementById("handDiv").innerHTML = imgHTML;
                }
            }
            xmlhttp.open("GET","hand",true);
            xmlhttp.send(null); 
        }
        ajaxCall();
    </script>    
    <h1>Lets Play Nommy</h1>
    <hr>
    </br>
    <p>Here are the player cards:</p>
    <h2>Player 1 - ${playerOne.name}</h2>
    <div id="handDiv"></div>
  </body>
</html>
