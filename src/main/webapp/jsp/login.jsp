<html>
<head>
	<title>Login Page for Whist</title>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
</head>

<body>
	<div id="loginForm">
		<form method="POST" action="j_spring_security_check" >
  			<table border="0" cellspacing="5">
    			<tr>
      				<th align="right">Username:</th>
      				<td align="left"><input type="text" name="j_username"></td>
    			</tr>
    			<tr>
      				<th align="right">Password:</th>
      				<td align="left"><input type="password" name="j_password"></td>
    			</tr>
    			<tr>
      				<td align="right"><input type="submit" value="Log In"></td>
      				<td align="left"><input type="reset"></td>
    			</tr>
  			</table>
		</form>
	</div>
	
	<jsp:include page="Instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="loginForm" />
 	</jsp:include>
 	<jsp:include page="footer.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="loginForm" />
 	</jsp:include>
</body>
</html>
