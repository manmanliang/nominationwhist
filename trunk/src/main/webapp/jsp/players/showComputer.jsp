<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>Players</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
  </head>
  <body>
  	<div id="user">
		<h2>Player ${player.prettyName}</h2>
  		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<p>Go to <a href="<c:url value="/admin/players"/>">player list</a></p>
  		</sec:authorize>
  		<p>Name: ${player.prettyName}</p>
  		<p>Short Name: ${player.shortName}</p>
  		<p>Type: ${player.type}</p>
  	</div>
  	
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="user" />
 	</jsp:include>
  	
  </body>
</html>