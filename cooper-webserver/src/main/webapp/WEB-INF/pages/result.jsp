<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="row-fluid">
				<div class="span12">
					<h3>
						您分析的jar为：${result.runningContext.path}
					</h3>
					<h3>
						您将jar分成了${result.summary.componentCount}个组件进行分析，其结构质量成绩为：${result.score}
					</h3>
					<h6>
						(其中类数量为${result.summary.classCount}，包数量为${result.summary.javaPackageCount})
					</h6>
					<h4>
						${result.summary.componentCount}个组件共产生了${result.relations.size}个关系，有问题的关系占总关系的比例为${result.summary.javaPackageCount}
					</h4>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
				</div>
				<div class="span6">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
				</div>
				<div class="span6">
				</div>
			</div>
		</div>
	</div>
</div>