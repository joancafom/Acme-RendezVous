<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h3><spring:message code="answer"/>: <a href="rendezVous/${actorWS}display.do?rendezVousId=<jstl:out value="${rendezVous.id}"/>"><jstl:out value="${rendezVous.name}"/></a> </h3>
<p><spring:message code="writtenBy"/> <a href="user/${actorWS}display.do?userId=<jstl:out value="${user.id}"/>"><jstl:out value="${user.name}"/> <jstl:out value="${user.surnames}"/></a></p>
<display:table name="answers" id="answer" requestURI="answer/${actorWS}list.do?userId=${answer.user.id}&rendezVousId=${rendezVous.id}" pagesize="5" style="text-align:center;" class="displaytag">

	<display:column titleKey="question.text" property="question.text" />
	<display:column titleKey="answer.text" property="text" />
	
</display:table>

<br/>
<br/>
