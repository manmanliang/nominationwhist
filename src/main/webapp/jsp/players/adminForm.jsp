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
		<h2><c:if test="${empty player.username}">New </c:if>Player</h2>
		<sec:authorize access="hasRole('ROLE_ADMIN')">		
			<p>Go back to <a href="<c:url value="/admin/players"/>">user list</a></p>
		</sec:authorize>		
		<form:form modelAttribute="player" method="put">
  			<table>
   	 			<c:if test="${empty player.username}">
   	 				<tr>
   	   					<td class="label">Username: <form:errors path="username" cssClass="formErrors"/></td>
   	     				<td><form:input path="username" size="30" maxlength="80"/></td>
   	 				</tr>
   	 			</c:if>
	    		<tr>
      				<td class="label">Password: <form:errors path="password" cssClass="formErrors"/></td>
        			<td><form:password path="password" size="30" maxlength="80"/></td>
    			</tr>
   	 			<tr>
   	   				<td class="label">Name: <form:errors path="prettyName" cssClass="formErrors"/></td>
   	     			<td><form:input path="prettyName" size="30" maxlength="80"/></td>
   	 			</tr>
   	 			<tr>
   	   				<td class="label">Short Name: <form:errors path="shortName" cssClass="formErrors"/></td>
   	     			<td><form:input path="shortName" size="30" maxlength="80"/></td>
   	 			</tr>
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
	    		<tr>
     		 		<td>
				        <c:choose>
          					<c:when test="${empty player.username}">
            					<p class="submit"><input type="submit" value="Create"/></p>
          					</c:when>
          					<c:otherwise>
            					<p class="submit"><input type="submit" value="Modify"/></p>
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