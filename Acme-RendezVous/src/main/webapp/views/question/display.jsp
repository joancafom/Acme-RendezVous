<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h1><jstl:out value="${question.text}"/></h1>

<h3><spring:message code="question.answers"/>:</h3>

<display:table name="question.answers" id="answer" requestURI="question/user/display.do?questionId=${question.id}" pagesize="5" style="text-align:center;" class="displaytag">

	<display:column titleKey="answer.user">
		<a href="user/user/display.do?userId=${answer.user.id}"><jstl:out value="${answer.user.name}"/></a>
	</display:column>
	
	<display:column titleKey="answer.text" property="text" />
	
</display:table>

<br/>
<br/>

<a href="question/user/delete.do?questionId=${question.id}"><spring:message code="question.delete" /></a>

<h3><spring:message code="question.rendezVous"/> <a href="rendezVous/user/display.do?rendezVousId=${question.rendezVous.id}"><jstl:out value="${question.rendezVous.name}"/></a></h3>