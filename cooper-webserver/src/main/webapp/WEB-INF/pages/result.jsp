<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="row-fluid">
				<div class="span12">
					<h3>您分析的jar为：${result.runningContext.path}</h3>
					<h3>
						您将jar分成了${result.summary.componentCount}个组件进行分析，其结构质量成绩为：${result.score}
					</h3>
					<h6>
						(其中类数量为${result.summary.classCount}，包数量为${result.summary.javaPackageCount})
					</h6>
					<h6>
						${result.summary.componentCount}个组件共产生了${result.summary.relationCount}个关系，有问题的关系占总关系的比例为${result.problemRelationScale}
					</h6>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<h4>抽象程度合理性得分：${result.d}</h4>
					<h6>${result.DAdvise}</h6>
					<canvas id="d" height="400" width="400"></canvas>
				</div>
				<div class="span6">
					<h4>内聚性得分：${result.balance}</h4>
					<canvas id="balance" height="400" width="400"></canvas>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<h4>封装性得分：${result.encapsulation}</h4>
					<canvas id="encapsulation" height="400" width="400"></canvas>
				</div>
				<div class="span6">
					<h4>关系合理性得分：${result.relationRationality}</h4>
					<canvas id="relationRationality" height="400" width="400"></canvas>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				
			</div>
		</div>
	</div>
</div>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/Chart.js"></script>
<script>
	var dPieData = [ {
		value : ${result.d},
		color : "#69D2E7"
	}, {
		value : 25 - ${result.d},
		color : "#E0E4CC"
	}];
	var balancePieData = [ {
		value : ${result.balance},
		color : "#69D2E7"
	}, {
		value : 25 - ${result.balance},
		color : "#E0E4CC"
	}];
	var encapsulationPieData = [ {
		value : ${result.encapsulation},
		color : "#69D2E7"
	}, {
		value : 25 - ${result.encapsulation},
		color : "#E0E4CC"
	}];
	var relationRationalityPieData = [ {
		value : ${result.relationRationality},
		color : "#69D2E7"
	}, {
		value : 25 - ${result.relationRationality},
		color : "#E0E4CC"
	}];

	new Chart(document.getElementById("d").getContext("2d")).Pie(dPieData);
	new Chart(document.getElementById("balance").getContext("2d")).Pie(balancePieData);
	new Chart(document.getElementById("encapsulation").getContext("2d")).Pie(encapsulationPieData);
	new Chart(document.getElementById("relationRationality").getContext("2d")).Pie(relationRationalityPieData);
</script>