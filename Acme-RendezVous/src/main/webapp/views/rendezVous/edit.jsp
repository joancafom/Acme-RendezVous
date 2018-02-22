<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${toEdit==true}">
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
	
	<acme:textbox code="rendezVous.name" path="name"/><br>
	<acme:textarea code="rendezVous.description" path="description"/><br>
	
	<form:label path="orgDate"><strong><spring:message code="rendezVous.orgDate"/>:</strong></form:label>
	<form:input path="orgDate" placeholder="dd/MM/aaaa HH:mm"/>
	<form:errors cssClass="error" path="orgDate"/><br><br>
	
	<acme:textbox code="rendezVous.picture" path="picture"/><br>
	<acme:textbox code="rendezVous.coordinates.latitude" path="coordinates.latitude"/><br>
	<acme:textbox code="rendezVous.coordinates.longitude" path="coordinates.longitude"/><br>
	
	<form:label path="isFinal"><strong><spring:message code="rendezVous.isFinal"/>:</strong></form:label>
	<form:radiobutton path="isFinal" value="false" checked="checked"/><spring:message code="rendezVous.yes"/>
	<form:errors path="isFinal" cssClass="error"/>
	<form:radiobutton path="isFinal" value="true"/><spring:message code="rendezVous.no"/>
	<form:errors path="isFinal" cssClass="error"/>
	<br><br>
	<form:label path="isForAdults"><strong><spring:message code="rendezVous.isForAdults"/>:</strong></form:label>
	<form:radiobutton path="isForAdults" value="true"/><spring:message code="rendezVous.yes"/>
	<form:errors path="isForAdults" cssClass="error"/>
	<form:radiobutton path="isForAdults" value="false" checked="checked"/><spring:message code="rendezVous.no"/>
	<form:errors path="isForAdults" cssClass="error"/>
	<br><br>
	<acme:submit name="save" code="rendezVous.save"/>
	<acme:cancel url="rendezVous/user/listMine.do" code="rendezVous.cancel"/>
	
</form:form>
</jstl:if>

<jstl:if test="${toDelete==true}">
<form:form action="rendezVous/user/delete.do" modelAttribute="rendezVous">
	<!-- Hidden Inputs -->
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="user"/>
	<form:hidden path="attendants"/>
	<form:hidden path="questions"/>
	<form:hidden path="comments"/>
	<form:hidden path="announcements"/>
	<form:hidden path="similarRendezVouses"/>
	<form:hidden path="name"/>
	<form:hidden path="description"/>
	<form:hidden path="orgDate"/>
	<form:hidden path="picture"/>
	<form:hidden path="coordinates"/>
	<form:hidden path="isFinal"/>
	<form:hidden path="isForAdults"/>
	<form:hidden path="isDeleted"/>
	
	<p><spring:message code="rendezVous.delete.confirmation"/></p>
	<acme:submit name="delete" code="rendezVous.yes"/>
	<acme:cancel url="rendezVous/user/listMine.do" code="rendezVous.no"/>
</form:form>
</jstl:if>