<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h3><spring:message code="rendezVous.rsvp.cancel.confirmation"/></h3>

<br/>

<form:form action="rendezVous/user/cancel.do" modelAttribute="rendezVous">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<acme:submit name="submitButton" code="rendezVous.rsvp.cancel"/>
	<acme:cancel url="rendezVous/user/list.do?show=all" code="rendezVous.back"/>
</form:form>	