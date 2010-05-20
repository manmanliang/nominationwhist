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
  	<div id="userAddForm">
		<h2>New Player</h2>
		<sec:authorize access="hasRole('ROLE_ADMIN')">		
			<p>Go back to <a href="<c:url value="/players"/>">user list</a></p>
		</sec:authorize>		
		<form:form modelAttribute="player" method="post">
  			<table>
   	 			<tr>
   	   				<th>
   	     				Username: <form:errors path="username" cssClass="errors"/>
   	     				<br/>
   	     				<form:input path="username" size="30" maxlength="80"/>
   	   				</th>
   	 			</tr>
	    		<tr>
      				<th>
        				Password: <form:errors path="password" cssClass="errors"/>
        				<br/>
        				<form:input path="password" size="30" maxlength="80"/>
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
        				<p class="submit"><input type="submit" value="Add user"/></p>
      				</td>
    			</tr>
  			</table>
  		</form:form>
  	</div>
  	
  	<jsp:include page="../instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="userAddForm" />
 	</jsp:include>
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="userAddForm" />
 	</jsp:include>
  	
  </body>
</html>