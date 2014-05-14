<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Cooper</title>
<link rel="stylesheet" href="${ctx}/styles/css/bootstrap.css">

<script type="text/javascript" src="${ctx}/styles/js/jquery-1.7.2.js"></script>

<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/jquery-ui-1.7.1.js"></script>

<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/jquery.highlighter.js"></script>

<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/Validform_v5.3.2.js"></script>

<script type="text/javascript" src="${ctx}/styles/js/bootstrap.js"></script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="navbar">
					<div class="navbar-inner">
						<div class="container-fluid">
							<a data-target=".navbar-responsive-collapse"
								data-toggle="collapse" class="btn btn-navbar"><span
								class="icon-bar"></span><span class="icon-bar"></span><span
								class="icon-bar"></span></a> <a href="#" class="brand">Cooper</a>
							<div class="nav-collapse collapse navbar-responsive-collapse">
								<ul class="nav">
									<li class="active"><a href="#">主页</a></li>
								</ul>
								<ul class="nav pull-right">
									<li class="dropdown"><a data-toggle="dropdown"
										class="dropdown-toggle" href="#">下拉菜单<strong class="caret"></strong></a>
										<ul class="dropdown-menu">
											<li><a href="#">下拉导航1</a></li>
											<li><a href="#">下拉导航2</a></li>
											<li><a href="#">其他</a></li>
											<li class="divider"></li>
											<li><a href="#">链接3</a></li>
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
