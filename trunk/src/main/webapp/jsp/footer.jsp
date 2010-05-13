    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<div id="footer">
 		<hr>
		<p id="footerRight">
			<sec:authorize access="isAuthenticated()">
				<a href="<c:url value="/j_spring_security_logout" />">Logout</a>
			</sec:authorize>
			Version: ${version}
		</p>
		<p id="footerLeft"><a href="javascript:toggleInstructions('<%= request.getParameter("nonInstructionsDiv") %>')" id="instructionsLink">Show Instructions</a>
			<sec:authorize access="isAuthenticated()">
				&nbsp;<a href="<c:url value="/"/>">Games List</a>
			</sec:authorize>
		</p>
		<div id="ajaxProgress" style="display: none;">
			<p><img id="ajaxProgressSpinner" src="<c:url value="/images/indicator.white.gif"/>"/></p><p id="ajaxProgressCount"></p><p id="ajaxProgressAction"></p>
 		</div>
 	</div>