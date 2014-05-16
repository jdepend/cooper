<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/styles/css/pa_ui.css">
<link rel="stylesheet" href="${ctx}/styles/css/contextmenu.css">
<link rel="stylesheet" href="${ctx}/styles/css/Validform-Style.css">
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<fieldset>
				<legend>第二步：设置组件模型</legend>
			</fieldset>
			<div
				style="overflow-x: auto; overflow-y: auto; height: 350px; width:100%; border: 1px solid #dddddd; ">
				<table class="table table-bordered" pa_ui_name="table,exinput"
					pa_ui_hover="true" pa_ui_selectable="true"
					pa_ui_select_mode="multi" pa_ui_select_trigger="tr"
					pa_ui_select_column="0" pa_ui_select_triggerelement=":checkbox">
					<thead>
						<tr>
							<th>选择</th>
							<th>包名称</th>
							<th>类数量</th>
						</tr>
					</thead>
					<tbody id="listPackages">
						<c:forEach items="${listPackages}" var="item">
							<tr>
								<td><input type="checkbox" /></td>
								<td class="itemName">${item.name}</td>
								<td>${item.classCount}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="row-fluid">
				<div class="span6" style="overflow-x: auto; overflow-y: auto; height: 100px; width:49%; border: 1px solid #dddddd; ">
					<ul id="componentList" class="componentModelView" style="height: 100px;"/>
				</div>
				<div class="span6" style="overflow-x: auto; overflow-y: auto; height: 100px; width:48.87%; border: 1px solid #dddddd;">
					<ul id="componentPackageList" class="componentModelView" style="height: 100px;"/>
				</div>
			</div>
			<button id="finish" type="submit" class="btn">提交</button>
		</div>
	</div>
</div>
<div id="componentNameModal" class="modal hide fade" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h5 id="myModalLabel">录入组件名</h5>
	</div>
	<form class="componentNameForm" action="#">
		<div class="modal-body">
			<label><span style="width:10px; color:#b20202;">*</span>组件名</label> <input
				 id="componentName" type="text" class="inputxt" datatype="*" nullmsg="请录入组件名！" />
		</div>
		<div class="modal-footer">
			<input id="createComponent" type="submit" class="btn btn-primary" value="确定" />
		</div>
	</form>
</div>
<form id="submitForm" action = '${ctx}/analyse/execute' method="POST">
	<input id="componentModel" name="componentModel" type="hidden"/>
</form>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/pa_ui.js"></script>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/jquery.contextmenu.js"></script>
<script type="text/javascript">
	//定义组件模型
	var componentModel = {};
	$().ready(function() {
		var option = {
			width : 150,
			items : [ {
				text : '增加组件',
				icon : '${ctx}/styles/css/images/application_form_add.png',
				alias : '',
				action : menuAction
			} ],
			onContextMenu : BeforeContextMenu
		};
		function menuAction() {
			//清空窗口数据
			$('#componentName').val('');
			//显示窗口
			$('#componentNameModal').modal('toggle');
		}
		function BeforeContextMenu() {
			return $('#listPackages .pa_ui_selected .itemName').size();
		}
		$('#listPackages').contextmenu(option);

		$('.componentNameForm').Validform({
			tiptype:3,
			label:'.label',
			showAllError:true,
			datatype:{
				'*':function(gets,obj,curform,regxp){
					    if(gets == ''){
					    	return '组件名不能为空！';
					    }
					    if(componentModel.hasOwnProperty(gets)){
							return '组件名已经存在！';
						}
						return true;
				}
			},
			callback:function(data){
				//添加组件名
				var componentName = $('#componentName').val(); 
				$('#componentList').append('<li>' + componentName + '</li>');
				var selectedPackages = $('#listPackages .pa_ui_selected .itemName');
				//添加包列表
				$('#componentPackageList').empty();
				var packageNames = [];
				selectedPackages.map(function() {
					var packageName = $(this).text();
					$('#componentPackageList').append('<li>' + packageName + '</li>');
					packageNames.push(packageName);
				});
				//保存组件信息
				componentModel[componentName] = packageNames;
				//删除选择的包集合
				$('#listPackages .pa_ui_selected').remove();
				//关闭窗口
				$('#componentNameModal').modal('toggle');
				return false;
			}
		});
		
		$('body').on('mouseover', 'ul.componentModelView li', function () {
			$(this).addClass('pa_ui_hover');
		}).on('mouseout', 'ul.componentModelView li', function () {
			$(this).removeClass('pa_ui_hover');
		}).on('click', 'ul#componentList li', function () {
			$('#componentPackageList').empty();
			var packageNames = componentModel[$(this).text()];
			for(var i in packageNames){
				$('#componentPackageList').append('<li>' + packageNames[i] + '</li>');
			}
		});
		
		$('#finish').click(function(){
			if($.isEmptyObject(componentModel)){
				$.Showmsg('请创建组件！');
				return;
			}
			$('#componentModel').val($.toJSON(componentModel));
			$('#submitForm').submit();
		});
	});
</script>

