<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<style>
	.tableRendezVous{
		text-align:center;
	}
</style>

<p><strong><spring:message code="user.name"/>:</strong> <jstl:out value="${user.name}"/></p>
<p><strong><spring:message code="user.surnames"/>:</strong> <jstl:out value="${user.surnames}"/></p>
<p><strong><spring:message code="user.postalAddress"/>:</strong> <jstl:out value="${user.postalAddress}"/></p>
<p><strong><spring:message code="user.phoneNumber"/>:</strong> <jstl:out value="${user.phoneNumber}"/></p>
<p><strong><spring:message code="user.email"/>:</strong> <jstl:out value="${user.email}"/></p>
<p><strong><spring:message code="user.dateOfBirth"/>:</strong><acme:dateFormat code="date.format2" value="${user.dateOfBirth}"/></p>

<h3><spring:message code="user.attendedRendezVouses"/></h3>
<display:table name="attendedRendezVouses" id="rendezVous" requestURI="rendezVous${actorWS}/list.do" class="displaytag">
		<display:column titleKey="user.rendezVous.state" class="tableRendezVous">
			<jstl:if test="${rendezVous.isDeleted==true}">
				<p style="color:red;"><strong><spring:message code="user.rendezVous.deleted"/></strong></p>
			</jstl:if>
			<jstl:if test="${rendezVous.isDeleted==false}">
				<p style="color:green;"><strong><spring:message code="user.rendezVous.public"/></strong></p>
			</jstl:if>
			<jstl:if test="${rendezVous.orgDate < now and rendezVous.isDeleted==false}">
				<p style="color:gray;"><strong><spring:message code="user.rendezVous.ended"/></strong></p>
			</jstl:if>
		</display:column>
		<display:column titleKey="user.rendezVous.name">
			<p><jstl:out value="${rendezVous.name}"/></p>
		</display:column>
		<display:column titleKey="user.rendezVous.description">
			<p><jstl:out value="${rendezVous.description}"/></p>
		</display:column>
		<display:column titleKey="user.rendezVous.orgDate" class="tableRendezVous">
			<spring:message code="date.format3" var="dateFormat"></spring:message>
			<p><fmt:formatDate value="${rendezVous.orgDate}" pattern="${dateFormat}" type="both"/></p>
		</display:column>
		<display:column class="tableRendezVous">
			<p><a href="rendezVous/${actorWS}display.do?rendezVousId=${rendezVous.id}"><spring:message code="user.rendezVous.display"/></a></p>
		</display:column>
	</display:table>