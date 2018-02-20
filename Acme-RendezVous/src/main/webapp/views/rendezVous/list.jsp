<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
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


<display:table name="rendezVouses" id="rendezVous" requestURI="rendezVous/user/list.do" class="displaytag">
	<display:column titleKey="rendezVous.name">
		<p><jstl:out value="${rendezVous.name}"/></p>
	</display:column>
	<display:column titleKey="rendezVous.description">
		<p><jstl:out value="${rendezVous.description}"/></p>
	</display:column>
	<display:column titleKey="rendezVous.orgDate" class="tableRendezVous">
		<p><jstl:out value="${rendezVous.orgDate}"/></p>
	</display:column>
	<display:column titleKey="rendezVous.coordinates" class="tableRendezVous">
		<p>(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)</p>
	</display:column>
	<display:column titleKey="rendezVous.restrictions" class="tableRendezVous">
		<jstl:if test="${rendezVous.isForAdults==true}">
			<p>+18</p>
		</jstl:if>
		<jstl:if test="${rendezVous.isForAdults==false}">
			<p>-</p>
		</jstl:if>
	</display:column>
	<display:column titleKey="rendezVous.mode" class="tableRendezVous">
		<jstl:if test="${rendezVous.isFinal==true}">
			<p><spring:message code="rendezVous.mode.draft"/></p>
		</jstl:if>
		<jstl:if test="${rendezVous.isForAdults==false}">
			<p><spring:message code="rendezVous.mode.final"/></p>
		</jstl:if>
	</display:column>
	<display:column class="tableRendezVous">
		<p><a href="rendezVous/user/display.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.display"/></a></p>
	</display:column>
	<display:column class="tableRendezVous">
		<p><a href="rendezVous/user/edit.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.edit"/></a>
		<a href="rendezVous/user/delete.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.delete"/></a></p>
	</display:column>
</display:table>

<a href="rendezVous/user/create.do"><spring:message code="rendezVous.create"/></a>