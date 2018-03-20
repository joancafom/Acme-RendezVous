<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="category/administrator/edit.do" modelAttribute="category">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="services"/>
	<form:hidden path="parentCategory"/>
	<form:hidden path="childCategories"/>

	<!-- Inputs -->
	
	<acme:textbox code="category.name" path="name"/><br>
	<acme:textarea code="category.description" path="description"/><br>
	
	<acme:cancel url="category/administrator/list.do" code="category.cancel"/>
	<jstl:if test="${category.id != 0}">
		<acme:submit name="delete" code="category.delete"/>
	</jstl:if>
	<acme:submit name="save" code="category.save"/>
	
</form:form>