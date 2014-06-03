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
			<th>名称</th>
			<th>代码行数</th>
			<th>传入</th>
			<th>传出</th>
			<th>抽象程度</th>
			<th>稳定性</th>
			<th>抽象程度合理性</th>
			<th>耦合值</th>
			<th>内聚值</th>
			<th>内聚性</th>
			<th>封装性</th>
			<th>易变性</th>
			<th>是否存在状态</th>
			<th>是否私有</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${component.classes}" var="item">
			<tr>
				<td><input type="checkbox"/></td>
				<td>${item.name}</td>
				<td>${item.lineCount}</td>
				<td>${item.afferentCoupling}</td>
				<td>${item.efferentCoupling}</td>
				<td><fmt:formatNumber value="${item.abstractness}"
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
				<td>${item.stable ? "是":"否"}</td>
				<td>${item.state ? "有":"无"}</td>
				<td>${item.usedByExternal ? "否":"是"}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

