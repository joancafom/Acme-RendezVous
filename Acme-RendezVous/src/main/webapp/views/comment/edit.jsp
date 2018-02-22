<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="comment/user/edit.do" modelAttribute="comment">

	<!-- User is pruned -->
	<!-- Hidden Inputs -->
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="writtenMoment"/>
	<form:hidden path="parentComment"/>
	<form:hidden path="replies"/>
	<form:hidden path="rendezVous"/>
	
	<!-- Inputs -->
	
	<acme:textbox code="comment.text" path="text"/><br>
	<acme:textbox code="comment.picture" path="picture"/><br>
	
	<acme:submit name="save" code="comment.save"/>
	<acme:cancel url="rendezVous/user/display.do?rendezVousId=${rendezVousId}" code="comment.cancel"/>

</form:form>