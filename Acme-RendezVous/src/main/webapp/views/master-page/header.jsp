<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<spring:message code="cookies" var="cookiesMessage"/>

<script>

window.onload = function(){
	// If the banner cookie isn't on
	if(cookieCurrentValue(window.cookieName) != window.cookieValue){
    	createDiv('${cookiesMessage}'); 
    	$("#readMore").hide();
    }
};

</script>


<div id="sticky-header">
	<div id="logo-container">
		<a href=""><img src="images/logo.png" alt="Acme, Inc" height="100%"/></a>
	</div>
	<div id="menu-container">
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<li><a href="administrator/display-dashboard.do"><spring:message code="master.page.administrator.dashboard"/></a></li>
			<li><a href="rendezVous/administrator/list.do"><spring:message code="master.page.administrator.rendezVouses"/></a></li>
			<li><a href="service/administrator/list.do"><spring:message code="master.page.actor.service" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a href="rendezVous/user/list.do?show=mine"><spring:message code="master.page.user.myRendezVouses" /></a></li>
			<li><a href="rendezVous/user/list.do?show=attended"><spring:message code="master.page.user.attendedRendezVouses" /></a></li>
			<li><a href="user/user/list.do"><spring:message code="master.page.user.users" /></a></li>
			<li><a href="rendezVous/user/list.do?show=all"><spring:message code="master.page.user.allRendezVouses" /></a></li>
			<li><a href="announcement/user/list.do"><spring:message code="master.page.user.announcements" /></a></li>
			<li><a href="service/user/list.do"><spring:message code="master.page.actor.service" /></a></li>
			<li><a href="user/user/display.do"><spring:message code="master.page.profile" /> (<security:authentication property="principal.username" />)</a></li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a href="rendezVous/list.do"><spring:message code="master.page.user.allRendezVouses" /></a></li>
			<li><a href="user/list.do"><spring:message code="master.page.user.allUsers" /></a></li>
			<li><a href="user/register.do"><spring:message code="master.page.user.register" /></a></li>
			<li><a href="manager/register.do"><spring:message code="master.page.manager.register" /></a></li>
			<li id="login" style="width:50%"><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li id="logout"><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
		</security:authorize>
	</ul>
	</div>
</div>