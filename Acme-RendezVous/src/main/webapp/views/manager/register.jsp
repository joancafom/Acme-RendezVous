<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<spring:message code="user.userAccount.passwordMatch" var="passwordMatchError"></spring:message>
<script type="text/javascript">
	function checkData(){
		var pass1 = document.getElementById('pass1').value;
		var pass2 = document.getElementById('pass2').value;
		var message = document.getElementById('passwordMatchMessage');
		var boton = document.getElementById('boton');
		var checkbox = document.getElementById('checkbox');
		if(!checkbox.checked || pass1!=pass2 || pass1 == ''){
			if(pass1!=pass2){
				message.innerHTML="${passwordMatchError}";
			}
			else{
				message.innerHTML="";
			}
			boton.disabled=true;
		}
		else{
			message.innerHTML="";
			boton.disabled=false;
		}
	}
</script>


<form:form action="manager/register.do" modelAttribute="managerRegisterForm">

	<!-- Hidden Inputs -->
	
	<!-- Inputs -->
	
	<h3 style="text-decoration:underline"><spring:message code="manager.userAccount"/></h3>
	
	<acme:textbox code="manager.userAccount.userName" path="username"/><br>
	
	<form:label path="password"><strong><spring:message code="manager.userAccount.password" />: </strong></form:label>
	<form:password id="pass1" path="password" onkeyup="checkData()"/>
	<form:errors cssClass="error" path="password"/>
	
	<br><br>
	<label for="pass2"><strong><spring:message code="manager.userAccount.repeatPassword" />:</strong></label>
	<form:password id="pass2" path="repeatedPassword" onkeyup="checkData()" /><br><br>
	<div id="passwordMatchMessage" style="color:red; text-decoration:underline"></div>
	
	<h3 style="text-decoration:underline"><spring:message code="manager.personalData"/></h3>
	<acme:textbox code="manager.name" path="name"/><br>
	<acme:textbox code="manager.surnames" path="surnames"/><br>
	<acme:textbox code="manager.postalAddress" path="postalAddress"/><br>
	<acme:textbox code="manager.phoneNumber" path="phoneNumber" placeholder="+34555555555"/><br>
	<acme:textbox code="manager.email" path="email"/><br>
	<acme:textbox code="manager.vat" path="vat"/>
	
	<form:checkbox path="termsAndConditions" onchange="checkData()" id="checkbox" name="checkbox" /><spring:message code="manager.accept"/> <a href="misc/termsAndConditions.do" target="_blank"><spring:message code="manager.termsAndConditions"/></a>
	<br><br>
	<input type="submit" name="save" value="<spring:message code="manager.save"/>" id="boton" disabled="disabled"/>
	<acme:cancel url="welcome/index.do" code="manager.cancel"/>	
</form:form>
