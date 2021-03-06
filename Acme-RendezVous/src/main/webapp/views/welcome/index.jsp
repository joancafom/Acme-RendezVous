<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="scripts/welcome.js"></script>

<script type="text/javascript">
	
$(document).ready(function() {

	<jstl:choose>
		<jstl:when test="${cookie.language.value eq null}">
		$('#welcomeMessage').html(getMessage('<jstl:out value="${welcomeMessages}" />', 'en'));
		</jstl:when>
		<jstl:otherwise>
		$('#welcomeMessage').html(getMessage('<jstl:out value="${welcomeMessages}" />', cookieCurrentValue('language')));
		</jstl:otherwise>
	</jstl:choose>
	
});
	
</script>

<p id="welcomeMessage">
</p>

<security:authorize access="isAnonymous()">
	<p><strong><spring:message code="welcome.registerQuestion"/><a href="user/register.do"><spring:message code="welcome.here"/></a></strong></p>
</security:authorize>
<security:authorize access="isAuthenticated()">
	<p><spring:message code="welcome.greeting.prefix" /><spring:message code="welcome.greeting.suffix" /></p>
</security:authorize>

<p><spring:message code="welcome.greeting.current.time" /> ${moment}</p> 


