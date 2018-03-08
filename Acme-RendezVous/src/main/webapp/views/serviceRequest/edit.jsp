<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:choose>
	
	<jstl:when test="${not empty availableRendezVouses}">
		<form:form action="serviceRequest/user/edit.do" modelAttribute="serviceRequest">
	
			<form:hidden path="id"/>
			<form:hidden path="version"/>
			<form:hidden path="service"/>
			
			<acme:textbox code="creditCard.holderName" path="creditCard.holderName"/>
			<acme:textbox code="creditCard.brandName" path="creditCard.brandName"/>
			<acme:textbox code="creditCard.number" path="creditCard.number"/>
			<acme:textbox code="creditCard.CVV" path="creditCard.CVV"/>
			<acme:textbox code="creditCard.month" path="creditCard.month"/>
			<acme:textbox code="creditCard.year" path="creditCard.year"/>
			<acme:select items="${availableRendezVouses}" itemLabel="name" code="serviceRequest.rendezVous" path="rendezVous"/>
			
			<acme:textarea code="serviceRequest.comments" path="comments"/>
			
			
			<acme:cancel url="service/user/list.do" code="action.cancel"/>
			<acme:submit name="save" code="action.save"/>
	
		</form:form>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="serviceRequest.noMoreRVs" />
		<br><br>
		<acme:cancel url="service/user/list.do" code="action.back"/>
	</jstl:otherwise>
</jstl:choose>
