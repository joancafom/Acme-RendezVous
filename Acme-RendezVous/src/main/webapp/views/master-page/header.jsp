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

<spring:message code="cookies" var="cookiesMessage"></spring:message>

<script>

var dropCookie = true;                      // false disables the Cookie, allowing you to style the banner
var cookieDuration = 14;                    // Number of days before the cookie expires, and the banner reappears
var cookieName = 'complianceCookie';        // Name of our cookie
var cookieValue = 'on';                     // Value of cookie

function createDiv(){
 var bodytag = document.getElementsByTagName('body')[0];
 var div = document.createElement('div');
 div.setAttribute('id','cookie-law');
 div.innerHTML = '<p>We request your permission to obtain stadistic data from your navigation in this webpage, in accordance with the Royal Decree-Law 13/2012. If you continue navigating, we consider that you accept the use of cookies.</p><p><a class="close-cookie-banner" href="javascript:void(0);" onclick="removeMe();"><span>Got it!</span></a></p><input type="button" id="hola" value="ReadMore"/><div id="readMore"><p>Hola</p></div>';    
  
 bodytag.insertBefore(div,bodytag.firstChild); // Adds the Cookie Law Banner just after the opening <body> tag
  
 document.getElementsByTagName('body')[0].className+=' cookiebanner'; //Adds a class tothe <body> tag when the banner is visible
  
 createCookie(window.cookieName,window.cookieValue, window.cookieDuration); // Create the cookie
}


function createCookie(name,value,days) {
 if (days) {
     var date = new Date();
     date.setTime(date.getTime()+(days*24*60*60*1000)); 
     var expires = "; expires="+date.toGMTString(); 
 }
 else var expires = "";
 if(window.dropCookie) { 
     document.cookie = name+"="+value+expires+"; path=/"; 
 }
}

function checkCookie(name) {
 var nameEQ = name + "=";
 var ca = document.cookie.split(';');
 for(var i=0;i < ca.length;i++) {
     var c = ca[i];
     while (c.charAt(0)==' ') c = c.substring(1,c.length);
     if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
 }
 return null;
}

function eraseCookie(name) {
 createCookie(name,"",-1);
}

window.onload = function(){
 if(checkCookie(window.cookieName) != window.cookieValue){
     createDiv(); 
 }
}

function removeMe(){
	var element = document.getElementById('cookie-law');
	element.parentNode.removeChild(element);
}

$(document).ready(function(){
	 $("#readMore").hide()
	    $("#hola").click(function(){
	        $("#readMore").toggle(1000);
	    });
	});

</script>

<style>

#cookie-law { 
    max-width:940px;
    background:#F5F5F5; 
    margin:10px auto 0; 
    border-radius: 17px;
    -webkit-border-radius: 17px;
    -moz-border-radius: 17px;
}
 
#cookie-law p { 
    padding:10px; 
    font-size:1.2em; 
    font-weight:bold; 
    text-align:center; 
    color:#FF5A72; 
    margin:0;
}

*{
	font-family: Arial;
}

</style>

<div id="sticky-header">
	<div id="logo-container">
		<img src="images/logo.png" alt="Sample Co., Inc." height="100%"/>
	</div>
	<div id="menu-container">
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<li><a href="administrator/display-dashboard.do"><spring:message code="master.page.administrator.dashboard"/></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a href="rendezVous/user/listMine.do"><spring:message code="master.page.user.myRendezVouses" /></a></li>
			<li><a href="user/user/list.do"><spring:message code="master.page.user.users" /></a></li>
			<li><a href="rendezVous/user/listAll.do"><spring:message code="master.page.user.allRendezVouses" /></a></li>
			<li><a href="announcement/user/list.do"><spring:message code="master.page.user.announcements" /></a></li>
			<li><a href="user/user/display.do"><spring:message code="master.page.profile" /> (<security:authentication property="principal.username" />)</a></li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li id="login" style="width:50%"><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li id="logout"><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
		</security:authorize>
	</ul>
	</div>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

