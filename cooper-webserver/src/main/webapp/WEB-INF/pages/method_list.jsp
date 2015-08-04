<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<table id="listMethodsTable" class="table table-bordered" pa_ui_name="table,exinput"
	pa_ui_hover="true" pa_ui_selectable="true" pa_ui_select_mode="multi"
	pa_ui_select_trigger="tr" pa_ui_select_column="0"
	pa_ui_select_triggerelement=":checkbox">
	<thead>
		<tr style="cursor: pointer;">
			<th>选择</th>
			<th>访问修饰符</th>
			<th>名称</th>
			<th>参数</th>
			<th>返回值</th>
			<th>行数</th>
			<th>传入</th>
			<th>级联传入</th>
			<th>传出</th>
			<th>稳定性</th>
			<th>是否包含进程间调用</th>
			<th>是否被进程间调用</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${methods}" var="method">
			<tr>
				<td><input type="checkbox" class="methodId" classid="${method.javaClass.id}" methodInfo="${method.info}"/></td>
				<td>${method.accessFlagName}</td>
				<td>${method.name}</td>
				<td>${method.argumentInfo}</td>
				<td>${method.returnTypes}</td>
				<td>${method.selfLineCount}</td>
				<td class="invokedMethods" style="color:blue;cursor:pointer;">${fn:length(method.invokedMethods)}</td>
				<td class="cascadeInvokedMethods" style="color:blue;cursor:pointer;">${fn:length(method.cascadeInvokedMethods)}</td>
				<td class="invokeMethods" style="color:blue;cursor:pointer;">${fn:length(method.invokeMethods)}</td>
				<td><fmt:formatNumber value="${method.stability}" pattern="#.###" /></td>
				<td>${item.remoteInvokeItem ? "是":"否"}</td>
				<td>${item.remoteInvokedItem ? "是":"否"}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<script type="text/javascript">
$('.invokedMethods').mousedown(function(e){
	e.stopPropagation();
});
$('.invokedMethods').click(function(){
	var javaClassId = $(this).parent().find('.methodId').attr('classid');
	var methodInfo = $(this).parent().find('.methodId').attr('methodInfo');
	$.ajax({    
	    url:'${ctx}/result/invokeitems/' + javaClassId + '/' + methodInfo + '/view.ajax',   
	    type:'get',    
	    success:function(data) {
	    	$('#myModalLabel3').text(methodInfo + '方法传入列表');
	    	$('#myData3').html(data);
	    	$('#myModal3').modal('toggle');
	    	
	    	$("#listMethodsTable").tablesorter();
	    }   
	});
});
</script>