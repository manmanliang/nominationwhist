<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<title>Players</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
  </head>
  <body>
  	<div id="userList">
  		<h2>Human Players</h2>
  		<c:forEach var="humanPlayer" items="${humanPlayers}">
    		<p><a href="<c:url value="/admin/human/${humanPlayer.user.username}"/>">${humanPlayer.prettyName}</a> - <a href="<c:url value="/admin/player/${humanPlayer.id}/delete"/>">Delete</a></p>
    	</c:forEach>
  		<h2>Computer Players</h2>
  		<c:forEach var="computerPlayer" items="${computerPlayers}">
    		<p><a href="<c:url value="/admin/computer/${computerPlayer.id}"/>">${computerPlayer.prettyName}</a> (${computerPlayer.type}) - <a href="<c:url value="/admin/player/${computerPlayer.id}/delete"/>">Delete</a></p>
    	</c:forEach>
    	<p>Click on a user to view and edit their account, <a href="<c:url value="/admin/human/new"/>">create a new account</a>, or <a href="<c:url value="/admin/computer/new"/>">create a new computer player</a></p>
  	</div>
  	
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="userList" />
 	</jsp:include>
  	
  </body>
</html>