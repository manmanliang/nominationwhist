<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>Whist Game - New Player</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
  </head>
  <body>
  	<div id="userAddForm">
		<h2>New Player</h2>
		<p>Go back to <a href="<c:url value="/players"/>">user list</a></p>
		<form:form modelAttribute="user" method="post">
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
	    		<tr>
     		 		<td>
        				<p class="submit"><input type="submit" value="Add user"/></p>
      				</td>
    			</tr>
  			</table>
  		</form:form>
  	</div>
  	
  	<jsp:include page="../Instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="userAddForm" />
 	</jsp:include>
 	<jsp:include page="../footer.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="userAddForm" />
 	</jsp:include>
  	
  </body>
</html>