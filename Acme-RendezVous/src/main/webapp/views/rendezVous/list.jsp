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
<jsp:useBean id="now" class="java.util.Date" />

<jstl:if test="${category!=null}">
	<jstl:set var="parentCategory" value="${category.name}"/>
</jstl:if>

<jstl:if test="${hasCategories==true}">
	<jstl:if test="${category==null}">
		<h2><spring:message code="rendezVous.categories"/></h2>
	</jstl:if>
	<jstl:if test="${category!=null}">
		<h2><spring:message code="rendezVous.subcategories"/> <jstl:out value="${category.name}"/></h2>
	</jstl:if>
	<display:table name="categories" id="category" requestURI="rendezVous${actorWS}/list.do" class="displaytag">
		<display:column titleKey="category.name">
			<a href="rendezVous/${actorWS}list.do?show=category&categoryId=${category.id}"><jstl:out value="${category.name}"/></a>
		</display:column>
		<display:column titleKey="category.description">
			<jstl:out value="${category.description}"/>
		</display:column>
	</display:table>
</jstl:if>

<jstl:if test="${parentCategory!=null}">
	<h2><spring:message code="rendezVous.rendezVouses.category"/> <jstl:out value="${parentCategory}"/></h2>
</jstl:if>

<jstl:if test="${hasRendezVouses==true}">
<jstl:if test="${!own}">
	<display:table name="rendezVouses" id="rendezVous" requestURI="rendezVous/${actorWS}list.do" class="displaytag" pagesize="5">
		<display:column titleKey="rendezVous.state" class="tableRendezVous">
			<jstl:if test="${rendezVous.isDeleted==true}">
				<p style="color:red;"><strong><spring:message code="rendezVous.deleted"/></strong></p>
			</jstl:if>
			<jstl:if test="${rendezVous.isDeleted==false}">
				<p style="color:green;"><strong><spring:message code="rendezVous.public"/></strong></p>
			</jstl:if>
			<jstl:if test="${rendezVous.orgDate < now and rendezVous.isDeleted==false}">
				<p style="color:gray;"><strong><spring:message code="rendezVous.ended"/></strong></p>
			</jstl:if>
		</display:column>
		<display:column titleKey="rendezVous.name">
			<p><jstl:out value="${rendezVous.name}"/></p>
		</display:column>
		<display:column titleKey="rendezVous.description">
			<p><jstl:out value="${rendezVous.description}"/></p>
		</display:column>
		
		<display:column titleKey="rendezVous.orgDate" class="tableRendezVous">
			<acme:dateFormat code="date.format2" value="${rendezVous.orgDate}" type="both"/>
		</display:column>
		
		<display:column class="tableRendezVous">
			<p><a href="rendezVous/${actorWS}display.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.display"/></a></p>
		</display:column>
		
		<security:authorize access="hasRole('USER')">
		<display:column class="tableRendezVous">
			<jstl:set var="contains" value="false"/>
			<jstl:forEach var="attendant" items="${rendezVous.attendants}">
				<jstl:if test="${attendant eq me}">
					<jstl:set var="contains" value="true"/>
				</jstl:if>
			</jstl:forEach>
			
			<jstl:if test="${contains and rendezVous.orgDate >= now and !rendezVous.isDeleted}">
				<a href="rendezVous/${actorWS}cancel.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.rsvp.cancel"/></a>
			</jstl:if>
			
			<jstl:if test="${!contains and rendezVous.orgDate >= now and !rendezVous.isDeleted}">
				<a href="rendezVous/${actorWS}rsvp.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.rsvp.accept"/></a>
			</jstl:if>
		</display:column>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column class="tableRendezVous">
			<a href="rendezVous/administrator/remove.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.remove"/></a>
		</display:column>
		</security:authorize>
		
		
	</display:table>
</jstl:if>

<jstl:if test="${own}">
	<display:table name="rendezVouses" id="rendezVous" requestURI="rendezVous/user/list.do" class="displaytag" pagesize="5">
		<display:column titleKey="rendezVous.state" class="tableRendezVous">
			<jstl:if test="${rendezVous.isDeleted==true}">
				<p style="color:red;"><strong><spring:message code="rendezVous.deleted"/></strong></p>
			</jstl:if>
			<jstl:if test="${rendezVous.isDeleted==false}">
				<p style="color:green;"><strong><spring:message code="rendezVous.public"/></strong></p>
			</jstl:if>
			<jstl:if test="${rendezVous.orgDate < now and rendezVous.isDeleted==false}">
				<p style="color:gray;"><strong><spring:message code="rendezVous.ended"/></strong></p>
			</jstl:if>
		
		</display:column>
		<display:column titleKey="rendezVous.name">
			<p><jstl:out value="${rendezVous.name}"/></p>
		</display:column>
		<display:column titleKey="rendezVous.description" style="width: 20%;">
			<p><jstl:out value="${rendezVous.description}"/></p>
		</display:column>
		
		<display:column titleKey="rendezVous.orgDate" class="tableRendezVous">
			<acme:dateFormat code="date.format2" value="${rendezVous.orgDate}" type="both"/>
		</display:column>
		
		<display:column titleKey="rendezVous.coordinates" class="tableRendezVous">
			<jstl:if test="${rendezVous.coordinates.latitude!=null || rendezVous.coordinates.longitude!=null}">
				<p>(Lat: <jstl:out value="${rendezVous.coordinates.latitude}"/>, Long: <jstl:out value="${rendezVous.coordinates.longitude}"/>)</p>
			</jstl:if>
		</display:column>
		<display:column titleKey="rendezVous.restrictions" class="tableRendezVous">
			<jstl:if test="${rendezVous.isForAdults==true}">
				<p style="color:red; text-decoration: underline;"><strong>+18</strong></p>
			</jstl:if>
			<jstl:if test="${rendezVous.isForAdults==false}">
				<p>-</p>
			</jstl:if>
		</display:column>
		<display:column titleKey="rendezVous.mode" class="tableRendezVous">
			<jstl:if test="${rendezVous.isFinal==false}">
				<p><spring:message code="rendezVous.mode.draft"/></p>
			</jstl:if>
			<jstl:if test="${rendezVous.isFinal==true}">
				<p><spring:message code="rendezVous.mode.final"/></p>
			</jstl:if>
		</display:column>
		<display:column class="tableRendezVous">
		
			<p><a href="rendezVous/user/display.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.display"/></a></p>
		</display:column>
		<display:column class="tableRendezVous">
			<jstl:if test="${rendezVous.isDeleted==false}">
				<p>
				<jstl:if test="${rendezVous.isFinal==false}">
					<a href="rendezVous/user/edit.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.edit"/></a>
					<a href="rendezVous/user/delete.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.delete"/></a>
				</jstl:if>
				</p>
			</jstl:if>
		</display:column>
		
		
		<display:column class="tableRendezVous">
			<jstl:set var="contains" value="false"/>
			<jstl:forEach var="attendant" items="${rendezVous.attendants}">
				<jstl:if test="${attendant eq me}">
					<jstl:set var="contains" value="true"/>
				</jstl:if>
			</jstl:forEach>
			
			<jstl:if test="${contains and rendezVous.orgDate >= now and !rendezVous.isDeleted}">
				<a href="rendezVous/${actorWS}cancel.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.rsvp.cancel"/></a>
			</jstl:if>
			
			<jstl:if test="${!contains and rendezVous.orgDate >= now and !rendezVous.isDeleted}">
				<a href="rendezVous/${actorWS}rsvp.do?rendezVousId=${rendezVous.id}"><spring:message code="rendezVous.rsvp.accept"/></a>
			</jstl:if>
		</display:column>
		
	</display:table>
	
	<a href="rendezVous/user/create.do"><spring:message code="rendezVous.create"/></a>
</jstl:if>
</jstl:if>
