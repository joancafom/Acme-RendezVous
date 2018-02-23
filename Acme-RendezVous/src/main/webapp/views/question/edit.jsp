<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="question/user/edit.do" modelAttribute="question">

	<!-- Hidden Inputs -->
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="rendezVous"/>
	<form:hidden path="answers"/>
	
	<!-- Inputs -->
	
	<acme:textarea code="question.text" path="text"/><br>
	
	<acme:submit name="save" code="question.save"/>
	<acme:cancel url="rendezVous/user/display.do?rendezVousId=${rendezVousId}" code="question.cancel"/>

</form:form>