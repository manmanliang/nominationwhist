<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>Edit Player ${player.username}</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
  </head>
  <body>
  	<div id="user">
		<h2>Edit Player ${player.username}</h2>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<p>Go back to <a href="<c:url value="/players"/>">user list</a></p>
		</sec:authorize>
  		<p>Username: ${player.username}</p>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
	  		<p>Roles are:
  				<c:forEach var="authorities" items="${player.authorities}">
    				${authorities}
	    		</c:forEach>
  			</p>
		</sec:authorize>
		<form:form modelAttribute="player" method="put">
  			<table>
	    		<tr>
      				<th>
        				New Password: <form:errors path="password" cssClass="errors"/>
        				<br/>
        				<form:password path="password" size="30" maxlength="80"/>
      				</th>
    			</tr>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
	    			<tr>
      					<th>
        					Enabled: <form:errors path="active" cssClass="errors"/>
        					<br/>
        					<form:checkbox path="active"/>
      					</th>
    				</tr>
				</sec:authorize>
	    		<tr>
     		 		<td>
        				<p class="submit"><input type="submit" value="Edit user"/></p>
      				</td>
    			</tr>
  			</table>
  		</form:form>
  	</div>
  	
  	<jsp:include page="../instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="user" />
 	</jsp:include>
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="user" />
 	</jsp:include>
  	
  </body>
</html>