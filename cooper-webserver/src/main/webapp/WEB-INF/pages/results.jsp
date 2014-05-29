<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/styles/css/pa_ui.css">
<div class="container-fluid">
		<c:if test="${!empty resultSummrys}">
			<div class="row-fluid">
				<div class="span12">
					<h3>分析结果列表：</h3>
					<table class="table table-bordered" pa_ui_name="table,exinput"
						pa_ui_hover="true" pa_ui_selectable="true"
						pa_ui_select_mode="multi" pa_ui_select_trigger="tr"
						pa_ui_select_column="0" pa_ui_select_triggerelement=":checkbox">
						<thead>
							<tr>
								<th>选择</th>
								<th>创建时间</th>
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
						<tbody>
							<c:forEach items="${resultSummrys}" var="item">
								<tr>
									<td><input type="checkbox" /></td>
									<td class="itemName"><fmt:formatDate value="${item.createDate}" type="both"/></td>
									<td>${item.summry.lineCount}</td>
									<td>${item.summry.classCount}</td>
									<td>${item.summry.abstractClassCount}</td>
									<td>${item.summry.concreteClassCount}</td>
									<td>${item.summry.afferentCoupling}</td>
									<td>${item.summry.efferentCoupling}</td>
									<td><fmt:formatNumber value="${item.summry.abstractness}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.summry.volatility}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.summry.stability}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.summry.distance}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.summry.coupling}"
											pattern="###,###.##" /></td>
									<td><fmt:formatNumber value="${item.summry.cohesion}"
											pattern="###,###.##" /></td>
									<td><fmt:formatNumber value="${item.summry.balance}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.summry.encapsulation}"
											pattern="#.###" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/pa_ui.js"></script>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/esl.js"></script>
