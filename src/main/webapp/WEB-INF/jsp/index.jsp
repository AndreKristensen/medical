<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>
<head>
<title>MEDICAL XACML</title>
</head>
<body>
	<h2>MEDICAL</h2>


	<c:forEach var="p" items="${patients}">
		<c:out value="${p}" />
		<br />
		<c:forEach var="d" items="${p.departments}">
			<c:out value="${d}" />
			<br />
		</c:forEach>
	</c:forEach>
</body>
</html>
