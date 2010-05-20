<html>
<head>
	<title>Access Denied</title>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.2.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/instructions.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/common.css"/>" />
</head>

<body>
	<div id="errorContent">
		<p>Sorry this page is only accessible by users who are more important than you (probably a lot more)</p>
	</div>
	
	<jsp:include page="instructions.jsp">
	 	<jsp:param name="nonInstructionsDiv" value="errorContent" />
 	</jsp:include>
 	<jsp:include page="/footer">
	 	<jsp:param name="nonInstructionsDiv" value="errorContent" />
 	</jsp:include>
	
</body>
</html>