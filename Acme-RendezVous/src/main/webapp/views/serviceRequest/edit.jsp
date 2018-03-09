<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<script type="text/javascript" src="scripts/cookieCard.js"></script>

<script>

window.onload = function showCard() {
	document.getElementById("holderName").value = cookieCurrentValue("holderName", "${userId}");
	document.getElementById("brandName").value = cookieCurrentValue("brandName", "${userId}");
	document.getElementById("number").value = cookieCurrentValue("number", "${userId}");
	document.getElementById("CVV").value = cookieCurrentValue("CVV", "${userId}");
	document.getElementById("month").value = cookieCurrentValue("month", "${userId}");
	document.getElementById("year").value = cookieCurrentValue("year", "${userId}");
};

</script>

<jstl:choose>
	
	<jstl:when test="${not empty availableRendezVouses}">
		<form:form action="serviceRequest/user/edit.do" modelAttribute="serviceRequest">
	
			<form:hidden path="id"/>
			<form:hidden path="version"/>
			<form:hidden path="service"/>
			
			<acme:textbox code="creditCard.holderName" path="creditCard.holderName" id="holderName" onkeyup="saveData('holderName', ${userId})"/>
			<acme:textbox code="creditCard.brandName" path="creditCard.brandName" id="brandName" onkeyup="saveData('brandName', ${userId})"/>
			<acme:textbox code="creditCard.number" path="creditCard.number" id="number" onkeyup="saveData('number', ${userId})"/>
			<acme:textbox code="creditCard.CVV" path="creditCard.CVV" id="CVV" onkeyup="saveData('CVV', ${userId})"/>
			<acme:textbox code="creditCard.month" path="creditCard.month" id="month" onkeyup="saveData('month', ${userId})"/>
			<acme:textbox code="creditCard.year" path="creditCard.year" id="year" onkeyup="saveData('year'), ${userId}"/>
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