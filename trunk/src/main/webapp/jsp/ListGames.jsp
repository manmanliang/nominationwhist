<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/xmlhttp.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gameList.js"/>"></script>
    <script>
    	xmlHttp['gameList'].init("<c:url value="/games" />");
    </script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/whist.css"/>" />
  </head>
  <body onload="onLoadEventHandler();">
	<h1>List of Games</h1>
	<p>User: ${user}</p>
	
	<form method="POST" action="create-game">
      <fieldset>
        <legend>Create Game</legend>
        <input type="submit" value="Create Game" />
      </fieldset>
    </form>
    
    <ul id="gameList"></ul>
  </body>
</html>