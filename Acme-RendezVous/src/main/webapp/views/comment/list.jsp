<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="comments" id="comment" requestURI="comment/${actorWS}list.do" pagesize="5" style="text-align:center;" class="displaytag">
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
	</display:column>>
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