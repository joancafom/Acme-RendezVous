<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>

<spring:message code="number.format" var="numberFormat"/>
<spring:message code="date.format" var="dateFormat"/>

<p><spring:message code="avg.rendezvousesCreatedPerUser"/>: <fmt:formatNumber pattern="${numberFormat}" value="${avgCreatedRendezVousesPerUser}" /></p>
<p><spring:message code="std.rendezvousesCreatedPerUser"/>: <fmt:formatNumber pattern="${numberFormat}" value="${stdDeviationCreatedRendezVousesPerUser}"/></p>
<p><spring:message code="ratio.usersCreatedRendevouses"/>: <fmt:formatNumber pattern="${numberFormat}" value="${usersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses}"/></p>
<p><spring:message code="avg.usersPerRendezvous"/>: <fmt:formatNumber pattern="${numberFormat}" value="${avgUsersPerRendezVous}"/></p>
<p><spring:message code="std.usersPerRendezvous"/>: <fmt:formatNumber pattern="${numberFormat}" value="${stdDeviationUsersPerRendezVous}"/></p>
<p><spring:message code="avg.RSVPPerUser"/>: <fmt:formatNumber pattern="${numberFormat}" value="${avgRSVPPerUser}"/></p>
<p><spring:message code="std.RSVPPerUser"/>: <fmt:formatNumber pattern="${numberFormat}" value="${stdDeviationRSVPPerUser}"/>

<display:table name="topTenMoreRSVP" id="rendezVous" requestURI="administrator/display-dashboard.do" class="displaytag">
	<display:column property="name" titleKey="rendezVous.name"/>
	<display:column property="orgDate" titleKey="rendezVous.orgDate" format="${dateFormat}"/>
	<display:column titleKey="rendezVous.coordinates">
		<p>(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)</p>
	</display:column>
	<display:column titleKey="rendezVous.restrictions">
		<jstl:if test="${rendezVous.isForAdults == true}">
			<p>+18</p>
		</jstl:if>
		<jstl:if test="${rendezVous.isForAdults == false}">
			<p>-</p>
		</jstl:if>
	</display:column>
</display:table>