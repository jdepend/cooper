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
			<th>组件名称</th>
			<th>代码行数</th>
			<th>类数量</th>
			<th>具体类数量</th>
			<th>抽象类数量</th>
			<th>传入</th>
			<th>传出</th>
			<th>抽象程度</th>
			<th>易变性</th>
			<th>稳定性</th>
			<th>抽象程度合理性</th>
			<th>耦合值</th>
			<th>内聚值</th>
			<th>内聚性</th>
			<th>封装性</th>
		</tr>
	</thead>
	<tbody id="listComponents">
		<c:forEach items="${components}" var="item">
			<tr>
				<td><input type="checkbox" /></td>
				<td class="itemName">${item.name}</td>
				<td>${item.lineCount}</td>
				<td>${item.classCount}</td>
				<td>${item.abstractClassCount}</td>
				<td>${item.concreteClassCount}</td>
				<td>${item.afferentCoupling}</td>
				<td>${item.efferentCoupling}</td>
				<td><fmt:formatNumber value="${item.abstractness}"
						pattern="#.###" /></td>
				<td><fmt:formatNumber value="${item.volatility}"
						pattern="#.###" /></td>
				<td><fmt:formatNumber value="${item.stability}" pattern="#.###" /></td>
				<td><fmt:formatNumber value="${item.distance}" pattern="#.###" /></td>
				<td><fmt:formatNumber value="${item.coupling}"
						pattern="###,###.##" /></td>
				<td><fmt:formatNumber value="${item.cohesion}"
						pattern="###,###.##" /></td>
				<td><fmt:formatNumber value="${item.balance}" pattern="#.###" /></td>
				<td><fmt:formatNumber value="${item.encapsulation}"
						pattern="#.###" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

