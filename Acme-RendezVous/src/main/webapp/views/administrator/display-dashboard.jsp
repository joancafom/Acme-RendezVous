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
<p><strong><spring:message code="avg.questionsPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgQuestionsPerRendezVous}"/></p>
<p><strong><spring:message code="std.questionsPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdQuestionsPerRendezVous}"/></p>
<p><strong><spring:message code="avg.repliesPerComment"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgRepliesPerComment}"/></p>
<p><strong><spring:message code="std.repliesPerComment"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdRepliesPerComment}"/></p>
<p><strong><spring:message code="avg.answersPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgAnswersPerRendezVous}"/></p>
<p><strong><spring:message code="std.answersPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdAnswersPerRendezVous}"/></p>

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

<h4><spring:message code="bestSellingServices"/>:</h4>
<display:table name="bestSellingServices" id="service" requestURI="administrator/display-dashboard.do" style="text-align:center;" class="displaytag" pagesize="5">
	
	<display:column titleKey="service.state">
		<jstl:if test="${service.isCanceled}">
			<p style="color:red;"><strong><spring:message code="service.canceled"/></strong></p>
		</jstl:if>
		<jstl:if test="${!service.isCanceled}">
			<p style="color:green;"><strong><spring:message code="service.available"/></strong></p>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="service.name" property="name" />
	<display:column titleKey="service.description" property="description" />
	
	<display:column>
		<jstl:if test="${service.picture ne null}">
			<img src="<jstl:out value="${service.picture}" />" alt="<spring:message code="img.alt.service.picture" />" style="max-width: 200px;" />
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${!service.isCanceled}">
			<a href="service/administrator/cancel.do?serviceId=<jstl:out value="${service.id}" />"><spring:message code="service.cancel" /></a>
		</jstl:if>
	</display:column>
	
</display:table>

<h4><spring:message code="managersMoreServicesThanAverage"/>:</h4>
<display:table name="managersMoreServicesThanAverage" id="manager" requestURI="administrator/display-dashboard.do" style="text-align:center;" class="displaytag" pagesize="5">

	<display:column titleKey="manager.vat" property="vat"/>
	<display:column titleKey="manager.name" property="name"/>
	<display:column titleKey="manager.surnames" property="surnames"/>
	<display:column titleKey="manager.postalAddress" property="postalAddress"/>
	<display:column titleKey="manager.phoneNumber" property="phoneNumber"/>
	<display:column titleKey="manager.email" property="email"/>

</display:table>

<h4><spring:message code="managersWithMoreServicesCancelled"/>:</h4>
<display:table name="managersWithMoreServicesCancelled" id="manager" requestURI="administrator/display-dashboard.do" style="text-align:center;" class="displaytag" pagesize="5">

	<display:column titleKey="manager.vat" property="vat"/>
	<display:column titleKey="manager.name" property="name"/>
	<display:column titleKey="manager.surnames" property="surnames"/>
	<display:column titleKey="manager.postalAddress" property="postalAddress"/>
	<display:column titleKey="manager.phoneNumber" property="phoneNumber"/>
	<display:column titleKey="manager.email" property="email"/>

</display:table>

<!-- RendezVous 2.0 - Level B -->
<p><strong><spring:message code="avg.categoriesPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgCategoriesPerRendezVous}"/></p>
<p><strong><spring:message code="avg.ratioServicesPerCategory"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgRatioServicesPerCategory}"/></p>
<p><strong><spring:message code="avg.servicesRequestedPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgServicesRequestedPerRendezVous}"/></p>
<p><strong><spring:message code="min.servicesRequestedPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${minServicesRequestedPerRendezVous}"/></p>
<p><strong><spring:message code="max.servicesRequestedPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${maxServicesRequestedPerRendezVous}"/></p>
<p><strong><spring:message code="std.servicesRequestedPerRendezVous"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdServicesRequestedPerRendezVous}"/></p>
<br/>
<h4><spring:message code="topSellingServices"/>:</h4>
<display:table name="topSellingServices" id="service" requestURI="administrator/display-dashboard.do" style="text-align:center;" class="displaytag" pagesize="5">
	
	<display:column titleKey="service.state">
		<jstl:if test="${service.isCanceled}">
			<p style="color:red;"><strong><spring:message code="service.canceled"/></strong></p>
		</jstl:if>
		<jstl:if test="${!service.isCanceled}">
			<p style="color:green;"><strong><spring:message code="service.available"/></strong></p>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="service.name" property="name" />
	<display:column titleKey="service.description" property="description" />
	
	<display:column>
		<jstl:if test="${service.picture ne null}">
			<img src="<jstl:out value="${service.picture}" />" alt="<spring:message code="img.alt.service.picture" />" style="max-width: 200px;" />
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${!service.isCanceled}">
			<a href="service/administrator/cancel.do?serviceId=<jstl:out value="${service.id}" />"><spring:message code="service.cancel" /></a>
		</jstl:if>
	</display:column>
	
</display:table>