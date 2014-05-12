<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<form method="POST" enctype="multipart/form-data">
				<fieldset>
					<legend>第一步</legend>
					<label>分析的jar路径</label>
						<input type="file" name="fileUpload"/>
					</div>
					<span class="help-block">（只能上传一个jar文件）.</span>
					<button class="btn" onclick="analyse()">下一步</button>
				</fieldset>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	function analyse(){
		document.forms[0].action = "${ctx}/upload";
		document.forms[0].submit();
	}
</script>

