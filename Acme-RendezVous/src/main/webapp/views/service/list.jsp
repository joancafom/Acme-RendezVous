<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<style>
	.tableService{
		text-align:center;
	}
</style>

<display:table name="services" id="service" requestURI="rendezVous/${actor}list.do" class="displaytag">
	
	<display:column titleKey="service.name" property="name" class="tableService" />
	<display:column titleKey="service.description" property="description" class="tableService" />
	<display:column class="tableService">
		<jstl:if test="${service.picture ne null}">
			<img src="<jstl:out value="${service.picture}" />" alt="<spring:message code="img.alt.service.picture" />" width="200px" />
		</jstl:if>
	</display:column>
	
</display:table>