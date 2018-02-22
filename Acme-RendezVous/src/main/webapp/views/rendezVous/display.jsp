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
		<img src="<jstl:out value="${rendezVous.picture}" />" style="max-width: 200px;" />
	</p>
</jstl:if>
<p>
	<spring:message code="rendezVous.name" />: <jstl:out value="${rendezVous.name}" />
</p>
<p>
	<spring:message code="rendezVous.creator" />: <a href="user/${actorWS}display.do?userId=<jstl:out value="${rendezVous.creator.id}" />"><jstl:out value="${rendezVous.creator.name}" /></a>
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
	<jstl:choose>
		<jstl:when test="${rendezVous.isFinal}">
			<spring:message code="rendezVous.final" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="rendezVous.draft" />
		</jstl:otherwise>
	</jstl:choose>
	
</p>
<p>
	<spring:message code="rendezVous.orgDate" />:
	<acme:dateFormat code="date.format2" value="${rendezVous.orgDate}"/>
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
<h1>
	<spring:message code="rendezVous.comments" />: 
</h1>
<display:table name="rendezVous.comments" id="comment" requestURI="" style="text-align:center;" class="displaytag">
	<display:column titleKey="comment.user">
		<a href="<jstl:out value="${comment.user.id}" />"><jstl:out value="${comment.user.name}" /></a>
	</display:column>
	<display:column titleKey="comment.text" property="text" />
	<display:column titleKey="comment.writtenMoment">
		<acme:dateFormat code="date.format2" value="${comment.writtenMoment}"/>
	</display:column>
	
	<display:column>
		<jstl:if test="${comment.picture ne null}">
			<img src="<jstl:out value="${comment.picture}" />" style="max-width: 200px;" />
		</jstl:if>
	</display:column>
</display:table>

<security:authorize access="hasRole('USER')">
	<jstl:if test="${hasRSVP}">
		<a href="comment/user/create.do?rendezVousId=<jstl:out value="${rendezVous.id}" />"><spring:message code="comment.create" /></a>
	</jstl:if>
</security:authorize>