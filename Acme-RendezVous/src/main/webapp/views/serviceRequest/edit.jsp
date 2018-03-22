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
	document.getElementById("holderName").value = Base64.decode(cookieCurrentValue("holderName", "${userId}"));
	document.getElementById("brandName").value = Base64.decode(cookieCurrentValue("brandName", "${userId}"));
	document.getElementById("number").value = Base64.decode(cookieCurrentValue("number", "${userId}"));
	document.getElementById("month").value = Base64.decode(cookieCurrentValue("month", "${userId}"));
	document.getElementById("year").value = Base64.decode(cookieCurrentValue("year", "${userId}"));
};

function saveCard(){
	saveData('holderName', '${userId}' );
	saveData('brandName', '${userId}' );
	saveData('number', '${userId}' );
	saveData('month', '${userId}' );
	saveData('year', '${userId}' );
}

</script>

<jstl:choose>
	
	<jstl:when test="${not empty availableRendezVouses}">
		<form:form action="serviceRequest/user/edit.do" modelAttribute="serviceRequest">
	
			<form:hidden path="id"/>
			<form:hidden path="version"/>
			<form:hidden path="service"/>
			
			<acme:textbox code="creditCard.holderName" path="creditCard.holderName" id="holderName"/>
			<acme:textbox code="creditCard.brandName" path="creditCard.brandName" id="brandName"/>
			<acme:textbox code="creditCard.number" path="creditCard.number" id="number"/>
			<acme:textbox code="creditCard.CVV" path="creditCard.CVV" id="CVV"/>
			<acme:textbox code="creditCard.month" path="creditCard.month" id="month"/>
			<acme:textbox code="creditCard.year" path="creditCard.year" id="year"/>
			<acme:select items="${availableRendezVouses}" itemLabel="name" code="serviceRequest.rendezVous" path="rendezVous"/>
			
			<acme:textarea code="serviceRequest.comments" path="comments"/>
			
			
			<acme:cancel url="service/user/list.do" code="action.cancel"/>
			<acme:submit name="save" code="action.save" onclick="saveCard()"/>
	
		</form:form>
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="serviceRequest.noMoreRVs" />
		<br><br>
		<acme:cancel url="service/user/list.do" code="action.back"/>
	</jstl:otherwise>
</jstl:choose>

<br/>
