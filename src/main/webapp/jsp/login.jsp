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
			<c:if test="${error}">
				<p class="formErrors">Incorrect username or password, please try again</p>
			</c:if>
  			<table border="0" cellspacing="5">
    			<tr>
      				<td class="label">Username:</td>
      				<td><input type="text" name="j_username"></td>
    			</tr>
    			<tr>
      				<td class="label">Password:</td>
      				<td><input type="password" name="j_password"></td>
    			</tr>
    			<tr>
      				<td align="right"><input type="submit" value="Log In"></td>
      				<td align="left"><input type="reset"></td>
    			</tr>
  			</table>
		</form>
		<p>Don't have a login? Use our <a href="<c:url value="/players/register"/>">registration page</a> to create an account</p>
	</div>
	
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="loginForm" />
 	</jsp:include>
</body>
</html>
