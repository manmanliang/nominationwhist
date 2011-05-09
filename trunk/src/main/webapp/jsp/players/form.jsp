<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>Whist Game - New Player</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
  </head>
  <body>
  	<div id="playerForm">
		<h2><c:if test="${player.new}">New </c:if>Player</h2>
		<p>Bold fields are required</p>
		<sec:authorize access="hasRole('ROLE_ADMIN')">		
			<p>Go back to <a href="<c:url value="/admin/players"/>">user list</a></p>
		</sec:authorize>		
		<form:form modelAttribute="player" method="put">
  			<table>
   	 			<c:if test="${player.new}">
   	 				<tr>
   	   					<td class="label"><b>Username: </b></td>
   	     				<td><form:input path="user.username" size="30" maxlength="80"/>  <form:errors path="user.username" cssClass="formErrors"/></td>
   	 				</tr>
		    		<tr>
    	    			<td></td><td><p>A username to be used for login.</p></td>
    				</tr>
   	 			</c:if>
	    		<tr>
      				<td class="label"><c:if test="${player.new}"><b></c:if>Password:<c:if test="${player.new}"></b></c:if> </td>
        			<td><form:password path="user.password" size="30" maxlength="80"/> <form:errors path="user.password" cssClass="formErrors"/></td>
    			</tr>
   	 			<tr>
   	   				<td class="label">Short Name: </td>
   	     			<td><form:input path="shortName" size="6" maxlength="6"/> <form:errors path="shortName" cssClass="formErrors"/></td>
   	 			</tr>
	    		<tr>
        			<td></td><td><p>A short name to be used in the Game UI.</p></td>
    			</tr>
   	 			<tr>
   	   				<td class="label">Name: </td>
   	     			<td><form:input path="prettyName" size="30" maxlength="80"/> <form:errors path="prettyName" cssClass="formErrors"/></td>
   	 			</tr>
	    		<tr>
        			<td></td><td><p>A full name to be used in the Game UI.</p></td>
    			</tr>
				<sec:authorize access="hasRole('ROLE_ADMIN')">		
			       	<tr>
    			       	<td class="label">Permissions: </td>
        			   	<td>
            		      	Player: <form:checkbox path="user.roles" value="ROLE_USER"/>
                		  	Admin: <form:checkbox path="user.roles" value="ROLE_ADMIN"/>
							<form:errors path="user.roles" cssClass="formErrors"/>
					</td>
		         	</tr>
		   			<tr>
      					<td class="label">Enabled: </td>
        				<td><form:checkbox path="user.active"/> <form:errors path="user.active" cssClass="formErrors"/></td>
    				</tr>
		 		</sec:authorize>
		 		<tr>
		 			<td>
		 				<c:choose>
		 					<c:when test="${player.new}">
		 						<p class="submit"><input type="submit" value="Create New User"/></p>
			 				</c:when>
			 				<c:otherwise>
			 					<p class="submit"><input type="submit" value="Edit"/></p>
		 					</c:otherwise>
		 				</c:choose>
		 			</td>
		 		</tr>
  			</table>
  		</form:form>
  	</div>
  	
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="playerForm" />
 	</jsp:include>
  	
  </body>
</html>