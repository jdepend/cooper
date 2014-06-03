<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>

<table class="table table-bordered" pa_ui_name="table,exinput"
	pa_ui_hover="true" pa_ui_selectable="true" pa_ui_select_mode="multi"
	pa_ui_select_trigger="tr" pa_ui_select_column="0"
	pa_ui_select_triggerelement=":checkbox">
	<thead>
		<tr>
			<th>选择</th>
			<th>当前类</th>
			<th>依赖类</th>
			<th>依赖类型</th>
			<th>依赖强度</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${relation.items}" var="item">
			<tr>
				<td><input type="checkbox"/></td>
				<td>${item.currentJavaClass}</td>
				<td>${item.dependJavaClass}</td>
				<td>${item.type.name}</td>
				<td><fmt:formatNumber value="${item.relationIntensity}"
						pattern="###,###.##" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

