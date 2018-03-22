<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p>
	<spring:message code="systemConfiguration.businessName"/>: <jstl:out value="${businessName}"></jstl:out>
</p>

<p>
	<spring:message code="systemConfiguration.bannerURL"/>: <a href="<jstl:out value="${logo}" />"><jstl:out value="${logo}" /></a>
</p>

<p>
	<jstl:forEach var="entry" items="${welcomeMessages}">
		<spring:message code="systemConfiguration.welcomeMessage"/> (<jstl:out value="${entry.key}" />): <jstl:out value="${entry.value}" /> <br />
	</jstl:forEach>
</p>

<a href="systemConfiguration/administrator/edit.do"><spring:message code="systemConfiguration.editCurrent"/></a>