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
		<h2>Player ${user.prettyName}</h2>
  		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<p>Go to <a href="<c:url value="/admin/players"/>">user list</a></p>
  		</sec:authorize>
  		<p>Username: ${player.user.username}</p>
  		<p>Name: ${player.prettyName}</p>
  		<p>Short Name: ${player.shortName}</p>
  		<sec:authorize access="hasRole('ROLE_ADMIN')">
	   		<p>Roles are:
  				<c:forEach var="authorities" items="${player.user.authorities}">
    				${authorities}
	    		</c:forEach>
  			</p>
  		</sec:authorize>
  		<p>Enabled: ${player.user.enabled}</p>
  		<p><a href="${player.user.username}/edit">Edit</a>
  	</div>
  	
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="user" />
 	</jsp:include>
  	
  </body>
</html>