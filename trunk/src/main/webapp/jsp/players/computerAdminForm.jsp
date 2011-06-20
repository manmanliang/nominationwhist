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
		<h2><c:if test="${computerPlayer.player.new}">New </c:if>Player</h2>
		<sec:authorize access="hasRole('ROLE_ADMIN')">		
			<p>Go to <a href="<c:url value="/admin/players"/>">user list</a></p>
		</sec:authorize>		
		<p>Bold fields are required</p>
		<form:form modelAttribute="computerPlayer" method="put">
  			<table>
   	 			<tr>
   	   				<td class="label">Short Name: </td>
   	     			<td><form:input path="player.shortName" size="6" maxlength="6"/> <form:errors path="player.shortName" cssClass="formErrors"/></td>
   	 			</tr>
	    		<tr>
        			<td></td><td><p>A short name to be used in the Game UI.</p></td>
    			</tr>
   	 			<tr>
   	   				<td class="label">Name: </td>
   	     			<td><form:input path="player.prettyName" size="30" maxlength="80"/> <form:errors path="player.prettyName" cssClass="formErrors"/></td>
   	 			</tr>
	    		<tr>
        			<td></td><td><p>A full name to be used in the Game UI.</p></td>
    			</tr>
		       	<tr>
			       	<td class="label">Computer Type: </td>
    			   	<td>
    			   		<form:radiobuttons path="type" items="${types}"/>
    					<form:errors path="type" cssClass="formErrors"/>
					</td>
	         	</tr>
		 		<tr>
		 			<td>
		 				<c:choose>
		 					<c:when test="${computerPlayer.player.new}">
		 						<p class="submit"><input type="submit" value="Create New Computer Player"/></p>
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