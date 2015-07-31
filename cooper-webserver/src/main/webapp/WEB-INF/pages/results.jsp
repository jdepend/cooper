<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/styles/css/pa_ui.css">
<link rel="stylesheet" href="${ctx}/styles/css/Validform-Style.css">
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<h3>分析结果列表：</h3>
			<shiro:hasPermission name="admin:view">
			<button id="viewResult" class="btn" style="margin-bottom:10px;">查看结果</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="admin:delete">
			<button id="deleteResult" class="btn" style="margin-bottom:10px;">删除结果</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="admin:download">
			<button id="downloadData" class="btn" style="margin-bottom:10px;">下载数据文件</button>
			</shiro:hasPermission>
			<table id="listResultsTable" class="table table-bordered" pa_ui_name="table,exinput"
				pa_ui_hover="true" pa_ui_selectable="true" pa_ui_select_mode="multi"
				pa_ui_select_trigger="tr" pa_ui_select_column="0"
				pa_ui_select_triggerelement=":checkbox">
				<thead>
					<tr style="cursor: pointer;">
						<th>选择</th>
						<th>创建时间</th>
						<th>分析路径</th>
						<th>代码行数</th>
						<th>类数量</th>
						<th>组件数量</th>
						<th>关系数量</th>
						<th>耦合值</th>
						<th>内聚值</th>
						<th>抽象程度合理性</th>
						<th>内聚性</th>
						<th>封装性</th>
						<th>关系合理性</th>
					</tr>
				</thead>
				<tbody id="listResult">
						<c:forEach items="${resultSummrys}" var="item">
							<tr>
								<td><input type="checkbox" class="itemId"
									value="${item.id}" /></td>
								<td><fmt:formatDate value="${item.createDate}" type="both" /></td>
								<td>${item.summry.path}</td>
								<td>${item.summry.lineCount}</td>
								<td>${item.summry.classCount}</td>
								<td>${item.summry.componentCount}</td>
								<td>${item.summry.relationCount}</td>
								<td><fmt:formatNumber value="${item.summry.coupling}"
										pattern="###,###.##" /></td>
								<td><fmt:formatNumber value="${item.summry.cohesion}"
										pattern="###,###.##" /></td>
								<td><fmt:formatNumber value="${item.summry.distance}"
										pattern="#.###" /></td>
								<td><fmt:formatNumber value="${item.summry.balance}"
										pattern="#.###" /></td>
								<td><fmt:formatNumber value="${item.summry.encapsulation}"
										pattern="#.###" /></td>
								<td><fmt:formatNumber value="${item.summry.normalRelation}"
										pattern="#.###" /></td>
							</tr>
						</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<form id="submitForm" action="" method="GET" />
	<script language="javascript" type="text/javascript"
		src="${ctx}/styles/js/pa_ui.js"></script>
	<script language="javascript" type="text/javascript"
		src="${ctx}/styles/js/esl.js"></script>
	<script type="text/javascript">
	$().ready(function() {
		$('#viewResult').click(function(){
			if($('#listResult .pa_ui_selected').size() == 1){
				var id = $('#listResult .pa_ui_selected .itemId').val();
				$('#submitForm').attr('action', '${ctx}/admin/result/' + id + '/view');
				$('#submitForm').submit();
			}else{
				$.Showmsg('请选择一个分析结果！');
			}
		});
		
		$('#deleteResult').click(function(){
			if($('#listResult .pa_ui_selected').size() > 0){
				var ids = [];
				 $('#listResult .pa_ui_selected .itemId').map(function() {
					 ids.push($(this).val());
				});
				$.ajax( {    
					    url:'${ctx}/admin/result/delete.ajax',   
					    data:{    
					         'ids' : $.toJSON(ids)    
					    },    
					    type:'post',    
					    dataType:'json',    
					    success:function(data) {    
					    	$('#listResult .pa_ui_selected').remove();
					    }   
					}); 
			}else{
				$.Showmsg('请选择分析结果！');
			}
		});
		
		$('#downloadData').click(function(){
			$('#submitForm').attr('action', '${ctx}/admin/result/data/download');
			$('#submitForm').submit();
		});
		
		$("#listResultsTable").tablesorter();
	});
</script>