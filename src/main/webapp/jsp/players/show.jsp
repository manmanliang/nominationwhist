<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>Players</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
  </head>
  <body>
  	<div id="user">
		<h2>Player ${player.username}</h2>
		<p>Go back to <a href="<c:url value="/players"/>">user list</a></p>
  		<p>Username: ${player.username}</p>
  		<p>Roles are:
  			<c:forEach var="authorities" items="${player.authorities}">
    			${authorities}
	    	</c:forEach>
  		</p>
  		<p>Enabled: ${player.enabled}</p>
  		<p><a href="${player.username}/edit">Edit User</a>
  	</div>
  	
  	<jsp:include page="../Instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="user" />
 	</jsp:include>
 	<jsp:include page="../footer.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="user" />
 	</jsp:include>
  	
  </body>
</html>