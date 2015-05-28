<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${ctx}/styles/css/pa_ui.css">
<style type='text/css'>
#myModal {
width: 1000px;
margin: 0 0 0 -500px;
}
</style>
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
					<h4>抽象程度合理性得分：${result.distance}</h4>
					<h6>${result.DAdvise}</h6>
					<div id="d" class="d" style="height:400px"></div>
				</div>
				<div class="span6">
					<h4>内聚性得分：${result.balance}</h4>
					<h6>${result.balanceAdvise}</h6>
					<div id="balance" class="balance" style="height:400px"></div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<h4>封装性得分：${result.encapsulation}</h4>
					<h6>${result.encapsulationAdvise}</h6>
					<div id="encapsulation" class="encapsulation" style="height:400px"></div>
				</div>
				<div class="span6">
					<h4>关系合理性得分：${result.relationRationality}</h4>
					<h6>${result.relationRationalityAdvise}</h6>
					<div id="relationRationality" class="relationRationality"
						style="height:400px"></div>
				</div>
			</div>
		</div>
		<c:if test="${!empty result.summarys}">
			<div class="row-fluid">
				<div class="span12">
					<h3>组件信息：</h3>
					<table class="table table-bordered" pa_ui_name="table,exinput"
						pa_ui_hover="true" pa_ui_selectable="true"
						pa_ui_select_mode="multi" pa_ui_select_trigger="tr"
						pa_ui_select_column="0" pa_ui_select_triggerelement=":checkbox">
						<thead>
							<tr>
								<th>选择</th>
								<th>组件名称</th>
								<th>区域</th>
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
							<c:forEach items="${result.summarys}" var="item">
								<tr>
									<td><input type="checkbox" /></td>
									<td class="itemName">${item.name}</td>
									<td>${item.areaComponent.name}</td>
									<td>${item.lineCount}</td>
									<td class="classCount" style="color:blue;">${item.classCount}</td>
									<td>${item.abstractClassCount}</td>
									<td>${item.concreteClassCount}</td>
									<td class="Ca" style="color:blue;">${item.afferentCoupling}</td>
									<td class="Ce" style="color:blue;">${item.efferentCoupling}</td>
									<td><fmt:formatNumber value="${item.abstractness}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.volatility}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.stability}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.distance}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.coupling}"
											pattern="###,###.##" /></td>
									<td><fmt:formatNumber value="${item.cohesion}"
											pattern="###,###.##" /></td>
									<td><fmt:formatNumber value="${item.balance}"
											pattern="#.###" /></td>
									<td><fmt:formatNumber value="${item.encapsulation}"
											pattern="#.###" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
		<c:if test="${!empty result.relations}">
			<div class="row-fluid">
				<div class="span12">
					<h3>关系信息：</h3>
					<table class="table table-bordered" pa_ui_name="table,exinput"
						pa_ui_hover="true" pa_ui_selectable="true"
						pa_ui_select_mode="multi" pa_ui_select_trigger="tr"
						pa_ui_select_column="0" pa_ui_select_triggerelement=":checkbox">
						<thead>
							<tr>
								<th>选择</th>
								<th>当前组件</th>
								<th>依赖组件</th>
								<th>关系强度</th>
								<th>当前组件内聚性</th>
								<th>依赖组件内聚性</th>
								<th>关系平衡值</th>
								<th>关注类型</th>
								<th>关注级别</th>
							</tr>
						</thead>
						<tbody id="listRelations">
							<c:forEach items="${result.relations}" var="item">
								<tr>
									<td><input type="checkbox" /></td>
									<td class="current">${item.current.name}</td>
									<td class="depend">${item.depend.name}</td>
									<td class="relation" style="color:blue;"><fmt:formatNumber value="${item.intensity}"
											pattern="###,###.##" /></td>
									<td><fmt:formatNumber
											value="${item.current.component.cohesion}"
											pattern="###,###.##" /></td>
									<td><fmt:formatNumber
											value="${item.depend.component.cohesion}"
											pattern="###,###.##" /></td>
									<td><fmt:formatNumber value="${item.balance}"
											pattern="###,###.##" /></td>
									<td>${item.attentionTypeName}</td>
									<td><fmt:formatNumber value="${item.attentionLevel}"
											pattern="##.###" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
		<c:if test="${!empty todoList}">
			<div class="row-fluid">
				<div class="span12">
					<h3>待做事项：</h3>
					<table class="table table-bordered" pa_ui_name="table,exinput"
						pa_ui_hover="true" pa_ui_selectable="true"
						pa_ui_select_mode="multi" pa_ui_select_trigger="tr"
						pa_ui_select_column="0" pa_ui_select_triggerelement=":checkbox">
						<thead>
							<tr>
								<th>选择</th>
								<th>描述</th>
								<th>依据</th>
							</tr>
						</thead>
						<tbody id="listTodos">
							<c:forEach items="${todoList}" var="item">
								<tr>
									<td><input type="checkbox" class="itemId" value="${item.id}" /></td>
									<td class="todoItem" style="color:blue;">${item.content}</td>
									<td>${item.according}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
		<c:if test="${!empty tableList}">
			<div class="row-fluid">
				<div class="span12">
					<h3>数据库表操作：</h3>
					<table class="table table-bordered" pa_ui_name="table,exinput"
						pa_ui_hover="true" pa_ui_selectable="true"
						pa_ui_select_mode="multi" pa_ui_select_trigger="tr"
						pa_ui_select_column="0" pa_ui_select_triggerelement=":checkbox">
						<thead>
							<tr>
								<th>选择</th>
								<th>表名</th>
								<th>出现次数</th>
								<th>操作</th>
								<th>组件名</th>
								<th>类名</th>
							</tr>
						</thead>
						<tbody id="listTables">
							<c:forEach items="${tableList}" var="item">
								<tr>
									<td><input type="checkbox" /></td>
									<td>${item.name}</td>
									<td>${item.count}</td>
									<td>${item.type}</td>
									<td>${item.component}</td>
									<td>${item.javaClass}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
		<c:if test="${!empty result.relations}">
			<div class="row-fluid">
				<div class="span12">
					<h3>组件关系图：</h3>
					<div id="relationGraph" class="relationGraph" style="height:500px"></div>
				</div>
			</div>
		</c:if>
		<c:if test="${!empty result.architectPatternResult}">
			<div class="row-fluid">
				<div class="span12">
					<h3>结构提示：</h3>
					<textarea style="width:100%;height:auto; overflow: hidden;">${result.architectPatternResult.result}</textarea>
				</div>
			</div>
		</c:if>
	</div>
</div>
<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel"></h3>
  </div>
  <div class="modal-body">
    <p id="myData">One fine body…</p>
  </div>
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/pa_ui.js"></script>
<script language="javascript" type="text/javascript"
	src="${ctx}/styles/js/esl.js"></script>
<script type="text/javascript">
    // 路径配置
    require.config({
        paths:{ 
            'echarts' : '${ctx}/styles/js/echarts',
            'echarts/chart/map' : '${ctx}/styles/js/echarts-map'
        }
    });
    
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/map'
        ],
        function(ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('relationGraph')); 
            
            option = {
            	    title : {
            	        x:'right',
            	        y:'bottom'
            	    },
            	    tooltip : {
            	        trigger: 'item',
            	        formatter: '{a} : {b}'
            	    },
            	    legend: {
            	        x: 'left',
            	    },
            	    series : [
            	        {
            	            type:'force',
            	            name : "组件关系",
            	            categories : [
            	                {
            	                    name: '组件',
            	                    itemStyle: {
            	                        normal: {
            	                            color : '#87cdfa'
            	                        }
            	                    }
            	                }
            	            ],
            	            itemStyle: {
            	                normal: {
            	                    label: {
            	                        show: true,
            	                        textStyle: {
            	                            color: '#800080'
            	                        }
            	                    },
            	                    nodeStyle : {
            	                        brushType : 'both',
            	                        strokeColor : 'rgba(255,215,0,0.4)',
            	                        lineWidth : 1
            	                    }
            	                },
            	                emphasis: {
            	                    label: {
            	                        show: false
            	                        // textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
            	                    },
            	                    nodeStyle : {
            	                        //r: 30
            	                    },
            	                    linkStyle : {}
            	                }
            	            },
            	            minRadius : 15,
            	            maxRadius : 25,
            	            density : 0.05,
            	            attractiveness: 1.2,
            	            linkSymbol: 'arrow',
            	            nodes: ${relation_graph_data.nodeInfo},
            	            links: ${relation_graph_data.edgeInfo}
            	        }
            	    ]
            	};
            	var ecConfig = require('echarts/config');
            	function focus(param) {
            	    var data = param.data;
            	    var links = option.series[0].links;
            	    var nodes = option.series[0].nodes;
            	    if (
            	        data.source !== undefined
            	        && data.target !== undefined
            	    ) { //点击的是边
            	        var sourceNode = nodes[data.source];
            	        var targetNode = nodes[data.target];
            	        console.log("选中了边 " + sourceNode.name + ' -> ' + targetNode.name + ' (' + data.weight + ')');
            	    } else { // 点击的是点
            	        console.log("选中了" + data.name + '(' + data.value + ')');
            	    }
            	    console.log(param);
            	}
            	myChart.on(ecConfig.EVENT.CLICK, focus);
            	myChart.setOption(option, true);
            	
            	myChart = ec.init(document.getElementById('d')); 
            	option = {
            		    tooltip : {
            		        trigger: 'item',
            		        formatter: "{a} <br/>{b} : {c} ({d}%)"
            		    },
            		    series : [
            		        {
            		            type:'pie',
            		            data:[
            		                {value:${result.distance}, name:'得分'},
            		                {value:25 - ${result.distance}, name:'未得分'},
            		            ]
            		        }
            		    ],
            		    color : [
							'#69D2E7', '#E0E4CC'
            		    ]
            		};
            	myChart.setOption(option, true);
            	
            	myChart = ec.init(document.getElementById('balance')); 
            	option = {
            		    tooltip : {
            		        trigger: 'item',
            		        formatter: "{a} <br/>{b} : {c} ({d}%)"
            		    },
            		    series : [
            		        {
            		            type:'pie',
            		            data:[
            		                {value:${result.balance}, name:'得分'},
            		                {value:25 - ${result.balance}, name:'未得分'},
            		            ]
            		        }
            		    ],
            		    color : [
							'#69D2E7', '#E0E4CC'
            		    ]
            		};
            	myChart.setOption(option, true);
            	
            	myChart = ec.init(document.getElementById('encapsulation')); 
            	option = {
            		    tooltip : {
            		        trigger: 'item',
            		        formatter: "{a} <br/>{b} : {c} ({d}%)"
            		    },
            		    series : [
            		        {
            		            type:'pie',
            		            data:[
            		                {value:${result.encapsulation}, name:'得分'},
            		                {value:25 - ${result.encapsulation}, name:'未得分'},
            		            ]
            		        }
            		    ],
            		    color : [
							'#69D2E7', '#E0E4CC'
            		    ]
            		};
            	myChart.setOption(option, true);
            	
            	myChart = ec.init(document.getElementById('relationRationality')); 
            	option = {
            		    tooltip : {
            		        trigger: 'item',
            		        formatter: "{a} <br/>{b} : {c} ({d}%)"
            		    },
            		    series : [
            		        {
            		            type:'pie',
            		            data:[
            		                {value:${result.relationRationality}, name:'得分'},
            		                {value:25 - ${result.relationRationality}, name:'未得分'},
            		            ]
            		        }
            		    ],
            		    color : [
							'#69D2E7', '#E0E4CC'
            		    ]
            		};
            	myChart.setOption(option, true);
        }
    );
    
    $().ready(function() {
    	$('.classCount').mousedown(function(e){
    		e.stopPropagation();
    	});
    	$('.classCount').click(function(e){
    		var componentName = $(this).parent().find('.itemName').text();
    		$.ajax({    
			    url:'${ctx}/result/component/' + componentName + '/classes/view.ajax',   
			    type:'get',    
			    success:function(data) {
			    	$('#myModalLabel').text(componentName + '组件类列表');
			    	$('#myData').html(data);
			    	$('#myModal').modal('toggle');
			    }   
			});
    	});
    	
    	$('.Ca').mousedown(function(e){
    		e.stopPropagation();
    	});
    	$('.Ca').click(function(){
    		var componentName = $(this).parent().find('.itemName').text();
    		$.ajax({    
			    url:'${ctx}/result/component/' + componentName + '/ca/view.ajax',   
			    type:'get',    
			    success:function(data) {
			    	$('#myModalLabel').text(componentName + '组件传入列表');
			    	$('#myData').html(data);
			    	$('#myModal').modal('toggle');
			    }   
			});
    	});
    	
    	$('.Ce').mousedown(function(e){
    		e.stopPropagation();
    	});
    	$('.Ce').click(function(){
    		var componentName = $(this).parent().find('.itemName').text();
    		$.ajax({    
			    url:'${ctx}/result/component/' + componentName + '/ce/view.ajax',   
			    type:'get',    
			    success:function(data) {
			    	$('#myModalLabel').text(componentName + '组件传出列表');
			    	$('#myData').html(data);
			    	$('#myModal').modal('toggle');
			    }   
			});
    	});
    	
    	$('.relation').mousedown(function(e){
    		e.stopPropagation();
    	});
    	$('.relation').click(function(){
    		var current = $(this).parent().find('.current').text();
    		var depend = $(this).parent().find('.depend').text();
    		$.ajax({    
			    url:'${ctx}/result/relation/' + current + '/' + depend + '/view.ajax',   
			    type:'get',    
			    success:function(data) {
			    	$('#myModalLabel').text(current + "->" + depend + '关系明细');
			    	$('#myData').html(data);
			    	$('#myModal').modal('toggle');
			    }   
			});
    	});
    	
    	$('.todoItem').mousedown(function(e){
    		e.stopPropagation();
    	});
    	$('.todoItem').click(function(){
    		var id = $(this).parent().find('.itemId').val();
    		var name = $(this).text();
    		$.ajax({    
			    url:'${ctx}/result/todoItem/' + id + '/view.ajax',   
			    type:'get',    
			    success:function(data) {
			    	$('#myModalLabel').text('待做事项说明');
			    	$('#myData').html(data);
			    	$('#myModal').modal('toggle');
			    }   
			});
    	});

    });
</script>