<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/styles/css/pa_ui.css"> 
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<fieldset>
				<legend>第二步：设置组件模型</legend>
			</fieldset>
			<div
				style="overflow-x: auto; overflow-y: auto; height: 200px; width:100%;">
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
					<tbody>
						<c:forEach items="${listPackages}" var="item">
							<tr>
								<td><input type="checkbox" /></td>
								<td>${item.name}</td>
								<td>${item.classCount}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<ol>
						<li>新闻资讯</li>
						<li>体育竞技</li>
						<li>娱乐八卦</li>
						<li>前沿科技</li>
						<li>环球财经</li>
						<li>天气预报</li>
						<li>房产家居</li>
						<li>网络游戏</li>
					</ol>
				</div>
				<div class="span6">
					<ol>
						<li>新闻资讯</li>
						<li>体育竞技</li>
						<li>娱乐八卦</li>
						<li>前沿科技</li>
						<li>环球财经</li>
						<li>天气预报</li>
						<li>房产家居</li>
						<li>网络游戏</li>
					</ol>
				</div>
			</div>
			<button type="submit" class="btn">提交</button>
		</div>
	</div>
</div>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/jquery-1.3.2.js"></script>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/jquery-ui-1.7.1.js"></script>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/pa_ui.js"></script>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/jquery.highlighter.js"></script>


