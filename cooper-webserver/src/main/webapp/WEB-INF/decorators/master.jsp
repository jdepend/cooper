<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="version" value="0.0.0"/>
<html>
<head>
    <title>Cooper</title>
    <link rel="stylesheet" href="${ctx}/styles/css/bootstrap.css?version=${version}">
    
</head>
<body>
<decorator:body/>
</body>
</html>
