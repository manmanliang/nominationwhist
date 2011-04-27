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
		<h2><c:if test="${not empty new}">New </c:if>Player</h2>
		<p>Bold fields are required</p>
		<sec:authorize access="hasRole('ROLE_ADMIN')">		
			<p>Go back to <a href="<c:url value="/admin/players"/>">user list</a></p>
		</sec:authorize>		
		<form:form modelAttribute="player" method="put">
  			<table>
	    		<c:if test="${not empty new}">
	    		<tr>
      				<td class="label"><b>Username:</b> <form:errors path="username" cssClass="formErrors"/></td>
        			<td><form:input path="username" size="30" maxlength="80"/></td>
    			</tr>
    			</c:if>
	    		<tr>
        			<td></td><td><p>A username to be used for login.</p></td>
    			</tr>
	    		<tr>
      				<td class="label"><b>Password:</b> <form:errors path="password" cssClass="formErrors"/></td>
        			<td><form:password path="password" size="30" maxlength="80"/></td>
    			</tr>
   	 			<tr>
   	   				<td class="label">Short Name: <form:errors path="shortName" cssClass="formErrors"/></td>
   	     			<td><form:input path="shortName" size="6" maxlength="6"/></td>
   	 			</tr>
	    		<tr>
        			<td></td><td><p>A short name to be used in the Game UI.</p></td>
    			</tr>
   	 			<tr>
   	   				<td class="label">Name: <form:errors path="prettyName" cssClass="formErrors"/></td>
   	     			<td><form:input path="prettyName" size="30" maxlength="80"/></td>
   	 			</tr>
	    		<tr>
        			<td></td><td><p>Your full name.</p></td>
    			</tr>
	    		<tr>
     		 		<td>
				        <c:choose>
          					<c:when test="${not empty new}">
            					<p class="submit"><input type="submit" value="Create New Player"/></p>
          					</c:when>
          					<c:otherwise>
            					<p class="submit"><input type="submit" value="Edit Player"/></p>
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