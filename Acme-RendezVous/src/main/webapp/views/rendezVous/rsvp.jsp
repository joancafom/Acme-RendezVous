<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h3><spring:message code="rendezVous.rsvp.accept.confirmation"/></h3>

<br/>

<form:form action="rendezVous/user/rsvp.do" modelAttribute="rsvpForm">
	
	<form:hidden path="rendezVous"/>
	
	<jstl:set var="index" value="${0}"/>
	<jstl:forEach items="${questions}" var="question">
		<p><strong><spring:message code="rendezVous.question"/>: </strong><jstl:out value="${question.text}"/></p>
		<acme:textarea code="answer.text" path="answers[${index}]"/>
		<jstl:set var="index" value="${index+1}"/>
	</jstl:forEach>
	
	<br><br><br>
	<input type="submit" id="submitButton" value="<spring:message code="rendezVous.rsvp.accept"/>">
	<input type="button" name="back" value="<spring:message code="rendezVous.back"/>" onclick="javascript: relativeRedir('rendezVous/user/list.do?show=all');" />
</form:form>	