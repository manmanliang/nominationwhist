    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 	<div id="footer">
 		<hr>
		<p id="footerRight"><a href="<c:url value="/j_spring_security_logout" />">Logout</a> Version: ${version}</p>
		<p id="footerLeft"><a href="javascript:toggleInstructions('<%= request.getParameter("nonInstructionsDiv") %>')" id="instructionsLink">Show Instructions</a>&nbsp;<a href="./">Games List</a></p>
		<div id="ajaxProgress">
			<p><img id="ajaxProgressSpinner" src="<c:url value="/images/indicator.white.gif"/>"/></p><p id="ajaxProgressCount"></p><p id="ajaxProgressAction"></p>
 		</div>
 	</div>
