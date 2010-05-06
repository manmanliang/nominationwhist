<html>
  <head>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<title>Whist Game - New Player</title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/whist.css"/>" />
  </head>
  <body>
	<h2>New Player:</h2>
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
</body>
</html>