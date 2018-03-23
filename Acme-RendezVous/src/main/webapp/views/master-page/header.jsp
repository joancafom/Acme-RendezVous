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

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <img src="<jstl:out value="${logo}" />" style="max-height: 50px;"/>
      <a class="navbar-brand" href="#"><jstl:out value="${businessName}" /></a>
    </div>
    <security:authorize access="hasRole('ADMINISTRATOR')">
    	<ul class="nav navbar-nav">
			<li><a href="administrator/display-dashboard.do"><spring:message code="master.page.administrator.dashboard"/></a></li>
			<li><a href="rendezVous/administrator/list.do"><spring:message code="master.page.administrator.rendezVouses"/></a></li>
			<li><a href="service/administrator/list.do"><spring:message code="master.page.actor.service" /></a></li>
			<li><a href="service/categories/list.do"><spring:message code="master.page.administrator.categories" /></a></li>
			<li><a href="systemConfiguration/administrator/display.do"><spring:message code="master.page.administrator.systemConfiguration" /></a></li>
		</ul>
	</security:authorize>
	<security:authorize access="hasRole('MANAGER')">
    	<ul class="nav navbar-nav">
			<li><a href="service/manager/list.do"><spring:message code="master.page.manager.myServices" /></a></li>
			<li><a href="service/manager/list.do?show=all"><spring:message code="master.page.manager.allServices" /></a></li>
		</ul>
	</security:authorize>
    <security:authorize access="hasRole('USER')">
    	<ul class="nav navbar-nav">
			<li><a href="rendezVous/user/list.do?show=mine"><spring:message code="master.page.user.myRendezVouses" /></a></li>
			<li><a href="rendezVous/user/list.do?show=attended"><spring:message code="master.page.user.attendedRendezVouses" /></a></li>
			<li><a href="user/user/list.do"><spring:message code="master.page.user.users" /></a></li>
			<li><a href="rendezVous/user/list.do?show=all"><spring:message code="master.page.user.allRendezVouses" /></a></li>
			<li><a href="rendezVous/user/list.do?show=category"><spring:message code="master.page.user.allRendezVousesByCategory" /></a></li>
			<li><a href="announcement/user/list.do"><spring:message code="master.page.user.announcements" /></a></li>
			<li><a href="service/user/list.do"><spring:message code="master.page.actor.service" /></a></li>
			<li><a href="user/user/display.do"><spring:message code="master.page.profile" /> (<security:authentication property="principal.username" />)</a></li>
			<li><a href="rendezVous/list.do?show=all"><spring:message code="master.page.user.allRendezVouses" /></a></li>
			<li><a href="rendezVous/list.do?show=category"><spring:message code="master.page.user.allRendezVousesByCategory" /></a></li>
			<li><a href="user/list.do"><spring:message code="master.page.user.allUsers" /></a></li>
			<li><a href="user/register.do"><spring:message code="master.page.user.register" /></a></li>
			<li><a href="manager/register.do"><spring:message code="master.page.manager.register" /></a></li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
      		<li><a href="user/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.singup" /></a></li>
      		<li><a href="security/login.do"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="master.page.login" /></a></li>
    	</ul>
	</security:authorize>
	
	<security:authorize access="isAuthenticated()">
		<ul class="nav navbar-nav navbar-right">
      		<li><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
  </div>
</nav>