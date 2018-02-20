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

var cookieDuration = 14;
var cookieName = 'bannerCookie';
var cookieValue = 'on';

function createDiv(){
 	var body = document.getElementsByTagName('body')[0];
 	var div = document.createElement('div');
 	
 	div.setAttribute('id','cookieBanner');
 	div.innerHTML = '${cookiesMessage}';
  
 	// Adds the Cookie Banner just after the opening <body> tag
 	body.insertBefore(div, body.firstChild);
  
 	// Create the cookie
 	createCookie(window.cookieName, window.cookieValue, window.cookieDuration);
}


function createCookie(name,value,days) {
	var expires = "";
	
	// Set the expiring date
 	if (days) {
    	var date = new Date();
     	date.setTime(date.getTime() + (days * 24 * 60 * 60 *1000)); 
     	
     	expires = "expires=" + date.toGMTString() + ";"; 
 	}
	
 	// Create the cookie with its name, value and expiring date
 	document.cookie = name + "=" + value + "; " + expires + " path=/"; 
}


function cookieCurrentValue(name) {
	var nameEquals = name + "=";
	var languageEquals = window.languageName + "=";
	
 	var allCookies = document.cookie.split(';');
 	
 	// Iterate over all the cookies that are in the page
 	for(var i=0; i < allCookies.length; i++) {
    	var currentCookie = allCookies[i];
     
    	// Eliminate spaces at the begining
    	while (currentCookie.charAt(0)==' ') currentCookie = currentCookie.substring(1, currentCookie.length);
    	
    	// Set language cookie
    	if (currentCookie.indexOf(languageEquals) == 0) window.languageValue = currentCookie.substring(languageEquals.length, currentCookie.length);
    	
    	// When we are in the banner cookie, return a substring with its value
    	if (currentCookie.indexOf(nameEquals) == 0) return currentCookie.substring(nameEquals.length, currentCookie.length);

 	}
 	return null;
}

window.onload = function(){
	
	// If the banner cookie isn't on
	if(cookieCurrentValue(window.cookieName) != window.cookieValue){
    	createDiv(); 
    	$("#readMore").hide();
    }
}

function removeMe(){
	var element = document.getElementById('cookieBanner');
	element.parentNode.removeChild(element);
}

function toggleMoreInfo(){
	$("#readMore").toggle(100);
}

</script>

<style>

#cookieBanner { 
    max-width:940px;
    background:#F5F5F5; 
    margin:10px auto 0; 
    border-radius: 17px;
    -webkit-border-radius: 17px;
    -moz-border-radius: 17px;
}
 
#cookieBanner p, input { 
    padding:10px; 
    font-size:1.2em; 
    font-weight:bold; 
    text-align:center; 
    color:#6699ff; 
    margin:0;
}

</style>

<div>
	<img src="images/logo.png" alt="Sample Co., Inc." />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/action-1.do"><spring:message code="master.page.administrator.action.1" /></a></li>
					<li><a href="administrator/action-2.do"><spring:message code="master.page.administrator.action.2" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv"><spring:message	code="master.page.customer" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="customer/action-1.do"><spring:message code="master.page.customer.action.1" /></a></li>
					<li><a href="customer/action-2.do"><spring:message code="master.page.customer.action.2" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/action-1.do"><spring:message code="master.page.profile.action.1" /></a></li>
					<li><a href="profile/action-2.do"><spring:message code="master.page.profile.action.2" /></a></li>
					<li><a href="profile/action-3.do"><spring:message code="master.page.profile.action.3" /></a></li>					
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

