<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Cooper</title>
<link rel="stylesheet" href="${ctx}/styles/css/bootstrap.min.css">
<script language="javascript" type="text/javascript" src="${ctx}/styles/js/jquery-1.7.2.min.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/styles/js/jquery-ui-1.7.1.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/styles/js/jquery.json-2.4.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/styles/js/jquery.highlighter.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/styles/js/jquery.latest.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/styles/js/jquery.tablesorter.js"></script>
<script language="javascript" type="text/javascript"src="${ctx}/styles/js/Validform_v5.3.2.js"></script>
<script language="javascript" type="text/javascript" src="${ctx}/styles/js/bootstrap.min.js"></script>
<script type="text/javascript">
function record(){
	$.ajax({url:"${ctx}/record/download"});
}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="navbar">
					<div class="navbar-inner">
						<div class="container-fluid">
						    <a href="${ctx}" class="brand">Cooper</a><a href="${ctx}" class="brand" style="font-size:16px;">致力于提高代码结构的质量</a>
							<div class="nav-collapse collapse navbar-responsive-collapse">
								<ul class="nav pull-right">
									<li class="dropdown"><a data-toggle="dropdown"
										class="dropdown-toggle" href="#">菜单<strong class="caret"></strong></a>
										<ul class="dropdown-menu">
											<li><a href="${ctx}/standalone/Cooper.zip" onclick="record()">下载单机版</a></li>
											<li><a href="https://github.com/jdepend/cooper">访问源码</a></li>
											<li><a href="https://www.ohwyaa.com/profile/wangdg/view">了解作者</a></li>
											<shiro:user>
												<li class="divider"></li>
												<li><a href="${ctx}/logout">退出</a></li>
											</shiro:user>
										</ul></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<decorator:body />
</body>
</html>
