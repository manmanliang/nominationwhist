<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<title>Players</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
  </head>
  <body>
  	<div id="registrationMessage">
  		<h1>Registration Complete for ${user.username}</h1>
  		<p>Thankyou ${user.username} for registering. Your account has now been created but it needs to be approved before you can login. You may or not get an email or something if and when you can log in.</p>
  	</div>
  	
  	<jsp:include page="../instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="registrationMessage" />
 	</jsp:include>
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="registrationMessage" />
 	</jsp:include>
  	
  </body>
</html>