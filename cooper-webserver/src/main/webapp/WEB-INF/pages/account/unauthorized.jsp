<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${msg!=null }">
	<p style="color: red; margin-left: 10px;">${msg }</p>
</c:if>
