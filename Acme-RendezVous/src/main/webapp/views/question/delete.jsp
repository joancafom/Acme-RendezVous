<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h3><spring:message code="question.delete.confirmation"/></h3>

<br/>

<form:form action="question/user/delete.do" modelAttribute="question">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="text"/>
	<form:hidden path="rendezVous"/>
	<form:hidden path="answers"/>

	<acme:submit name="save" code="question.delete"/>
	<acme:cancel url="question/user/display.do?questionId=${question.id}" code="question.back"/>
</form:form>	