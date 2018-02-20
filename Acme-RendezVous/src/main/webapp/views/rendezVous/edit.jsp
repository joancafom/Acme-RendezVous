<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="rendezVous/user/edit.do" modelAttribute="rendezVous">

	<!-- Hidden Inputs -->

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="user"/>
	<form:hidden path="attendants"/>
	<form:hidden path="questions"/>
	<form:hidden path="comments"/>
	<form:hidden path="announcements"/>
	<form:hidden path="similarRendezVouses"/>
	<form:hidden path="isDeleted"/>
	
	<!-- Inputs -->
	
	<acme:textbox code="rendezVous.name" path="name"/>
	<acme:textarea code="rendezVous.description" path="description"/>
	
	<form:label path="orgDate"><spring:message code="rendezVous.orgDate"/></form:label>
	<form:input path="orgDate" placeholder="dd/MM/aaaa HH:mm"/>
	<form:errors cssClass="error" path="orgDate"/>
	
	<acme:textbox code="rendezVous.picture" path="picture"/>
	<acme:textbox code="rendezVous.coordinates.latitude" path="coordinates.latitude"/>
	<acme:textbox code="rendezVous.coordinates.longitude" path="coordinates.longitude"/>
	
	<form:label path="isFinal"><spring:message code="rendezVous.isFinal"/></form:label>
	<form:radiobutton path="isFinal" value="true"/><spring:message code="rendezVous.yes"/>
	<form:errors path="isFinal" cssClass="error"/>
	<form:radiobutton path="isFinal" value="false" checked="checked"/><spring:message code="rendezVous.no"/>
	<form:errors path="isFinal" cssClass="error"/>
	<br>
	<form:label path="isForAdults"><spring:message code="rendezVous.isForAdults"/></form:label>
	<form:radiobutton path="isForAdults" value="true"/><spring:message code="rendezVous.yes"/>
	<form:errors path="isForAdults" cssClass="error"/>
	<form:radiobutton path="isForAdults" value="false" checked="checked"/><spring:message code="rendezVous.no"/>
	<form:errors path="isForAdults" cssClass="error"/>
	<br>
	<acme:submit name="save" code="rendezVous.save"/>
	
</form:form>