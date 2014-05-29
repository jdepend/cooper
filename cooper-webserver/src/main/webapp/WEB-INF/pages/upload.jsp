<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<form method="POST" enctype="multipart/form-data">
				<fieldset>
					<legend>第一步：选择分析的jar（共两步）</legend>
					<label>分析的jars：</label>
						<input type="file" name="files" multiple="true"/>
					</div>
					<span class="help-block">（IE9及以下不支持上传多文件，其它浏览器最新版本均已支持）</span>
					<button class="btn" onclick="analyse()">下一步</button>
				</fieldset>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	function analyse(){
		document.forms[0].action = "${ctx}/analyse/upload";
		document.forms[0].submit();
	}
</script>

