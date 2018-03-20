<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h2>

	<jstl:choose>
		<jstl:when test="${rootCategories ne null}">
			<spring:message code="category.rootCategories" />
			<jstl:set var="categoriesCollection" value="${rootCategories}" />
			<jstl:set var="parentCategoryId" value="" />
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="category.childCategories" />: 
			<jstl:choose>
				<jstl:when test="${rootCategory.parentCategory ne null}">
					<a href="category/administrator/list.do?rootCategoryId=<jstl:out value="${rootCategory.parentCategory.id}" />"><jstl:out value="${rootCategory.parentCategory.name}" /></a>/<jstl:out value="${rootCategory.name}" />
				</jstl:when>
				<jstl:otherwise>
					<a href="category/administrator/list.do">...</a>/<jstl:out value="${rootCategory.name}" />
				</jstl:otherwise>
			</jstl:choose>
	
			<jstl:set var="categoriesCollection" value="${rootCategory.childCategories}" />
			<jstl:set var="parentCategoryId" value="${rootCategory.id}" />
		</jstl:otherwise>
	</jstl:choose>

</h2>

<display:table name="${categoriesCollection}" id="categoryItem" requestURI="category/administrator/list.do" class="displaytag" pagesize="5" >
	<display:column titleKey="category.name" sortable="true">
		<a href="category/administrator/list.do?rootCategoryId=${categoryItem.id}"><jstl:out value="${categoryItem.name}" /></a>
	</display:column>
	<display:column titleKey="category.description" property="description" />
	<display:column>
		<a href="category/administrator/edit.do?categoryId=${categoryItem.id}"><spring:message code="category.edit"/></a>
	</display:column>
</display:table>

<a href="category/administrator/create.do?parentCategoryId=${parentCategoryId}"><spring:message code="category.create"/></a>