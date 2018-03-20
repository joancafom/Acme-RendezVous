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
			<spring:message code="category.childCategories" />: <jstl:out value="${rootCategory.name}" />
			<jstl:set var="categoriesCollection" value="${rootCategory.childCategories}" />
			<jstl:set var="parentCategoryId" value="${rootCategory.id}" />
		</jstl:otherwise>
	</jstl:choose>

</h2>

<display:table name="categoriesCollection" id="categoryItem" requestURI="category/admin/list.do" class="displaytag">
	<display:column titleKey="category.name" sortable="true">
		<a href="category/admin/list.do?rootCategoryId=${categoryItem.id}"><jstl:out value="${categoryItem.name}" /></a>
	</display:column>
	<display:column titleKey="category.description" property="categoryItem.description" />
	<display:column>
		<a href="category/admin/edit.do?categoryId=${categoryItem.id}"><spring:message code="category.edit"/></a>
	</display:column>
</display:table>

<a href="category/admin/create.do?parentCategoryId=${parentCategoryId}"><spring:message code="category.create"/></a>