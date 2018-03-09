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
			
			<acme:textbox code="creditCard.holderName" path="creditCard.holderName" id="holderName"/>
			<acme:textbox code="creditCard.brandName" path="creditCard.brandName" id="brandName"/>
			<acme:textbox code="creditCard.number" path="creditCard.number" id="number"/>
			<acme:textbox code="creditCard.CVV" path="creditCard.CVV" id="CVV"/>
			<acme:textbox code="creditCard.month" path="creditCard.month" id="month"/>
			<acme:textbox code="creditCard.year" path="creditCard.year" id="year"/>
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

<script>

var cookieDuration = 14;
var cookieName = 'pruebaCookie';
var cookieValue = 'oleeeeeeeeeeeeeeee';

window.onload = function(){
	// If the banner cookie isn't on
	if(cookieCurrentValue(window.cookieName) != window.cookieValue){
    	createDiv('<p>Solicitamos su permiso para obtener datos estadísticos sobre su navegación en este sitio, de acuerdo con el Real Decreto-Ley 13/2012. Si continúa navegando, acepta el uso de cookies.</p><div id="actions"><input type="button" value="De acuerdo" onclick="removeMe();" /><input type="button" value="Mostrar más/menos info" onclick="toggleMoreInfo();" /></div><div id="readMore"><p>Nuestro sistema usa las cookies para:</p><ul><li>Determinar en qué idioma debe mostrarse la información.</li><li>Guardar un identificador para gestionar sesiones.</li><li>Asegurarnos de que usted ha aceptado el uso de cookies.</li></ul></div>'); 
    	$("#readMore").hide();
    }
};

window.onload = function storeCard() {
	document.getElementById("holderName").value = "Hello JavaScript";
	document.getElementById("brandName").value = "Mastercard";
	document.getElementById("number").value = "5530476482194131";
	document.getElementById("CVV").value = "123";
	document.getElementById("month").value = "12";
	document.getElementById("year").value = "2019";
	
	createCookie(window.cookieName, window.cookieValue, window.cookieDuration);
};

function createDiv(inContent) {
	var contentWrapper = document.getElementById("content-wrapper");
	var div = document.createElement('div');

	div.setAttribute('id', 'cookieBanner');
	div.innerHTML = inContent;

	// Adds the Cookie Banner just after the content-wrapper
	contentWrapper.insertBefore(div, contentWrapper.firstChild);

	// Create the cookie
	createCookie(window.cookieName, window.cookieValue, window.cookieDuration);
}

function createCookie(name, value, days) {
	var expires = "";

	// Set the expiring date
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));

		expires = "expires=" + date.toGMTString() + ";";
	}

	// Create the cookie with its name, value and expiring date
	document.cookie = name + "=" + value + "; " + expires + " path=/";
}

function cookieCurrentValue(name) {
	var nameEquals = name + "=";
	var languageEquals = window.languageName + "=";

	var allCookies = document.cookie.split(';');

	// Iterate over all the cookies that are in the page
	for ( var i = 0; i < allCookies.length; i++) {
		var currentCookie = allCookies[i];

		// Eliminate spaces at the begining
		while (currentCookie.charAt(0) == ' ')
			currentCookie = currentCookie.substring(1, currentCookie.length);

		// Set language cookie
		if (currentCookie.indexOf(languageEquals) == 0)
			window.languageValue = currentCookie.substring(languageEquals.length, currentCookie.length);

		// When we are in the banner cookie, return a substring with its value
		if (currentCookie.indexOf(nameEquals) == 0)
			return currentCookie.substring(nameEquals.length, currentCookie.length);

	}
	return null;
}

</script>

<br/>

<jstl:forEach items="${cookie}" var="currentCookie">  
    Cookie name as map entry key: <jstl:out value="${currentCookie.key}"/><br/>
    Cookie object as map entry value: <jstl:out value="${currentCookie.value}"/><br/>
    Name property of Cookie object: <jstl:out value="${currentCookie.value.name}"/><br/>
    Value property of Cookie object: ${currentCookie.value.value}<br/>
</jstl:forEach>
