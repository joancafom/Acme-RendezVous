<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="now" class="java.util.Date" />
<jstl:set var="rvid" value="${rendezVous.id}"/>
<jstl:set var="isDeleted" value="${rendezVous.isDeleted}"/>

<jstl:if test="${rendezVous.picture ne null}">
	<p>
		<img src="<jstl:out value="${rendezVous.picture}" />" style="max-width: 200px;" />
	</p>
</jstl:if>
<p>
	<strong><spring:message code="rendezVous.name" />:</strong> <jstl:out value="${rendezVous.name}" />
</p>
<p>
	<strong><spring:message code="rendezVous.creator" />:</strong> <a href="user/${actorWS}display.do?userId=<jstl:out value="${rendezVous.creator.id}" />"><jstl:out value="${rendezVous.creator.name}" /></a>
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
	<strong><spring:message code="rendezVous.orgDate" />:</strong>
	<acme:dateFormat code="date.format2" value="${rendezVous.orgDate}"/>
</p>

<jstl:if test="${rendezVous.coordinates.latitude ne null and rendezVous.coordinates.longitude ne null}">
	<p>
	(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)
	</p>
</jstl:if>
<p>
	<strong><spring:message code="rendezVous.description" />:</strong> <jstl:out value="${rendezVous.description}" />
</p>
<br>
<!-- Similar RendezVouses -->
<h2><spring:message code="rendezVous.similarRendezVouses"/></h2>
<display:table name="rendezVous.similarRendezVouses" id="similarRendezVous" requestURI="" class="displaytag" style="width:50%;">
	<display:column titleKey="rendezVous.state" class="tableRendezVous">
		<jstl:if test="${similarRendezVous.isDeleted==true}">
			<p style="color:red;"><strong><spring:message code="rendezVous.deleted"/></strong></p>
		</jstl:if>
		<jstl:if test="${similarRendezVous.isDeleted==false}">
			<p style="color:green;"><strong><spring:message code="rendezVous.public"/></strong></p>
		</jstl:if>
		<jstl:if test="${similarRendezVous.orgDate < now and similarRendezVous.isDeleted==false}">
			<p style="color:gray;"><strong><spring:message code="rendezVous.ended"/></strong></p>
		</jstl:if>
	</display:column>
	<display:column titleKey="rendezVous.name">
		<p><jstl:out value="${similarRendezVous.name}"/></p>
	</display:column>
	<display:column titleKey="rendezVous.description">
		<p><jstl:out value="${similarRendezVous.description}"/></p>
	</display:column>
	
	<display:column titleKey="rendezVous.orgDate" class="tableRendezVous">
		<acme:dateFormat code="date.format2" value="${similarRendezVous.orgDate}" type="both"/>
	</display:column>
	
	<display:column class="tableRendezVous">
		<p><a href="rendezVous/${actorWS}display.do?rendezVousId=${similarRendezVous.id}"><spring:message code="rendezVous.display"/></a></p>
	</display:column>
	<jstl:if test="${own && isDeleted==false}">
		<display:column class="tableRendezVous">
			<p><a href="rendezVous/user/deleteLink.do?similarRendezVousId=${similarRendezVous.id}&parentRendezVousId=${rvid}"><spring:message code="rendezVous.delete"/></a></p>
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${own && isDeleted==false}">
	<a href="rendezVous/user/createLink.do?rendezVousId=${rvid}"><spring:message code="rendezVous.createLink"/></a>
</jstl:if>
<br>

<!-- Attendants -->
<h2><spring:message code="rendezVous.attendants"/></h2>

<display:table name="rendezVous.attendants" id="attendant" requestURI="" class="displaytag" style="width:25%;">
	<display:column titleKey="rendezVous.attendants" style="text-align:center;">
		<a href="user/${actorWS}display.do?userId=<jstl:out value="${attendant.id}" />"><jstl:out value="${attendant.name}" /></a>
	</display:column>
	<display:column titleKey="attendant.surnames" property="surnames" style="text-align:center;" />
	<jstl:if test="${not empty rendezVous.questions}">
		<display:column>
			<a href="answer/${actorWS}list.do?rendezVousId=<jstl:out value="${rendezVous.id}" />&userId=<jstl:out value="${attendant.id}" />" ><spring:message code="answers.show" /></a>
		</display:column>
	</jstl:if>
</display:table>
<br/>

<security:authorize access="hasRole('USER')">
	<jstl:if test="${hasRSVP and rendezVous.orgDate >= now and !rendezVous.isDeleted}">
		<h3><a href="rendezVous/${actorWS}cancel.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.rsvp.cancel"/></a></h3>
	</jstl:if>

	<jstl:if test="${!hasRSVP and rendezVous.orgDate >= now and !rendezVous.isDeleted}">
		<h3><a href="rendezVous/${actorWS}rsvp.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.rsvp.accept"/></a></h3>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('ADMINISTRATOR')">
	<a href="rendezVous/administrator/remove.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.remove"/></a>
</security:authorize>



<!-- Announcements -->

<h2>
	<spring:message code="rendezVous.announcements" />: 
</h2>

<display:table name="rendezVous.announcements" id="announcement" requestURI="rendezVous/${actorWS}display.do?rendezVousId=${rendezVous.id}" pagesize="3" style="text-align:center;" class="displaytag">
	<display:column titleKey="announcement.title" property="title" />
	<display:column titleKey="announcement.description" property="description" />
	<display:column titleKey="announcement.moment">
		<acme:dateFormat code="date.format2" value="${announcement.creationMoment}"/>
	</display:column>

	<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column>
			<a href="announcement/administrator/delete.do?announcementId=<jstl:out value="${announcement.id}" />"><spring:message code="announcement.delete" /></a>
		</display:column>
	</security:authorize>
</display:table>

<jstl:if test="${own}">
	<a href="announcement/user/create.do?rendezVousId=${rendezVous.id}"><spring:message code="announcement.create"/></a>
</jstl:if>

<!-- Comments -->
<h2>
	<spring:message code="rendezVous.comments" />: 
</h2>
<display:table name="rootComments" id="comment" requestURI="rendezVous/${actorWS}display.do?rendezVousId=${rendezVous.id}" pagesize="5" style="text-align:center;" class="displaytag">
	<display:column titleKey="comment.user">
		<a href="user/${actorWS}display.do?userId=<jstl:out value="${comment.user.id}" />"><jstl:out value="${comment.user.name}" /></a>
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
	<security:authorize access="hasRole('USER')">
	<display:column>
		<a href="comment/user/create.do?rendezVousId=<jstl:out value="${comment.rendezVous.id}"/>&commentId=<jstl:out value="${comment.id}"/>"><spring:message code="comment.reply"/></a>
	</display:column>
	</security:authorize>
	
	<display:column titleKey="comment.replies">
		<a href="comment/${actorWS}list.do?commentId=<jstl:out value="${comment.id}"/>"><spring:message code="comment.listReplies"/></a>
	</display:column>
	
	<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column>
			<a href="comment/administrator/delete.do?commentId=<jstl:out value="${comment.id}" />"><spring:message code="comment.delete" /></a>
		</display:column>
	</security:authorize>
</display:table>

<security:authorize access="hasRole('USER')">
	<jstl:if test="${hasRSVP}">
		<a href="comment/user/create.do?rendezVousId=<jstl:out value="${rendezVous.id}" />"><spring:message code="comment.create" /></a>
	</jstl:if>
</security:authorize>

<br/>

<jstl:if test="${own}">
	<h2><spring:message code="rendezVous.questions"/></h2>
	
	<display:table name="rendezVous.questions" id="question" requestURI="rendezVous/user/display.do?rendezVousId=${rendezVous.id}" pagesize="5" style="text-align:center;" class="displaytag">
		<display:column titleKey="question.text">
			<a href="question/user/display.do?questionId=${question.id}"><jstl:out value="${question.text}"/></a>
		</display:column>
		
		<display:column>
			<a href="question/user/delete.do?questionId=${question.id}"><spring:message code="question.delete" /></a>
		</display:column>
	</display:table>
	
	<a href="question/user/create.do?rendezVousId=${rendezVous.id}"><spring:message code="question.create"/></a>
</jstl:if>