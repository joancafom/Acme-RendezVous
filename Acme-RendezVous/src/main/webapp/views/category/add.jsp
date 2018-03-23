<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="category/manager/edit.do" modelAttribute="addCategoryForm">

	<!-- Hidden Inputs -->
	<form:hidden path="serviceId"/>
	
	<!-- Inputs -->
	
	<form:label path="categoryId">
		<strong><spring:message code="category.category"/>:</strong>
	</form:label>
	<form:select path="categoryId">
		<jstl:forEach items="${categories}" var="category">
			<jstl:if test="${category.parentCategory==null}">
				<form:option value="${category.id}" label=".../${category.name}"/>
			</jstl:if>
			<jstl:if test="${category.parentCategory!=null}">
				<form:option value="${category.id}" label="${category.parentCategory.name}/${category.name}"/>
			</jstl:if>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="categoryId"/>
	<br><br>
	<acme:cancel url="service/manager/edit.do?serviceId=${addCategoryForm.serviceId}" code="category.cancel"/>
	<acme:submit name="add" code="category.add"/>
	
</form:form>

