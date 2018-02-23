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

<p><strong><spring:message code="avg.rendezvousesCreatedPerUser"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgCreatedRendezVousesPerUser}" /></p>
<p><strong><spring:message code="std.rendezvousesCreatedPerUser"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdDeviationCreatedRendezVousesPerUser}"/></p>
<p><strong><spring:message code="ratio.usersCreatedRendevouses"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${usersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses}"/></p>
<p><strong><spring:message code="avg.usersPerRendezvous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgUsersPerRendezVous}"/></p>
<p><strong><spring:message code="std.usersPerRendezvous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdDeviationUsersPerRendezVous}"/></p>
<p><strong><spring:message code="avg.RSVPPerUser"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgRSVPPerUser}"/></p>
<p><strong><spring:message code="std.RSVPPerUser"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdDeviationRSVPPerUser}"/>

<h4><spring:message code="topTenMoreRSVP"/>:</h4>
<display:table name="topTenMoreRSVP" id="rendezVous" requestURI="administrator/display-dashboard.do" class="displaytag" pagesize="5">
	<display:column>
		<p><a href="rendezVous/administrator/display.do?rendezVousId=${rendezVous.id}"><jstl:out value="${rendezVous.name}"/></a></p>
	</display:column>
	<display:column property="orgDate" titleKey="rendezVous.orgDate" format="${dateFormat}"/>
	<display:column titleKey="rendezVous.coordinates">
		<jstl:if test="${rendezVous.coordinates ne null}">
			<p>(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)</p>
		</jstl:if>
		<jstl:if test="${rendezVous.coordinates eq null}">
			<p>-</p>
		</jstl:if>
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

<p><strong><spring:message code="avg.announcementsPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgAnnouncementsPerRendezVous}"/></p>
<p><strong><spring:message code="std.announcementsPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdAnnouncementsPerRendezVous}"/></p>

<h4><spring:message code="rendezVousAbove75"/>:</h4>
<display:table name="rendezVousAbove75" id="rendezVous" requestURI="administrator/display-dashboard.do" class="displaytag" pagesize="5">
	<display:column>
		<p><a href="rendezVous/administrator/display.do?rendezVousId=${rendezVous.id}"><jstl:out value="${rendezVous.name}"/></a></p>
	</display:column>
	<display:column property="orgDate" titleKey="rendezVous.orgDate" format="${dateFormat}"/>
	<display:column titleKey="rendezVous.coordinates">
		<jstl:if test="${rendezVous.coordinates ne null}">
			<p>(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)</p>
		</jstl:if>
		<jstl:if test="${rendezVous.coordinates eq null}">
			<p>-</p>
		</jstl:if>
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

<h4><spring:message code="rendezVousAboveAvgPlus10"/>:</h4>
<display:table name="rendezVousAboveAvgPlus10" id="rendezVous" requestURI="administrator/display-dashboard.do" class="displaytag" pagesize="5">
	<display:column>
		<p><a href="rendezVous/administrator/display.do?rendezVousId=${rendezVous.id}"><jstl:out value="${rendezVous.name}"/></a></p>
	</display:column>
	<display:column property="orgDate" titleKey="rendezVous.orgDate" format="${dateFormat}"/>
	<display:column titleKey="rendezVous.coordinates">
		<jstl:if test="${rendezVous.coordinates ne null}">
			<p>(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)</p>
		</jstl:if>
		<jstl:if test="${rendezVous.coordinates eq null}">
			<p>-</p>
		</jstl:if>
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
