    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<div id="footer">
	  	<jsp:include page="/jsp/instructions.jsp">
	 		<jsp:param name="nonInstructionsDiv" value="<%= request.getParameter(\"nonInstructionsDiv\") %>" />
 		</jsp:include>
	
 		<hr>
 		<div id="footerLinks">
			<p id="footerRight">
				<c:if test="${not empty username}">
					<a href="<c:url value="/player/${username}"/>">${playerName}</a>
					&nbsp;(<a href="<c:url value="/j_spring_security_logout" />">logout</a>)
				</c:if>
				<a href="<c:url value="/information" />">Info</a>
			</p>
			<p id="footerLeft"><a href="javascript:toggleInstructions('<%= request.getParameter("nonInstructionsDiv") %>')" id="instructionsLink">Show Instructions</a>
				<c:if test="${not empty username}">
					&nbsp;<a href="<c:url value="/"/>">Games List</a>
				</c:if>
			</p>
			<div id="ajaxProgress" style="display: none;">
				<p><img id="ajaxProgressSpinner" src="<c:url value="/images/indicator.white.gif"/>"/></p><p id="ajaxProgressCount"></p><p id="ajaxProgressAction"></p>
 			</div>
 		</div>
 	</div>