<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${toEdit==true}">
<form:form action="service/manager/edit.do" modelAttribute="service">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<!-- Inputs -->
	
	<acme:textbox code="service.name" path="name"/><br>
	<acme:textarea code="service.description" path="description"/><br>
	<acme:textbox code="service.picture" path="picture"/><br>
	
	<acme:submit name="save" code="service.save"/>
	<acme:cancel url="service/manager/list.do" code="service.cancel2"/>
</form:form>
</jstl:if>
<jstl:if test="${toDelete==true}">
	<jstl:if test="${able==true}">
		<form:form action="service/manager/delete.do" modelAttribute="service">

			<!-- Hidden Inputs -->
			<form:hidden path="id"/>
			<form:hidden path="version"/>
			<!-- Inputs -->
			<p><spring:message code="service.delete.message"/></p>
	
			<acme:submit name="delete" code="service.delete"/>
			<acme:cancel url="service/manager/list.do" code="service.cancel2"/>
		</form:form>
	</jstl:if>
	<jstl:if test="${able==false}">
		<p style="color:red;"><strong><spring:message code="service.cannotDelete"/></strong></p>
		<br>
		<acme:cancel url="service/manager/list.do" code="service.back"/>
	</jstl:if>
</jstl:if>