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

<display:table name="users" id="user" requestURI="user/list.do" class="displaytag">
	<display:column titleKey="user.name" class="tableRendezVous">
		<jstl:out value="${user.name}"/>
	</display:column>
	<display:column titleKey="user.surnames" class="tableRendezVous">
		<jstl:out value="${user.surnames}"/>
	</display:column>
	<display:column titleKey="user.email" class="tableRendezVous">
		<jstl:out value="${user.email}"/>
	</display:column>
	<display:column titleKey="user.dateOfBirth" class="tableRendezVous">
		<acme:dateFormat code="date.format2" value="${user.dateOfBirth}"/>
	</display:column>
	<display:column class="tableRendezVous">
		<a href="user/display.do?userId=${user.id}"><spring:message code="user.display"/></a>
	</display:column>
</display:table>