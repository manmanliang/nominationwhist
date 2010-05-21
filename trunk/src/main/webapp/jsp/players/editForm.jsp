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
		<form:form modelAttribute="player" method="put">
  			<table>
	    		<tr>
      				<td class="label">New Password: <form:errors path="password" cssClass="formErrors"/></td>
        			<td><form:password path="password" size="30" maxlength="80"/></td>
    			</tr>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
		         	<tr>
    		          	<td class="label">Permissions: <form:errors path="password" cssClass="formErrors"/></td>
        		      	<td>
            	    	  	Player: <form:checkbox path="roles" value="ROLE_USER"/>
                		  	Admin: <form:checkbox path="roles" value="ROLE_ADMIN"/>
						</td>
	          		</tr>
    	      		<tr>
      					<td class="label">Enabled: <form:errors path="active" cssClass="formErrors"/></td>
        				<td><form:checkbox path="active"/></td>
    				</tr>
				</sec:authorize>
	    		<tr>
     		 		<td>
        				<p class="submit"><input type="submit" value="Edit"/></p>
      				</td>
    			</tr>
  			</table>
  		</form:form>
  	</div>
  	
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="user" />
 	</jsp:include>
  	
  </body>
</html>