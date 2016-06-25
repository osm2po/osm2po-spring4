<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<body>
<h2>Controller is ${foo}</h2>
<h3><spring:message code="welcome" /></h3>
	<form:form method="post" modelAttribute="account">
		<form:input path="user" type="text" />
		<form:input path="pass" type="password" />
		<form:errors path="user" />
		<form:button>Login</form:button>
	</form:form>

</body>
</html>
