<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>

<%
	String errorMsg = null;
	Throwable ex = null;
	if (request.getAttribute("javax.servlet.error.exception") != null) {
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	} else {
		ex = exception;
	}

	//记录日志
	if (ex != null) {
		Logger logger = LoggerFactory.getLogger("500.jsp");
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		ex.printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		errorMsg = stringWriter.toString();
		
		logger.error(errorMsg);
	}

%>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="row-fluid">
				<div class="span12">
					<h3>系统发生内部错误.</h3>
					<a href="<c:url value='/'/>" target="_top">返回首页</a><br>
					<%=errorMsg%>
				</div>
			</div>
		</div>
	</div>
</div>

