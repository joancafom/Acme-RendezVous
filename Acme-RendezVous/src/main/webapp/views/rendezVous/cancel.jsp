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
	<%-- <form:hidden path="name"/>
	<form:hidden path="description"/>
	<form:hidden path="orgDate"/>
	<form:hidden path="picture"/>
	<form:hidden path="coordinates"/>
	<form:hidden path="isFinal"/>
	<form:hidden path="isDeleted"/>
	<form:hidden path="isForAdults"/>
	<form:hidden path="creator"/>
	<form:hidden path="attendants"/>
	<form:hidden path="comments"/>
	<form:hidden path="similarRendezVouses"/>
	<form:hidden path="announcements"/>
	<form:hidden path="questions"/> --%>

	<input type="submit" id="submitButton" value="<spring:message code="rendezVous.rsvp.cancel"/>">
	<input type="button" name="back" value="<spring:message code="rendezVous.back"/>" onclick="javascript: relativeRedir('rendezVous/user/list.do?show=all');" />
</form:form>	