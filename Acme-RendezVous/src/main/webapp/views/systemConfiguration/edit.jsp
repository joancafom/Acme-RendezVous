<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="systemConfiguration/administrator/edit.do" modelAttribute="systemConfiguration">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<!-- Inputs -->
	
	<acme:textbox code="systemConfiguration.businessName" path="businessName"/><br>
	<acme:textbox code="systemConfiguration.bannerURL" path="bannerURL"/><br>
	
	<acme:submit name="save" code="systemConfiguration.save"/>
	<acme:cancel url="systemConfiguration/administrator/display.do" code="systemConfiguration.cancel"/>
</form:form>