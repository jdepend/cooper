<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>

<table id="listInvokeItemsTable" class="table table-bordered" pa_ui_name="table,exinput"
	pa_ui_hover="true" pa_ui_selectable="true" pa_ui_select_mode="multi"
	pa_ui_select_trigger="tr" pa_ui_select_column="0"
	pa_ui_select_triggerelement=":checkbox">
	<thead>
		<tr style="cursor: pointer;">
			<th>选择</th>
			<th>调用者</th>
			<th>被调用者</th>
			<th>是否包含进程间调用</th>
			<th>调用类型</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${invokeitems}" var="invokeitem">
			<tr>
				<td><input type="checkbox"/></td>
				<td>${invokeitem.caller.methodInfo}</td>
				<td>${invokeitem.callee.methodInfo}</td>
				<td></td>
				<td>${invokeitem.name}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>