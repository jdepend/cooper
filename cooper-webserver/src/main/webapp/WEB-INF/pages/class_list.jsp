<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<table id="listJavaClassTable" class="table table-bordered" pa_ui_name="table,exinput"
	pa_ui_hover="true" pa_ui_selectable="true" pa_ui_select_mode="multi"
	pa_ui_select_trigger="tr" pa_ui_select_column="0"
	pa_ui_select_triggerelement=":checkbox">
	<thead>
		<tr style="cursor: pointer;">
			<th>选择</th>
			<th>名称</th>
			<th>代码行数</th>
			<th>方法数量</th>
			<th>传入</th>
			<th>传出</th>
			<th>抽象程度</th>
			<th>耦合值</th>
			<th>内聚值</th>
			<th>内聚性</th>
			<th>是否稳定</th>
			<th>是否存在状态</th>
			<th>是否私有</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${classes}" var="item">
			<tr>
				<td><input type="checkbox" class="classId" id="${item.javaClass.id}"/></td>
				<td>${item.javaClass.name}</td>
				<td>${item.javaClass.lineCount}</td>
				<td class="Methods" style="color:blue;cursor:pointer;">${fn:length(item.javaClass.methods)}</td>
				<td>${item.afferentCoupling}</td>
				<td>${item.efferentCoupling}</td>
				<td>${item.abstractClassCount}</td>
				<td><fmt:formatNumber value="${item.coupling}"
						pattern="###,###.##" /></td>
				<td><fmt:formatNumber value="${item.cohesion}"
						pattern="###,###.##" /></td>
				<td><fmt:formatNumber value="${item.balance}" pattern="#.###" /></td>
				<td>${item.stable ? "是":"否"}</td>
				<td>${item.javaClass.state ? "有":"无"}</td>
				<td>${item.usedByExternal ? "否":"是"}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<script type="text/javascript">
$('.Methods').mousedown(function(e){
	e.stopPropagation();
});
$('.Methods').click(function(){
	var javaClassId = $(this).parent().find('.classId').attr('id');
	$.ajax({    
	    url:'${ctx}/result/methods/' + javaClassId + '/view.ajax',   
	    type:'get',    
	    success:function(data) {
	    	$('#myModalLabel2').text(javaClassId + '方法列表');
	    	$('#myData2').html(data);
	    	$('#myModal2').modal('toggle');
	    	
	    	$("#listMethodsTable").tablesorter();
	    }   
	});
});
</script>

