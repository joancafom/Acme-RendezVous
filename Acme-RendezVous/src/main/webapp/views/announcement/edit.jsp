<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="announcement/user/edit.do" modelAttribute="announcement">

	<!-- Hidden Inputs -->
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="creationMoment"/>
	<form:hidden path="rendezVous"/>
	
	<!-- Inputs -->
	
	<acme:textbox code="announcement.title" path="title"/><br>
	<acme:textarea code="announcement.description" path="description"/><br>
	
	<acme:submit name="save" code="announcement.save"/>
	<acme:cancel url="rendezVous/user/display.do?rendezVousId=${rendezVousId}" code="announcement.cancel"/>

</form:form>