<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="announcements" id="announcement" requestURI="announcement/user/list.do" pagesize="5" style="text-align:center;" class="displaytag">
	<display:column titleKey="announcement.rendezVous">
		<a href="rendezVous/user/display.do?rendezVousId=<jstl:out value="${announcement.rendezVous.id}" />"><jstl:out value="${announcement.rendezVous.name}" /></a>
	</display:column>
	<display:column titleKey="announcement.title" property="title" />
	<display:column titleKey="announcement.description" property="description" />
	<display:column titleKey="announcement.moment">
		<acme:dateFormat code="date.format2" value="${announcement.creationMoment}"/>
	</display:column>
</display:table>