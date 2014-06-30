<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="layout">
	<div class="login">
		<div class="login-form">
			<form name="f" action="${ctx}/login" method="POST">
				<c:if test="${msg!=null }">
					<p style="color: red; margin-left: 10px;">${msg }</p>
				</c:if>

				<div class="control-group">
					<div class="controls">
						<div
							class="input-prepend ${not empty param.login_error ? 'error' : ''}">
							<span class="add-on">邮箱&nbsp;</span> <input id="username"
								name='username' type="text" />
						</div>
					</div>
				</div>

				<div class="control-group">
					<div class="controls">
						<div
							class="input-prepend ${not empty param.login_error ? 'error' : ''}">
							<span class="add-on">密码&nbsp;</span> <input id="password"
								name='password' type="password" />
						</div>
					</div>
				</div>

				<div class="control-group">
					<div class="controls rememberme">
						<label> <input type="checkbox" name="rememberMe" value="true"> <span>两周内记住我</span></label>
						<div style="clear:both"></div>
					</div>
				</div>

				<%-- 登录失败的处理 --%>
				<c:if test="${not empty param.login_error}">
					<div class="control-group">
						<div class="controls rememberme">
							<span class="login_error"> <strong>登录失败：</strong> <c:if
									test="${param.login_error == 2}">
								非法的越权访问，必须重新登录。
								</c:if> <c:if test="${(param.login_error == 1) and (true) }">
									帐号被锁定，请<a class="link" href="mailto:ohwyaa@neusoft.com">联系</a>网站管理员。
								</c:if> <c:out value="TODO" />
							</span>
						</div>
					</div>
				</c:if>

				<div class="name">
					<h3></h3>
					<h2></h2>
				</div>

				<div class="control-group">
					<div class="controls submit-a">
						<input name="submit" type="submit" class="btn-primary-new"
							value="登录" />
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<style type="text/css">
.login {
	font-family: '微软雅黑';
	width: 400px;
	margin: auto;
	margin-top: 10%;
}

.login .name h2 {
	margin-top: 0;
	border-top: 1px solid #fff;
}

.login .name h3 {
	border-bottom: 1px solid #ddd;
	padding: 0 0 20px 0;
	margin-bottom: 0;
	margin-top: 12px;
	color: #444;
	font-size: 24px;
	font-weight: normal;
}

.login .name h3 span {
	font-size: 15px;
}

.login .input-prepend .add-on {
	font-size: 16px;
	color: #666;
}

.login .rememberme label span {
	float: left;
	margin: -1px 0 0 4px;
}

.login .rememberme label input {
	margin-left: 0;
	float: left;
}

.login .rememberme {
	font-size: 14px;
	color: #666;
	margin-left: 43px;
}

.login .input-prepend input:focus {
	border-color: #2795DC !important;
}

.login .login_error {
	color: #E54245;
}

.login .input-prepend.error input {
	border-color: #E54245 !important;
}

.login .input-prepend input {
	color: #555 !important;
	border-radius: 3px;
	width: 260px;
	height: 30px;
	line-height: 30px;
	font-size: 15px;
	border: 1px solid #ccc !important;
	padding: 2px !important;
	background: white !important;
}

.login .control-group {
	padding-top: 12px;
	margin: 8px 0 10px 0;
}

.login .control-group a.link:hover {
	text-decoration: underline;
}

.login .control-group a.link {
	color: #666;
	font-size: 14px;
	text-decoration: none;
}

.login .control-group .submit-a a.link {
	margin-left: 12px;
}

.submit-a {
	margin-top: -6px;
}

.submit-a .btn-primary-new:hover {
	background: #1F86C9;
}

.submit-a .btn-primary-new {
	padding: 6px 30px !important;
	background: #2795dc;
	border-bottom: 3px solid #0078b3;
	color: #fff;
	cursor: pointer;
	border-radius: 4px;
	font-size: 16px;
}
</style>
