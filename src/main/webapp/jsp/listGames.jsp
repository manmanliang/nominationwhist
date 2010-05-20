<html>
  <head>
    <title>Whist Game</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/json.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jQueryGlobals.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/gameList.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>" />
	<script type="text/javascript">
    	$(function() {
    		onLoadEventHandler();
    	});
    </script>
  </head>
  <body>
	<div id="gamesList">
		<h1>List of Games</h1>
		<p>User: ${user}</p>
	
		<p>You can create a new game:</p>
		<form method="POST" action="create-game">
    		<input type="submit" value="Create Game" />
    	</form>
    
    	<div id="runningGames" style="display: none;">
			<p>Rejoin a game you are already in:</p>
    		<div id="runningGamesList"></div>
		</div>
	
    	<div id="newGames" style="display: none;">
			<p>Or join an existing new game:</p>
    		<div id="newGamesList"></div>
    	</div>
  	</div>
 	<jsp:include page="Instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="gamesList" />
 	</jsp:include>
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="gamesList" />
 	</jsp:include>
  </body>
</html>