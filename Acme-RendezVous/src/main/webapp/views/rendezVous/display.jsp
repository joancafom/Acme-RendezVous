<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${rendezVous.picture ne null}">
	<p>
		<img src="<jstl:out value="${rendezVous.picture}" />" />
	</p>
</jstl:if>
<p>
	<spring:message code="rendezVous.name" />: <jstl:out value="${rendezVous.name}" />
</p>
<p>
	<spring:message code="rendezVous.creator" />: <a href="user/${actorWS}display.do?userId=<jstl:out value="${rendezVous.user.id}" />"><jstl:out value="${rendezVous.user.name}" /></a>
</p>
<jstl:if test="${rendezVous.isDeleted==true}">
	<p style="color:red;"><strong><spring:message code="rendezVous.deleted"/></strong></p>
</jstl:if>
<jstl:if test="${rendezVous.isDeleted==false}">
	<p style="color:green;"><strong><spring:message code="rendezVous.public"/></strong></p>
</jstl:if>

<jstl:if test="${rendezVous.isForAdults==true}">
	<p style="color:red; text-decoration: underline;"><strong>+18</strong></p>
</jstl:if>

<p>
	<spring:message code="rendezVous.orgDate" />:
	<spring:message code="date.format2" var="dateFormat"></spring:message>
	<fmt:formatDate value="${rendezVous.orgDate}" pattern="${dateFormat}" type="date"/>
</p>

<jstl:if test="${rendezVous.coordinates.latitude ne null and rendezVous.coordinates.longitude ne null}">
	<p>
	(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)
	</p>
</jstl:if>
<p>
	<spring:message code="rendezVous.description" />: <jstl:out value="${rendezVous.description}" />
</p>
<display:table name="rendezVous.attendants" id="attendant" requestURI="" class="displaytag" style="width:25%;">
	<display:column titleKey="rendezVous.attendants" style="text-align:center;">
		<a href="user/${actorWS}display.do?userId=<jstl:out value="${attendant.id}" />"><jstl:out value="${attendant.name}" /></a>
	</display:column>
</display:table>