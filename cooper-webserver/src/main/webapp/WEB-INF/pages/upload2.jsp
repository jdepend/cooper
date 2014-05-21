<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<form method="POST" enctype="multipart/form-data">
				<fieldset>
					<legend>第一步：选择分析的jar</legend>
					<input type="button" value="添加附件" onclick="createInput('more');" />
				    <div id="more"></div>
					<button class="btn" onclick="analyse()">下一步</button>
				</fieldset>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	var count = 1;
	/** 
	 * 生成多附件上传框 
	 */
	function createInput(parentId) {
		count++;
		var str = '<div name="div" ><font style="font-size:12px;">附件</font>'
				+ '   '
				+ '<input type="file" contentEditable="false" id="uploads' + count + '' +  
    '" name="uploads'+ count +'" value="" style="width: 220px"/><input type="button"  value="删除" onclick="removeInput(event)" />'
				+ '</div>';
		document.getElementById(parentId).insertAdjacentHTML("beforeEnd", str);
	}
	/** 
	 * 删除多附件删除框 
	 */
	function removeInput(evt, parentId) {
		var el = evt.target == null ? evt.srcElement : evt.target;
		var div = el.parentNode;
		var cont = document.getElementById(parentId);
		if (cont.removeChild(div) == null) {
			return false;
		}
		return true;
	}
	function analyse(){
		document.forms[0].action = "${ctx}/analyse/upload2";
		document.forms[0].submit();
	}
</script>

