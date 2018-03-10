<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="services" id="service" requestURI="rendezVous/${actor}list.do" style="text-align:center;" class="displaytag">
	
	<display:column titleKey="service.state">
		<jstl:if test="${service.isCanceled}">
			<p style="color:red;"><strong><spring:message code="service.canceled"/></strong></p>
		</jstl:if>
		<jstl:if test="${!service.isCanceled}">
			<p style="color:green;"><strong><spring:message code="service.available"/></strong></p>
		</jstl:if>
	</display:column>
	<display:column titleKey="service.name" property="name" />
	<display:column titleKey="service.description" property="description" />
	<display:column>
		<jstl:if test="${service.picture ne null or service.picture == ''}">
			<img src="<jstl:out value="${service.picture}" />" alt="<spring:message code="img.alt.service.picture" />" style="max-width: 200px;" />
		</jstl:if>
	</display:column>
	
	<jstl:if test="${show=='own'}">
		<display:column>
			<a href="service/manager/edit.do?serviceId=${service.id}"><spring:message code="service.edit"/></a>
			<a href="service/manager/delete.do?serviceId=${service.id}"><spring:message code="service.delete"/></a>
		</display:column>
	</jstl:if>
	
	<display:column>
		<security:authorize access="hasRole('USER')">
			<jstl:if test="${!service.isCanceled}">
				<a href="serviceRequest/user/create.do?serviceId=<jstl:out value="${service.id}" />"><spring:message code="service.serviceRequest.new" /></a>
			</jstl:if>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<jstl:if test="${!service.isCanceled}">
				<a href="service/administrator/cancel.do?serviceId=<jstl:out value="${service.id}" />"><spring:message code="service.cancel.service" /></a>
			</jstl:if>
		</security:authorize>
	</display:column>
	
</display:table>

<jstl:if test="${show=='own'}">
	<a href="service/manager/create.do"><spring:message code="service.create"/></a>
</jstl:if>